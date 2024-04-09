package com.omdb.app.di

import com.omdb.app.api.MovieSearchApi
import com.omdb.app.api.RetrofitBuilder
import com.omdb.app.dispatchers.MoviesTaskDispatchers
import com.omdb.app.dispatchers.TaskDispatchers
import com.omdb.app.repository.MoviesRepository
import com.omdb.app.repository.Repository
import com.omdb.app.source.MovieDataSource
import com.omdb.app.source.RemoteMoviesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideMovieSearchClient(retrofitBuilder: RetrofitBuilder) : MovieSearchApi = retrofitBuilder.retrofit.create(MovieSearchApi::class.java)

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(searchApi: MovieSearchApi) : MovieDataSource = RemoteMoviesDataSource(searchApi)

    @Provides
    @Singleton
    fun providesTaskDispatchers() : TaskDispatchers = MoviesTaskDispatchers()

    @Provides
    @Singleton
    fun provideMovieRepository(dataSource: MovieDataSource) : Repository = MoviesRepository(dataSource)

}