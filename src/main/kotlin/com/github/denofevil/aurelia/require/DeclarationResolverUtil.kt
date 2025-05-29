package com.github.denofevil.aurelia.require

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.config.AureliaSettings
import com.github.denofevil.aurelia.index.AureliaIndexUtil
import com.intellij.lang.javascript.psi.JSElement
import com.intellij.lang.javascript.psi.JSRecordType.PropertySignature
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeListOwner
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag

/**
 * Resolves declarations of custom elements and attributes by searching the project for matching files
 */
object DeclarationResolverUtil {

    fun resolveCustomElementDeclaration(tag: XmlTag): JSClass? {
        return resolveCustomElementDeclaration(tag, tag.name)
    }

    private fun resolveCustomElementDeclaration(element: PsiElement, name: String): JSClass? {
        if (DumbService.getInstance(element.project).isDumb) return null
        val candidates = AureliaIndexUtil.resolveCustomElementClasses(name, element.project)
        return resolveClassDeclaration(element, name, candidates)
    }

    fun resolveBindableAttributesOnlyWithAnnotation(jsClass: JSClass?): List<PropertySignature> {
        if (jsClass == null) return emptyList()
        if (DumbService.getInstance(jsClass.project).isDumb) return emptyList()
        return CachedValuesManager.getCachedValue(jsClass) {
            val resolvedAttributes = resolveBindableAttributesImpl(jsClass, true)
            CachedValueProvider.Result.create(
                resolvedAttributes,
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    fun resolveBindableAttributes(jsClass: JSClass?): List<PropertySignature> {
        jsClass ?: return emptyList()
        if (DumbService.getInstance(jsClass.project).isDumb) return emptyList()
        return CachedValuesManager.getCachedValue(jsClass) {
            val resolvedAttributes = resolveBindableAttributesImpl(jsClass, false)
            CachedValueProvider.Result.create(
                resolvedAttributes,
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun resolveClassDeclaration(element: PsiElement, name: String, annotatedClassCandidates: List<JSClass>): JSClass? {
        val importedClasses = RequireImportUtil.resolveImportByName(element, name).map { findClassesOf(it) }.flatten()
        importedClasses.firstOrNull { annotatedClassCandidates.contains(it) }?.let { return it }

        val module = ModuleUtil.findModuleForPsiElement(element)
        annotatedClassCandidates.firstOrNull { ModuleUtil.findModuleForPsiElement(it) == module }?.let { return it }
        return annotatedClassCandidates.firstOrNull()
    }


    private fun resolveBindableAttributesImpl(jsClass: JSClass, onlyWithAnnotation: Boolean = true): List<PropertySignature> {
        val members = arrayListOf<PropertySignature>()
        for (jsMember in jsClass.members) {
            val isWithoutAnnotation = !hasBindableAnnotation(jsMember)
            if (AureliaSettings.getInstance().checkPropertyBindableAnnotation && isWithoutAnnotation) continue
            if (onlyWithAnnotation && isWithoutAnnotation) continue
            if (jsMember is PropertySignature) {
                members.add(jsMember)
            }
        }
        return members
    }

    private fun findClassesOf(psiClass: PsiFile): Collection<JSClass> {
        return PsiTreeUtil.findChildrenOfType(psiClass, JSClass::class.java)
    }

    private fun hasBindableAnnotation(member: JSElement): Boolean {
        if (member !is JSAttributeListOwner) return false
        return member.attributeList?.decorators?.any { Aurelia.BINDABLE_ANNOTATIONS.contains(it.decoratorName) } ?: false
    }
}