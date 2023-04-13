package com.project.movieapp.adapters

import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.movieapp.R
import com.project.movieapp.utils.GridSpacingItemDecorationUtil
import com.project.movieapp.utils.UIUtils
import java.lang.String

class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("sourceCompat")
        fun setImageSrcCompat(imageView: ImageView, imageResource: Int) {
            imageView.setImageResource(imageResource)
        }

        @JvmStatic
        @BindingAdapter("setAdapter")
        fun setAdapter(recyclerView: RecyclerView, adapter: SimpleRecyclerAdapter<*>?) {
            recyclerView.adapter = adapter
        }

        @JvmStatic
        @BindingAdapter("setAdapter")
        fun setAdapter(recyclerView: RecyclerView, adapter: SimpleLoadMoreRecyclerAdapter<*>?) {
            recyclerView.adapter = adapter
        }

        @JvmStatic
        @BindingAdapter("setupGridRecyclerView")
        fun setupGridRecyclerView(recyclerView: RecyclerView, margin: Float) {
            Log.v(
                "column",
                String.valueOf(UIUtils.newInstance(recyclerView.context).calculateColumnNumber())
            )
            recyclerView.layoutManager =
                GridLayoutManager(
                    recyclerView.context,
                    UIUtils.newInstance(recyclerView.context).calculateColumnNumber()
                )
            recyclerView.addItemDecoration(
                GridSpacingItemDecorationUtil(margin, "grid")
            )
        }

        @JvmStatic
        @BindingAdapter("setupHorizontalRecyclerView")
        fun setupHorizontalRecyclerView(recyclerView: RecyclerView, margin: Float) {
            recyclerView.layoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
            recyclerView.addItemDecoration(
                GridSpacingItemDecorationUtil(margin, "horizontal")
            )
        }

        @JvmStatic
        @BindingAdapter("setupVerticalRecyclerView")
        fun setupVerticalRecyclerView(recyclerView: RecyclerView, margin: Float) {
            recyclerView.layoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.addItemDecoration(
                GridSpacingItemDecorationUtil(margin, "vertical")
            )
        }

        @JvmStatic
        @BindingAdapter("setFilterStatus")
        fun setupFilterStatus(layout: LinearLayout, isFiltering: Boolean) {
            var resId: Int = R.drawable.roundrect_all_gray_outline_gray
            if (isFiltering) {
                resId = R.drawable.roundrect_all_primary
            }
            layout.background = ContextCompat.getDrawable(layout.context, resId)
        }
    }
}
