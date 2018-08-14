package cz.lamorak.captionthis.viewmodel

import cz.lamorak.architecture.ViewModel
import cz.lamorak.architecture.ViewModel.*

abstract class CaptionViewModel(initialState: CaptionState) : ViewModel<CaptionIntent, CaptionState, CaptionAction>(initialState)

sealed class CaptionIntent : ViewIntent() {
    object GallerySelect: CaptionIntent()
    object CameraSelect: CaptionIntent()
    data class Caption(val imageUri: String): CaptionIntent()
    data class ImageCaptioned(val caption: String): CaptionIntent()
    data class ErrorCaptioned(val error: String): CaptionIntent()
    object Retry: CaptionIntent()
    object NewImage: CaptionIntent()
}

sealed class CaptionState(open val imageUri: String) : ViewState() {
    object SelectImage : CaptionState("")
    data class Processing(override val imageUri: String): CaptionState(imageUri)
    data class CaptionedImage(override val imageUri: String, val caption: String): CaptionState(imageUri)
    data class CaptionError(override val imageUri: String, val error: String): CaptionState(imageUri)
}

sealed class CaptionAction : ViewAction() {
    object None: CaptionAction()
    object OpenGallery: CaptionAction()
    object OpenCamera: CaptionAction()
}