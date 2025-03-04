package com.example.pizza

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PizzaSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizza_selection)

        val radioGroupPizzaType = findViewById<RadioGroup>(R.id.radioGroupPizzaType)
        val radioGroupPizzaSize = findViewById<RadioGroup>(R.id.radioGroupPizzaSize)
        val imagePizza = findViewById<ImageView>(R.id.imagePizza)
        val tvSubtotal = findViewById<TextView>(R.id.tvSubtotal)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)

        val toppings = arrayOf(
            findViewById<CheckBox>(R.id.cbMushroom),
            findViewById<CheckBox>(R.id.cbOnions),
            findViewById<CheckBox>(R.id.cbOlives),
            findViewById<CheckBox>(R.id.cbTomatoes),
            findViewById<CheckBox>(R.id.cbBroccoli),
            findViewById<CheckBox>(R.id.cbSpinach)
        )

        // Pizza image changes
        radioGroupPizzaType.setOnCheckedChangeListener { _, checkedId ->
            val imageRes = when (checkedId) {
                R.id.rbPepperoni -> R.drawable.pepperoni
                R.id.rbBBQChicken -> R.drawable.bbq_chicken
                R.id.rbMargherita -> R.drawable.margherita
                R.id.rbHawaiian -> R.drawable.hawaiian
                else -> R.drawable.pizza_crust
            }
            imagePizza.setImageResource(imageRes)
        }

        // Update prices
        val updateSubtotal = {
            val sizeId = radioGroupPizzaSize.checkedRadioButtonId
            val basePrice = when (sizeId) {
                R.id.rbSmall -> 10.29
                R.id.rbMedium -> 12.59
                R.id.rbLarge -> 14.89
                else -> 0.0
            }

            val toppingCount = toppings.count { it.isChecked }
            val toppingPrice = when (sizeId) {
                R.id.rbSmall -> toppingCount * 1.39
                R.id.rbMedium -> toppingCount * 2.29
                R.id.rbLarge -> toppingCount * 2.99
                else -> 0.0
            }

            val total = basePrice + toppingPrice
            tvSubtotal.text = "Subtotal: $%.2f".format(total)
        }

        radioGroupPizzaSize.setOnCheckedChangeListener { _, _ -> updateSubtotal() }
        toppings.forEach { it.setOnCheckedChangeListener { _, _ -> updateSubtotal() } }

        //Reset button
        btnReset.setOnClickListener {
            radioGroupPizzaType.clearCheck()
            radioGroupPizzaSize.clearCheck()
            toppings.forEach { it.isChecked = false }
            imagePizza.setImageResource(R.drawable.pizza_crust)
            tvSubtotal.text = "Subtotal: $0.00"
        }

        //Checkout button
        btnCheckout.setOnClickListener {
            val typeId = radioGroupPizzaType.checkedRadioButtonId
            val sizeId = radioGroupPizzaSize.checkedRadioButtonId

            //Toast message
            if (typeId == -1 || sizeId == -1) {
                Toast.makeText(this, "Please select a pizza type and size", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedType = findViewById<RadioButton>(typeId).text.toString()
            val selectedSize = findViewById<RadioButton>(sizeId).text.toString()

            val subtotal = tvSubtotal.text.toString().substringAfter(": $").toDouble()
            val toppingCount = toppings.count { it.isChecked }

            val intent = Intent(this, OrderSummaryActivity::class.java).apply {
                putExtra("pizzaType", selectedType)
                putExtra("pizzaSize", selectedSize)
                putExtra("subtotal", subtotal)
                putExtra("toppingCount", toppingCount)
            }

            startActivity(intent)
        }

        //Toast messages
        intent.getStringExtra("finalPrice")?.let {
            Toast.makeText(this, "Your pizza has been ordered! $it. Enjoy your meal!", Toast.LENGTH_LONG).show()
        }
    }
}
