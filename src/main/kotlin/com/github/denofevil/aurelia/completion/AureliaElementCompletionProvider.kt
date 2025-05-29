package com.github.denofevil.aurelia.completion

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.index.AureliaIndexUtil
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.xml.XmlText
import com.intellij.util.ProcessingContext

class AureliaElementCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val element = parameters.position.parent as? XmlText ?: return
        if (!Aurelia.isFrameworkCandidate(element)) return
        AureliaIndexUtil.getAllCustomElementNames(element.project).forEach {
            result.addElement(
                LookupElementBuilder.create(it).withIcon(Aurelia.CLASS_ICON)
                    .withInsertHandler { context, _ ->
                        context.editor.document.replaceString(context.startOffset, context.tailOffset, "<$it></$it>")
                        context.editor.caretModel.moveToOffset(context.startOffset + it.length + 1)
                    })
        }
    }
}