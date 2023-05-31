package com.example.swipecard.ui.custom

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.animation.addListener
import com.example.swipecard.CardItem
import com.example.swipecard.animTranslationX
import kotlin.math.abs

class CustomViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    var onRightSwipeTouchListener: (() -> Unit)? = null
    var onLeftSwipeTouchListener: (() -> Unit)? = null

    private val children = mutableListOf<CustomCardView>()
    private val touchPoint = PointF()
    private val actionPoint = PointF()
    private var diffX = 0F
    private var diffY = 0F


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        children.forEach { child ->
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutChildren(children)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (children.size == 0) return false

        val child = children.first()

        var action = ""
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"

                touchPoint.x = event.x
                touchPoint.y = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"

                actionPoint.x = event.x
                actionPoint.y = event.y

                diffX = (actionPoint.x - touchPoint.x)
                diffY = (actionPoint.y - touchPoint.y)

                child.translationX = diffX
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"

                actionPoint.x = event.x
                actionPoint.y = event.y
                if (abs(diffX) > width / 4) {
                    if (diffX > 0) {
                        child.animTranslationX(width.toFloat()).apply {
                            addListener(onEnd = {
                               onRightSwipeTouchListener?.invoke()

                            })
                            start()
                        }
                    } else {
                        child.animTranslationX(-width.toFloat()).apply {
                            addListener(onEnd = {
                                onLeftSwipeTouchListener?.invoke()
                            })
                            start()
                        }

                    }
                } else cancelAnimation()
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
            }
        }

        Log.i("CustomViewGroup", "Action: $action X: ${actionPoint.x} Y: ${actionPoint.y}")

        return true
    }

    fun addView(cardItem: CardItem?) {
        if (children.size < CHILDREN_CAPACITY && cardItem != null) {
            val view = CustomCardView(context, null).apply {
                radius = CARD_RADIUS
                this.cardItem = cardItem
            }
            children.add(0, view)
            super.addView(view)
        }
    }

    fun removeView(): CardItem? {
        Log.i("CustomViewGroup", "Delete view")
        val view = children.removeAt(0)
        super.removeView(view)
        return view.cardItem
    }

    private fun layoutChildren(children: MutableList<CustomCardView>) {
        var elevation = CARD_ELEVATION * children.size
        children.forEachIndexed { index, view ->
            layoutChild(index, view, elevation)
            elevation -= CARD_ELEVATION
        }
    }

    private fun layoutChild(index: Int, view: CustomCardView, elevation: Float) {

        view.layout(
            width / 2 - view.measuredWidth / 2 + index * CARD_BIAS,
            height / 2 - view.measuredHeight / 2 + index * CARD_BIAS,
            width / 2 + view.measuredWidth / 2 + index * CARD_BIAS,
            height / 2 + view.measuredHeight / 2 + index * CARD_BIAS
        )
        view.elevation = elevation
    }

    fun cancelAnimation() {
        children.first().animTranslationX(0F).start()
    }


    companion object {
        private const val CARD_RADIUS = 25F
        private const val CHILDREN_CAPACITY = 10
        private const val CARD_BIAS = 20
        private const val CARD_ELEVATION = 10F
    }
}