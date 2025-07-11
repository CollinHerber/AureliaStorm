package com.github.denofevil.aurelia.completion

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.AureliaClassUtil
import com.github.denofevil.aurelia.config.AureliaSettings
import com.github.denofevil.aurelia.inject.InjectionUtils
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.openapi.project.DumbService
import com.intellij.util.ProcessingContext

class AureliaInjectedControllerMemberCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val expression = parameters.position.parent as? JSReferenceExpression ?: return
        if (DumbService.getInstance(expression.project).isDumb) return
        if (!AureliaSettings.getInstance().jsInjectionEnabled) return
        val host = InjectionUtils.findInjectionHost(expression) ?: return
        if(!Aurelia.isFrameworkCandidate(host)) return
        val controller = InjectionUtils.findControllerOfInjectedElement(expression) ?: return

        AureliaClassUtil.getAllMembers(controller).forEach {
            if(it.name != null){
                result.addElement(LookupElementBuilder.create(it.name!!).withIcon(it.getIcon(0)))
            }
        }
    }
}