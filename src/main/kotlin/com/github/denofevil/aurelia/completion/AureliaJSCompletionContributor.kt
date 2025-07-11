package com.github.denofevil.aurelia.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.patterns.PlatformPatterns

class AureliaJSCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                .inVirtualFile(PlatformPatterns.virtualFile().withExtension("html"))
                .withParent(JSReferenceExpression::class.java),
            AureliaInjectedControllerMemberCompletionProvider()
        )
    }
}
