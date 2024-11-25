package com.ideas2it.sample.adaptor.out;

import com.ideas2it.sample.domain.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByNameAndIsDeletedFalse(String name);
    List<Book> findAllByIsDeletedFalse();
    Optional<Book> findByIdAndIsDeletedFalse(Long id);
}
