package org.mirgar.android.cryptoelection.viewmodel

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.*

/**
 * View model factory for easy injecting view models with their dependencies using Kodein library
 */
class KodeinVMFactory(private val injector: DKodein) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        injector.InstanceOrNull(TT(modelClass)) ?: modelClass.newInstance()
}

class KodeinVMFactoryWithArgs<A>(
    private val injector: DKodein, private val args: A, private val argType: TypeToken<in A>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        injector.InstanceOrNull(argType, TT(modelClass), arg = args) ?: modelClass.newInstance()

    companion object {
        inline fun <reified A> new(injector: DKodein, args: A) =
            KodeinVMFactoryWithArgs(injector, args, generic())
    }
}

inline fun <reified VM : ViewModel, T> T.viewModels(): Lazy<VM> where T : Fragment, T : KodeinAware =
    viewModels(factoryProducer = ::kodeinVMFactory)

inline fun <reified VM : ViewModel, T> T.viewModels(): Lazy<VM> where T : ComponentActivity, T : KodeinAware =
    viewModels(::kodeinVMFactory)

fun KodeinAware.kodeinVMFactory() = KodeinVMFactory(direct)
inline fun <reified A> KodeinAware.kodeinVMFactory(args: A) =
    KodeinVMFactoryWithArgs.new(direct, args)
