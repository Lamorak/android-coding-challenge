package cz.lamorak.captionthis.interactor

import cz.lamorak.captionthis.extensions.toSingle
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class CaptionInteractorImpl : CaptionInteractor {

    override fun caption(imageUri: String): Single<String> {
        return imageUri.toSingle().delay(2, TimeUnit.SECONDS).flatMap { Single.error<String>(Throwable("Error")) }
    }
}