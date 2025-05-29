package com.github.denofevil.aurelia.require

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag

/**
 * Reference for "from" attributes of <require from=""> tags
 */
class AureliaRequireReference(attribute: XmlAttributeValue) : PsiReferenceBase<XmlAttributeValue>(attribute) {

    override fun resolve(): PsiElement? {
        val tag = element.parent?.parent as? XmlTag ?: return null
        return RequireImportUtil.resolveImportByPath(tag, element.value).firstOrNull()
    }

    override fun getVariants(): Array<Any> = emptyArray()
}
