package cz.lamorak.captionthis.viewmodel

import cz.lamorak.captionthis.extensions.either
import cz.lamorak.captionthis.interactor.CaptionInteractor
import cz.lamorak.captionthis.viewmodel.CaptionAction.*
import cz.lamorak.captionthis.viewmodel.CaptionIntent.*
import cz.lamorak.captionthis.viewmodel.CaptionIntent.ErrorCaptioned
import cz.lamorak.captionthis.viewmodel.CaptionState.*
import io.reactivex.Observable

class CaptionViewModelImpl(initialState: CaptionState,
                           private val captionInteractor: CaptionInteractor) : CaptionViewModel(initialState) {

    override fun resolveState(intent: CaptionIntent, state: CaptionState) = when (intent) {
        is Caption -> Processing(intent.imageUri)
        is ImageCaptioned -> CaptionedImage(state.imageUri, intent.caption)
        is ErrorCaptioned -> CaptionError(state.imageUri, intent.error)
        Retry -> Processing(state.imageUri)
        NewImage -> SelectImage
        else -> state
    }

    override fun resolveAction(intent: CaptionIntent, state: CaptionState) = when (intent) {
        GallerySelect -> OpenGallery
        CameraSelect -> OpenCamera
        else -> None
    }

    override fun resolveModel(intent: CaptionIntent, state: CaptionState) = when (intent) {
        is Caption -> captionObservable(intent.imageUri)
        Retry -> captionObservable(state.imageUri)
        else -> Observable.empty<CaptionIntent>()
    }

    private fun captionObservable(imageUri: String): Observable<CaptionIntent> {
        return captionInteractor.caption(imageUri)
                .either(
                        { ImageCaptioned(it) },
                        { ErrorCaptioned(it.message ?: "") } //TODO: Implement error recognition
                )
                .toObservable()
    }
}