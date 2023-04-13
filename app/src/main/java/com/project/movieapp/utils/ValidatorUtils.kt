package com.project.movieapp.utils

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.EditText

object ValidatorUtils {
    fun checkNotNullOrEmpty(string: String?): Boolean {
        return string != null && !string.contains("null") && !string.isEmpty()
    }

    fun checkNotNullOrEmpty(integer: Int?): Boolean {
        return integer != null
    }

    fun isCollectionNotEmpty(collection: List<*>): Boolean {
        return collection.size > 0
    }

    fun validateRequiredText(
        activity: Activity,
        editText: EditText,
        errorMessage: String?
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            editText.error = errorMessage
            requestFocus(activity, editText)
            return false
        } else {
            editText.error = null
        }
        return true
    }

    private fun requestFocus(activity: Activity, view: View) {
        if (view.requestFocus()) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }
}
