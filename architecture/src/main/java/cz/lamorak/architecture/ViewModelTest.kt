package cz.lamorak.architecture

import cz.lamorak.architecture.ViewModel.*
import io.reactivex.observers.TestObserver
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class ViewModelTest<Intent: ViewIntent, State: ViewState, Action: ViewAction> {

    lateinit var viewModel: ViewModel<Intent, State, Action>

    abstract fun initViewModel(initialState: State)

    fun testIntent(intent: Intent): Pair<TestObserver<State>, TestObserver<Action>> {
        val states = viewModel.state().test()
        val actions = viewModel.action().test()
        viewModel.intents().accept(intent)
        return states to actions
    }

    protected fun <T> whenever(methodCall: T) = `when`(methodCall)

    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}