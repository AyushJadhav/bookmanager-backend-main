package com.acme.bookmanagement.repository;

import com.acme.bookmanagement.model.Author;
import com.acme.bookmanagement.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorRepository authorRepo;  // Inject Author repo to save author entity

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setName("author-1");
        author = authorRepo.save(author);  // save author first

        book = new Book();
        book.setTitle("title-1");
        book.setAuthor(author);  // assign author entity
        book.setPublishedDate(LocalDate.of(2021, 2, 3));
        book = bookRepo.save(book);  // save book
    }

    @AfterEach
    void tearDown() {
        bookRepo.deleteAll();
        authorRepo.deleteAll();
    }

    @Test
    void testSavedBookCanBeFoundById() {
        Book savedBook = bookRepo.findById(book.getId()).orElse(null);

        assertNotNull(savedBook);
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getPublishedDate(), savedBook.getPublishedDate());

        // Check author object equality (could compare id or name)
        assertNotNull(savedBook.getAuthor());
        assertEquals(author.getName(), savedBook.getAuthor().getName());
    }
}
