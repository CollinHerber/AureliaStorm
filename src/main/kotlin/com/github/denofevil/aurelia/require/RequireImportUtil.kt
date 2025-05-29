package com.github.denofevil.aurelia.require

import com.github.denofevil.aurelia.Aurelia
import com.intellij.lang.javascript.frameworks.commonjs.CommonJSUtil
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlFile
import com.intellij.util.asSafely

/**
 * Utility for resolving imports
 */
object RequireImportUtil {

    fun resolveImportByPath(element: PsiElement, importPath: String): List<JSFile> {
        return (CommonJSUtil.resolveReferencedElements(element, importPath)
                // some projects require src/ prefix for correctly resolving local references
                + CommonJSUtil.resolveReferencedElements(element, "src/${importPath}")).distinct()
    }

    fun resolveImportByName(element: PsiElement, componentName: String): List<JSFile> {
        val file = element.containingFile.asSafely<XmlFile>() ?: return emptyList()
        val imports: List<String> = findRequireImports(file, componentName).map { it.replace(".", "") }
        return imports.map { resolveImportByPath(element, it) }.flatten()
    }

    private fun findRequireImports(xmlFile: XmlFile, componentName: String): List<String> {
        val rootTag = xmlFile.rootTag ?: return emptyList()
        // Find <require> elements with a "from" attribute
        val requireTags = Aurelia.IMPORT_ELEMENTS.map { rootTag.findSubTags(it).toList() }.flatten()
        return requireTags.filter { it.getAttributeValue(Aurelia.IMPORT_ELEMENT_ATTRIBUTE) != null }.map { it.getAttributeValue("from")!! }
            .filter {
                return@filter it.endsWith("/$componentName")
            }
    }
}