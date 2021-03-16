package com.example.corountinekotlindemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val startTime = System.currentTimeMillis()
       val parentJob= CoroutineScope(IO).launch {
            val job1= launch {
                val time1 = measureTimeMillis {
                    println("debug: launching job1 in thread: ${Thread.currentThread().name}")
                    val result1=getResult1FromApi()

                    setTextOnMainThread(result1)
                }
                println("debug: completed job1 in $time1 ms")
            }
            val job2= launch {
                val time2 = measureTimeMillis {
                    println("debug: launching job2 in thread: ${Thread.currentThread().name}")
                    val result2=getResult2FromApi()

                    setTextOnMainThread(result2)
                }
                println("debug: completed job2 in $time2 ms")
            }


        }
        parentJob.invokeOnCompletion {
            println("debug: Total time taken : ${System.currentTimeMillis() - startTime}")
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
        delay(1900)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000)
        return "Result #2"
    }


}