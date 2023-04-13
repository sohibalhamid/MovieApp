package com.project.movieapp.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.movieapp.R
import com.project.movieapp.utils.ValidatorUtils.checkNotNullOrEmpty
import java.util.*

class UIUtils private constructor() {
    private var context: Context? = null
    fun calculateColumnNumber(): Int {
        val displayMetrics: DisplayMetrics = context!!.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / 100).toInt()
    }

    fun showDialogError(context: Context, info: String?, subInfo: String?) {
        showInfoDialog(
            context,
            info,
            subInfo,
            R.drawable.ic_error,
            context.getString(R.string.title_common_ok),
            R.drawable.roundrect_all_error,
            null
        )
    }

    fun showDialogSuccess(
        context: Context,
        info: String?,
        subInfo: String?,
        onButtonClick: Runnable?
    ) {
        showInfoDialog(
            context,
            info,
            subInfo,
            R.drawable.ic_success,
            context.getString(R.string.title_common_ok),
            null,
            onButtonClick
        )
    }

    fun showInfoDialog(
        context: Context,
        info: String?,
        subInfo: String?,
        imageInfo: Int?,
        positiveText: String?,
        positiveDrawable: Int?,
        onButtonClick: Runnable?
    ) {
        var imageInfo = imageInfo
        if (isContextRunning(context)) {
            val dialog: Dialog = BottomSheetDialog(context, R.style.BottomAlertTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_info)
            if (imageInfo == null) imageInfo = R.drawable.ic_success
            val ivDialogLogo = dialog.findViewById<ImageView>(R.id.iv_dialog_logo)
            ivDialogLogo?.setImageResource(imageInfo)
            val tvDialogTitle = dialog.findViewById<TextView>(R.id.tv_dialog_title)
            if (tvDialogTitle != null) {
                tvDialogTitle.text = info
            }
            val tvDialogSubtitle = dialog.findViewById<TextView>(R.id.tv_dialog_subtitle)
            if (tvDialogSubtitle != null) {
                tvDialogSubtitle.text = subInfo
            }
            val btnPositive = dialog.findViewById<Button>(R.id.btn_positive)
            if (btnPositive != null) {
                btnPositive.setOnClickListener { onClick: View? ->
                    onButtonClick?.run()
                    dialog.dismiss()
                }
                if (checkNotNullOrEmpty(positiveText)) btnPositive.text =
                    positiveText
                if (positiveDrawable != null) btnPositive.setBackgroundResource(positiveDrawable)
            }
            val activity = context as Activity
            if (!activity.isFinishing) {
                dialog.show()
            }
        }
    }

    fun showConfirmationDialog(
        context: Context,
        info: String?,
        subInfo: String?,
        onPositive: Runnable?
    ) {
        showConfirmationDialog(
            context,
            info,
            subInfo,
            null,
            null,
            null,
            null,
            null,
            onPositive,
            null,
            false
        )
    }

    fun showConfirmationDialog(
        context: Context,
        info: String?,
        subInfo: String?,
        onPositive: Runnable?,
        onNegative: Runnable?
    ) {
        showConfirmationDialog(
            context,
            info,
            subInfo,
            null,
            null,
            null,
            null,
            null,
            onPositive,
            onNegative,
            false
        )
    }

    fun showConfirmationDialog(
        context: Context,
        info: String?,
        subInfo: String?,
        positiveText: String?,
        negativeText: String?,
        imageInfo: Int?,
        positiveDrawable: Int?,
        negativeDrawable: Int?,
        onPositive: Runnable?,
        onNegative: Runnable?,
        isCancelable: Boolean
    ) {
        var imageInfo = imageInfo
        if (isContextRunning(context)) {
            val dialog: Dialog = BottomSheetDialog(context, R.style.BottomAlertTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_confirmation)
            dialog.setCancelable(isCancelable)
            if (imageInfo == null) imageInfo = R.drawable.ic_question
            val ivDialogLogo = dialog.findViewById<ImageView>(R.id.iv_dialog_logo)
            ivDialogLogo?.setImageResource(imageInfo)
            val tvDialogTitle = dialog.findViewById<TextView>(R.id.tv_dialog_title)
            if (tvDialogTitle != null) {
                tvDialogTitle.text = info
            }
            val tvDialogSubtitle = dialog.findViewById<TextView>(R.id.tv_dialog_subtitle)
            if (tvDialogSubtitle != null) {
                if (checkNotNullOrEmpty(subInfo)) tvDialogSubtitle.text =
                    subInfo else tvDialogSubtitle.visibility = View.GONE
            }
            val btnPositive = dialog.findViewById<Button>(R.id.btn_positive)
            if (btnPositive != null) {
                btnPositive.setOnClickListener { onClick: View? ->
                    onPositive?.run()
                    dialog.dismiss()
                }
                if (checkNotNullOrEmpty(positiveText)) btnPositive.text =
                    positiveText
                if (positiveDrawable != null) btnPositive.setBackgroundResource(positiveDrawable)
            }
            val btnNegative = dialog.findViewById<Button>(R.id.btn_negative)
            if (btnNegative != null) {
                btnNegative.setOnClickListener { onClick: View? ->
                    onNegative?.run()
                    dialog.dismiss()
                }
                if (checkNotNullOrEmpty(negativeText)) btnNegative.text =
                    negativeText
                if (negativeDrawable != null) btnNegative.setBackgroundResource(negativeDrawable)
            }
            val activity = context as Activity
            if (!activity.isFinishing) {
                dialog.show()
            }
        }
    }

    private fun isContextRunning(ctx: Context): Boolean {
        return if (ctx is Activity) {
            val act : Activity = ctx
            !act.isFinishing
        } else {
            false
        }
    }

    companion object {
        fun newInstance(context: Context?): UIUtils {
            val uiHelper = UIUtils()
            uiHelper.context = context
            return uiHelper
        }

        fun fullscreenDarkUI(window: Window) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            setDarkIconStatusBar(window)
        }

        fun setDarkIconStatusBar(window: Window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = Color.TRANSPARENT
        }

        fun fullscreenLightUI(window: Window) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            setLightIconStatusBar(window)
        }

        fun setLightIconStatusBar(window: Window) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}
