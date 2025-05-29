package com.github.denofevil.aurelia.hook

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement

class AureliaHookUsageProvider : ImplicitUsageProvider {
    override fun isImplicitUsage(element: PsiElement): Boolean {
        if (DumbService.getInstance(element.project).isDumb) return false
        val method = element as? JSFunction ?: return false
        return HookUtil.isLifecycleMethod(method) || HookUtil.isChangeCallback(method)
    }

    override fun isImplicitRead(element: PsiElement): Boolean = false
    override fun isImplicitWrite(element: PsiElement): Boolean = false
}