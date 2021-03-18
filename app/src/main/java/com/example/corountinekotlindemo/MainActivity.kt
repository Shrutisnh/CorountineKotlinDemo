package com.example.corountinekotlindemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Main).launch {
            println("debug: Starting job in Thread: ${Thread.currentThread().name}")

            val result1=getResult()
            println("debug: Result#1 :$result1")

            val result2=getResult()
            println("debug: Result#2 :$result2")

            val result3 = getResult()
            println("debug: Result#3 :$result3")

            val result4 = getResult()
            println("debug: Result#4 :$result4")

        }

        CoroutineScope(Main).launch {
            runBlocking {
                println("debug: Blocking Thread: ${Thread.currentThread().name}")
                delay(1000)
                println("debug: Done Blocking thread: ${Thread.currentThread().name}")
            }
            //runblocking is used to block everything on that thread and executes this code first
        }


    }

    private suspend fun getResult(): Int{
       delay(1000)
       return Random.nextInt(1,100)
    }




}