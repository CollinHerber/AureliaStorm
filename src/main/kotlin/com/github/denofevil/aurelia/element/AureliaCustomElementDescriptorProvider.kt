package com.github.denofevil.aurelia.element

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.Aurelia.CUSTOM_ELEMENTS
import com.github.denofevil.aurelia.Aurelia.IMPORT_ELEMENTS
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlElementDescriptor
import org.jetbrains.annotations.Nullable
import javax.swing.text.html.HTML

/**
 * Provides descriptor for custom elements
 */
class AureliaCustomElementDescriptorProvider : XmlElementDescriptorProvider {
    private val htmlTags = HTML.getAllTags().map { it.toString().lowercase() }

    @Nullable
    override fun getDescriptor(tag: XmlTag): XmlElementDescriptor? {
        if (!isCustomElementCandidate(tag)) return null
        val descriptor = AureliaCustomElementDescriptor(tag)
        if (descriptor.declaration != null) {
            return descriptor
        }
        return null
    }

    private fun isCustomElementCandidate(tag: XmlTag): Boolean {
        val tagName = tag.name.lowercase()
        val isExcludedTag = htmlTags.stream().anyMatch { it.equals(tagName) }
                || CUSTOM_ELEMENTS.contains(tagName) || IMPORT_ELEMENTS.contains(tagName)
        return !isExcludedTag && Aurelia.isFrameworkCandidate(tag)
    }
}