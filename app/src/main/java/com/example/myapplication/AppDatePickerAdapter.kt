package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.text.DateFormatSymbols
import java.util.*
import java.util.concurrent.TimeUnit

class AppDatePickerAdapter(
    data: List<DateData>,
    private val ctx: Context,
    typedArray: TypedArray,
    private val startDate: Calendar,
    maxDate: Calendar,
    private val defaultData: List<String>? = null
) : RecyclerView.Adapter<AppDatePickerViewHolder>() {


    var listener: OnItemClickListener? = null

    var monthFont: Typeface? = Typeface.DEFAULT
    var dayOfWeekFont: Typeface? = Typeface.DEFAULT_BOLD
    var dayOfMonthFont: Typeface? = Typeface.DEFAULT
    var yearFont: Typeface? = Typeface.DEFAULT
    var monthFontSizeInDp = 12f
    var dayOfWeekFontSizeInDp = 12f
    var dayOfMonthFontSizeInDp = 12f
    var yearFontSizeInDp = 12f
    var monthFontColor = Color.BLACK
    var dayOfWeekColor = Color.BLACK
    var dayOfMonthColor = Color.BLACK
    var yearColor = Color.BLACK
    var itemFont: Typeface? = Typeface.DEFAULT
    var itemFontSize: Float? = 12f
    var itemTextColor: Int? = Color.BLACK
    var lastCurrentPosition = 0
    var selectionColor = Color.BLUE
    var pickerBackgroundColor = Color.WHITE


    init {
        monthFont =
            getFont(ctx, typedArray, R.styleable.AppDatePicker_month_font) ?: Typeface.DEFAULT
        dayOfWeekFont =
            getFont(ctx, typedArray, R.styleable.AppDatePicker_day_of_week_font) ?: Typeface.DEFAULT
        dayOfMonthFont = getFont(ctx, typedArray, R.styleable.AppDatePicker_day_of_month_font)
            ?: Typeface.DEFAULT
        itemFont = getFont(ctx, typedArray, R.styleable.AppDatePicker_item_font)
            ?: Typeface.DEFAULT
        yearFont = getFont(ctx, typedArray, R.styleable.AppDatePicker_year_font)
            ?: Typeface.DEFAULT
        monthFontSizeInDp = getDimension(
            typedArray, R.styleable.AppDatePicker_month_text_size, ctx.resources, 12f
        )
        dayOfWeekFontSizeInDp = getDimension(
            typedArray, R.styleable.AppDatePicker_day_of_week_text_size, ctx.resources, 12f
        )
        dayOfMonthFontSizeInDp = getDimension(
            typedArray, R.styleable.AppDatePicker_day_of_month_text_size, ctx.resources, 12f
        )
        yearFontSizeInDp = getDimension(
            typedArray, R.styleable.AppDatePicker_year_text_size, ctx.resources, 12f
        )
        itemFontSize = getDimension(
            typedArray, R.styleable.AppDatePicker_item_text_size, ctx.resources, 12f
        )
        monthFontColor = getcolor(
            typedArray, R.styleable.AppDatePicker_month_text_color, ctx.resources, Color.BLACK
        )
        dayOfWeekColor = getcolor(
            typedArray, R.styleable.AppDatePicker_day_of_week_color, ctx.resources, Color.BLACK
        )
        dayOfMonthColor = getcolor(
            typedArray, R.styleable.AppDatePicker_day_of_month_color, ctx.resources, Color.BLACK
        )
        yearColor = getcolor(
            typedArray, R.styleable.AppDatePicker_year_color, ctx.resources, Color.BLACK
        )
        itemTextColor = getcolor(
            typedArray, R.styleable.AppDatePicker_item_color, ctx.resources, Color.BLACK
        )
        selectionColor = getcolor(
            typedArray, R.styleable.AppDatePicker_selectionColor, ctx.resources, Color.BLUE
        )
        pickerBackgroundColor = getcolor(
            typedArray, R.styleable.AppDatePicker_pickerBackgroundColor, ctx.resources, Color.WHITE
        )
    }


    private val row: Long =
        TimeUnit.DAYS.convert(
            maxDate.time.time - startDate.time.time,
            TimeUnit.MILLISECONDS
        )
    private val mapped = data.associate {
        val diff = it.date.time.time - startDate.time.time
        TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt() to it.data
    }


    fun getCurrentPosition(): Int {
        lastCurrentPosition = TimeUnit.DAYS.convert(
            Calendar.getInstance().time.time - startDate.time.time,
            TimeUnit.MILLISECONDS
        ).toInt()
        Log.e("LastCurrent"," #1 ${lastCurrentPosition}")
        return lastCurrentPosition
    }


    fun findPosition(cal: Calendar): Int {
        val data =
            TimeUnit.DAYS.convert(
                cal.time.time - startDate.time.time,
                TimeUnit.MILLISECONDS
            ).toInt() +1
        lastCurrentPosition = data

        Log.e("LastCurrent"," #2 ${lastCurrentPosition}")
        return data
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppDatePickerViewHolder =
        AppDatePickerViewHolder(
            defaultData,
            this,
            startDate,
            itemView =
            LayoutInflater.from(ctx).inflate(R.layout.date_picker_layout, parent, false)
        ).apply {
            val listener = {
                with(Calendar.getInstance()) {
                    listener?.let {
                        time = startDate.time
                        add(Calendar.DATE, adapterPosition)
                        mapped[adapterPosition]
                        val lastPos = lastCurrentPosition
                        it.onItemClick(this, mapped[adapterPosition])
                        lastCurrentPosition = adapterPosition

                        Log.e("LastCurrent"," #3 ${lastCurrentPosition}")
                        notifyItemChanged(lastPos)
                        notifyItemChanged(lastCurrentPosition)
                    }
                }
            }
            val cliclListener = View.OnClickListener {
                listener()
            }
            this.itemView.setOnClickListener {
                listener()
            }

            this.itemView.findViewById<View>(R.id.cardView).setOnClickListener {
                listener()
            }
            this.itemView.findViewById<ListView>(R.id.dataList).setOnItemClickListener { _, _, _, _ ->
                listener()
            }
            this.itemView.findViewById<ListView>(R.id.dataList).setOnTouchListener { view, event ->
                if(event.action == MotionEvent.ACTION_UP ){
                    listener()
                }
                false
            }

        }

    override fun onBindViewHolder(holder: AppDatePickerViewHolder, position: Int) {
        holder.bind(mapped[position], position)
    }


    override fun getItemCount(): Int = row.toInt()
}

class AppDatePickerViewHolder(
    private val defaultData: List<String>?,
    private val adapter: AppDatePickerAdapter,
    private val startDate: Calendar,
    itemView: View
) :
    RecyclerView.ViewHolder(itemView) {

    private val dateFormatSymbols: DateFormatSymbols = DateFormatSymbols()

    fun bind(data: List<String>?, pos: Int) {
        with(itemView) {
            if (adapterPosition == adapter.lastCurrentPosition) {
                setBackgroundColor(adapter.selectionColor)
            } else {
                setBackgroundColor(adapter.pickerBackgroundColor)
            }
            with(Calendar.getInstance()) {
                time = startDate.time
                add(Calendar.DATE, pos)
                findViewById<TextView>(R.id.dayOfMonthText).apply {
                    setTextColor(adapter.dayOfMonthColor)
                    text =
                        get(Calendar.DAY_OF_MONTH).toString()
                    setTypeface(adapter.dayOfMonthFont, Typeface.BOLD)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, adapter.dayOfMonthFontSizeInDp)
                }
                findViewById<TextView>(R.id.monthText).apply {
                    setTextColor(adapter.monthFontColor)
                    text = dateFormatSymbols.months[get(Calendar.MONTH)]
                    typeface = adapter.monthFont
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, adapter.monthFontSizeInDp)
                }
                findViewById<TextView>(R.id.dayOfWeekText).apply {
                    setTextColor(adapter.dayOfWeekColor)
                    text =
                        dateFormatSymbols.weekdays[get(Calendar.DAY_OF_WEEK)]
                    typeface = adapter.dayOfWeekFont

                    setTextSize(TypedValue.COMPLEX_UNIT_PX, adapter.dayOfWeekFontSizeInDp)
                }

                findViewById<TextView>(R.id.year).apply {
                    setTextColor(adapter.yearColor)
                    text = get(Calendar.YEAR).toString()
                    typeface = adapter.yearFont
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, adapter.yearFontSizeInDp)
                }
                findViewById<ListView>(R.id.dataList).adapter = data?.let {
                    InnerDataAdapter(context, adapter, data)
                } ?: kotlin.run {
                    defaultData?.let {
                        InnerDataAdapter(context, adapter, it)
                    }
                }
            }
        }

    }
}

class InnerDataAdapter(
    private val context: Context,
    private val parentAdapter: AppDatePickerAdapter,
    private val data: List<String>
) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }


    override fun getItem(p0: Int): Any = data[p0]

    override fun getItemId(p0: Int): Long = data[p0].hashCode().toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = if (convertView == null) {
            LayoutInflater.from(context).inflate(R.layout.date_picker_item, parent, false).apply {
                this.findViewById<TextView>(R.id.item).text = data[position]
            }
        } else {
            convertView.findViewById<TextView>(R.id.item).text = data[position]
            convertView
        }
        view.findViewById<TextView>(R.id.item).apply {
            parentAdapter.itemTextColor?.let { setTextColor(it) }
            parentAdapter.itemFontSize?.let { textSize = it }
            typeface = parentAdapter.itemFont
        }
        return view
    }

}