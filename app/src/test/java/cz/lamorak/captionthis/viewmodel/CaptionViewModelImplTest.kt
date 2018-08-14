package cz.lamorak.captionthis.viewmodel

import cz.lamorak.architecture.ViewModelTest
import cz.lamorak.captionthis.extensions.toSingle
import cz.lamorak.captionthis.interactor.CaptionInteractor
import cz.lamorak.captionthis.viewmodel.CaptionAction.*
import cz.lamorak.captionthis.viewmodel.CaptionIntent.*
import cz.lamorak.captionthis.viewmodel.CaptionState.*
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CaptionViewModelImplTest: ViewModelTest<CaptionIntent, CaptionState, CaptionAction>() {

    @Mock
    lateinit var captionInteractor: CaptionInteractor

    @Test
    fun openGallery() {
        initViewModel(SelectImage)
        val (states, actions) = testIntent(GallerySelect)
        states.assertValues(SelectImage, SelectImage)
        actions.assertValues(OpenGallery)
    }

    @Test
    fun openCamera() {
        initViewModel(SelectImage)
        val (states, actions) = testIntent(CameraSelect)
        states.assertValues(SelectImage, SelectImage)
        actions.assertValues(OpenCamera)
    }

    @Test
    fun imageCaption() {
        val imageUri = "imageUri"
        val caption = "caption"
        whenever(captionInteractor.caption(any())).thenReturn(caption.toSingle())

        initViewModel(SelectImage)
        val (states, actions) = testIntent(Caption(imageUri))
        states.assertValues(
                SelectImage,
                Processing(imageUri),
                CaptionedImage(imageUri, caption)
        )
        actions.assertValues(None, None)
    }

    @Test
    fun imageCaptionError() {
        val imageUri = "imageUri"
        val error = "errorMessage"
        whenever(captionInteractor.caption(any())).thenReturn(Single.error(Throwable(error)))

        initViewModel(SelectImage)
        val (states, actions) = testIntent(Caption(imageUri))
        states.assertValues(
                SelectImage,
                Processing(imageUri),
                CaptionError(imageUri, error)
        )
        actions.assertValues(None, None)
    }

    @Test
    fun imageCaptionRetry() {
        val caption = "caption"
        whenever(captionInteractor.caption(any())).thenReturn(caption.toSingle())

        val initialState = CaptionError("imageUri", "error")
        initViewModel(initialState)
        val (states, actions) = testIntent(Caption(initialState.imageUri))
        states.assertValues(
                initialState,
                Processing(initialState.imageUri),
                CaptionedImage(initialState.imageUri, caption)
        )
        actions.assertValues(None, None)
    }

    @Test
    fun selectNewImage() {
        val initialState = CaptionedImage("imageUri", "caption")
        initViewModel(initialState)
        val (states, actions) = testIntent(NewImage)
        states.assertValues(initialState, SelectImage)
        actions.assertValues(None)
    }

    override fun initViewModel(initialState: CaptionState) {
        viewModel = CaptionViewModelImpl(initialState, captionInteractor)
    }
}