package com.acme.bookmanagement.service;

import com.acme.bookmanagement.model.Author;
import com.acme.bookmanagement.model.Book;
import com.acme.bookmanagement.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookServiceTest {
    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final BookService bookService = new BookService(bookRepository);

    private final Author author = new Author(1L, "author-1");
    private final Book book = new Book(1L,
            "title-1",
            author,
            LocalDate.of(2021, 2, 3));

    @Test
    void testFindAll() {
        Mockito.when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));
        assertEquals(1, bookService.findAll().size());
    }
}
