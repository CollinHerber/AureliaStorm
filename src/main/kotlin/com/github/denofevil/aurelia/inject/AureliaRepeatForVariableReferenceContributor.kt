package com.github.denofevil.aurelia.inject

import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext

/**
 * Registers the ReferenceProviders for references that are coming from "repeat.for" attributes
 */
class AureliaRepeatForVariableReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            JSPatterns.jsReferenceExpression()
                .inVirtualFile(PlatformPatterns.virtualFile().withExtension("html"))
                .with(object : PatternCondition<JSReferenceExpression>("wasInjected") {
                    override fun accepts(expression: JSReferenceExpression, context: ProcessingContext?): Boolean {
                        return InjectionUtils.isAureliaInjected(expression)
                    }
                }),
            AureliaRepeatForVariableReferenceProvider()
        )
    }
}