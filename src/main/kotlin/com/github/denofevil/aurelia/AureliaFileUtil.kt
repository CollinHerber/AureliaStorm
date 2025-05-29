package com.github.denofevil.aurelia

import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlFile

object AureliaFileUtil {

    fun findControllerClassOfHtmlFile(hostFile: PsiFile): JSClass? {
        val directory = hostFile.originalFile.parent ?: return null
        val name = FileUtil.getNameWithoutExtension(hostFile.name)

        val controllerFile = directory.findFile("$name.ts") ?: directory.findFile("$name.js")
        return PsiTreeUtil.findChildOfType(controllerFile, JSClass::class.java)
    }

    fun findViewOfControllerFile(hostFile: PsiFile): XmlFile? {
        val directory = hostFile.originalFile.parent ?: return null
        val name = FileUtil.getNameWithoutExtension(hostFile.name)

        return directory.findFile("$name.html") as? XmlFile
    }
}