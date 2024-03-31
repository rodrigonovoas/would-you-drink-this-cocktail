package com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink


@Database(entities = [Drink::class], version = 1)
abstract class DrinksDb : RoomDatabase() {

    abstract fun drinkDao(): DrinkDao

    companion object {
        private var instance: DrinksDb? = null

        @Synchronized
        fun getInstance(ctx: Context): DrinksDb {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, DrinksDb::class.java,
                    "drinks_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }
    }

}