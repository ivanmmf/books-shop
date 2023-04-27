package ru.maxima.booksshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.maxima.booksshop.exception.BookNotFoundException;
import ru.maxima.booksshop.exception.UserNotFoundException;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.repository.BookRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")                     // указываем версию API приложения
//@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3000/search-employee"})


// разрешаем React приложению обращаться к SpringBoot приложению
public class CrudBookController {

    @Autowired
    private BookRepository bookRepository;

    public Book findBookById(int id){
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return book;
    }

    @ResponseBody
    @GetMapping("/books")   // GET метод, который будет возвращать список книг в React приложение
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @PostMapping("/books")  // POST метод, который добавляет книгу
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping("/book/{id}")
    public Book getBook(@PathVariable Integer id) {
        Book book = findBookById(id);

        return book;
    }

    @GetMapping("/books/author/{author}")   // GET метод, который будет возвращать список работников в React приложение
    public List<Book> getBook(@PathVariable("author") String author) {


        List<Book> bookByAuthor = bookRepository.findByAuthorStartingWith(author);






        return  bookByAuthor;
    }

    @PutMapping("/book/{id}")
    public Book update(@PathVariable("id") Integer id, @RequestBody Book updatedBook) {
        Book initialBook = findBookById(id);
        initialBook.setName(updatedBook.getName());
        initialBook.setAuthor(updatedBook.getAuthor());
        initialBook.setIban(updatedBook.getIban());
        return bookRepository.save(initialBook);
        }
    @DeleteMapping({"/book/{id}"})
    public void deleteBook(@PathVariable Integer id) {
        Book deleteBook = findBookById(id);
        bookRepository.delete(deleteBook);
    }

}

