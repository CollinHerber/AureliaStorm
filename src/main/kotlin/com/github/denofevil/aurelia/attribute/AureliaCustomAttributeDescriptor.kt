package com.github.denofevil.aurelia.attribute

import com.intellij.psi.PsiElement

/**
 * Describes custom attributes
 */
open class AureliaCustomAttributeDescriptor(
    name: String,
    private val declaration: PsiElement?,
    isFixed: Boolean = false
) : AureliaAttributeDescriptor(name, isFixed) {

    override fun getDeclaration(): PsiElement? = declaration
}