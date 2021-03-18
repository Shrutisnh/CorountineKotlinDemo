package com.example.coroutineexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.corountinekotlindemo.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity() {
    private val TAG: String = "AppDebug"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main()
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception")
    }

    fun main() {
        val parentJob = CoroutineScope(IO).launch (handler){
            supervisorScope {
                val jobA = launch {
                    val resultA = getResult(1)
                    println("resultA: ${resultA}")
                }

                // --------- JOB B ---------
                val jobB = launch {

                    val resultB = getResult(2)
                    println("resultB: ${resultB}")
                }

                // --------- JOB C ---------
                val jobC = launch {
                    val resultC = getResult(3)
                    println("resultC: ${resultC}")
                }

            }


        }
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed: ${throwable}")
            } else {
                println("Parent job SUCCESS")
            }
        }
    }

    suspend fun getResult(number: Int): Int {
        return withContext(Main) {
            delay(number * 500L)
            if (number == 2) {
//                cancel(CancellationException("Error getting result for number: ${number}"))
//                throw CancellationException("Error getting result for number: ${number}") // treated like "cancel()"
                throw Exception("Error getting result for number: ${number}")
            }
            number * 2
        }
    }


    private fun println(message: String) {
        Log.d(TAG, message)
    }
}