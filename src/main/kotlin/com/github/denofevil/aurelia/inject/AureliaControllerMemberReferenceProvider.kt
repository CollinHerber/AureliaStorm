package com.github.denofevil.aurelia.inject

import com.github.denofevil.aurelia.AureliaClassUtil
import com.github.denofevil.aurelia.config.AureliaSettings
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import java.util.stream.Collectors

/**
 * Provides references for all members of the controller class
 */
class AureliaControllerMemberReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: com.intellij.util.ProcessingContext): Array<PsiReference> {
        if (DumbService.getInstance(element.project).isDumb) return PsiReference.EMPTY_ARRAY
        if (!AureliaSettings.getInstance().jsInjectionEnabled) return PsiReference.EMPTY_ARRAY
        val refExpr = element as? JSReferenceExpression ?: return PsiReference.EMPTY_ARRAY
        val name = refExpr.referenceName ?: return PsiReference.EMPTY_ARRAY
        val controller = InjectionUtils.findControllerOfInjectedElement(element) ?: return PsiReference.EMPTY_ARRAY

        val resolvedMembers = AureliaClassUtil.getAllMembersByName(controller, name)
        if (resolvedMembers.isEmpty()) return PsiReference.EMPTY_ARRAY
        return resolvedMembers.stream().map {
            object : PsiReferenceBase<JSReferenceExpression>(refExpr) {
                override fun resolve(): PsiElement = it
                override fun getVariants(): Array<Any> = emptyArray()
            }
        }.collect(Collectors.toList()).toTypedArray()
    }
}
