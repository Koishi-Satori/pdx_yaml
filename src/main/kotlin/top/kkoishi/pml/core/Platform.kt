package top.kkoishi.pml.core

import top.kkoishi.pml.exception.SyntaxException
import kotlin.jvm.Throws

object Platform {
    private val osName = System.getProperty("os.name").lowercase()
    val isLinux = osName.contains("linux") || osName.contains("bsd")
    val isWindows = osName.contains("windows")
    val isMacos = osName.contains("macos")
}