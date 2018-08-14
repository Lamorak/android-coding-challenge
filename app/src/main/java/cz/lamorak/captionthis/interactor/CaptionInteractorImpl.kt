package cz.lamorak.captionthis.interactor

import cz.lamorak.captionthis.extensions.toSingle
import io.reactivex.Single

class CaptionInteractorImpl : CaptionInteractor {

    override fun caption(imageUri: String): Single<String> {
        return imageUri.toSingle()
    }
}