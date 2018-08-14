package cz.lamorak.architecture

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.jakewharton.rxrelay2.PublishRelay
import cz.lamorak.architecture.ViewModel.*
import io.reactivex.disposables.CompositeDisposable

abstract class ViewActivity<Intent: ViewIntent, State: ViewState, Action: ViewAction>: FragmentActivity(), View<Intent, State, Action> {

    override lateinit var viewModelFactory: ViewModelFactory<ViewModel<Intent, State, Action>>
    override lateinit var viewModel: ViewModel<Intent, State, Action>
    override lateinit var intentRelay: PublishRelay<Intent>
    override lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentRelay = PublishRelay.create()
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        unbind()
    }
}
