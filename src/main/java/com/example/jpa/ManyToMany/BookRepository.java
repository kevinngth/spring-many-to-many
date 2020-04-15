package com.example.jpa.ManyToMany;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BookRepository extends CrudRepository<Book, Long> {
     List<Book> findByTitle(String title);
}