package com.watu.clone

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    
    // >>> PUT YOUR SERVER URL HERE AFTER YOU DEPLOY <<<
    val SERVER_URL = "https://YOUR-RENDER-URL.onrender.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val log = findViewById<TextView>(R.id.tvLog)
        val btnPay = findViewById<Button>(R.id.btnPay)
        val btnStatement = findViewById<Button>(R.id.btnStatement)

        btnPay.setOnClickListener {
            log.text = "Sending STK to 0700000000 for 1 bob..."
            val json = """{"phone":"0700000000","amount":1}""".trimIndent()
            val body = json.toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url("$SERVER_URL/stk").post(body).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { log.text = "Error: ${e.message}" }
                }
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread { log.text = "STK Response: ${response.body?.string()}" }
                }
            })
        }

        btnStatement.setOnClickListener {
            val request = Request.Builder().url("$SERVER_URL/statement").get().build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { log.text = "Error: ${e.message}" }
                }
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread { log.text = "Statement: ${response.body?.string()}" }
                }
            })
        }
    }
}
