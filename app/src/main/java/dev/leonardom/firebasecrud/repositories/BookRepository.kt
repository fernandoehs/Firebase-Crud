package dev.leonardom.firebasecrud.repositories

import com.google.firebase.firestore.CollectionReference
import dev.leonardom.firebasecrud.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepository
@Inject
constructor(
    private val booklist: CollectionReference
){
    fun addNewBook(book: Book){
        try {
            booklist.document(book.id).set(book)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun getBookList(): Flow<Result<List<Book>>> = flow{
     try {
         emit(Result.Loading<List<Book>>())

         val bookList = booklist.get().await().map{
             document -> document.toObject(Book::class.java)
         }

         emit(Result.Success<List<Book>>(data = bookList))
     }catch (e:Exception){
         emit(Result.Error<List<Book>>(message = e.localizedMessage ?:"Error desconocido"))
     }
    }


}
