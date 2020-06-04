package se.mobileinteraction.sleip.ui.newHorse

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.add_new_horse_layout.*
import lv.chi.photopicker.PhotoPickerFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import timber.log.Timber
import java.io.File
import java.util.*


class CreateHorseFragment : Fragment(R.layout.add_new_horse_layout), PhotoPickerFragment.Callback {

    private val viewModel by viewModel<CreateHorseViewModel>()

    private var cameraFilePath: String? = ""
    private val file: File by lazy { File(cameraFilePath) }
    private val fileReqBody: RequestBody by lazy {
        file
            .asRequestBody("multipart/from-data".toMediaTypeOrNull())
    }
    private val part by lazy{
        MultipartBody.Part.createFormData("upload", file.name, fileReqBody)}


    var date: String? = null
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
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        et_date.text = ("$year-$month-$dayOfMonth").toString()
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }


        save_btn.setOnClickListener {
            /** to close keyboard**/
            val imm =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)


            val checkDate: String? = et_date.text.toString()
            et_date.text.toString()
            date = if (checkDate.isNullOrBlank()) {
                null
            } else {
                et_date.text.toString()
            }
            var horseImage: MultipartBody.Part? = null

            if (!cameraFilePath.isNullOrBlank()) {
                cameraFilePath?.let { it1 ->  horseImage = uploadToServer(it1) }
            }

            viewModel.createHorse(
                et_name.text.toString(), date, et_description?.text?.toString(), horseImage
            )
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
                findNavController().popBackStack()
            } else if (it.error != null) {
                Timber.e(it.error)
            }
        })
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
<<<<<<< HEAD
//        onePhoto.setImageURI(photos.joinToString(separator = "\n") { it.toString() }.toUri())
=======
>>>>>>> 970a605a10c175ef397eb85268be5af3315894fd
        onePhoto.setImageURI(photos.first())
        onePhoto.aspectRatio = onePhoto.height.toDouble() / onePhoto.width.toDouble()
        cameraFilePath = (photos.first().path.toString())
    }

    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = false,
            allowCamera = true,
            maxSelection = 1
        ).show(childFragmentManager, "picker")
    }

    private fun uploadToServer(filePath: String):MultipartBody.Part? {
        //Create a file object using file path
        val file = File(filePath)
        // Create a request body with file and image media type
        val fileReqBody: RequestBody =
            file.asRequestBody("multipart/from-data".toMediaTypeOrNull())
        // Create MultipartBody.Part using file request-body,file name and part name

        return MultipartBody.Part.createFormData("upload", file.name, fileReqBody)

    }
<<<<<<< HEAD

=======
>>>>>>> 970a605a10c175ef397eb85268be5af3315894fd
}
