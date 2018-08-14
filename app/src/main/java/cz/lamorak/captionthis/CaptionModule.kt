package cz.lamorak.captionthis

import cz.lamorak.architecture.ViewModel
import cz.lamorak.architecture.ViewModelFactory
import cz.lamorak.captionthis.interactor.CaptionInteractor
import cz.lamorak.captionthis.interactor.CaptionInteractorImpl
import cz.lamorak.captionthis.viewmodel.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CaptionModule {

    @Provides
    @Singleton
    fun viewModelFactory(viewModel: ViewModel<CaptionIntent, CaptionState, CaptionAction>) = ViewModelFactory(viewModel)

    @Provides
    fun captionViewModel(initialState: CaptionState,
                         captionInteractor: CaptionInteractor): ViewModel<CaptionIntent, CaptionState, CaptionAction> = CaptionViewModelImpl(initialState, captionInteractor)

    @Provides
    fun initialState(): CaptionState = CaptionState.SelectImage

    @Provides
    fun captionInteractor(): CaptionInteractor = CaptionInteractorImpl()
}