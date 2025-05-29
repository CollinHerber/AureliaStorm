package com.github.denofevil.aurelia.hook

import com.github.denofevil.aurelia.Aurelia
import com.intellij.lang.javascript.psi.JSElement
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeListOwner
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object HookUtil {
    fun isLifecycleMethod(method: JSFunction): Boolean {
        val methodClass = PsiTreeUtil.findFirstParent(method) { it is JSClass } as JSClass? ?: return false
        // Aurelia treats any exported JavaScript class as a component by default
        if (methodClass.isExported) {
            return Aurelia.LIFECYCLE_METHODS.contains(method.name)
        }
        return false
    }

    fun isChangeCallback(method: JSFunction): Boolean = findChangeCallbackTarget(method) != null

    fun findChangeCallbackTarget(method: JSFunction): PsiElement? {
        if (method.name?.endsWith("Changed") != true) return null
        val methodClass = method.parent as? JSClass ?: return null
        return methodClass.members.firstOrNull { hasObservableAnnotation(it) && it.name == method.name?.substringBeforeLast("Changed") }
    }

    private fun hasObservableAnnotation(member: JSElement): Boolean {
        if (member !is JSAttributeListOwner) return false
        return member.attributeList?.decorators?.any { Aurelia.OBSERVABLE_ANNOTATIONS.contains(it.decoratorName) } ?: false
    }
}