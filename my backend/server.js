require('dotenv').config();
const express = require('express');
const { Client } = require('pg');
const cors = require('cors');

const app = express();
const port = process.env.PORT || 3000;


app.use(cors());
app.use(express.json()); 


const client = new Client({
  connectionString: process.env.DATABASE_URL,
  ssl: {
    rejectUnauthorized: false
  }
});

// --- Connection Status ---
client.connect()
  .then(() => console.log('Successful connection to Neon Database'))
  .catch(err => console.error('Connection error to Neon:', err.stack));

//                       API 

//Login API
app.post('/api/login', async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: "Please enter Email and Password" });
  }

  try {
    const query = 'SELECT * FROM "User" WHERE email = $1';
    const values = [email];

    const result = await client.query(query, values);

    // Check if user exists
    if (result.rows.length === 0) {
      return res.status(401).json({ message: "Email does not exist!" });
    }

    const user = result.rows[0];

    // Check password (Simple comparison)
    if (user.password !== password) {
      return res.status(401).json({ message: "Incorrect password" });
    }

    // Login successful -> Return user info (excluding password)
    const { password: _, ...userWithoutPass } = user; 
    
    console.log(`User ${email} logged in successfully!`);
    res.json(userWithoutPass);

  } catch (err) {
    console.error("Login Error:", err);
    res.status(500).json({ message: "Internal Server Error" });
  }
});

// Subjects API
app.get('/api/subjects', async (req, res) => {
  try {
    // Get all subjects, sorted by name
    const query = 'SELECT * FROM subject ORDER BY name ASC';
    const result = await client.query(query);
    
    res.json(result.rows);
  } catch (err) {
    console.error("Get Subjects Error:", err);
    res.status(500).json({ message: "Error fetching subject list" });
  }
}); 

// Enroll API (tự động thêm vào class luôn nên chỉ cần đăng ký môn)
app.post('/api/enroll-auto', async (req, res) => {
  const { userId, subjectId } = req.body; 

  console.log("Processing enrollment for:", userId, subjectId);

  if (!userId || !subjectId) {
    return res.status(400).json({ message: "Missing User ID or Subject ID" });
  }

  try {
    // 1: tìm lớp xem tồn tại chưa
    const findClassQuery = 'SELECT id FROM class WHERE "subjectId" = $1 LIMIT 1';
    const classResult = await client.query(findClassQuery, [subjectId]);

    if (classResult.rows.length === 0) {
      return res.status(404).json({ message: "No open classes found for this subject!" });
    }

    const classId = classResult.rows[0].id; 
    
    // 2: check xem đã đăng ký trước môn này chưa
    const checkQuery = 'SELECT * FROM enrollment WHERE "studentId" = $1 AND "classId" = $2';
    const checkResult = await client.query(checkQuery, [userId, classId]);

    if (checkResult.rows.length > 0) {
      return res.status(400).json({ message: "You have already enrolled in this course!" });
    }

    // 3: Insert vào enrolment table
    const insertQuery = 'INSERT INTO enrollment ("studentId", "classId") VALUES ($1, $2)';
    await client.query(insertQuery, [userId, classId]);

    console.log(`Enrollment success: User ${userId} -> Class ${classId}`);
    res.json({ message: "Enrollment successful!" });

  } catch (err) {
    console.error("Enroll Error:", err);
    res.status(500).json({ message: "Server Error: " + (err.detail || err.message) });
  }
});

