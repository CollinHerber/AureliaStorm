package com.github.denofevil.aurelia.require

import com.intellij.lang.ecmascript6.resolve.JSFileReferencesUtil
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue

/**
 * Provides references for <require from=""> tags by resolving the from attribute
 */
class AureliaRequireReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: com.intellij.util.ProcessingContext): Array<PsiReference> {
        if (element is XmlAttributeValue) {
            val text = ElementManipulators.getValueText(element)
            val range = ElementManipulators.getValueTextRange(element)
            val reference = AureliaRequireReference(element)
            // TODO: this is a bit hacky ... find a better solution to get suggestions and correct error detection
            if (reference.resolve() != null) {
                return arrayOf(reference)
            }
            // this shows suggestions but produces wierd warnings as well
            return JSFileReferencesUtil.createImportExportFromClauseReferences(element, range.startOffset, text, this)
        }
        return PsiReference.EMPTY_ARRAY
    }
}
