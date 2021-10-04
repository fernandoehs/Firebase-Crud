package dev.leonardom.firebasecrud.presentation.book_detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonardom.firebasecrud.model.Book

import dev.leonardom.firebasecrud.repositories.BookRepository
import dev.leonardom.firebasecrud.repositories.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel
@Inject
constructor(
    private val bookRepository:BookRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state: MutableState<BookDetailState> = mutableStateOf(BookDetailState())
    val state: State<BookDetailState>
        get()=_state
    init {
        savedStateHandle.get<String>("bookId")?.let {
            bookId -> getBook(bookId)
            
        }
    }

    private fun getBook(bookId: String) {
        bookRepository.getBookById(bookId).onEach { result ->
            when(result){
                is Result.Error-> {
                    _state.value = BookDetailState(error = result.message ?: "Unexpected error")
                }
                is Result.Loading -> {
                    _state.value = BookDetailState(isLoading = true)
                }
                is Result.Success -> {
                    _state.value = BookDetailState(book = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addNewBook(title:String,author:String){
        val book = Book(
        id = UUID.randomUUID().toString(),
        coverURL = "https://media.istockphoto.com/photos/blank-book-cover-white-picture-id178447904",
        title = title,
        author = author,
        rating = 0.0f,
        downloads = 0
        )

        bookRepository.addNewBook(book)

    }

    fun updateBook(newTitle:String,newAuthor:String){
        //revisa si el valor del estado del libro es null
        if(state.value.book == null){
            _state.value = BookDetailState(error =  "Unexpected error")
            return   //no continua
        }
        //se crea una variable bookedited donde copia titulo y autor
        val bookEdited = state.value.book!!.copy(
            title =newTitle,
            author =newAuthor
        )
        //llama a la funcion updatebook pero del Repositorio con los valores id y bookedited
        bookRepository.updateBook(bookEdited.id, bookEdited )
    }
}