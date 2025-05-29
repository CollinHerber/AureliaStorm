package com.github.denofevil.aurelia.require

import com.github.denofevil.aurelia.Aurelia
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext

/**
 * Registers the ReferenceProviders for <require> or <import> tags
 */
class AureliaRequireReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        Aurelia.IMPORT_ELEMENTS.forEach { element ->
            registrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue("from")
                    .with(object : PatternCondition<XmlAttributeValue>("ExcludeCSS") {
                        override fun accepts(attribute: XmlAttributeValue, context: ProcessingContext?): Boolean {
                            if (!Aurelia.isFrameworkCandidate(attribute)) return false
                            // Currently css imports references are not properly detected :(
                            return !attribute.value.endsWith(".css") && !attribute.value.endsWith(".scss")
                        }
                    })
                    .inside(XmlPatterns.xmlTag().withName(element)),
                AureliaRequireReferenceProvider()
            )
        }
    }
}