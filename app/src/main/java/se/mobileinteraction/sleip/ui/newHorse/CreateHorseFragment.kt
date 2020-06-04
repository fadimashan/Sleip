package se.mobileinteraction.sleip.ui.newHorse

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.add_new_horse_layout.*
import lv.chi.photopicker.PhotoPickerFragment
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.util.MyWorker
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.util.*


class CreateHorseFragment : Fragment(R.layout.add_new_horse_layout), PhotoPickerFragment.Callback {

    private val viewModel by viewModel<CreateHorseViewModel>()

    private val requestNotif = OneTimeWorkRequestBuilder<MyWorker>().build()

    private var cameraFilePath: String? = ""
    var date: String? = null

    lateinit var builder: MultipartBody.Builder
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** adding the calender picker **/
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        date_piker.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { dataPiker, year, month, dayOfMonth ->
                        et_date.text = ("$year-$month-$dayOfMonth").toString()
                    },
                    year,
                    month,
                    day
                )

            }
            dpd?.datePicker?.maxDate = System.currentTimeMillis()
            dpd?.show()
        }


        save_btn.setOnClickListener {
            hideKeyboard()
            RxPermissions(this).request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
                .subscribe { granted ->
                    if (granted) {
                        val checkDate: String? = et_date.text.toString()
                        et_date.text.toString()
                        date = if (checkDate.isNullOrBlank()) {
                            null
                        } else {
                            et_date.text.toString()
                        }
                        var horseImage: File? = null

                        if (!cameraFilePath.isNullOrBlank()) {
                            cameraFilePath?.let { it1 -> horseImage = File(it1) }
                        }

                        createHorse(
                            et_name.text.toString(),
                            date,
                            et_description?.text?.toString(),
                            horseImage
                        )
                        viewModel.uploadImage(builder)

                        /** https://stackoverflow.com/questions/33338181/is-it-possible-to-show-progress-bar-when-upload-image-via-retrofit-2/44125313#44125313 **/
//                        val mediaPart = ProgressRequestBody(File(horseImage?.path))
//                        val requestBody = MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("image", horseImage?.name, mediaPart)
//                            .build()
//
//                        mediaPart.getProgressSubject()
//                            .subscribeOn(Schedulers.io())
//                            ?.subscribe({ r ->
//                                Log.i("Progress", "$it")
//                            }, { e ->
//                                OnErrorNotImplementedException("ERROR", e)
//                            })
//                        var postSub: Disposable? = null
//                        val name = et_name.text.toString()
//                            val date =  et_date.text.toString()
//                            val description = et_description.text.toString()
//                        postSub = viewModel.api.createHorse(name,null,description,requestBody.part(0))
//                            ?.subscribeOn(Schedulers.io())
//                            ?.observeOn(AndroidSchedulers.mainThread())
//                            ?.subscribe({ _
//                                -> },
//                                { e ->
//                                    OnErrorNotImplementedException("ERROR", e)
//                                    e.printStackTrace()
//                                    postSub?.dispose()
//                                }, {
//                                    Toast.makeText(
//                                        requireContext(),
//                                        "Upload Success!",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                })
//
//                    } else {
//                        Timber.e("Error")
                    }
                }
        }

        cancel_btn.setOnClickListener {
            /** to close keyboard**/
            val imm =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)

            /** to back one step back on click **/
            findNavController().popBackStack()

        }

        onePhoto.setOnClickListener {
            RxPermissions(this).request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .subscribe { granted ->
                    if (granted) {
                        openPicker()
                    } else {
                        Timber.e("tag")
                    }

                }
        }


    }

    private fun observeData() {
        viewModel.createHorseResponse.observe(viewLifecycleOwner, Observer {
            if (it.horse != null) {
                WorkManager.getInstance(requireContext()).enqueue(requestNotif)
                hideKeyboard()
                findNavController().popBackStack()

            } else if (it.error != null) {
                Timber.e(it.error)
            }
        })

    }

    private fun hideKeyboard() {
        val imm =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
    }

    @SuppressLint("CheckResult")
    override fun onImagesPicked(photos: ArrayList<Uri>) {
        val fileName = File(photos.first().toString()).name
        val fileUri = photos.first()
        onePhoto.setImageURI(photos.first())
        onePhoto.aspectRatio = onePhoto.height.toDouble() / onePhoto.width.toDouble()
        saveFile(fileName, fileUri)
        val newImageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + File.separator + SLEIP_DIRECTORY_PATH + fileName
        cameraFilePath = newImageDir

    }


    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = false,
            allowCamera = true,
            maxSelection = 1
        ).show(childFragmentManager, "picker")
    }

    private fun saveFile(photoName: String, photoPath: Uri) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, photoName)
            put(MediaStore.MediaColumns.MIME_TYPE, IMAGE_TYPE)
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + SLEIP_DIRECTORY_PATH
            )
        }

        val imageUri =
            requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        val parcelFileDescriptor =
            requireContext().contentResolver?.openFileDescriptor(photoPath, "r", null)
        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val outputStream =
            imageUri?.let { requireContext().contentResolver.openOutputStream(it) }

        inputStream.use { input ->
            outputStream.use { output ->
                output?.let { input.copyTo(it) }
            }
        }

    }

    private fun createHorse(
        name: String,
        date: String?,
        description: String?,
        horsePhoto: File?
    ) {
        builder = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart("name", name)
            date?.let { addFormDataPart("date_of_birth", it) }
            description?.let {
                addFormDataPart("description", it)
            }

            horsePhoto?.let {
                addFormDataPart(
                    "image", horsePhoto.name,
                    it.asRequestBody(MultipartBody.FORM)
                )
            }

        }
    }

