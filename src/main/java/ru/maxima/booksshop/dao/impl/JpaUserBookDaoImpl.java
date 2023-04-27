package ru.maxima.booksshop.dao.impl;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.booksshop.DTO.UpdateUserBook;
import ru.maxima.booksshop.dao.UserBookDao;
import ru.maxima.booksshop.model.Book;
import ru.maxima.booksshop.model.User;
import ru.maxima.booksshop.model.UserBook;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component                          // создаем бин jpaUserDao
@Transactional(readOnly = true)
@Repository
// помечаем методы класса как только на чтение
public class JpaUserBookDaoImpl implements UserBookDao{

        @PersistenceContext(unitName = "entityManagerFactory")  // контекст в котором находятся сущности,
        // которые управляются с помощью entityManager
        // entityManagerFactory бин определяется в persistence-config.xml и на его основе строится PersistenceContext
        private EntityManager entityManager;

        @Override
//        @Transactional(isolation = Isolation.READ_COMMITTED)
        public List<Book> findUserBooksByLogin(String login) {
            // language=JPQL
           Query query = entityManager.createQuery("select c from User u inner join u.books as b inner join b.book as c where u.login = :login");
           query.setParameter("login", login);


           return query.getResultList();
        }

        @Override
        @Transactional  // помечаем метод add на запись
        public void addBook(User user, Book book) {
            Integer userId = user.getId();
            Integer bookId = book.getId();
            Query query = entityManager.createNativeQuery("insert into user_books(user_id, book_id) values (:userId,:bookId)");
            query.setParameter("bookId", bookId);
            query.setParameter("userId", userId);
            query.executeUpdate();
                // обозначает сущность в PersistenceContext как готовую на запись
        }

    public User findUserByLogin(String login) {
        // language=JPQL
        Query query = entityManager.createQuery("select u from User u where u.login = :login");
        query.setParameter("login", login);
        return (User) query.getSingleResult();
    }
    public Book findBookById(int bookId) {
        // language=JPQL
        Query query = entityManager.createQuery("select b from Book b where b.id = :bookId");
        query.setParameter("bookId", bookId);
        return (Book) query.getSingleResult();
    }



    public void deleteBookFromUser(User user, Book book) {
        // language=JPQL
        Query query = entityManager.createQuery("delete from UserBook ub where ub.user = :user and ub.book = :book");
        query.setParameter("user", user);
        query.setParameter("book", book);
        query.executeUpdate();
        }
}


