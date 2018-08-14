package cz.lamorak.captionthis

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.jakewharton.rxbinding2.view.clicks
import cz.lamorak.architecture.ViewActivity
import cz.lamorak.architecture.ViewModel
import cz.lamorak.architecture.ViewModelFactory
import cz.lamorak.captionthis.extensions.loadUri
import cz.lamorak.captionthis.extensions.setVisible
import cz.lamorak.captionthis.viewmodel.CaptionAction
import cz.lamorak.captionthis.viewmodel.CaptionAction.*
import cz.lamorak.captionthis.viewmodel.CaptionIntent
import cz.lamorak.captionthis.viewmodel.CaptionIntent.*
import cz.lamorak.captionthis.viewmodel.CaptionState
import cz.lamorak.captionthis.viewmodel.CaptionState.*
import cz.lamorak.captionthis.viewmodel.CaptionViewModel
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_select_image.*
import java.io.File
import javax.inject.Inject

class MainActivity : ViewActivity<CaptionIntent, CaptionState, CaptionAction>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelFactory<ViewModel<CaptionIntent, CaptionState, CaptionAction>>
    private var resultIntent: CaptionIntent? = null
    private var cameraImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CaptionViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        resultIntent = null
    }

    override fun defaultIntent(): CaptionIntent? = resultIntent

    override fun subscribeIntents() = CompositeDisposable(
            select_image_gallery.clicks().asIntent { GallerySelect },
            select_image_camera.clicks().asIntent { CameraSelect },
            error_retry.clicks().asIntent { Retry }
    )

    override fun display(state: CaptionState) {
        select_image.setVisible(state === SelectImage)
        processing_label.setVisible(state is Processing)
        image_preview.setVisible(state.imageUri.isNotBlank())
        image_preview.loadUri(state.imageUri) // TODO: Alllow image loading in espresso tests
        caption.setVisible(state is CaptionedImage)
        if (state is CaptionedImage) caption.text = state.caption
        error_label.setVisible(state is CaptionError)
        error_retry.setVisible(state is CaptionError)
        if (state is CaptionError) error_label.text = state.error
    }

    override fun handle(action: CaptionAction) = when(action) {
        OpenGallery -> openGallery()
        OpenCamera -> openCamera()
        else -> {}
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, IMAGE_GALLERY)
    }

    private fun openCamera() {
        cameraImageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, createFile())

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, IMAGE_CAMERA)
        }
    }

    private fun createFile() = File.createTempFile("attachment", ".jpg", externalCacheDir)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        val imageUri = when (requestCode) {
            IMAGE_GALLERY -> data?.data
            IMAGE_CAMERA -> cameraImageUri
            else -> null
        }

        if (imageUri != null) {
            resultIntent = Caption(imageUri.toString())
        }
    }

    companion object {
        private const val IMAGE_GALLERY = 11
        private const val IMAGE_CAMERA = 12
    }
}
