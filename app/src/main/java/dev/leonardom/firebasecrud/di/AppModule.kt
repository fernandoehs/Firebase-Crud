package dev.leonardom.firebasecrud.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideBookList(
        firestore: FirebaseFirestore
    )=firestore.collection("books")
}