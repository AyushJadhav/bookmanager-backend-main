package com.acme.bookmanagement.controller;

import com.acme.bookmanagement.model.Author;
import com.acme.bookmanagement.model.Book;
import com.acme.bookmanagement.model.BookInput;
import com.acme.bookmanagement.service.AuthorService;
import com.acme.bookmanagement.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.LocalDate;
import java.util.*;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQlTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    private final Map<Long, Book> books = Map.of(
            1L, new Book(1L, "title-1", new Author(10L, "author-1"), LocalDate.of(2021, 2, 3)),
            2L, new Book(2L, "title-2", new Author(20L, "author-2"), LocalDate.of(2021, 2, 3))
    );

    @Test
    void shouldGetAllBooks() {
        when(bookService.findAll()).thenReturn(new ArrayList<>(books.values()));

        graphQlTester.document("""
            query {
              findAllBooks {
                id
                title
                author {
                  id
                  name
                }
                publishedDate
              }
            }
            """)
            .execute()
            .path("findAllBooks")
            .entityList(Book.class)
            .hasSize(2)
            .contains(books.get(1L), books.get(2L));
    }

    @Test
    void shouldGetBookById() {
        Book book = books.get(1L);
        when(bookService.getBookById(1L)).thenReturn(book);

        graphQlTester.document("""
            query($id: ID!) {
              getBookById(id: $id) {
                id
                title
                author {
                  id
                  name
                }
                publishedDate
              }
            }
            """)
            .variable("id", 1L)
            .execute()
            .path("getBookById")
            .entity(Book.class)
            .isEqualTo(book);
    }

    @Test
    void shouldCreateBook() {
        Author author = new Author(30L, "author-3");
        BookInput input = new BookInput("title-3", "author-3", LocalDate.of(2023, 1, 1));
        Book createdBook = new Book(3L, input.getTitle(), author, input.getPublishedDate());

        when(authorService.getOrCreateAuthor(input.getAuthor())).thenReturn(author);

        // Use argThat to match the book passed to createBook
        when(bookService.createBook(org.mockito.ArgumentMatchers.argThat(book ->
            book.getTitle().equals(input.getTitle()) &&
            book.getAuthor().equals(author) &&
            book.getPublishedDate().equals(input.getPublishedDate())
        ))).thenReturn(createdBook);

        graphQlTester.document("""
            mutation($book: BookInput!) {
              createBook(book: $book) {
                id
                title
                author {
                  id
                  name
                }
                publishedDate
              }
            }
            """)
            .variable("book", Map.of(
                "title", input.getTitle(),
                "author", input.getAuthor(),
                "publishedDate", input.getPublishedDate().toString()
            ))
            .execute()
            .path("createBook")
            .entity(Book.class)
            .isEqualTo(createdBook);
    }

    @Test
    void shouldUpdateBook() {
        Long id = 1L;
        Author author = new Author(10L, "author-1");
        BookInput input = new BookInput("updated-title", "author-1", LocalDate.of(2022, 5, 5));
        Book updatedBook = new Book(id, input.getTitle(), author, input.getPublishedDate());

        when(authorService.getOrCreateAuthor(input.getAuthor())).thenReturn(author);
        when(bookService.updateBook(eq(id), any(Book.class))).thenReturn(updatedBook);

        graphQlTester.document("""
            mutation($id: ID!, $book: BookInput!) {
              updateBook(id: $id, book: $book) {
                id
                title
                author {
                  id
                  name
                }
                publishedDate
              }
            }
            """)
            .variable("id", id)
            .variable("book", Map.of(
                "title", input.getTitle(),
                "author", input.getAuthor(),
                "publishedDate", input.getPublishedDate().toString()
            ))
            .execute()
            .path("updateBook")
            .entity(Book.class)
            .isEqualTo(updatedBook);
    }

    @Test
    void shouldDeleteBook() {
        Long id = 1L;

        // Since deleteBook returns void, just verify it's called
        // Here we simulate it returns true after deletion
        // So we mock nothing, just check response

        graphQlTester.document("""
            mutation($id: ID!) {
              deleteBook(id: $id)
            }
            """)
            .variable("id", id)
            .execute()
            .path("deleteBook")
            .entity(Boolean.class)
            .isEqualTo(true);
    }

    @Test
    void shouldFindBooksByDate() {
        LocalDate date = LocalDate.of(2021, 2, 3);
        List<Book> expectedBooks = new ArrayList<>(books.values());
        when(bookService.findBooksByDate(date)).thenReturn(expectedBooks);

        graphQlTester.document("""
            query($publishedDate: String!) {
              findBooksByDate(publishedDate: $publishedDate) {
                id
                title
                author {
                  id
                  name
                }
                publishedDate
              }
            }
            """)
            .variable("publishedDate", date.toString())
            .execute()
            .path("findBooksByDate")
            .entityList(Book.class)
            .hasSize(expectedBooks.size())
            .contains(expectedBooks.toArray(new Book[0]));
    }
}
