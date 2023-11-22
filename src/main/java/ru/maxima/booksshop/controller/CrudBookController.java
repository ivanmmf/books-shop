package ru.maxima.booksshop.controller;

import javassist.bytecode.stackmap.BasicBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.maxima.booksshop.exception.BookNotFoundException;
import ru.maxima.booksshop.exception.UserNotFoundException;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.repository.BookRepository;
import ru.maxima.booksshop.service.BookService;
import ru.maxima.booksshop.service.BookServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")                     // указываем версию API приложения
//@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3000/search-employee"})


// разрешаем React приложению обращаться к SpringBoot приложению
public class CrudBookController {
    @Autowired
    BookService service;

    @ResponseBody
    @GetMapping("/books")   // GET метод, который будет возвращать список книг в React приложение
    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }

    @PostMapping("/books")  // POST метод, который добавляет книгу
    public void createBook(@RequestBody Book book) {
         service.save(book);
    }

    @GetMapping("/book/{id}")
    public Book getBook(@PathVariable Integer id) {
        Book book = service.findById(id);

        return book;
    }

    @GetMapping("/books/author/{author}")   // GET метод, который будет возвращать список работников в React приложение
    public List<Book> getBook(@PathVariable("author") String author) {


        List<Book> bookByAuthor = service.findByAuthorStartingWith(author);
        return  bookByAuthor;
    }

    @PutMapping("/book/{id}")
    public void update(@PathVariable("id") Integer id, @RequestBody Book updatedBook) {
        Book initialBook = service.findById(id);
        initialBook.setName(updatedBook.getName());
        initialBook.setAuthor(updatedBook.getAuthor());
        initialBook.setIban(updatedBook.getIban());
        service.save(initialBook);
        };
    @DeleteMapping({"/book/{id}"})
    public void deleteBook(@PathVariable Integer id) {
        Book deleteBook = service.findById(id);
        service.delete(deleteBook);
    }

}

