plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.edu.usth.classroomschedulemanagementapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "vn.edu.usth.classroomschedulemanagementapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.12.0")

    // Thư viện Kizitonwose Calendar
    implementation("com.kizitonwose.calendar:view:2.5.0")

    // Thư viện xử lý ngày tháng chuẩn Java 8 (Cần thiết)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}