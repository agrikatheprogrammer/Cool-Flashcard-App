package com.example.flashcardforcodepath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards=mutableListOf<Flashcard>()
    var currentCardIndex =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        val flashcardAns2 = findViewById<TextView>(R.id.flashcard_answer2)
        val flashcardAns3 = findViewById<TextView>(R.id.flashcard_answer3)
        val EYE = findViewById<ImageView>(R.id.eye)
        val DASHEDEYE = findViewById<ImageView>(R.id.dashedeye)

        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
            flashcardAns2.text=allFlashcards[0].wrongAnswer1
            flashcardAns3.text=allFlashcards[0].wrongAnswer2
        }

        flashcardAns2.setOnClickListener {
            flashcardAns2.setBackgroundColor(getResources().getColor(R.color.my_red_color, null))
            flashcardAnswer.setBackgroundColor(getResources().getColor(R.color.my_green_color, null)
            )
        }
        flashcardAns3.setOnClickListener {
            flashcardAns3.setBackgroundColor(getResources().getColor(R.color.my_red_color, null))
            flashcardAnswer.setBackgroundColor(
                getResources().getColor(
                    R.color.my_green_color,
                    null
                )
            )
        }
        flashcardAnswer.setOnClickListener {
            flashcardAnswer.setBackgroundColor(
                getResources().getColor(
                    R.color.my_green_color,
                    null
                )
            )
        }
        DASHEDEYE.setOnClickListener {
            if (flashcardAnswer.isShown) {
                flashcardAnswer.visibility = View.INVISIBLE
                flashcardAns2.visibility = View.INVISIBLE
                flashcardAns3.visibility = View.INVISIBLE
                DASHEDEYE.visibility = View.INVISIBLE
                EYE.visibility = View.VISIBLE
            }
        }
        EYE.setOnClickListener {
            flashcardAns2.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.VISIBLE
            flashcardAns3.visibility = View.VISIBLE
            EYE.visibility = View.INVISIBLE
            DASHEDEYE.visibility = View.VISIBLE
        }
        val addie = findViewById<ImageView>(R.id.addbutton)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
                val data: Intent?=result.data
            if (data!=null) {
                Snackbar.make(flashcardAns2,"Flashcard successfully changed!", Snackbar.LENGTH_SHORT).setBackgroundTint(resources.getColor(R.color.teal_700)).show()
                val questionString=data.getStringExtra("qskey")
                val ans1string=data.getStringExtra("ans1key")
                val ans2string=data.getStringExtra("ans2key")
                val ans3string=data.getStringExtra("ans3key")
                flashcardQuestion.text=questionString
                flashcardAns2.text=ans2string
                flashcardAns3.text=ans3string
                flashcardAnswer.text=ans1string

                if (!questionString.isNullOrEmpty()&&!ans1string.isNullOrEmpty()&&!ans2string.isNullOrEmpty()&&!ans3string.isNullOrEmpty()) {
                    flashcardDatabase.insertCard(Flashcard(questionString,ans1string,ans2string,ans3string))
                    allFlashcards=flashcardDatabase.getAllCards().toMutableList()
                }
            }
        }

        addie.setOnClickListener {
            val intent=Intent(this,secondactivity::class.java)
            resultLauncher.launch(intent)
        }

        findViewById<ImageView>(R.id.editbutton).setOnClickListener {
            val intent=Intent(this,secondactivity::class.java)
            intent.putExtra("key1",flashcardQuestion.text.toString())
            intent.putExtra("key2",flashcardAnswer.text.toString())
            intent.putExtra("key3",flashcardAns2.text.toString())
            intent.putExtra("key4",flashcardAns3.text.toString())
            resultLauncher.launch(intent)
        }

        val next=findViewById<ImageView>(R.id.nextbutton)
        next.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }
            currentCardIndex++
            if (currentCardIndex>=allFlashcards.size) {
                currentCardIndex=0
            }
            allFlashcards=flashcardDatabase.getAllCards().toMutableList()
            flashcardAnswer.text=allFlashcards[currentCardIndex].answer
            flashcardAns2.text=allFlashcards[currentCardIndex].wrongAnswer1
            flashcardAns3.text=allFlashcards[currentCardIndex].wrongAnswer2
            flashcardQuestion.text=allFlashcards[currentCardIndex].question
        }
    }
}