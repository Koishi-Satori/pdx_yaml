plugins {
    kotlin("jvm") version "2.0.0"
}

group = "top.kkoishi"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
