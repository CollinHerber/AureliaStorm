package com.github.denofevil.aurelia.attribute

import com.github.denofevil.aurelia.Aurelia
import com.intellij.psi.PsiElement
import com.intellij.psi.meta.PsiPresentableMetaData
import com.intellij.psi.xml.XmlElement
import com.intellij.util.ArrayUtil
import com.intellij.xml.XmlAttributeDescriptor

/**
 * Describes basic aurelia attributes (without declaration)
 */
open class AureliaAttributeDescriptor(
    private val name: String,
    private val isFlagAttribute: Boolean = false
) : XmlAttributeDescriptor, PsiPresentableMetaData {

    override fun getIcon() = Aurelia.ICON

    override fun getTypeName(): String? = if (isFlagAttribute) "boolean" else null

    override fun init(psiElement: PsiElement) {
    }

    override fun isRequired(): Boolean = false
    override fun hasIdType(): Boolean = false
    override fun hasIdRefType(): Boolean = false
    override fun isEnumerated(): Boolean = false
    override fun getDeclaration(): PsiElement? = null
    override fun getName(p0: PsiElement?): String = name
    override fun getName(): String = name
    override fun isFixed(): Boolean = isFlagAttribute
    override fun getDefaultValue(): String? = null
    override fun getEnumeratedValues(): Array<String>? = ArrayUtil.EMPTY_STRING_ARRAY
    override fun validateValue(p0: XmlElement?, p1: String?): String? = null
}