package com.github.denofevil.aurelia.element

import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

@TestDataPath("\$CONTENT_ROOT/src/test/test-data")
class AureliaCustomElementIntegrationTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/test-data"
    }

    fun testShouldFindCustomElementDeclaration_ResolvedByAnnotation() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-element.ts")
        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<annotation-custom-element></annotation-custom-element>")
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val provider = AureliaCustomElementDescriptorProvider()

        val descriptor = provider.getDescriptor(tag)
        assertNotNull(descriptor)
        val declaration = descriptor!!.declaration as? JSClass
        assertNotNull(declaration)
        TestCase.assertEquals("annotation-custom-element.ts", declaration!!.containingFile.name)
    }

    fun testShouldFindCustomElementDeclaration_ResolvedByName() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("name-custom-element.ts")
        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<name-custom-element></name-custom-element>")
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val provider = AureliaCustomElementDescriptorProvider()

        val descriptor = provider.getDescriptor(tag)
        assertNotNull(descriptor)
        val declaration = descriptor!!.declaration as? JSClass
        assertNotNull(declaration)
        TestCase.assertEquals("name-custom-element.ts", declaration!!.containingFile.name)
    }

    fun testShouldFindCustomElementDeclaration_resolvedMultipleCandidatesByRequire() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-element.ts", "a/annotation-custom-element.ts")
        myFixture.copyFileToProject("annotation-custom-element.ts", "b/annotation-custom-element.ts")
        myFixture.copyFileToProject("annotation-custom-element.ts", "c/annotation-custom-element.ts")
        val psiFile = myFixture.configureByText(
            XmlFileType.INSTANCE, """
            <template>
                <require from="./b/annotation-custom-element"></require>
                <annotation-custom-element></annotation-custom-element>
            </template>
        """
        )
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!.findFirstSubTag("annotation-custom-element")!!
        val provider = AureliaCustomElementDescriptorProvider()

        val descriptor = provider.getDescriptor(tag)
        assertNotNull(descriptor)
        val declaration = descriptor!!.declaration as? JSClass
        assertNotNull(declaration)
        TestCase.assertEquals("b", declaration!!.containingFile.parent?.name)
    }

    fun testShouldIgnoreCustomElementDeclarationOutsideOfAureliaProjects() {
        myFixture.copyFileToProject("annotation-custom-element.ts")
        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<annotation-custom-element></annotation-custom-element>")
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val provider = AureliaCustomElementDescriptorProvider()

        val descriptor = provider.getDescriptor(tag)
        assertNull(descriptor)
    }
}