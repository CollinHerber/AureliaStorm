package com.github.denofevil.aurelia.inject

import com.github.denofevil.aurelia.config.AureliaSettings
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.javascript.psi.JSForInStatement
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.openapi.project.DumbService
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil

/**
 * Provides references for variables that are coming from "repeat.for" attributes
 */
class AureliaRepeatForVariableReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: com.intellij.util.ProcessingContext): Array<PsiReference> {
        if (DumbService.getInstance(element.project).isDumb) return PsiReference.EMPTY_ARRAY
        if (!AureliaSettings.getInstance().jsInjectionEnabled) return PsiReference.EMPTY_ARRAY
        val refExpr = element as? JSReferenceExpression ?: return PsiReference.EMPTY_ARRAY
        val name = refExpr.referenceName ?: return PsiReference.EMPTY_ARRAY
        val injectionHost = InjectionUtils.findInjectionHost(element) ?: return PsiReference.EMPTY_ARRAY

        val repeatAttribute = InjectionUtils.findParentRepeatForAttribute(injectionHost, name) ?: return PsiReference.EMPTY_ARRAY
        val repeatAttributeValue = repeatAttribute.valueElement ?: return PsiReference.EMPTY_ARRAY
        val injectedPsi = InjectedLanguageManager.getInstance(repeatAttribute.project)
            .getInjectedPsiFiles(repeatAttributeValue)
            ?.firstOrNull()
            ?.first as? PsiFile
        val forStatement = PsiTreeUtil.findChildOfType(injectedPsi, JSForInStatement::class.java)
        val loopVariable = forStatement?.varDeclaration?.declarations?.firstOrNull() ?: return PsiReference.EMPTY_ARRAY

        return arrayOf(object : PsiReferenceBase<JSReferenceExpression>(refExpr) {
            override fun resolve(): PsiElement = loopVariable
            override fun getVariants(): Array<Any> = emptyArray()
        })
    }
}
