plugins {
    id("java")
}

group = "org.darkness"
version = "1.0-SNAPSHOT"

var lwjglJarsPath = "libs/lwjgl-2.9.3/jar/"
var slickPath = "libs/slick/"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.1.0")
    implementation(files(slickPath + "slick-util.jar", slickPath + "slick2d-core-1.0.2.jar", slickPath + "jogg-0.0.7.jar", slickPath + "jorbis-0.0.15.jar"))
    implementation(files(lwjglJarsPath + "AppleJavaExtensions.jar", lwjglJarsPath + "asm-debug-all.jar", lwjglJarsPath + "jinput.jar",
        lwjglJarsPath + "lwjgl.jar", lwjglJarsPath + "lwjgl_test.jar", lwjglJarsPath + "lwjgl_util.jar", lwjglJarsPath + "lwjgl_util_applet.jar",
        lwjglJarsPath + "lwjgl-debug.jar", lwjglJarsPath + "lzma.jar", lwjglJarsPath + "substance-7.2.1.jar", lwjglJarsPath + "trident-7.2.1.jar",
        lwjglJarsPath + "laf-plugin-7.21.jar", lwjglJarsPath + "laf-widget-7.2.1.jar"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}