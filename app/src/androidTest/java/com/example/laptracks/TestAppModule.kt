package com.example.laptracks

import android.content.Context
import com.example.laptracks.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
	@Provides
	@Named("test_db")
	fun provideAppDatabaseTest(@ApplicationContext context: Context): AppDatabase {
		return AppDatabase.getDatabase(context)
	}
}