package dev.sergeitimoshenko.simplecontacts.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sergeitimoshenko.simplecontacts.db.ContactsDatabase
import dev.sergeitimoshenko.simplecontacts.utils.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideContactsDatabase(app: Application) =
        Room.databaseBuilder(
            app,
            ContactsDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideContactsDao(db: ContactsDatabase) = db.getContactDao()
}