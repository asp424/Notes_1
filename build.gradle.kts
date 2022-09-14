buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.3.13")
    }
    repositories.repository
}

allprojects { repositories.repository }

clearProject(rootProject.buildDir)


