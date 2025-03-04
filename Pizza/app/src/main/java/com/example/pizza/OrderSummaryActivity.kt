package com.example.pizza

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class OrderSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        val tvQuantity = findViewById<TextView>(R.id.tvQuantity)
        val btnIncreaseQuantity = findViewById<Button>(R.id.btnIncreaseQuantity)
        val btnDecreaseQuantity = findViewById<Button>(R.id.btnDecreaseQuantity)
        val switchDeliveryFee = findViewById<Switch>(R.id.switchDeliveryFee)
        val seekBarTip = findViewById<SeekBar>(R.id.seekBarTip)
        val tvTip = findViewById<TextView>(R.id.tvTip)
        val tvTotalPrice = findViewById<TextView>(R.id.tvTotalPrice)
        val tvSubtotal = findViewById<TextView>(R.id.tvSubtotal)
        val tvTax = findViewById<TextView>(R.id.tvTax)
        val tvTipAmount = findViewById<TextView>(R.id.tvTipAmount)
        val btnEditPizza = findViewById<Button>(R.id.btnEditPizza)
        val btnOrder = findViewById<Button>(R.id.btnOrder)

        //Intent
        val pizzaType = intent.getStringExtra("pizzaType") ?: "Unknown"
        val pizzaSize = intent.getStringExtra("pizzaSize") ?: "Unknown"
        val baseSubtotal = intent.getDoubleExtra("subtotal", 0.0)
        val toppingCount = intent.getIntExtra("toppingCount", 0)

        var quantity = 1
        var deliveryFee = 0.0
        var tipPercentage = 0.0
        val taxRate = 0.0635

        findViewById<TextView>(R.id.tvPizzaType).text = pizzaType
        findViewById<TextView>(R.id.tvPizzaSize).text = pizzaSize
        findViewById<TextView>(R.id.tvToppings).text = "$toppingCount Toppings"

        fun updatePrice() {
            val subtotal = baseSubtotal * quantity
            val taxAmount = (subtotal + deliveryFee) * taxRate
            val tipAmount = subtotal * (tipPercentage / 100)
            val totalPrice = subtotal + deliveryFee + taxAmount + tipAmount

            tvSubtotal.text = "Subtotal: $%.2f".format(subtotal)
            tvTax.text = "Tax (6.35%%): $%.2f".format(taxAmount)
            tvTipAmount.text = "Tip: $%.2f".format(tipAmount)
            tvTotalPrice.text = "Total Price: $%.2f".format(totalPrice)
        }

        btnEditPizza.setOnClickListener {
            startActivity(Intent(this, PizzaSelectionActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            finish()
        }

        btnIncreaseQuantity.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
            updatePrice()
        }

        btnDecreaseQuantity.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
                updatePrice()
            }
        }

        switchDeliveryFee.setOnCheckedChangeListener { _, isChecked ->
            deliveryFee = if (isChecked) 2.00 else 0.00
            switchDeliveryFee.text = if (isChecked) "Delivery Fee: Yes (\$2.00)" else "Delivery Fee: No (\$0.00)"
            updatePrice()
        }


        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipPercentage = progress.toDouble()
                tvTip.text = "$progress%"
                updatePrice()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnOrder.setOnClickListener {
            val totalPrice = tvTotalPrice.text.toString()
            startActivity(Intent(this, PizzaSelectionActivity::class.java).apply {
                putExtra("finalPrice", totalPrice)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            finish()
        }

        updatePrice()
    }
}
