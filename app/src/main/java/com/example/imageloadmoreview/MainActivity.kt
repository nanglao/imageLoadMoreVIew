package com.example.imageloadmoreview

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?): String {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Async and Await kotlin coroutines
        GlobalScope.launch {
            val time = measureTimeMillis {
                val answer1 = async { networkCall1() }
                val answer2 = async { networkCall2() }
                Log.d(this@MainActivity.toString(), "Answer1:${answer1.await()}")
                Log.d(this@MainActivity.toString(), "Answer2:${answer2.await()}")
            }
            Log.d(this@MainActivity.toString(), "Requests took $time")
        }

        suspend fun networkCall1() : String {
        delay(3000L)
        return "answer1"
        }

        suspend fun networkCall2() : String {
            delay(5000L)
            return "answer2"
        }
}