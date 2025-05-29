package com.github.denofevil.aurelia.element

import com.github.denofevil.aurelia.attribute.AureliaAttributeDescriptor
import com.intellij.psi.PsiElement
import com.intellij.xml.XmlAttributeDescriptor

/**
 * Describes bindable attributes
 */
class AureliaBindingAttributeDescriptor(
    attributeName: String,
    private val baseDescriptor: XmlAttributeDescriptor,
) : AureliaAttributeDescriptor(attributeName, false) {


    override fun getDeclaration(): PsiElement? = baseDescriptor.declaration
}