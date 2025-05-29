package com.github.denofevil.aurelia

import com.github.denofevil.aurelia.config.AureliaSettings
import com.github.denofevil.aurelia.inject.InjectionUtils
import com.intellij.lang.javascript.index.FrameworkIndexingHandler
import com.intellij.lang.javascript.psi.JSQualifiedNameImpl
import com.intellij.lang.javascript.psi.resolve.JSTypeInfo
import com.intellij.lang.javascript.psi.types.JSContext
import com.intellij.lang.javascript.psi.types.JSNamedTypeFactory
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement

/**
 * Provides context (resolved controller classes) to code injections
 */
class FrameworkHandler : FrameworkIndexingHandler() {
    override fun addContextType(info: JSTypeInfo, context: PsiElement) {
        if (DumbService.getInstance(context.project).isDumb) return
        if (!AureliaSettings.getInstance().jsInjectionEnabled) return
        val controller = InjectionUtils.findControllerOfInjectedElement(context) ?: return
        val namespace = JSQualifiedNameImpl.buildProvidedNamespace(controller)
        info.addType(JSNamedTypeFactory.createNamespace(namespace, JSContext.INSTANCE, null, true), false)
    }

    override fun addContextNames(context: PsiElement, names: MutableList<String>) {
        if (!AureliaSettings.getInstance().jsInjectionEnabled) return
        val controller = InjectionUtils.findControllerOfInjectedElement(context) ?: return
        names.add(controller.qualifiedName!!)
    }
}
