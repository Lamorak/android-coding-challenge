package cz.lamorak.architecture

import com.jakewharton.rxrelay2.PublishRelay
import cz.lamorak.architecture.ViewModel.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

interface View<Intent : ViewIntent, State : ViewState, Action : ViewAction> {

    var viewModelFactory: ViewModelFactory<ViewModel<Intent, State, Action>>
    var viewModel: ViewModel<Intent, State, Action>
    var intentRelay: PublishRelay<Intent>
    var disposables: CompositeDisposable

    fun bind() {
        disposables = CompositeDisposable(
                intentRelay.subscribe(viewModel.intents()),

                viewModel.state()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(::display),

                viewModel.action()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(::handle),

                subscribeIntents()
        )

        val defaultIntent = defaultIntent()
        if (defaultIntent != null) intentRelay.accept(defaultIntent)
    }

    fun unbind() {
        disposables.dispose()
    }

    fun defaultIntent(): Intent? = null
    fun subscribeIntents(): CompositeDisposable
    fun display(state: State)
    fun handle(action: Action)

    fun <T> Observable<T>.asIntent(mapper: (T) -> Intent): Disposable = map(mapper).subscribe(intentRelay)
}