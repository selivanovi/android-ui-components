package com.example.swipecard.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import com.example.swipecard.CardItem
import com.example.swipecard.decodeSampledBitmapFromResource


class CustomGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    var onLongTouchListener: ((CardItem) -> Unit)? = null

    private val children = hashMapOf<CardItem, View>()

    val keys: List<CardItem>
        get() = children.keys.toList()


    fun addCardItem(cardItem: CardItem) {
        val imageView = ImageView(context).apply {
            setImageBitmap(decodeSampledBitmapFromResource(resources, cardItem.drawable, 50, 50))
        }
        imageView.setOnLongClickListener { view ->

            searchKeys(view)?.let {
                onLongTouchListener?.invoke(it)
            }
            true
        }
        addView(imageView)
        children[cardItem] = imageView
    }

    fun removeCardView(cardItem: CardItem) {
        val view = children[cardItem]
        children.remove(cardItem)
        super.removeView(view)
    }

    override fun removeAllViews() {
        children.clear()
        super.removeAllViews()
    }

    private fun searchKeys(view: View) = children.keys.find { key ->
        children[key] == view
    }

}