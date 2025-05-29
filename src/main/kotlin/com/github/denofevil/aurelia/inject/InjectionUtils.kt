package com.github.denofevil.aurelia.inject

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.AureliaFileUtil
import com.intellij.codeInsight.completion.CompletionUtil
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.resolve.FileContextUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

object InjectionUtils {

    fun isAureliaInjected(element: PsiElement): Boolean {
        return findInjectionHost(element) != null
    }

    fun findInjectionHost(element: PsiElement): PsiElement? {
        if (element.language !is JavascriptLanguage) return null // only js can be injected
        val wasInjected = InjectedLanguageManager.getInstance(element.project).isInjectedFragment(element.containingFile)
        // Only injected elements shall be taken into consideration for this
        if (wasInjected) {
            val host = InjectedLanguageManager.getInstance(element.project).getInjectionHost(element)
            if (!Aurelia.isPresentFor(host?.containingFile)) return null
            if (host?.containingFile?.fileType is HtmlFileType) return host
        }
        return null
    }

    fun findParentRepeatForTag(element: PsiElement, variableName: String? = null): XmlTag? {
        return PsiTreeUtil.findFirstParent(element) { parent ->
            val tag = (parent as? XmlTag)?.attributes?.firstOrNull { Aurelia.REPEAT_FOR.contains(it.name) } ?: return@findFirstParent false
            if (variableName == null) return@findFirstParent true
            return@findFirstParent tag.value?.contains("$variableName ") == true
        } as? XmlTag?
    }

    fun findParentRepeatForAttribute(element: PsiElement, variableName: String? = null): XmlAttribute? {
        return findParentRepeatForTag(element)?.attributes?.firstOrNull {
            Aurelia.REPEAT_FOR.contains(it.name) && it.value?.contains("$variableName ") == true
        }
    }

    fun findControllerOfInjectedElement(context: PsiElement): JSClass? {
        if (context !is JSReferenceExpression || context.qualifier != null) {
            return null
        }
        if (!Aurelia.isPresentFor(context.getProject())) return null

        val original = CompletionUtil.getOriginalOrSelf<PsiElement>(context)
        val hostFile =
            FileContextUtil.getContextFile(if (original !== context) original else context.getContainingFile().originalFile) ?: return null

        return AureliaFileUtil.findControllerClassOfHtmlFile(hostFile)
    }
}