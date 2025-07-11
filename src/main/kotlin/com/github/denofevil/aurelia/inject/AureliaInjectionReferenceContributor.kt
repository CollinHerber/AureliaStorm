package com.github.denofevil.aurelia.inject

import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext

/**
 * Registers the ReferenceProviders for injected members
 */
class AureliaInjectionReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            jsInjectedReferenceExpressionPattern(),
            AureliaControllerMemberReferenceProvider()
        )
        registrar.registerReferenceProvider(
            jsInjectedReferenceExpressionPattern(),
            AureliaRepeatForVariableReferenceProvider()
        )
    }

    private fun jsInjectedReferenceExpressionPattern() = JSPatterns.jsReferenceExpression()
        .inVirtualFile(PlatformPatterns.virtualFile().withExtension("html"))
        .with(object : PatternCondition<JSReferenceExpression>("wasInjected") {
            override fun accepts(expression: JSReferenceExpression, context: ProcessingContext?): Boolean {
                return InjectionUtils.isAureliaInjected(expression)
            }
        })
}