//    private fun createHorse(
//        name: String,
//        date: String?,
//        description: String?,
//        horsePhoto: File?
//    ) {
//        MultipartBody.Builder().setType(MultipartBody.FORM).apply {
//            addFormDataPart("name", name)
//            date?.let { addFormDataPart("date_of_birth", it) }
//            description?.let {
//                addFormDataPart("description", it)
//            }
//
//            horsePhoto?.let {
//                addFormDataPart(
//                    "image", horsePhoto.name,
//                    it.asRequestBody(MultipartBody.FORM)
//                )
//            }
//
//        }.build()
//    }

    /** this way according to: https://proandroiddev.com/uploading-a-file-with-progress-in-kotlin-6cae3aa4a2d4 **/
//_______________________________________________________________________________________________
    // create upload request
//    private fun createHorse(
//            name: String,
//            date: String?,
//            description: String?,
//            horsePhoto: File?,
//            progressEmitter: PublishSubject<Double>
//    ): Single<Horse> {
//        val requestBody = horsePhoto?.let { createUploadRequestBody(it, progressEmitter) }
//        return viewModel.api.createHorse(
//                name = name.toPlainTextBody(),
//                date_of_birth = date?.toPlainTextBody(),
//                description = description?.toPlainTextBody(),
//                image = requestBody?.let {
//                    MultipartBody.Part.createFormData(
//                            name = "images",
//                            filename = horsePhoto.name,
//                            body = it
//                    )
//                }
//        )
//    }
//
//    private fun String.toPlainTextBody() = toRequestBody("text/plain".toMediaType())
//
//
//    private fun createUploadRequestBody(
//            file: File,
//            progressEmitter: PublishSubject<Double>
//    ): RequestBody {
//        val extension: String = MimeTypeMap.getFileExtensionFromUrl(file.path)
//        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
//        val fileRequestBody = file.asRequestBody(mimeType?.toMediaType())
//        return CountingRequestBody(fileRequestBody) { bytesWritten, contentLength ->
//            val progress = 1.0 * bytesWritten / contentLength
//            progressEmitter.onNext(progress)
//
//            if (progress >= 1.0) {
//                progressEmitter.onComplete()
//            }
//        }
//    }
//
//    fun uploadAttachment(name: String, date: String?, description: String?, horsePhoto: File?)
//            : Observable<AttachmentUploadRemoteResult> {
//        val progressEmitter = PublishSubject.create<Double>()
//        val uploadRequest = createHorse(
//                name, date, description, horsePhoto, progressEmitter)
//
//        val uploadResult = uploadRequest
//                .map<AttachmentUploadRemoteResult> {
//            CountingRequestResult.Completed(it)
//        }.toObservable()
//
//        val progressResult = progressEmitter
//                .map<AttachmentUploadRemoteResult>{
//            CountingRequestResult.Progress(it)
//        }
//
//        return progressResult.mergeWith(uploadResult)
//    }
//
//
    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension: String = MimeTypeMap.getFileExtensionFromUrl(url)
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return type
    }
// ________________________________________________________________________________________________

    companion object {
        const val SLEIP_DIRECTORY_PATH = "/Sliep/"
        const val IMAGE_TYPE = "image/jpg"

    }
}
