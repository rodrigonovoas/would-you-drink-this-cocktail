package com.rodrigonovoa.wouldyoudrinkthiscocktail.koin

import com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity.MainActivityViewModel
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.TheCocktailDbRepository
import com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit.RetrofitClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val serviceModules = module {
    single { TheCocktailDbRepository(RetrofitClient.getDrinkApiService()) }
}

val viewModelModules = module {
    viewModelOf(::MainActivityViewModel)
}