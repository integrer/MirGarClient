package org.mirgar.android.cryptoelection.viewmodel

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.*
import org.kodein.di.generic.bind
import org.kodein.di.generic.instanceOrNull

/**
 * View model factory for easy injecting view models with their dependencies using Kodein library
 */
class KodeinVMFactory(private val injector: DKodein) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        injector.instanceOrNull<ViewModel>(tag = modelClass.ident()) as T? ?: modelClass.newInstance()
}

class KodeinVMFactoryWithArgs<A : Any>(
    private val injector: DKodein, private val args: Typed<A>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        injector.instanceOrNull<A, ViewModel>(modelClass.ident(), args) as T?
            ?: modelClass.newInstance()

    companion object {
        inline fun <reified A : Any> new(injector: DKodein, args: A): KodeinVMFactoryWithArgs<A> {
            return KodeinVMFactoryWithArgs(injector, Typed(generic(), args))
        }
    }
}

@MainThread
inline fun <reified VM : ViewModel, T> T.viewModels(): Lazy<VM> where T : Fragment, T : KodeinAware =
    viewModels(factoryProducer = ::kodeinVMFactory)

fun <T: KodeinAware> T.kodeinVMFactory() = KodeinVMFactory(direct)
inline fun <T : KodeinAware, reified A : Any> T.kodeinVMFactory(args: A) = KodeinVMFactoryWithArgs.new(direct, args)

fun <T> Class<T>.ident() = name

inline fun <reified VM : ViewModel> Kodein.Builder.bindViewModel(overrides: Boolean? = null) =
    bind<ViewModel>(VM::class.java.ident(), overrides)
