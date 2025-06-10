package com.acme.bookmanagement.controller;

import com.acme.bookmanagement.model.Book;
import com.acme.bookmanagement.service.BookService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/graphql")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<Book> findAllBooks() {
        return bookService.findAll();
    }
    
    
 // --- CREATE ---
    @MutationMapping
    public Book createBook(@Argument("title") String title, @Argument("author") String author, @Argument("publishedDate") String publishedDate) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublishedDate(LocalDate.parse(publishedDate));
        return bookService.save(book);
    }
    
 // --- UPDATE ---
    @MutationMapping
    public Book updateBook(@Argument Long id, @Argument String title, @Argument String author, @Argument String publishedDate) {
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublishedDate(LocalDate.parse(publishedDate));
        return bookService.save(book);
    }

    // --- DELETE ---
    @MutationMapping
    public boolean deleteBook(@Argument Long id) {
        return bookService.deleteById(id);
    }

}