// MyCourse API (Fetch enrolled courses)
app.get('/api/my-courses/:userId', async (req, res) => {
  const { userId } = req.params;
  console.log("Fetching your courses (MyCourses) for User ID:", userId);

  try {
    const query = `
      SELECT 
        c.id,                          
        c.name,                        
        COALESCE(s.credits, 3) AS credits,     
        COALESCE(up."fullName", 'Unknown Lecturer') AS lecturer

      FROM enrollment e
      JOIN class c ON e."classId" = c.id
      LEFT JOIN subject s ON c."subjectId" = s.id       
      LEFT JOIN user_profile up ON c."lecturerId" = up.id 
      WHERE e."studentId" = $1
    `;
    
    const result = await client.query(query, [userId]);
    
    console.log(`Found ${result.rows.length} enrolled courses.`);
    res.json(result.rows);

  } catch (err) {
    console.error("Get My Courses Error:", err);
    res.status(500).json({ message: "Error fetching enrolled courses" });
  }
});
// Schedule API (Lấy lịch học hoặc lịch dạy tùy theo Role)
app.get('/api/schedule/:userId', async (req, res) => {
  const { userId } = req.params;
  console.log("Fetching schedule for User ID:", userId);

  try {
    // 1. Kiểm tra role của user trước
    const roleQuery = 'SELECT role FROM "User" WHERE id = $1';
    const roleRes = await client.query(roleQuery, [userId]);

    if (roleRes.rows.length === 0) {
      return res.status(404).json({ message: "User not found" });
    }

    const role = roleRes.rows[0].role;
    let query = "";

    // 2. Chọn Query dựa trên Role
    // Sửa đổi: Dùng TO_CHAR để định dạng ngày tháng chuẩn ISO 8601 (có chữ T) để Android parse được.
    // Dùng LEFT JOIN cho room và user_profile để tránh mất dữ liệu nếu thông tin đó chưa có.
    if (role === 'LECTURER') {
      // Dành cho Giảng viên
      query = `
        SELECT 
          c.name AS "subjectName", 
          TO_CHAR(s."startTime", 'YYYY-MM-DD"T"HH24:MI:SS"Z"') AS "startTime", 
          TO_CHAR(s."endTime", 'YYYY-MM-DD"T"HH24:MI:SS"Z"') AS "endTime", 
          COALESCE(r.name, 'N/A') AS "roomName", 
          COALESCE(up."fullName", 'Lecturer') AS "lecturerName"
        FROM class c
        JOIN class_schedule s ON c.id = s."classId"
        LEFT JOIN room r ON s."roomId" = r.id
        LEFT JOIN user_profile up ON c."lecturerId" = up.id
        WHERE c."lecturerId" = $1
        ORDER BY s."startTime" ASC
      `;
    } else {
      // Dành cho Sinh viên
      query = `
        SELECT 
          c.name AS "subjectName", 
          TO_CHAR(s."startTime", 'YYYY-MM-DD"T"HH24:MI:SS"Z"') AS "startTime", 
          TO_CHAR(s."endTime", 'YYYY-MM-DD"T"HH24:MI:SS"Z"') AS "endTime", 
          COALESCE(r.name, 'N/A') AS "roomName", 
          COALESCE(up."fullName", 'Lecturer') AS "lecturerName"
        FROM enrollment e
        JOIN class c ON e."classId" = c.id
        JOIN class_schedule s ON c.id = s."classId"
        LEFT JOIN room r ON s."roomId" = r.id
        LEFT JOIN user_profile up ON c."lecturerId" = up.id
        WHERE e."studentId" = $1
        ORDER BY s."startTime" ASC
      `;
    }

    const result = await client.query(query, [userId]);
    console.log(`Sent ${result.rows.length} schedule records to App.`);
    res.json(result.rows);

  } catch (err) {
    console.error("Get Schedule Error:", err);
    res.status(500).json({ message: "Error fetching schedule" });
  }
});

// Get Profile API
app.get('/api/profile/:userId', async (req, res) => {
  const { userId } = req.params;
  console.log("Fetching profile for User ID:", userId);

  try {
    // Truy vấn bảng user_profile (giả sử bảng này chứa studentCode và major)
    // Cần đảm bảo tên cột trong DB khớp với tên biến ở đây
    // Dùng dấu " " nếu tên cột trong Postgres có chữ hoa
    const query = `
      SELECT 
        id, 
        "fullName", 
        "studentCode", 
        major 
      FROM user_profile 
      WHERE id = $1
    `;

    const result = await client.query(query, [userId]);

    if (result.rows.length === 0) {
      // Nếu không tìm thấy trong user_profile, thử tìm trong bảng User cơ bản
      const userQuery = 'SELECT id, "fullName", role FROM "User" WHERE id = $1';
      const userResult = await client.query(userQuery, [userId]);
      
      if (userResult.rows.length === 0) {
        return res.status(404).json({ message: "User not found" });
      }
      return res.json(userResult.rows[0]);
    }

    // Trả về kết quả
    res.json(result.rows[0]);

  } catch (err) {
    console.error("Get Profile Error:", err);
    res.status(500).json({ message: "Error fetching profile" });
  }
});

// --- Start Server ---
const PORT = process.env.PORT || 3000;
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running at http://0.0.0.0:${PORT}`);
});