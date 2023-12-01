package ru.maxima.booksshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.booksshop.exception.BookNotFoundException;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service    // создаём бин userService
//@Transactional(readOnly = true) // помечаем методы класса только на чтение
public class BookServiceImpl implements BookService{

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Book> getAllBooks() {
        return repository.findAll();
    };

    @Override
    @Transactional
    public Book findById(int id){
        Book book = repository.findById(id).orElseThrow(BookNotFoundException::new);
        return book;
    };
    @Override
    public List<Book>findByAuthorStartingWith(String author){
        List<Book> bookByAuthor = repository.findByAuthorStartingWith(author);
        return  bookByAuthor;
    };
    @Override
    @Transactional
    public void save(Book book){
        repository.save(book);
        }



    @Override
    @Transactional
    public void delete(Book book) {
        repository.delete(book);
    }

    @Override
   // @Transactional
    public void update(int id, Book book) throws BookNotFoundException {
        Optional<Book> initialBook = repository.findById(id);

        if(initialBook.isPresent()) {
            initialBook.get().setName(book.getName());
            initialBook.get().setAuthor(book.getAuthor());
            initialBook.get().setIban(book.getIban());
            repository.save(initialBook.get());
        } else throw new BookNotFoundException() ;

    }
};

