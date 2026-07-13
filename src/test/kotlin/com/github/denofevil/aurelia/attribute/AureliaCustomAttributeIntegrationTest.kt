package com.github.denofevil.aurelia.attribute

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.index.AureliaCustomAttributeIndex
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.lang.javascript.psi.ecmal4.JSClass
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.indexing.FileBasedIndexExtension
import io.mockk.every
import io.mockk.mockkObject
import junit.framework.TestCase

@TestDataPath("\$CONTENT_ROOT/src/test/test-data")
class AureliaCustomAttributeIntegrationTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/test-data"
    }

    override fun setUp() {
        super.setUp()
        PluginManagerCore.enablePlugin(PluginId("Javascript"))
        PluginManagerCore.enablePlugin(PluginId("AureliaStormCommunity"))
    }

    fun testCustomAttribute() {

    }

//    fun testIndexIsRegistered() {
//        PluginManagerCore.enablePlugin(PluginId("Javascript"))
//        PluginManagerCore.enablePlugin(PluginId("AureliaStormCommunity"))
//        val indexes = FileBasedIndexExtension.EXTENSION_POINT_NAME.extensionList
//
//        DumbService.getInstance(project).waitForSmartMode()
//
//        assertTrue(
//            PluginManagerCore.getPluginSet().enabledPlugins.any {
//                it.pluginId.idString == "AureliaStormCommunity"
//            }
//        )
//        assertTrue(
//            indexes.any {
//                it is AureliaCustomAttributeIndex
//            }
//        )
//    }
//
//    fun testShouldFindCustomAttributeDeclaration_ResolvedByAnnotation() {
//        mockkObject(Aurelia)
//
//        every { Aurelia.isPresentFor(any<PsiFile>()) } returns true
//        every { Aurelia.isPresentFor(any<Project>()) } returns true
//        every { Aurelia.isFrameworkCandidate(any()) } returns true
//
//        myFixture.copyFileToProject("package.json")
//        myFixture.copyFileToProject("annotation-custom-attribute.ts")
//        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<div annotation-custom-attribute></div>")
//        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)
//
//        DumbService.getInstance(project).waitForSmartMode()
//
//        val tag: XmlTag = htmlFile.rootTag!!
//        val provider = AureliaCustomAttributeDescriptorsProvider()
//        val descriptor = provider.getAttributeDescriptor("annotation-custom-attribute", tag)
//        assertNotNull(descriptor)
//        val declaration = descriptor!!.declaration as? JSClass
//        assertNotNull(declaration)
//        TestCase.assertEquals("annotation-custom-attribute.ts", declaration!!.containingFile.name)
//    }
//
//    fun testShouldFindCustomElementDeclaration_ResolvedByName() {
//        myFixture.copyFileToProject("package.json")
//        myFixture.copyFileToProject("name-custom-attribute.ts")
//        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<div name-custom-attribute></div>")
//        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)
//
//        val tag: XmlTag = htmlFile.rootTag!!
//        val provider = AureliaCustomAttributeDescriptorsProvider()
//
//        val descriptor = provider.getAttributeDescriptor("name-custom-attribute", tag)
//        assertNotNull(descriptor)
//        val declaration = descriptor!!.declaration as? JSClass
//        assertNotNull(declaration)
//        TestCase.assertEquals("name-custom-attribute.ts", declaration!!.containingFile.name)
//    }
//
//    fun testShouldIgnoreCustomElementDeclarationOutsideOfAureliaProjects() {
//        myFixture.copyFileToProject("annotation-custom-attribute.ts")
//        val psiFile = myFixture.configureByText(HtmlFileType.INSTANCE, "<div annotation-custom-attribute></div>")
//        val htmlFile = assertInstanceOf(psiFile, XmlFile::class.java)
//
//        val tag: XmlTag = htmlFile.rootTag!!
//        val provider = AureliaCustomAttributeDescriptorsProvider()
//
//        val descriptor = provider.getAttributeDescriptors(tag).firstOrNull()
//        assertNull(descriptor)
//    }

}