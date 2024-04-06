package com.rodrigonovoa.wouldyoudrinkthiscocktail.koin

import android.app.Application
import androidx.room.Room
import com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao.DrinkDao
import com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao.DrinksDb
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.TheCocktailDbRepository
import com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit.RetrofitClient
import com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity.MainActivityViewModel
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.GetCocktailUseCase
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.InsertDrinkUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val serviceModules = module {
    single { TheCocktailDbRepository(RetrofitClient.getDrinkApiService()) }
}

val useCaseModules = module {
    factory { GetCocktailUseCase(get()) }
    factory { InsertDrinkUseCase(get()) }
}

val roomModules  = module {
    single { provideDataBase(get()) }
    single { provideDao(get()) }
}

fun provideDataBase(application: Application): DrinksDb =
    Room.databaseBuilder(
        application,
        DrinksDb::class.java,
        "drinks_database"
    ).
    fallbackToDestructiveMigration().build()

fun provideDao(drinksDb: DrinksDb): DrinkDao = drinksDb.drinkDao()


val viewModelModules = module {
    viewModelOf(::MainActivityViewModel)
}