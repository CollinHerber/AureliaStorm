package com.github.denofevil.aurelia.attribute

import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

@TestDataPath("\$CONTENT_ROOT/src/test/test-data")
class AureliaCustomAttributeIntegrationTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/test-data"
    }

    fun testShouldFindCustomAttributeDeclaration_ResolvedByAnnotation() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-attribute.ts")
        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<div annotation-custom-attribute></div>")
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val provider = AureliaCustomAttributeDescriptorsProvider()

        val descriptor = provider.getAttributeDescriptor("annotation-custom-attribute", tag)
        assertNotNull(descriptor)
        val declaration = descriptor!!.declaration as? JSClass
        assertNotNull(declaration)
        TestCase.assertEquals("annotation-custom-attribute.ts", declaration!!.containingFile.name)
    }

    fun testShouldFindCustomElementDeclaration_ResolvedByName() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("name-custom-attribute.ts")
        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<div name-custom-attribute></div>")
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val provider = AureliaCustomAttributeDescriptorsProvider()

        val descriptor = provider.getAttributeDescriptor("name-custom-attribute", tag)
        assertNotNull(descriptor)
        val declaration = descriptor!!.declaration as? JSClass
        assertNotNull(declaration)
        TestCase.assertEquals("name-custom-attribute.ts", declaration!!.containingFile.name)
    }

    fun testShouldIgnoreCustomElementDeclarationOutsideOfAureliaProjects() {
        myFixture.copyFileToProject("annotation-custom-attribute.ts")
        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<div annotation-custom-attribute></div>")
        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)

        val tag: XmlTag = htmlFile.rootTag!!
        val provider = AureliaCustomAttributeDescriptorsProvider()

        val descriptor = provider.getAttributeDescriptors(tag).firstOrNull()
        assertNull(descriptor)
    }

}