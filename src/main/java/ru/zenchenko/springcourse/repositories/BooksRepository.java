package ru.zenchenko.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zenchenko.springcourse.model.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwnerId(int id);
    List<Book> findByNameStartingWith(String pattern);
}
