package com.github.denofevil.aurelia.hook

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.AureliaFileUtil
import com.github.denofevil.aurelia.config.AureliaBundle
import com.github.denofevil.aurelia.index.AureliaIndexUtil
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.lang.javascript.JSElementType
import com.intellij.lang.javascript.JSKeywordElementType
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement

/**
 * Line markers to show aurelia hooks
 */
class AureliaHookLineMarkerProvider : LineMarkerProvider {

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        super.collectSlowLineMarkers(elements, result)
        for (element in elements) {
            if (DumbService.getInstance(element.project).isDumb) return
            collectLineMarkerInfo(element)?.let { result.add(it) }
        }
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        return null
    }

    private fun collectLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element !is LeafPsiElement || element.elementType !is JSElementType) return null
        if (!Aurelia.isFrameworkCandidate(element)) return null

        if (element.parent is JSClass && element.elementType is JSKeywordElementType) {
            val jsClass = element.parent as JSClass
            checkAnnotation(jsClass, element)?.let { return it }
        }
        if (element.parent is JSFunction) {
            val method = element.parent as JSFunction
            checkLifecycleMethod(method, element)?.let { return it }
            checkChangeHandler(method, element)?.let { return it }
        }
        return null
    }

    private fun checkLifecycleMethod(method: JSFunction, element: PsiElement): LineMarkerInfo<*>? {
        if (!HookUtil.isLifecycleMethod(method)) return null

        return NavigationGutterIconBuilder.create(Aurelia.HOOK_ICON)
            .setTooltipText(AureliaBundle.get("hint.lifecycleMethod"))
            .setTarget(method.parent)
            .createLineMarkerInfo(element)
    }

    private fun checkChangeHandler(method: JSFunction, element: PsiElement): LineMarkerInfo<*>? {
        val observable = HookUtil.findChangeCallbackTarget(method) ?: return null

        return NavigationGutterIconBuilder.create(Aurelia.OBSERVER_ICON)
            .setTooltipText(AureliaBundle.get("hint.observableCallbackMethod"))
            .setTarget(observable)
            .createLineMarkerInfo(element)
    }

    private fun checkAnnotation(jsClass: JSClass, element: PsiElement): LineMarkerInfo<*>? {
        if (AureliaIndexUtil.isCustomAttributeClass(jsClass)) {
            return NavigationGutterIconBuilder.create(Aurelia.ANNOTATION_ICON)
                .setTooltipText(AureliaBundle.get("hint.customAttribute"))
                .setTarget(jsClass)
                .createLineMarkerInfo(element)
        }
        if (AureliaIndexUtil.isCustomElementClass(jsClass)) {
            val target = AureliaFileUtil.findViewOfControllerFile(jsClass.containingFile) ?: jsClass
            return NavigationGutterIconBuilder.create(Aurelia.ANNOTATION_ICON)
                .setTooltipText(AureliaBundle.get("hint.customElement"))
                .setTarget(target)
                .createLineMarkerInfo(element)
        }
        return null
    }
}
