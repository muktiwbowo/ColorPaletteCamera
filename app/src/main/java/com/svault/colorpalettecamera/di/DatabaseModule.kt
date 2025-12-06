package com.svault.colorpalettecamera.di

import android.content.Context
import androidx.room.Room
import com.svault.colorpalettecamera.data.local.dao.PaletteDao
import com.svault.colorpalettecamera.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePaletteDao(database: AppDatabase): PaletteDao {
        return database.paletteDao()
    }
}
