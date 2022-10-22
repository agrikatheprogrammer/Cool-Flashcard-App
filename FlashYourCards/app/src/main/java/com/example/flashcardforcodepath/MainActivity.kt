package com.example.flashcardforcodepath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards=mutableListOf<Flashcard>()
    var cardToEdit: Flashcard? = null
    var currentCardIndex =0
    fun getRandomNumber(minNumber: Int, maxNumber: Int): Int {
        return (minNumber..maxNumber).random() // generated random from 0 to 10 included
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        var flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        var flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        var flashcardAns2 = findViewById<TextView>(R.id.flashcard_answer2)
        var flashcardAns3 = findViewById<TextView>(R.id.flashcard_answer3)
        val EYE = findViewById<ImageView>(R.id.eye)
        val DASHEDEYE = findViewById<ImageView>(R.id.dashedeye)
        val next=findViewById<ImageView>(R.id.nextbutton)
        val delete=findViewById<ImageView>(R.id.deletebutton)
        var konfetti = findViewById<KonfettiView>(R.id.konfettiView)
        val addie = findViewById<ImageView>(R.id.addbutton)
        val meme=findViewById<ImageView>(R.id.meme)


        if (allFlashcards.size > 0 || !allFlashcards.isEmpty()) {
            meme.visibility=View.INVISIBLE
            flashcardAnswer.visibility=View.VISIBLE
            flashcardAns2.visibility=View.VISIBLE
            flashcardAns3.visibility=View.VISIBLE
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
            flashcardAns2.text = allFlashcards[0].wrongAnswer1
            flashcardAns3.text = allFlashcards[0].wrongAnswer2
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
                konfetti.start(
                    Party(
                        speed = 0f,
                        maxSpeed = 30f,
                        damping = 0.9f,
                        spread = 360,
                        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                        position = Position.Relative(0.5, 0.3)
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


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
                val data: Intent?=result.data
            if (data!=null) {
                Snackbar.make(flashcardAns2,"Flashcard successfully added!", Snackbar.LENGTH_SHORT).setBackgroundTint(resources.getColor(R.color.teal_700)).show()
                val questionString=data.getStringExtra("qskey")
                val ans1string=data.getStringExtra("ans1key")
                val ans2string=data.getStringExtra("ans2key")
                val ans3string=data.getStringExtra("ans3key")
                flashcardQuestion.text=questionString
                flashcardAns2.text=ans2string
                flashcardAns3.text=ans3string
                flashcardAnswer.text=ans1string

                if (!questionString.isNullOrEmpty()&&!ans1string.isNullOrEmpty()&&!ans2string.isNullOrEmpty()&&!ans3string.isNullOrEmpty()) {
                    meme.visibility=View.INVISIBLE
                    flashcardAns2.visibility=View.VISIBLE
                    flashcardAnswer.visibility=View.VISIBLE
                    flashcardAns3.visibility=View.VISIBLE
                    flashcardDatabase.insertCard(Flashcard(questionString,ans1string,ans2string,ans3string))
                    allFlashcards=flashcardDatabase.getAllCards().toMutableList()
                    currentCardIndex++
                }
            }
        }

        addie.setOnClickListener {
            val intent=Intent(this,secondactivity::class.java)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        val editResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) { // Check that we have data returned
                Snackbar.make(flashcardAns2,"Flashcard successfully edited!", Snackbar.LENGTH_SHORT).setBackgroundTint(resources.getColor(R.color.teal_700)).show()
                val question=data.getStringExtra("qskey")
                val answer=data.getStringExtra("ans1key")
                val ans2string=data.getStringExtra("ans2key")
                val ans3string=data.getStringExtra("ans3key")
                if (question != null) {
                    cardToEdit?.question = question
                }
                if (answer != null) {
                    cardToEdit?.answer= answer
                }
                cardToEdit?.wrongAnswer1 =ans2string
                cardToEdit?.wrongAnswer2 =ans3string
                flashcardQuestion.text=question
                flashcardAnswer.text=answer
                flashcardAns2.text=ans2string
                flashcardAns3.text=ans3string
                cardToEdit?.let { flashcardDatabase.updateCard(it) }

            } else {
                Log.i("MainActivity", "Returned null data from EditActivity")
            }
        }

        findViewById<ImageView>(R.id.editbutton).setOnClickListener {
            allFlashcards=flashcardDatabase.getAllCards().toMutableList()
            for (flashcard: Flashcard in allFlashcards) {
                if (flashcard.question == allFlashcards[currentCardIndex].question) {
                    cardToEdit = flashcard
                }
            }
            val intent=Intent(this,secondactivity::class.java)
            intent.putExtra("key1",flashcardQuestion.text.toString())
            intent.putExtra("key2",flashcardAnswer.text.toString())
            intent.putExtra("key3",flashcardAns2.text.toString())
            intent.putExtra("key4",flashcardAns3.text.toString())
            editResultLauncher.launch(intent)
        }

        next.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }
            //var number: Int
            //do {
            //    number = getRandomNumber(0, allFlashcards.size - 1)
            //} while (allFlashcards[number].question.equals(flashcardQuestion.text))

            val leftOutAnim = AnimationUtils.loadAnimation(it.context, R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(it.context, R.anim.right_in)
            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                }

                override fun onAnimationEnd(animation: Animation?) {
                    flashcardQuestion.startAnimation(rightInAnim)
                    flashcardAns2.startAnimation(rightInAnim)
                    flashcardAnswer.startAnimation(rightInAnim)
                    flashcardAns3.startAnimation(rightInAnim)
                    // this method is called when the animation is finished playing
                    currentCardIndex++

                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if(currentCardIndex >= allFlashcards.size) {
                        Snackbar.make(
                            findViewById<TextView>(R.id.flashcard_question), // This should be the TextView for displaying your flashcard question
                            "You've reached the end of the cards! Back to the start!",
                            Snackbar.LENGTH_SHORT)
                            .show()
                        currentCardIndex = 0
                    }
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                    flashcardAnswer.text = allFlashcards[currentCardIndex].answer
                    flashcardAns2.text = allFlashcards[currentCardIndex].wrongAnswer1
                    flashcardAns3.text = allFlashcards[currentCardIndex].wrongAnswer2
                    flashcardQuestion.text = allFlashcards[currentCardIndex].question
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })
            flashcardQuestion.startAnimation(leftOutAnim)
            flashcardAns2.startAnimation(leftOutAnim)
            flashcardAnswer.startAnimation(leftOutAnim)
            flashcardAns3.startAnimation(leftOutAnim)
        }
        delete.setOnClickListener {
            val flashcardQuestionToDelete = findViewById<TextView>(R.id.flashcard_question).text.toString()
            flashcardDatabase.deleteCard(flashcardQuestionToDelete)
            //var number: Int
            //do {
            //    number = getRandomNumber(0, allFlashcards.size - 1)
            //} while (allFlashcards[number].question.equals(flashcardQuestion.text))
            currentCardIndex--

            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
            if(currentCardIndex >= allFlashcards.size) {
                Snackbar.make(
                    findViewById<TextView>(R.id.flashcard_question), // This should be the TextView for displaying your flashcard question
                    "You've reached the end of the cards! Back to the start!",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardIndex = 0
            }
            if (allFlashcards.size==0) {
                return@setOnClickListener
            }
            while (currentCardIndex<0) {
                currentCardIndex++
            }
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            flashcardAnswer.text = allFlashcards[currentCardIndex].answer
            flashcardAns2.text = allFlashcards[currentCardIndex].wrongAnswer1
            flashcardAns3.text = allFlashcards[currentCardIndex].wrongAnswer2
            flashcardQuestion.text = allFlashcards[currentCardIndex].question
        }
    }
}