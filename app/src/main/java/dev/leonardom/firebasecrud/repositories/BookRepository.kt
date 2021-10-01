package dev.leonardom.firebasecrud.repositories

import com.google.firebase.firestore.CollectionReference
import dev.leonardom.firebasecrud.model.Book
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


}
