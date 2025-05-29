package com.github.denofevil.aurelia.inject

import com.github.denofevil.aurelia.config.AureliaSettings
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.ProcessingContext
import junit.framework.TestCase

@TestDataPath("\$CONTENT_ROOT/src/test/test-data")
class AureliaInjectionIntegrationTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/test-data"
    }

    fun testShouldInjectBinding() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-element.ts", "annotation-custom-element.ts")
        AureliaSettings.getInstance().jsInjectionEnabled = true
        val psiFile = myFixture.configureByText(
            HtmlFileType.INSTANCE, """
            <annotation-custom-element my-prop.bind="name"></annotation-custom-element>
        """
        )
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val attribute = tag.attributes.first { it.name.contains("my-prop") }
        val injectedPsi = InjectedLanguageManager.getInstance(attribute.project)
            .getInjectedPsiFiles(attribute.valueElement!!)
            ?.firstOrNull()
            ?.first as? PsiFile
        val reference = PsiTreeUtil.findChildrenOfType(injectedPsi, JSReferenceExpression::class.java).firstOrNull { it.text == "name" }

        TestCase.assertNotNull(reference)
        TestCase.assertTrue(InjectionUtils.isAureliaInjected(reference!!))
    }

    fun testShouldInjectExpression() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-element.ts", "annotation-custom-element.ts")
        AureliaSettings.getInstance().jsInjectionEnabled = true
        val psiFile = myFixture.configureByText(
            HtmlFileType.INSTANCE, "<template>\${var test = 'test'}</template>"
        )
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val value: XmlText = PsiTreeUtil.findChildOfType(tag, XmlText::class.java)!!
        val injectedPsi = InjectedLanguageManager.getInstance(tag.project)
            .getInjectedPsiFiles(value)
            ?.firstOrNull()
            ?.first as? PsiFile
        val reference = PsiTreeUtil.findChildOfType(injectedPsi, JSExpression::class.java)

        TestCase.assertNotNull(reference)
        TestCase.assertTrue(InjectionUtils.isAureliaInjected(reference!!))
    }

    fun testShouldResolveRepeatForVariableReference() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-element.ts", "annotation-custom-element.ts")
        AureliaSettings.getInstance().jsInjectionEnabled = true
        val psiFile = myFixture.configureByText(
            HtmlFileType.INSTANCE, """
            <template repeat.for="name of ['a','b']">
                <annotation-custom-element my-prop.bind="name"></annotation-custom-element>
            </template>
        """
        )
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!.findFirstSubTag("annotation-custom-element")!!
        val attribute = tag.attributes.first { it.name == "my-prop.bind" }
        val injectedPsi = InjectedLanguageManager.getInstance(attribute.project)
            .getInjectedPsiFiles(attribute.valueElement!!)
            ?.firstOrNull()
            ?.first as? PsiFile
        val reference = PsiTreeUtil.findChildrenOfType(injectedPsi, JSReferenceExpression::class.java).firstOrNull { it.text == "name" }

        TestCase.assertNotNull(reference)
        TestCase.assertTrue(InjectionUtils.isAureliaInjected(reference!!))

        val referenceProvider = AureliaRepeatForVariableReferenceProvider()
        val resolvedReference = referenceProvider.getReferencesByElement(reference, ProcessingContext())

        TestCase.assertTrue(resolvedReference.isNotEmpty())
        TestCase.assertNotNull(resolvedReference.first().resolve())
    }

    fun testShouldIgnoreInjectionsOutsideOfAureliaProjects() {
        myFixture.copyFileToProject("annotation-custom-element.ts", "annotation-custom-element.ts")
        AureliaSettings.getInstance().jsInjectionEnabled = true
        val psiFile = myFixture.configureByText(
            HtmlFileType.INSTANCE, """
            <annotation-custom-element my-prop.bind="name"></annotation-custom-element>
        """
        )
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val attribute = tag.attributes.first { it.name.contains("my-prop") }
        val injectedPsi = InjectedLanguageManager.getInstance(tag.project)
            .getInjectedPsiFiles(attribute.valueElement!!)
            ?.firstOrNull()
            ?.first as? PsiFile

        TestCase.assertNull(injectedPsi)
    }
}