package com.example.imageloadmoreview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso

class LoadAdapter
    (
    private var list: List<LoadModel.Item>,
    private var rowClickListener: MyRecyclerViewRowClickListener?
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    /*
        * isLoading - to set the remote loading and complete status to fix back to back load more call
        * isMoreDataAvailable - to set whether more data from server available or not.
        * It will prevent useless load more request even after all the server data loaded
        * */
    private val typeData = 0
    private val typeLoad = 1
    private var loadMoreListener: MyAdapterOnLoadMoreListener? = null
    private var isLoading = false
    private var isMoreDataAvailable = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == typeData) {
            MyViewHolder(
                inflater.inflate(R.layout.viewholder_search_more, parent, false),
                rowClickListener
            )
        } else {
            LoadHolder(inflater.inflate(R.layout.viewholder_load, parent, false))
        }
    }

    override fun onBindViewHolder(
        holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
        position: Int
    ) {

        if (position >= itemCount - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true
            loadMoreListener!!.onLoadMore()
        }

        if (getItemViewType(position) == typeData) {
            (holder as MyViewHolder).bindData(list[position], position)
        }
        holder.setIsRecyclable(false)
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isLoadingView) {
            typeLoad
        } else {
            typeData
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /* VIEW HOLDERS */

    internal class MyViewHolder(itemView: View, rowClickListener: MyRecyclerViewRowClickListener?) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        private var tv1 = itemView.findViewById<View>(R.id.tv1) as TextView
        private var tv2 = itemView.findViewById<View>(R.id.tv2) as TextView
        private var ivBanner = itemView.findViewById<View>(R.id.ivBanner) as ImageView
        private var rl = itemView.findViewById<View>(R.id.rl) as RelativeLayout

        init {
            itemView.setSafeOnClickListener {
                rowClickListener?.onRowClicked(adapterPosition)
            }
        }

        fun bindData(dataModel: LoadModel.Item, position: Int) {
            tv1.text = dataModel.item?.itemCode
            if (!dataModel.item?.mediumImageUrls?.get(0)?.imageUrl.isNullOrEmpty())
                Picasso.get().load(dataModel.item?.mediumImageUrls?.get(0)?.imageUrl)
                    .placeholder(R.drawable.placeholder_16_9)
                    .into(ivBanner)
        }
    }

    internal class LoadHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)

    fun setMoreDataAvailable(moreDataAvailable: Boolean) {
        isMoreDataAvailable = moreDataAvailable
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    fun notifyDataChanged() {
        notifyDataSetChanged()
        isLoading = false
    }


    fun setLoadMoreListener(loadMoreListener: MyAdapterOnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }

    fun isMoreDataAvailable(): Boolean {
        return isMoreDataAvailable
    }
}