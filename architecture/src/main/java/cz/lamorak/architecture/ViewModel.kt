package cz.lamorak.architecture

import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import cz.lamorak.architecture.ViewModel.*
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer

abstract class ViewModel<Intent : ViewIntent, State : ViewState, Action : ViewAction>(initialState: State) : ViewModel() {

    private val stateRelay: BehaviorRelay<State> = BehaviorRelay.createDefault(initialState)
    private val intentRelay: PublishRelay<Intent> = PublishRelay.create()
    private val actionRelay: PublishRelay<Action> = PublishRelay.create()

    init {
        intentRelay.withLatestFrom(stateRelay, BiFunction<Intent, State, Pair<Intent, State>> { intent, state -> intent to state })
                .filter({ filterIntent(it.first, it.second) })
                .subscribe({
                    val (intent, state) = it
                    stateRelay.accept(resolveState(intent, state))
                    actionRelay.accept(resolveAction(intent, state))
                    resolveModel(intent, state).subscribe(intentRelay)
                })
    }

    fun intents(): Consumer<Intent> = intentRelay
    fun state(): Observable<State> = stateRelay
    fun action(): Observable<Action> = actionRelay

    @Suppress("UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
    protected fun filterIntent(intent: Intent, state: State) = true

    protected abstract fun resolveState(intent: Intent, state: State): State
    protected abstract fun resolveAction(intent: Intent, state: State): Action
    protected abstract fun resolveModel(intent: Intent, state: State): Observable<Intent>

    abstract class ViewIntent {
        override fun toString(): String = javaClass.simpleName
    }

    abstract class ViewState {
        override fun toString(): String = javaClass.simpleName
    }

    abstract class ViewAction {
        override fun toString(): String = javaClass.simpleName
    }
}