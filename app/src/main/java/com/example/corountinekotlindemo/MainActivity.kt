package com.example.coroutineexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.corountinekotlindemo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity(){
    private val RESULT_1 = "RESULT#1"
    private val RESULT_2 = "RESULT#2"

    val JOB_TIMEOUT = 2100L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private suspend fun setTextOnMainThread(text:String){
        withContext(Main){
            textView.text = textView.text.toString()+"\n"+text
        }
    }

    private suspend fun fakeApiRequest(){
        val job = withContext(IO){
            withTimeoutOrNull(JOB_TIMEOUT) {

                val result1= getResult1fromApi()
                println("debug: $result1")
                setTextOnMainThread(result1)
                val result2 = getResult2FromApi()
                setTextOnMainThread(result2)
                println("debug: $result2")
            }

        }
        if (job==null){
            println("debug: TIMEOUT")
        }

    }

    private suspend fun getResult1fromApi():String{
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String{
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName:String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}