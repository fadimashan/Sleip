package se.mobileinteraction.sleip.util

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProgressRequestBody(
    private var file: File?
//    private val contentType: String
) : RequestBody() {


    var ignoreFirstNumberOfWriteToCalls: Int = 0
    var numWriteToCalls = 0

    val getProgressSubject: PublishSubject<Float> = PublishSubject.create()


    fun getProgressSubject(): Observable<Float> {
        return getProgressSubject
    }
    override fun contentType(): MediaType? {
        return ("contentType/*").toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file!!.length()

    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = file?.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val input = FileInputStream(file)
        var uploaded: Long = 0


        input.use { input ->
            var read = 0
            var lastProgressPercentUpdate = 0.0f
            read = input.read(buffer)
            while (read != -1) {

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = input.read(buffer)

                if (numWriteToCalls > ignoreFirstNumberOfWriteToCalls) {
                    val progress = (uploaded.toFloat() / fileLength!!.toFloat()) * 100f
                    if (progress - lastProgressPercentUpdate > 1 || progress == 100f) {
                        getProgressSubject.onNext(progress)
                        lastProgressPercentUpdate = progress
                    }
                }
            }
        }




//        input.use { input ->
//            val handler = Handler(Looper.getMainLooper())
//            var msg = Message()
//            while (input.read(buffer) != -1) {
//
//                handler.post(fileLength?.let { ProgressUpdater(uploaded, it) })
////                msg.what = (uploaded)
////                handler.sendMessage(msg)
//                uploaded += read
//                sink.write(buffer,0,read)
////                Thread.sleep(10)
//            }
//        }


    }

//class ProgressUpdater(uploaded: Long, total: Long) : Runnable {
//     private var uploaded: Long = uploaded
//     private var total: Long = total
//     lateinit var listener: UploadCallbacks
//
//    interface UploadCallbacks {
//        fun onProgressUpdate(percentage: Int)
//        fun onError()
//        fun onFinish()
//    }
//    override fun run() {
//
//        listener.onProgressUpdate((100 * uploaded/total).toInt())
//    }

}