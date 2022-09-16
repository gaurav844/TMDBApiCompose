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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesAuthApi(remoteDataSource: RemoteDataSource): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java)
    }

    @Provides
    fun provideNetworkUtil(@ApplicationContext context: Context) : NetworkUtil {
        return NetworkUtil(context)
    }

//    @Provides
//    fun provideProgressDialog(@ApplicationContext context: Context) : CShowProgress {
//        return CShowProgress(context)
//    }
}