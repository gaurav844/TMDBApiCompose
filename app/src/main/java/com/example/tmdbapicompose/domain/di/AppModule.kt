package com.example.tmdbapicompose.domain.di

import android.content.Context
import com.example.tmdbapicompose.data.RemoteDataSource
import com.example.tmdbapicompose.data.apiCall.UserApi
import com.example.tmdbapicompose.domain.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * An Hilt @Module class/object to create dependencies to provide in defined binding hilt components
 */
@Module
@InstallIn(SingletonComponent::class)// SingletonComponent is a binding component in hilt to install dependencies in Singleton
object AppModule {

    @Provides
    @Singleton
    fun providesAuthApi(remoteDataSource: RemoteDataSource): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkUtil(@ApplicationContext context: Context) : NetworkUtil {
        return NetworkUtil(context)
    }

//    @Provides
//    @Singleton
//    fun provideLogger():Logger = Logger()

}