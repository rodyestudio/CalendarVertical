package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class AppDatePicker(val ctx: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    FrameLayout(ctx, attrs, defStyleAttr, defStyleRes) {

    lateinit var typedArray: TypedArray

    @SuppressLint("SimpleDateFormat")
    private val startDate = Calendar.getInstance().apply {
        this.set(Calendar.YEAR, this.get(Calendar.YEAR) - 100)
    }
    private val maxDate = Calendar.getInstance().apply {
        this.set(Calendar.YEAR, this.get(Calendar.YEAR) + 100)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
        attrs?.let {
            typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.AppDatePicker, defStyleAttr, 0)
            addView(recyclerView)
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private val recyclerView by lazy {
        RecyclerView(ctx).apply {
            this.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            layoutManager = LinearLayoutManager(context)
        }
    }
    lateinit var appAdapter:AppDatePickerAdapter

    fun fillData(data: List<DateData>) {
        appAdapter = AppDatePickerAdapter(data, ctx, typedArray, startDate, maxDate)
        recyclerView.adapter = appAdapter
        recyclerView.scrollToPosition(appAdapter.getCurrentPosition())
    }

    fun fillData(defaultData:List<String>,data: List<DateData>) {
        appAdapter = AppDatePickerAdapter(data, ctx, typedArray, startDate, maxDate,defaultData)
        recyclerView.adapter = appAdapter
        recyclerView.scrollToPosition(appAdapter.getCurrentPosition())
    }

    fun addOnClickListener(click:OnItemClickListener){
        if(::appAdapter.isInitialized){
            appAdapter.listener = click
        }
    }

    fun focusTo(it: Long?) {
        if(it!=null){

            with(Calendar.getInstance()){
                timeInMillis = it
                var lastCurrentPosition = appAdapter.lastCurrentPosition
                val manager = recyclerView.layoutManager as LinearLayoutManager
                val pos = appAdapter.findPosition(this)
                manager.scrollToPositionWithOffset(pos,0)
                appAdapter.notifyItemChanged(lastCurrentPosition)
                appAdapter.notifyItemChanged(pos)
            }
        }
    }


}


interface OnItemClickListener{
    fun onItemClick(date:Calendar,data:List<String>? )
}

data class DateData(
    val date: Calendar,
    val data: List<String>
)