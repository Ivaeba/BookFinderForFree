package com.example.library;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService{
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public void saveBook(Book book){
        bookRepository.save(book);
    }
    public List<Book> getBooksByAuthor(String author){
        return bookRepository.findByAuthor(author);
    }
    public Book getById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
    }
    public String buyBook(Long id){
        Book book = getById(id);
        // 2. Проверяем статус книги. Используем геттер book.getAvailable()
        if (book.getAvailable() == true) {
            book.setAvailable(false);
            bookRepository.save(book);
            return "Thank you for buying '" + book.getTitle() + "'! It will be delivered soon.";
        } else {
            return "Sorry, the book '" + book.getTitle() + "' is out of stock!";
        }
    }
}