package com.github.denofevil.aurelia

import com.github.denofevil.aurelia.index.AureliaIndexUtil
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
        val candidates = PsiTreeUtil.findChildrenOfType(controllerFile, JSClass::class.java)
        candidates.firstOrNull{ AureliaIndexUtil.isCustomElementClass(it) }?.let { return it }
        candidates.firstOrNull{ it.isExported && !it.isInterface }?.let { return it }
        return candidates.firstOrNull()
    }

    fun findViewOfControllerFile(hostFile: PsiFile): XmlFile? {
        val directory = hostFile.originalFile.parent ?: return null
        val name = FileUtil.getNameWithoutExtension(hostFile.name)

        return directory.findFile("$name.html") as? XmlFile
    }
}