package com.rodrigonovoa.wouldyoudrinkthiscocktail

import android.app.Application
import com.rodrigonovoa.wouldyoudrinkthiscocktail.koin.roomModules
import com.rodrigonovoa.wouldyoudrinkthiscocktail.koin.serviceModules
import com.rodrigonovoa.wouldyoudrinkthiscocktail.koin.useCaseModules
import com.rodrigonovoa.wouldyoudrinkthiscocktail.koin.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WouldYouDrinkThisCocktailApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WouldYouDrinkThisCocktailApp)
            androidLogger()
            modules(serviceModules, useCaseModules, viewModelModules, roomModules)
        }
    }
}