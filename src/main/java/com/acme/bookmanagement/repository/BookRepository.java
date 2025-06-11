package com.acme.bookmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.bookmanagement.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByPublishedDate(LocalDate publishedDate);

}

