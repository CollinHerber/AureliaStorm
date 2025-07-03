package com.github.denofevil.aurelia

import com.intellij.lang.javascript.psi.JSElement
import com.intellij.lang.javascript.psi.ecmal4.JSClass

object AureliaClassUtil {

    fun getAllMembers(jsClass: JSClass): List<JSElement> {
        return jsClass.members + jsClass.superClasses.flatMap { getAllMembers(it, arrayListOf()) }
    }

    private fun getAllMembers(jsClass: JSClass, processedClasses: ArrayList<JSClass>): List<JSElement> {
        processedClasses.add(jsClass) // to avoid endless loop
        return jsClass.members + jsClass.superClasses.filter { !processedClasses.contains(it) }.flatMap { getAllMembers(it, processedClasses) }
    }
}