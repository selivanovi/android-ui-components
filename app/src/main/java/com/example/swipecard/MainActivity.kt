package com.example.swipecard

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText


import androidx.appcompat.app.AlertDialog
import com.example.swipecard.ui.custom.CustomGridLayout
import com.example.swipecard.ui.custom.CustomViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {

    private val cardDeck: CustomViewGroup by lazy {
        findViewById(R.id.customViewGroup)
    }
    private val savedArea: CustomGridLayout by lazy {
        findViewById(R.id.customGridLayout)
    }
    private val clearFAB: FloatingActionButton by lazy {
        findViewById(R.id.clearFAB)
    }
    private val addFAB: FloatingActionButton by lazy {
        findViewById(R.id.addFAB)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createCustomViewGroup()
        createFloatingActionButtons()
    }

    private fun createFloatingActionButtons() {
        addFAB.setOnClickListener {
            showAddCardAlertDialog()
        }
        clearFAB.setOnClickListener {
            showAlertDialog(
                R.string.delete,
                R.string.delete_all_message,
                {
                    savedArea.keys.forEach {
                        cardDeck.addView(it)
                    }
                    savedArea.removeAllViews()
                },
                {}
            )
        }
    }

    private fun createCustomViewGroup() {

        cardDeck.onRightSwipeTouchListener = ::deleteViewFromCardDeck
        cardDeck.onLeftSwipeTouchListener = ::saveViewInCardDeck

        cardDeck.addView(CardItem("Title", "Description", R.drawable.dislike_emoji))
        cardDeck.addView(CardItem("Title", "Description", R.drawable.happy_emoji))
        cardDeck.addView(CardItem("Title", "Description", R.drawable.sad_emoji))

        savedArea.onLongTouchListener = ::returnViewFromSavedArea

    }

    private fun deleteViewFromCardDeck() {
        showAlertDialog(
            title = R.string.delete,
            message = R.string.delete_message,
            {
                cardDeck.removeView()
            },
            { cardDeck.cancelAnimation()}
        )
    }

    private fun saveViewInCardDeck() {
        cardDeck.removeView()?.let { savedArea.addCardItem(it) }
    }

    private fun returnViewFromSavedArea(cardItem: CardItem) {
        savedArea.removeCardView(cardItem)
        cardDeck.addView(cardItem)
    }


    private fun showAlertDialog(
        title: Int,
        message: Int,
        yesAction: () -> Unit,
        noAction: () -> Unit
    ) {
        AlertDialog
            .Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.yes) { _, _ ->
                yesAction()
            }
            .setNegativeButton(R.string.no) { _, _ ->
                noAction()
            }
            .create()
            .show()
    }

    private fun showAddCardAlertDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater;
        val view = inflater.inflate(R.layout.dialog_add, null)
        val title = view.findViewById<EditText>(R.id.username)
        val description = view.findViewById<EditText>(R.id.password)

        builder.setView(view)
            .setPositiveButton(R.string.yes,
                DialogInterface.OnClickListener { dialog, id ->

                    cardDeck.addView(
                        CardItem(
                            title.text.toString(),
                            description.text.toString(),
                            listOfDrawables.random()
                        )
                    )
                })
            .setNegativeButton(R.string.no,
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.create()
        builder.show()
    }
}