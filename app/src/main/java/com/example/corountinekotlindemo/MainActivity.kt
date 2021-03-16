package com.example.coroutineexample

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.corountinekotlindemo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.cancellation.CancellationException


class MainActivity : AppCompatActivity(){

    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000
    private lateinit var job: CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job_button.setOnClickListener {
            if(!::job.isInitialized){
                initJob()
            }
            job_progress_bar.startJobOrCancel(job)
        }
    }

    fun ProgressBar.startJobOrCancel(job: Job){
        if(this.progress>0) {
            println("This job has already been started")
            resetJob(job)
        }
        else {
            job_button.setText("Cancel Job#1")

            CoroutineScope(IO + job).launch {
                println("coroutine ${this} is activated with job ${job}")
                println("thread: ${Thread.currentThread().name}")

                for(i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is Complete")

            }

        }
    }

    private fun resetJob(job: Job){
        if(job.isActive|| job.isCompleted)
            job.cancel(CancellationException("Resetting Job"))
    initJob()
    }


    fun initJob(){
        job_button.setText("Start Job#1")
        job_complete_text.setText("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg =it
                if(msg.isNullOrBlank()){
                    println("Unknown Cancellation error")
                }else {
                    println("$job was cancelled because: $msg")
                }
                msg?.let { it1 -> showToast(it1) }
            }
        }

        job_progress_bar.max = PROGRESS_MAX
        job_progress_bar.progress = PROGRESS_START

    }

    private fun updateJobCompleteTextView(text:String){
        GlobalScope.launch(Main) {
            job_complete_text.text = text
        }
    }

    fun showToast(text: String){
        GlobalScope.launch(Main) {
            Toast.makeText(this@MainActivity,text,Toast.LENGTH_LONG).show()
        }
    }

}