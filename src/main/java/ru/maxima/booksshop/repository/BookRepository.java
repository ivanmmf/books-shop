package ru.maxima.booksshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.maxima.booksshop.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
List<Book> findByAuthorStartingWith(String author);

};


