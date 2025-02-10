package com.example.simplemathgame

import android.graphics.Color
import android.os.Bundle
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
    private lateinit var resetButton: Button

    private var currentOperator: Char = '+'
    private var totalQuestions = 0
    private var correctAnswers = 0
    private var consecutiveCorrect = 0

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
        resetButton = findViewById(R.id.resetButton)

        // Set up listeners for the operator buttons
        buttonPlus.setOnClickListener { checkAnswer('+') }
        buttonMinus.setOnClickListener { checkAnswer('-') }
        buttonMultiply.setOnClickListener { checkAnswer('*') }

        // Reset button starts a new game
        resetButton.setOnClickListener { resetGame() }

        // Start the first puzzle
        generateNewPuzzle()
    }

    // Generates a new puzzle
    private fun generateNewPuzzle() {
        // Randomly choose an operator from the list
        val operators = listOf('+', '-', '*')
        currentOperator = operators.random()

        // Generate two-digit numbers based on given ranges:
        // 11 ≤ randomNumber1 < 100 and 10 ≤ randomNumber2 < 99.
        var num1: Int
        var num2: Int

        if (currentOperator == '-') {
            // Ensure the first number is greater than the second for subtraction.
            do {
                num1 = Random.nextInt(11, 100)
                num2 = Random.nextInt(10, 99)
            } while (num1 <= num2)
        } else {
            num1 = Random.nextInt(11, 100)
            num2 = Random.nextInt(10, 99)
        }

        // Compute the result based on the chosen operator.
        val result = when (currentOperator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            else -> 0
        }

        // Display the puzzle with a question mark instead of the operator.
        puzzleTextView.text = "$num1 ? $num2 = $result"
        // Clear any previous feedback.
        feedbackTextView.text = ""
    }

    // Checks the user’s answer when an operator button is clicked.
    private fun checkAnswer(selectedOperator: Char) {
        totalQuestions++
        if (selectedOperator == currentOperator) {
            correctAnswers++
            consecutiveCorrect++
            feedbackTextView.text = "Correct!"
            feedbackTextView.setTextColor(Color.GREEN)
            // Every time the user gets 3 consecutive correct answers, show a congratulatory Toast.
            if (consecutiveCorrect % 3 == 0) {
                Toast.makeText(
                    this,
                    "Great job! You've answered $consecutiveCorrect in a row!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            feedbackTextView.text = "Wrong! The correct answer is $currentOperator"
            feedbackTextView.setTextColor(Color.RED)
            // Reset the consecutive counter if the answer is incorrect.
            consecutiveCorrect = 0
        }
        // Update score display.
        scoreTextView.text = "Score: $correctAnswers/$totalQuestions"
        // Generate the next puzzle.
        generateNewPuzzle()
    }

    // Resets the game to the initial state.
    private fun resetGame() {
        totalQuestions = 0
        correctAnswers = 0
        consecutiveCorrect = 0
        scoreTextView.text = "Score: 0/0"
        feedbackTextView.text = ""
        generateNewPuzzle()
    }
}
