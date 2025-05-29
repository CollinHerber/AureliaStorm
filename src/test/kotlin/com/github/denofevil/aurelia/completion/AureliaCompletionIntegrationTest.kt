package com.github.denofevil.aurelia.completion

import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

@TestDataPath("\$CONTENT_ROOT/src/test/test-data")
class AureliaCompletionIntegrationTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/test-data"
    }

    fun testShouldCompleteCustomElementAttribute() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("completion-custom-element.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<completion-custom-element <caret>></completion-custom-element>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.isNotEmpty())
        // only suggestion bindable
        TestCase.assertEquals(0, variants.count { it.lookupString == "public-property" })
        TestCase.assertEquals(0, variants.count { it.lookupString == "private-property" })
        TestCase.assertEquals(1, variants.count { it.lookupString == "public-property-with-binding" })
        TestCase.assertEquals(1, variants.count { it.lookupString == "private-property-with-binding" })
    }

    fun testShouldNotCompleteCustomElementAttributeWithoutAurelia() {
        myFixture.copyFileToProject("completion-custom-element.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<completion-custom-element <caret>></completion-custom-element>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.none { it.lookupString == "public-property-with-binding" })
        TestCase.assertTrue(variants.none { it.lookupString == "private-property-with-binding" })
    }

    fun testShouldNotCompleteExistingCustomElementAttribute() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("completion-custom-element.ts")
        myFixture.configureByText(
            HtmlFileType.INSTANCE,
            "<completion-custom-element <caret> public-property-with-binding.bind=\"true\"></completion-custom-element>"
        )

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.none { it.lookupString == "public-property-with-binding" })
    }

    fun testShouldCompleteCustomAttribute() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-attribute.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<div <caret>></div>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.isNotEmpty())
        TestCase.assertEquals(1, variants.count { it.lookupString == "annotation-custom-attribute" })
    }

    fun testShouldCompleteCustomAttributeForCustomElement() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("completion-custom-element.ts")
        myFixture.copyFileToProject("annotation-custom-attribute.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<completion-custom-element <caret>></completion-custom-element>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.isNotEmpty())
        TestCase.assertEquals(1, variants.count { it.lookupString == "annotation-custom-attribute" })
    }

    fun testShouldNotCompleteCustomAttributeWithoutAurelia() {
        myFixture.copyFileToProject("annotation-custom-attribute.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<div <caret>></div>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.none { it.lookupString == "annotation-custom-attribute" })
    }

    fun testShouldNotCompleteExistingCompleteCustomAttribute() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("annotation-custom-attribute.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<div <caret> annotation-custom-attribute></div>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.none { it.lookupString == "annotation-custom-attribute" })
    }

    fun testShouldCompleteCustomElement() {
        myFixture.copyFileToProject("package.json")
        myFixture.copyFileToProject("name-custom-element.ts")
        myFixture.copyFileToProject("annotation-custom-element.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<div><caret></div>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.isNotEmpty())
        TestCase.assertEquals(1, variants.count { it.lookupString == "name-custom-element" })
        TestCase.assertEquals(1, variants.count { it.lookupString == "annotation-custom-element" })
    }

    fun testShouldNotCompleteCustomElementWithoutAurelia() {
        myFixture.copyFileToProject("name-custom-element.ts")
        myFixture.copyFileToProject("annotation-custom-element.ts")
        myFixture.configureByText(HtmlFileType.INSTANCE, "<div><caret></div>")

        val variants = myFixture.completeBasic()

        TestCase.assertTrue(variants.none { it.lookupString == "name-custom-element" })
        TestCase.assertTrue(variants.none { it.lookupString == "annotation-custom-element" })
    }
}