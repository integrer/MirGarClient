package org.mirgar.android.cryptoelection

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.mirgar.android.cryptoelection.failure.CommonFailure
import org.mirgar.android.cryptoelection.failure.Failure
import org.mirgar.android.cryptoelection.failure.PermissionFailure
import org.mirgar.android.cryptoelection.failure.TransactionFailure
import org.mirgar.android.cryptoelection.operations.BaseOperationHandler
import org.mirgar.android.cryptoelection.operations.OperationResult
import org.mirgar.android.cryptoelection.ui.AuthorizationFragment
import org.mirgar.android.cryptoelection.viewmodel.MainViewModel
import org.mirgar.android.cryptoelection.viewmodel.viewModels
import java.lang.ref.WeakReference
import org.mirgar.android.cryptoelection.databinding.ActivityMainBinding as Binding

class MainActivity : AppCompatActivity(), KodeinAware {

    private val parentKodein by closestKodein()

    override val kodein by retainedKodein {
        extend(parentKodein)
        bind<BaseOperationHandler>() with provider { OperationHandler() }
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState

        viewModel.switchToAuthorization = ::openAuthorization
        viewModel.switchToNavigation = ::openNavigation
        viewModel.lifecycleOwner = this

        Binding.inflate(layoutInflater).apply {
            isLoading = viewModel.isLoading
            lifecycleOwner = this@MainActivity
            executePendingBindings()
            setContentView(root)
        }

        viewModel.bindingsCompleted()
    }

    private var savedInstanceState: Bundle? = null
    private var navHostFragment: WeakReference<NavHostFragment>? = null

    private fun openNavigation() {
        val mgr = supportFragmentManager
        val navHostFragment = NavHostFragment.create(R.navigation.nav_graph, savedInstanceState)
        mgr.beginTransaction()
            .replace(R.id.content, navHostFragment)
            .commit()
        this.navHostFragment = WeakReference(navHostFragment)
    }

    private fun openAuthorization() {
        val mgr = supportFragmentManager
        mgr.beginTransaction()
            .replace(R.id.content, AuthorizationFragment::class.java, savedInstanceState)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.logout).apply {
            val itemRef = WeakReference(this)
            viewModel.isAuthorized.observe(this@MainActivity) { itemRef.get()?.isVisible = it }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> {
            viewModel.logOut(); true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = try {
        navHostFragment?.get()?.navController?.navigateUp() ?: false || super.onSupportNavigateUp()
    } catch (_: IllegalStateException) {
        super.onSupportNavigateUp()
    }

    private inner class OperationHandler : BaseOperationHandler() {
        override val context: Context get() = this@MainActivity

        override fun beforeOperation() {
            viewModel.operationStarted()
        }

        override fun afterOperation() {
            viewModel.operationFinished()
        }

        override fun handleResult(result: OperationResult) {
            when (result) {
                OperationResult.LoggedIn -> viewModel.loggedIn()
                OperationResult.LoggedOut -> viewModel.loggedOut()
            }
        }

        override fun handleException(exception: Failure) {
            when (exception) {
                is CommonFailure -> when (exception) {
                    is CommonFailure.Unknown -> alert()
                    CommonFailure.NotFound -> alert(R.string.not_found)
                    else -> alert("")
                }
                is PermissionFailure -> alert("")
                is TransactionFailure -> alert("")
            }
        }

        override fun handleUnknown(exception: Throwable) {
            exception.printStackTrace()
            alert()
        }

        override fun getScope(): CoroutineScope = lifecycleScope

    }

}

