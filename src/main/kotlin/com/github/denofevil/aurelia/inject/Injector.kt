package com.github.denofevil.aurelia.inject

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.config.AureliaSettings
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.impl.source.xml.XmlTextImpl
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlElementType
import com.intellij.psi.xml.XmlTokenType
import kotlin.math.max
import kotlin.math.min

/**
 * Injects js/ts code into HTML files
 */
class Injector : MultiHostInjector {
    private val injectBeforeAll = "var \$this;var \$parent;"
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, host: PsiElement) {
        if (!AureliaSettings.getInstance().jsInjectionEnabled) return
        if (!Aurelia.isPresentFor(host.project)) return

        val range = ElementManipulators.getValueTextRange(host)
        if (host is XmlAttributeValue) {
            val parent = host.getParent()
            if (parent is XmlAttribute) {
                val name = parent.name

                if (Aurelia.REPEAT_FOR.contains(name)) {
                    val trimmedRange = rangeWithoutBindingBehaviours(host.text, range)
                    registrar.startInjecting(JavascriptLanguage)
                        .addPlace("${injectBeforeAll}for(var ", ")", host as PsiLanguageInjectionHost, trimmedRange)
                        .doneInjecting()
                    return
                }
                for (attr in Aurelia.INJECTABLE) {
                    var before: String? = injectBeforeAll
                    if (InjectionUtils.findParentRepeatForTag(host) != null) {
                        // events have a $event parameter
                        before += "var \$index;var \$first;var \$last;var \$odd;var \$even;"
                    }
                    if (Aurelia.EVENT_BINDINGS.contains(attr)) {
                        // events have a $event parameter
                        before += "var \$event;"
                    }
                    if (Aurelia.PROPERTY_BINDINGS.contains(attr)) {
                        // bindings should be values or lambdas
                        before += "\$this['__binding__']="
                    }
                    if (name.endsWith(".$attr")) {
                        val trimmedRange = rangeWithoutBindingBehaviours(host.text, range)
                        registrar.startInjecting(JavascriptLanguage)
                            .addPlace(before, null, host as PsiLanguageInjectionHost, trimmedRange)
                            .doneInjecting()
                        return
                    }
                }
            }
        }
        injectInXmlTextByDelimiters(registrar, host)
    }

    private fun injectInXmlTextByDelimiters(registrar: MultiHostRegistrar, context: PsiElement, start: String = "\${", end: String = "}") {
        if (context.textContains(start[0])) {
            val text: String = context.text
            var previousSearchStart = -1
            var searchStart = 0

            while (searchStart > previousSearchStart && searchStart < text.length) {
                previousSearchStart = searchStart
                var startIdx: Int = text.indexOf(start, searchStart)
                if (startIdx < 0) {
                    return
                }

                startIdx += start.length
                var endIndex = findMatchingEnd(start, end, text, startIdx)
                endIndex = if (endIndex > 0) endIndex else ElementManipulators.getValueTextRange(context).endOffset
                searchStart = max((endIndex + 1).toDouble(), startIdx.toDouble()).toInt()
                if (startIdx < endIndex) {
                    var injectionCandidate: PsiElement?
                    injectionCandidate = context.findElementAt(startIdx)
                    while (injectionCandidate is PsiWhiteSpace) {
                        injectionCandidate = injectionCandidate.getNextSibling()
                    }

                    if (injectionCandidate != null && injectionCandidate.startOffsetInParent <= endIndex && !XmlTokenType.COMMENTS.contains(
                            injectionCandidate.node.elementType
                        ) && injectionCandidate.node.elementType !== XmlElementType.XML_COMMENT && (injectionCandidate !is OuterLanguageElement)
                    ) {
                        var before: String? = injectBeforeAll
                        if (InjectionUtils.findParentRepeatForTag(context) != null) {
                            // events have a $event parameter
                            before += "var \$index;"
                        }
                        val range = rangeWithoutBindingBehaviours(text.substring(startIdx, endIndex), TextRange(startIdx, endIndex))
                        registrar.startInjecting(JavascriptLanguage)
                            .addPlace(before, null, context as PsiLanguageInjectionHost, range)
                            .doneInjecting()
                    }
                }
            }
        }
    }

    private fun findMatchingEnd(startSymbol: String, endSymbol: String, text: String, afterStartIdx: Int): Int {
        if (afterStartIdx < 0) {
            return -1
        } else {
            var totalNumStarts = 1
            var lookFrom = afterStartIdx

            while (totalNumStarts > 0) {
                --totalNumStarts
                val nextEndIdx = text.indexOf(endSymbol, lookFrom)
                if (nextEndIdx == -1) {
                    return -1
                }

                val numStarts = getOccurrenceCount(text, lookFrom, nextEndIdx, startSymbol)
                if (numStarts > 0) {
                    totalNumStarts += numStarts
                }

                lookFrom = nextEndIdx + endSymbol.length
                if (totalNumStarts == 0) {
                    return nextEndIdx
                }
            }

            return -1
        }
    }

    private fun getOccurrenceCount(text: String, from: Int, toExcluding: Int, s: String): Int {
        var res = 0
        var i = from

        val limit = min(text.length.toDouble(), toExcluding.toDouble()).toInt()
        while (i < limit) {
            i = text.indexOf(s, i)
            if (i < 0 || i >= limit) {
                break
            }
            ++res
            i += s.length
        }

        return res
    }

    private fun rangeWithoutBindingBehaviours(text: String, range: TextRange): TextRange {
        //https://regex101.com/r/yXvK41/1
        val bindingPattern = Regex(" [&|] .*$")
        val matchResult = bindingPattern.find(text)
        if (matchResult != null) {
            val stopIndex = matchResult.range.first  // Get the index where the match starts
            return TextRange(range.startOffset, range.startOffset + stopIndex)
        }
        return range
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> {
        return listOf(XmlTextImpl::class.java, XmlAttributeValueImpl::class.java)
    }
}
