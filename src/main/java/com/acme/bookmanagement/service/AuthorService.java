package com.acme.bookmanagement.service;

import org.springframework.stereotype.Service;

import com.acme.bookmanagement.model.Author;
import com.acme.bookmanagement.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getOrCreateAuthor(String name) {
        return authorRepository.findByName(name)
                .orElseGet(() -> {
                    Author author = new Author();
                    author.setName(name);
                    return authorRepository.save(author);
                });
    }
}
