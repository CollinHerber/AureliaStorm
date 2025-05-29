package com.github.denofevil.aurelia.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlElement

class AureliaCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(XmlAttribute::class.java),
            AureliaAttributeCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(XmlAttribute::class.java),
            AureliaBindingCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(XmlElement::class.java),
            AureliaElementCompletionProvider()
        )
    }
}
