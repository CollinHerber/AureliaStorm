package com.github.denofevil.aurelia
import com.intellij.codeInspection.XmlSuppressionProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import org.jetbrains.annotations.NotNull


class HtmlXmlSuppressionProvider : XmlSuppressionProvider() {
    override fun isProviderAvailable(@NotNull file: PsiFile): Boolean {
        return true
    }

    override fun isSuppressedFor(@NotNull element: PsiElement, @NotNull inspectionId: String): Boolean {
        if (inspectionId == "HtmlUnknownTag" && element is XmlToken && element.node.elementType === XmlTokenType.XML_NAME) {
            val text = element.text
            for (customElement in Aurelia.CUSTOM_ELEMENTS) {
                if (text.contains(customElement)) return true;
            }
        }
        return false
    }

    override fun suppressForFile(@NotNull element: PsiElement, @NotNull inspectionId: String) {}
    override fun suppressForTag(@NotNull element: PsiElement, @NotNull inspectionId: String) {}
}
