package se.mobileinteraction.sleip.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.exoplayer2.util.Log


class MyWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {


    override fun doWork(): Result {

        return try {

            makeStatusNotification("Adding new Horse!", applicationContext)
            sleep()
            Result.success()

        } catch (e: Exception) {
            Log.d("MyWorker", "exception in doWork ${e.message}")
            Result.failure()
        }
    }


}

