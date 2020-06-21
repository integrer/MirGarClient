package org.mirgar.android.cryptoelection.viewmodel

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.*

/**
 * View model factory for easy injecting view models with their dependencies using Kodein library
 */
class KodeinVMFactory(private val injector: DKodein) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        injector.InstanceOrNull(TT(modelClass)) ?: modelClass.newInstance()
}

class KodeinVMFactoryWithArgs<A>(
    private val injector: DKodein, private val args: A, private val argType: TypeToken<in A>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        injector.InstanceOrNull(argType, TT(modelClass), arg = args) ?: modelClass.newInstance()

    companion object {
        inline fun <reified A> new(injector: DKodein, args: A): KodeinVMFactoryWithArgs<A> {
            return KodeinVMFactoryWithArgs(injector, args, generic())
        }
    }
}

@MainThread
inline fun <reified VM : ViewModel, T> T.viewModels(): Lazy<VM> where T : Fragment, T : KodeinAware =
    viewModels(factoryProducer = ::kodeinVMFactory)

fun <T : KodeinAware> T.kodeinVMFactory() = KodeinVMFactory(direct)
inline fun <T : KodeinAware, reified A> T.kodeinVMFactory(args: A) =
    KodeinVMFactoryWithArgs.new(direct, args)
