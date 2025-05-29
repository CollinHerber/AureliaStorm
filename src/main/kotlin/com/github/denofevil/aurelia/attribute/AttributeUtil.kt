package com.github.denofevil.aurelia.attribute

import com.github.denofevil.aurelia.Aurelia

object AttributeUtil {

    fun withoutInjectable(attribute: String): String {
        if (Aurelia.INJECTABLE.any { attribute.endsWith(".$it") }) {
            return attribute.substringBeforeLast(".")
        }
        return attribute
    }
}