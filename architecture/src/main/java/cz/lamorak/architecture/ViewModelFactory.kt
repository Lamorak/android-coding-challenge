package cz.lamorak.architecture

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory<T : ViewModel> constructor(private val viewModel: T) : ViewModelProvider.Factory {

    override fun <U : ViewModel> create(modelClass: Class<U>): U {
        if (modelClass.isAssignableFrom(viewModel.javaClass)) {
            return viewModel as U
        } else {
            throw IllegalArgumentException("${javaClass.simpleName}<${viewModel.javaClass.simpleName}> cannot instantiate ${modelClass.simpleName}")
        }
    }
}