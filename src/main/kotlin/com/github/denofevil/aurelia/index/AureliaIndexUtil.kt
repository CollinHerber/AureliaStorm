package com.github.denofevil.aurelia.index

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.config.AureliaBundle
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.asSafely
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID

/**
 * Utility class for reading file indexes
 */
object AureliaIndexUtil {

    fun resolveCustomElementClasses(componentName: String, project: Project): List<JSClass> {
        if (!canUseIndexes(project)) return emptyList()
        return resolveAnnotatedClasses(componentName, project, CUSTOM_ELEMENT_INDEX_KEY) {
            resolveClassCustomElementName(it)
        }
    }

    fun getAllCustomElementNames(project: Project): Collection<String> {
        if (!canUseIndexes(project)) return emptyList()
        return FileBasedIndex.getInstance().getAllKeys(CUSTOM_ELEMENT_INDEX_KEY, project).filter {
            FileBasedIndex.getInstance().getContainingFiles(
                CUSTOM_ELEMENT_INDEX_KEY, it, GlobalSearchScope.projectScope(project)
            ).isNotEmpty()
        }
    }

    fun resolveCustomAttributeClasses(attributeName: String, project: Project): List<JSClass> {
        if (!canUseIndexes(project)) return emptyList()
        return resolveAnnotatedClasses(attributeName, project, CUSTOM_ATTRIBUTE_INDEX_KEY) {
            resolveClassCustomAttributeName(it)
        }
    }

    fun getAllCustomAttributeNames(project: Project): Collection<String> {
        if (!canUseIndexes(project)) return emptyList()
        return FileBasedIndex.getInstance().getAllKeys(CUSTOM_ATTRIBUTE_INDEX_KEY, project).filter {
            FileBasedIndex.getInstance().getContainingFiles(
                CUSTOM_ATTRIBUTE_INDEX_KEY, it, GlobalSearchScope.projectScope(project)
            ).isNotEmpty()
        }
    }

    private fun canUseIndexes(project: Project): Boolean {
        val dumbService = DumbService.getInstance(project)
        if (dumbService.isDumb) {
            dumbService.showDumbModeNotificationForAction(
                AureliaBundle.get("index.notReadyYet"),
                "aurelia.index.read"
            )
            return false
        }
        return true
    }

    private fun resolveAnnotatedClasses(
        searchName: String,
        project: Project,
        index: ID<String, Void>,
        resolver: (JSClass) -> String?
    ): List<JSClass> {
        val result = mutableListOf<JSClass>()
        val scope = GlobalSearchScope.allScope(project)

        FileBasedIndex.getInstance().getContainingFiles(index, searchName.lowercase(), scope)
            .forEach { virtualFile ->
                val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
                psiFile?.accept(object : PsiRecursiveElementVisitor() {
                    override fun visitElement(jsClass: PsiElement) {
                        super.visitElement(jsClass)
                        if (jsClass is JSClass) {
                            if (resolver(jsClass)?.lowercase() == searchName.lowercase()) {
                                result.add(jsClass)
                            }
                        }
                    }
                })
            }
        return result
    }

    fun isCustomAttributeClass(jsClass: JSClass): Boolean = resolveClassCustomAttributeName(jsClass) != null
    fun isCustomElementClass(jsClass: JSClass): Boolean = resolveClassCustomElementName(jsClass) != null

    fun resolveClassCustomAttributeName(jsClass: JSClass): String? {
        return resolveClassNameByAnnotation(jsClass, Aurelia.CustomAttribute.ANNOTATION, Aurelia.CustomAttribute.CLASS_SUFFIX)
    }

    fun resolveClassCustomElementName(jsClass: JSClass): String? {
        return resolveClassNameByAnnotation(jsClass, Aurelia.CustomElement.ANNOTATION, Aurelia.CustomElement.CLASS_SUFFIX)
    }

    private fun resolveClassNameByAnnotation(
        jsClass: JSClass,
        annotationName: String,
        nameSuffix: String,
        annotationProperty: String = "name"
    ): String? {
        val annotation = jsClass.attributeList?.decorators?.find { it.decoratorName == annotationName }
        val value = annotation?.expression?.asSafely<JSCallExpression>()?.argumentList?.arguments?.firstOrNull()
        val strValue = value?.asSafely<JSLiteralExpression>()?.stringValue
            ?: value?.asSafely<JSObjectLiteralExpression>()?.findProperty(annotationProperty)
                ?.value?.asSafely<JSLiteralExpression>()?.stringValue
        strValue?.let { return it }
        jsClass.name?.takeIf { it.endsWith(nameSuffix) }?.substringBeforeLast(nameSuffix)?.let { name ->
            name.takeIf { it.isNotEmpty() }?.let { return Aurelia.camelToKebabCase(it) }
        }
        return null
    }
}