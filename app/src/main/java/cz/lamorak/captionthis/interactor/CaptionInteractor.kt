package cz.lamorak.captionthis.interactor

import io.reactivex.Single

interface CaptionInteractor {

    fun caption(imageUri: String): Single<String>
}