package com.github.denofevil.aurelia.attribute

import com.github.denofevil.aurelia.Aurelia
import com.github.denofevil.aurelia.index.AureliaIndexUtil
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider

/**
 * Provides descriptors with custom attributes
 */
class AureliaCustomAttributeDescriptorsProvider : XmlAttributeDescriptorsProvider {

    override fun getAttributeDescriptors(element: XmlTag): Array<XmlAttributeDescriptor> {
        return emptyArray()
    }

    override fun getAttributeDescriptor(attributeName: String, tag: XmlTag): XmlAttributeDescriptor? {
        if (!Aurelia.isFrameworkCandidate(tag)) return null
        AureliaIndexUtil.resolveCustomAttributeClasses(attributeName, tag.project).firstOrNull()?.let {
            return AureliaCustomAttributeDescriptor(attributeName, it, true)
        }
        return null
    }
}