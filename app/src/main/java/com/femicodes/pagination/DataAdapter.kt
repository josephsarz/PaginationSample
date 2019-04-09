package com.femicodes.pagination

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class DataAdapter(private val studentList: List<Student>, recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 5
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false
    private var onLoadMoreListener: OnLoadMoreListener? = null


    init {

        if (recyclerView.layoutManager is LinearLayoutManager) {

            val linearLayoutManager = recyclerView
                .layoutManager as LinearLayoutManager?


            recyclerView
                .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(
                        recyclerView: RecyclerView,
                        dx: Int, dy: Int
                    ) {
                        super.onScrolled(recyclerView, dx, dy)

                        totalItemCount = linearLayoutManager!!.itemCount
                        lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition()
                        if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                            // End has been reached
                            // Do something
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener!!.onLoadMore()
                            }
                            loading = true
                        }
                    }
                })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (studentList[position] != null) VIEW_ITEM else VIEW_PROG
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.list_row, parent, false
            )

            vh = StudentViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.progressbar_item, parent, false
            )

            vh = ProgressViewHolder(v)
        }
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StudentViewHolder) {

            val singleStudent = studentList[position]

            holder.tvName.text = singleStudent.name

            holder.tvEmailId.text = singleStudent.emailId

            holder.student = singleStudent

        } else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    fun setLoaded() {
        loading = false
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }


    //
    class StudentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvName: TextView = v.findViewById<View>(R.id.tvName) as TextView
        var tvEmailId: TextView = v.findViewById<View>(R.id.tvEmailId) as TextView
        var student: Student? = null

        init {

            v.setOnClickListener { v ->
                Toast.makeText(
                    v.context,
                    "OnClick :" + student!!.name + " \n " + student!!.emailId,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: ProgressBar = v.findViewById<View>(R.id.progressBar1) as ProgressBar

    }
}
