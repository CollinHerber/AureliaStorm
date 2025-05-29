package com.github.denofevil.aurelia.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Plugin settings
 */
@State(name = "aureliaSettings", storages = [Storage("aureliaSettings.xml")])
class AureliaSettings : PersistentStateComponent<AureliaSettings> {
    var jsInjectionEnabled: Boolean = true
    var checkPropertyBindableAnnotation: Boolean = false

    companion object {
        fun getInstance(): AureliaSettings {
            return ApplicationManager.getApplication().getService(AureliaSettings::class.java)
        }
    }

    override fun getState(): AureliaSettings {
        return this
    }

    override fun loadState(state: AureliaSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}

