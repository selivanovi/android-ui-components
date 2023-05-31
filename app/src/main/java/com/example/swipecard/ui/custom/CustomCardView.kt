package com.example.swipecard.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.swipecard.CardItem
import com.example.swipecard.R
import com.example.swipecard.decodeSampledBitmapFromResource

class CustomCardView(context: Context, attrs: AttributeSet? = null) : CardView(context, attrs) {
    private val image: ImageView
    private val title: TextView
    private val description: TextView
    var cardItem: CardItem? = null
        set(value) {
            value?.let {
                field = value
                title.text = value.title
                description.text = value.description
                image.setImageBitmap(
                    decodeSampledBitmapFromResource(resources, value.drawable, 100, 100)
                )
            }
        }

    init {
        inflate(context, R.layout.custom_card, this)

        image = findViewById(R.id.image)
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)


        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomCardView)
        image.setImageDrawable(attributes.getDrawable(R.styleable.CustomCardView_cc_image))
        title.text = attributes.getString(R.styleable.CustomCardView_cc_title)
        description.text = attributes.getString(R.styleable.CustomCardView_cc_description)
        attributes.recycle()
    }

}