package org.mirgar.android.cryptoelection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.mirgar.android.cryptoelection.data.Repository
import org.mirgar.android.cryptoelection.failure.CommonFailure
import org.mirgar.android.cryptoelection.failure.Failure
import org.mirgar.android.cryptoelection.failure.PermissionFailure
import org.mirgar.android.cryptoelection.failure.TransactionFailure
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.operations.OperationResult
import org.mirgar.android.cryptoelection.databinding.ActivityMainBinding as Binding

class MainActivity : AppCompatActivity(), KodeinAware {

    private val parentKodein by closestKodein()

    override val kodein by retainedKodein {
        extend(parentKodein)
        bind<MutableLiveData<Boolean>>(tag = "is-authorized") with singleton { isAuthorized }
        bind<MutableLiveData<Boolean>>(tag = "is-loading") with singleton { isLoading }
        bind<BaseOperationHandler>() with provider { OperationHandler() }
    }

    private val repository by instance<Repository>()

    private val isAuthorized = MutableLiveData<Boolean>()
    private val isLoading = MutableLiveData(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = Binding.inflate(layoutInflater)

        isAuthorized.value = repository.isAuthorized

        binding.isLoading = isLoading
        binding.lifecycleOwner = this
        setContentView(binding.root)
        isAuthorized.distinctUntilChanged().observe(this) { isAuthorized ->
            val navController = findNavController(R.id.nav_host_fragment)
            val id = navController.currentDestination?.id
            if (isAuthorized) {
                if (id == null || id == R.id.dst_authorization)
                    navController.navigate(R.id.dst_elections_list)
            } else {
                if (id != R.id.dst_authorization)
                    navController.navigate(R.id.dst_authorization)
            }
        }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()

    private inner class OperationHandler : BaseOperationHandler() {

        override fun beforeOperation() {
            isLoading.value = true
        }

        override fun afterOperation() {
            isLoading.value = false
        }

        override fun handleResult(result: OperationResult) {}

        override fun handleException(exception: Failure) {
            when (exception) {
                is CommonFailure -> TODO()
                is PermissionFailure -> TODO()
                is TransactionFailure -> TODO()
            }
        }

        override fun handleUnknown(exception: Throwable) {
            TODO("Not yet implemented")
        }

        override fun getScope(): CoroutineScope = lifecycleScope

    }

}

