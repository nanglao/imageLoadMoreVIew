package com.example.imageloadmoreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //coroutines
       val job = GlobalScope.launch(Dispatchers.Default) {
           repeat(5){
               delay(1000L)
               }
            Log.d(this@MainActivity.toString(), "Coroutine says hello from thread ${Thread.currentThread().name}")
        }
        Log.d(this@MainActivity.toString(), "Hello from thread ${Thread.currentThread().name}")

        Log.d(this@MainActivity.toString(), "Before RunBlocking")
        runBlocking {
            job.join()
            job.cancel()
            launch(Dispatchers.IO) {
                delay(3000L)
            }
            Log.d(this@MainActivity.toString(), "start RunBlocking")
            delay(100L)
            Log.d(this@MainActivity.toString(), "")
        }
        //Thread.sleep(5000L)
        Log.d(this@MainActivity.toString(), "")

    }
}