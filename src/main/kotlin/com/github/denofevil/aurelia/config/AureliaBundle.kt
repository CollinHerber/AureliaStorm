package com.github.denofevil.aurelia.config

import com.intellij.AbstractBundle

/**
 * Provides translations from the translation file
 * translations: resources/messages/AureliaBundle.properties
 */
object AureliaBundle : AbstractBundle("messages.AureliaBundle") {
    fun get(key: String, vararg params: Any): String {
        return getMessage(key, *params)
    }
}