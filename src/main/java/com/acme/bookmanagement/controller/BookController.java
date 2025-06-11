package com.acme.bookmanagement.controller;

import com.acme.bookmanagement.model.Book;
import com.acme.bookmanagement.model.BookInput;
import com.acme.bookmanagement.service.BookService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<Book> findAllBooks() {
        return bookService.findAll();
    }

    @QueryMapping
    public Book getBookById(@Argument("id") Long id) {
        return bookService.getBookById(id);
    }

    @MutationMapping
    public Book createBook(@Argument("book") BookInput bookInput) {
        Book book = toBook(null, bookInput);
        return bookService.createBook(book);
    }

    @MutationMapping
    public Book updateBook(@Argument("id") Long id, @Argument("book") BookInput bookInput) {
        Book book = toBook(id, bookInput);
        return bookService.updateBook(id, book);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument("id") Long id) {
        bookService.deleteBook(id);
        return true;
    }

    private Book toBook(Long id, BookInput input) {
        Book book = new Book();
        if (id != null) book.setId(id);
        book.setTitle(input.getTitle());
        book.setAuthor(input.getAuthor());
        book.setPublishedDate(input.getPublishedDate());
        return book;
    }
    
    @QueryMapping
    public List<Book> findBooksByDate(@Argument("publishedDate") String publishedDate) {
        LocalDate date = LocalDate.parse(publishedDate);
        return bookService.findBooksByDate(date);
    }
}
