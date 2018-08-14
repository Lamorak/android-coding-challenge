package cz.lamorak.captionthis

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import cz.lamorak.architecture.ViewActivity
import cz.lamorak.architecture.ViewModel
import cz.lamorak.architecture.ViewModelFactory
import cz.lamorak.captionthis.viewmodel.CaptionAction
import cz.lamorak.captionthis.viewmodel.CaptionIntent
import cz.lamorak.captionthis.viewmodel.CaptionState
import cz.lamorak.captionthis.viewmodel.CaptionViewModel
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : ViewActivity<CaptionIntent, CaptionState, CaptionAction>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelFactory<ViewModel<CaptionIntent, CaptionState, CaptionAction>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CaptionViewModel::class.java)
    }

    override fun subscribeIntents() = CompositeDisposable()

    override fun display(state: CaptionState) {
    }

    override fun handle(action: CaptionAction) {
    }
}
