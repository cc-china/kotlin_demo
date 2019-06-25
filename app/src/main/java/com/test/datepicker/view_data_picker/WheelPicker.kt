package com.test.datepicker.view_data_picker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.test.datepicker.R

/**
 * Created by Administrator on 2019\5\25 0025.
 *
 */
open class WheelPicker<T> : View {
    //画笔
    private val mPaint: Paint = Paint()
    //声明两个Rect，一个用来显示所有Item，一个用来显示选中Item
    private var mDrawRect: Rect = Rect()
    private var mSelectedRect: Rect = Rect()
    // 是否显示幕布，选中的Item会用一个颜色凸显出来
    private var mIsShowCurtain: Boolean = true
    //幕布的颜色
    private var mCurtainColor: Int = 0
    //是否显示幕布的边框
    private var mIsShowCurtainBorder: Boolean = true
    //幕布边框的颜色
    @ColorInt
    private var mCurtainBorderColor: Int = 0
    //一半的Item显示数量，总量是 mHalfVisibleItemCount * 2 +1
    private var mHalfVisibleItemCount: Int? = 2
    //一个Item的宽度(不包含文字那一段，假如没有赋值的时候默认也是这个宽高的矩形)、高度
    private var mItemWidthSpec: Int = 0
    private var mItemHeightSpec: Int = 0
    //一个Item的高度，是getPadding+文字的高度
    private var mItemHeight: Int = 0
    //在一个Item中文字最大的宽度、高度
    private var mTextMaxWidthSpec: Int = 0
    private var mTextMaxHeightSpec: Int = 0
    //Item的默认字体color
    private var mTextColor: Int = 0
    //Item的选中字体color
    private var mSelectedTextColor: Int = 0
    //Item的字号大小
    private var mTextSize: Float = 0f


    private var ctx:Context?
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.ctx = context
        initAttrs(context, attrs)
        initPaint()
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        val attributes = context!!.obtainStyledAttributes(attrs, R.styleable.WheelPicker)
        mItemWidthSpec = attributes.getDimensionPixelSize(R.styleable.WheelPicker_itemWidthSpec,
                context.resources.getDimensionPixelOffset(R.dimen.width_WheelPicker))
        mItemHeightSpec = attributes.getDimensionPixelSize(R.styleable.WheelPicker_itemHeightSpec,
                context.resources.getDimensionPixelOffset(R.dimen.height_WheelPicker))
        mTextColor = attributes.getColor(R.styleable.WheelPicker_itemTextColor,
                Color.BLACK)
        mSelectedTextColor = attributes.getColor(R.styleable.WheelPicker_itemTextColor,
                Color.parseColor("#33AAFF"))
        mTextSize = attributes.getDimension(R.styleable.WheelPicker_itemTextSize,
                context.resources.getDimension(R.dimen.sp_14))
        mIsShowCurtain = attributes.getBoolean(R.styleable.WheelPicker_wheelCurtain, true)
        mCurtainColor = attributes.getColor(R.styleable.WheelPicker_curtainColor, Color.parseColor("#303d3d3d"))
        mIsShowCurtainBorder = attributes.getBoolean(R.styleable.WheelPicker_wheelCurtainBorder, true)
        mCurtainBorderColor = attributes.getColor(R.styleable.WheelPicker_wheelCurtainBorderColor, Color.BLACK)
        attributes.recycle()
    }

    private fun initPaint() {
        mPaint.isAntiAlias = true//抗锯齿
        mPaint.isDither = true//抗抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
        mPaint.isLinearText = true//文本缓存，设置线性文本，为true时无缓存
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = mTextColor
        mPaint.textSize = mTextSize

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //这里算出的width和height是整体的宽高，不是具体某个Item的宽高
        var width: Int = mItemWidthSpec + mTextMaxWidthSpec
        var height: Int = (mItemHeightSpec + mTextMaxHeightSpec) * getVisibleItemCount()
        //再加上getPadding的l、t、r、b四个方向的值
        width += paddingLeft + paddingRight
        height += paddingTop + paddingBottom
        setMeasuredDimension(getMeasureSpec(widthMode, widthSize, width),
                getMeasureSpec(heightMode, heightSize, height))
    }

    private fun getMeasureSpec(specMode: Int, specSize: Int, measure: Int): Int {
        return if (specMode == MeasureSpec.EXACTLY){
            specSize
        }else{
            Math.min(specSize, measure)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mDrawRect.set(paddingLeft, paddingTop, width - paddingLeft,
                height - paddingBottom)
        mItemHeight = mDrawRect.height() / getVisibleItemCount()

        mSelectedRect.set(paddingLeft, mItemHeight * getHalfVisibleItemCount() + paddingTop,
                w - paddingLeft, mItemHeight * (getHalfVisibleItemCount() + 1) + paddingTop)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mIsShowCurtain) {
            //是否显示被选中的幕布
            mPaint.style = Paint.Style.FILL_AND_STROKE
            mPaint.color = mCurtainColor
            canvas?.drawRect(mSelectedRect, mPaint)
        }
        if (mIsShowCurtainBorder) {
            mPaint.style = Paint.Style.STROKE
            mPaint.color = mCurtainBorderColor
            canvas?.drawRect(mDrawRect, mPaint)
        }
    }


    /**
     * return halfVisibleItemCount
     * */
    private fun getHalfVisibleItemCount(): Int {
        return mHalfVisibleItemCount!!
    }

    /**
     * 返回显示Item的总数
     * 总数为 halfVisibleItemCount * 2 + 1
     * */
    private fun getVisibleItemCount(): Int {
        return mHalfVisibleItemCount!! * 2 + 1
    }

    /**
     * 设置显示数据量的一半，为保证总显示个数为奇数，这里将总数拆分，
     * 总数为 halfVisibleItemCount * 2 + 1
     * @param halfVisibleItemCount 总数量的一半
     * */
    fun setHalfVisibleItemCount(halfVisibleItemCount: Int?) {
        if (this.mHalfVisibleItemCount == halfVisibleItemCount) {
            return
        }
        this.mHalfVisibleItemCount = halfVisibleItemCount
        requestLayout()
    }
}