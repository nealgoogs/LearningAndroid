package com.example.simplemathgame

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var puzzleTextView: TextView
    private lateinit var feedbackTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonMultiply: Button
    private lateinit var buttonDivide: Button
    private lateinit var resetButton: Button

    private var currentOperator: Char = '+'
    private var totalQuestions = 0
    private var correctAnswers = 0
    private var consecutiveCorrect = 0
    private var num1: Int = 0
    private var num2: Int = 0
    private var result: Int = 0

    //Make toast messages more fun
    private val praiseMessages = listOf(
        "Godly!!!!",
        "Math genius!!!!",
        "Next Albert Einstein????",
        "Unstoppable!",
        "Legendary brainpower!",
        "You're on fire!"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views from layout
        puzzleTextView = findViewById(R.id.puzzleTextView)
        feedbackTextView = findViewById(R.id.feedbackTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        buttonPlus = findViewById(R.id.buttonPlus)
        buttonMinus = findViewById(R.id.buttonMinus)
        buttonMultiply = findViewById(R.id.buttonMultiply)
        buttonDivide = findViewById(R.id.buttonDivide)
        resetButton = findViewById(R.id.resetButton)

        buttonPlus.setOnClickListener { onOperatorSelected('+') }
        buttonMinus.setOnClickListener { onOperatorSelected('-') }
        buttonMultiply.setOnClickListener { onOperatorSelected('*') }
        buttonDivide.setOnClickListener { onOperatorSelected('/') }


        resetButton.setOnClickListener { resetGame() }

        generateNewPuzzle()
    }

    //Math logic
    private fun generateNewPuzzle() {
        val operators = listOf('+', '-', '*', '/')
        currentOperator = operators.random()

        when (currentOperator) {
            '-' -> {
                do {
                    num1 = Random.nextInt(11, 100)
                    num2 = Random.nextInt(10, 99)
                } while (num1 <= num2)
            }
            '/' -> {
                do {
                    num2 = Random.nextInt(2, 20) // Ensure non-zero divisor
                    num1 = num2 * Random.nextInt(2, 10) // Ensure integer result
                } while (num1 % num2 != 0 || num1 == 0)
            }
            else -> {
                num1 = Random.nextInt(11, 100)
                num2 = Random.nextInt(10, 99)
            }
        }

        result = when (currentOperator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            '/' -> num1 / num2
            else -> 0
        }

        puzzleTextView.text = "$num1 ? $num2 = $result"
        feedbackTextView.text = ""
        // Hide feedback until an answer is selected
    }

    //Hacky thing to make correct and wrong text show up
    private fun onOperatorSelected(selectedOperator: Char) {
        checkAnswer(selectedOperator)
        feedbackTextView.visibility = TextView.VISIBLE
        feedbackTextView.postDelayed({ generateNewPuzzle() }, 1500)
    }

    private fun checkAnswer(selectedOperator: Char) {
        totalQuestions++

        if (selectedOperator == currentOperator) {
            correctAnswers++
            consecutiveCorrect++
            feedbackTextView.text = "Correct!"
            feedbackTextView.setTextColor(Color.GREEN)
            if (consecutiveCorrect % 3 == 0) {
                val randomPraise = praiseMessages.random()
                val toast = Toast.makeText(this, "$randomPraise You've answered $consecutiveCorrect in a row!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        } else {
            feedbackTextView.text = "Wrong! $num1 $currentOperator $num2 = $result"
            feedbackTextView.setTextColor(Color.RED)
            consecutiveCorrect = 0
        }

        scoreTextView.text = "Score: $correctAnswers/$totalQuestions"
    }

    private fun resetGame() {
        totalQuestions = 0
        correctAnswers = 0
        consecutiveCorrect = 0
        scoreTextView.text = "Score: 0/0"
        feedbackTextView.text = ""
        generateNewPuzzle()
    }
}
