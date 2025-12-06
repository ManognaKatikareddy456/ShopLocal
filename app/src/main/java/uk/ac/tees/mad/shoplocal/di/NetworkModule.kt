package uk.ac.tees.mad.shoplocal.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.shoplocal.data.local.AppDatabase
import uk.ac.tees.mad.shoplocal.data.local.ShopDao
import uk.ac.tees.mad.shoplocal.data.remote.api.YelpApiService
import uk.ac.tees.mad.shoplocal.data.remote.api.YelpApiServiceSlow
import uk.ac.tees.mad.shoplocal.data.repository.YelpRepositoryImpl
import uk.ac.tees.mad.shoplocal.domain.reposiotry.YelpRepository
import uk.ac.tees.mad.shoplocal.domain.usecase.YelpUseCase

import uk.ac.tees.mad.shoplocal.domain.usecase.YelpUseCaseSlowUseCase
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.yelp.com/v3/"


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideYelpApiService(retrofit: Retrofit): YelpApiService {
        return retrofit.create(YelpApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideYelpApiServiceSlow(retrofit: Retrofit): YelpApiServiceSlow {
        return retrofit.create(YelpApiServiceSlow::class.java)
    }

    @Provides
    @Singleton
    fun yelpRepositoryImpl(yelpApiService: YelpApiService,yelpUseCaseSlow: YelpApiServiceSlow): YelpRepository {
        return YelpRepositoryImpl(
            api = yelpApiService,
            apiSlow = yelpUseCaseSlow,
        )

    }


    @Provides
    @Singleton
    fun providesYelpUseCase( yelpRepository: YelpRepository): YelpUseCase {

     return   YelpUseCase(
           yelpRepository = yelpRepository
        )

    }
    @Provides
    @Singleton
    fun providesYelpUseCaseSlow( yelpRepository: YelpRepository): YelpUseCaseSlowUseCase {

        return YelpUseCaseSlowUseCase(
            yelpRepository = yelpRepository
        )

    }

    @Provides
    @Singleton
    fun providesDB(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java,"app_db").build()
    }

    @Provides
    fun providesDao(db: AppDatabase): ShopDao {
        return db.plantDao()
    }



}