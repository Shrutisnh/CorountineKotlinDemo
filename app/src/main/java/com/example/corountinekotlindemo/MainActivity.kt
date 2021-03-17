package com.example.corountinekotlindemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Clicked!!")
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest(){


            CoroutineScope(IO).launch {
              val executionTime = measureTimeMillis {
                  val result1: Deferred<String> = async {
                      println("debug: launching job1 in thread ${Thread.currentThread().name}")
                      getResult1FromApi()
                  }
                  val result2: Deferred<String> = async {
                      println("debug: launching job2 in thread ${Thread.currentThread().name}")
                      getResult2FromApi()
                  }
                  setTextOnMainThread("Got ${result1.await()}")
                  setTextOnMainThread("Got ${result2.await()}")
              }
                println("debug: Time elapsed: $executionTime")
            }




    }
    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }
    private suspend fun setTextOnMainThread(input: String) {
        withContext (Main) {
            println("debug: setting text in ${Thread.currentThread().name} thread")
            setNewText(input)
        }
    }
    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1700)
        return "Result #2"
    }


}