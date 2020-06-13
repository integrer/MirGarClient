package org.mirgar.android.cryptoelection

import org.kodein.di.KodeinAware
import org.mirgar.android.cryptoelection.data.DIFactory
import android.app.Application as BaseApplication

@Suppress("UNUSED")
class CryptoelectionApplication : BaseApplication(), KodeinAware {
    override val kodein = DIFactory.getInstance(::getApplicationContext)
}