package com.github.denofevil.aurelia.action

import com.github.denofevil.aurelia.AureliaFileUtil
import com.github.denofevil.aurelia.config.AureliaBundle
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.TypeScriptFileType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile

/**
 * Custom action to switch between ts and html file
 */
class AureliaGoToAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.PSI_FILE) ?: return

        if (file.fileType is HtmlFileType) {
            AureliaFileUtil.findControllerClassOfHtmlFile(file)?.let {
                openFile(it.containingFile)
            }
        } else if (file.fileType is JavaScriptFileType || file.fileType is TypeScriptFileType) {
            AureliaFileUtil.findViewOfControllerFile(file)?.let {
                openFile(it)
            }
        }

    }

    private fun openFile(file: PsiFile?) {
        if (file?.virtualFile != null) {
            ApplicationManager.getApplication().invokeLater {
                FileEditorManager.getInstance(file.project).openFile(file.virtualFile, true)
            }
        } else {
            Messages.showInfoMessage(
                file?.project, AureliaBundle.get("action.goto.errorMessage"),
                AureliaBundle.get("action.goto.errorTitle")
            )
        }
    }
}