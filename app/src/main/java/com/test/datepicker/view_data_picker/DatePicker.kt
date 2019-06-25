package com.test.datepicker.view_data_picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.test.datepicker.R
import kotlinx.android.synthetic.main.view_date_picker.view.*

/**
 * Created by Administrator on 2019\5\20 0020.
 *
 */
class DatePicker : LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_date_picker, this, true)
        initChild()
        initAttr(context, attrs)
    }

    private fun initChild() {
        year_picker
    }

    private fun initAttr(context: Context?, attrs: AttributeSet?) {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.DatePicker)
        val halfVisibleItemCount = array?.getInteger(R.styleable.DatePicker_halfVisibleItemCount, 2)
        setHalfVisibleItemCount(halfVisibleItemCount)
        array?.recycle()
    }

    private fun setHalfVisibleItemCount(halfVisibleItemCount: Int?) {
        year_picker.setHalfVisibleItemCount(halfVisibleItemCount)
        month_picker.setHalfVisibleItemCount(halfVisibleItemCount)
        day_picker.setHalfVisibleItemCount(halfVisibleItemCount)
    }
}
