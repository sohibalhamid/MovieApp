package com.project.movieapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import lombok.Getter
import lombok.Setter

@Getter
@Setter
class SimpleLoadMoreRecyclerAdapter<A>(private var mainData: MutableList<A>, @field:LayoutRes private val layoutRes: Int, private val listener: OnViewHolder<A>) :
    RecyclerView.Adapter<SimpleLoadMoreRecyclerAdapter.SimpleViewHolder>() {
    interface OnViewHolder<A> {
        fun onBindView(holder: SimpleViewHolder?, item: A)
    }

    class SimpleViewHolder(itemView: View, listener: OnViewHolder<*>) :
        RecyclerView.ViewHolder(itemView) {
        private var listener: OnViewHolder<*>? = null
        var layoutBinding: ViewDataBinding? = null

        init {
            try {
                this.listener = listener
                layoutBinding = DataBindingUtil.bind(itemView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return SimpleViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val a = mainData[position]

        listener.onBindView(holder, a)
    }

    override fun getItemCount(): Int {
        return mainData.size
    }

    @JvmName("setMainData1")
    fun setMainData(mainData: MutableList<A>) {
        this.mainData = mainData
        notifyDataSetChanged()
    }

    fun loadMore(collection: MutableList<A>?): SimpleLoadMoreRecyclerAdapter<A> {
        mainData!!.addAll(collection!!)
        notifyDataSetChanged()
        return this
    }
}
