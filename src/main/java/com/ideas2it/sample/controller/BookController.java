package com.ideas2it.sample.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ideas2it.sample.dto.BookDto;
import com.ideas2it.sample.dto.BookResponseDto;
import com.ideas2it.sample.helper.JwtHelper;
import com.ideas2it.sample.service.BookService;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(@RequestBody BookDto bookDto) {
        if (!JwtHelper.isAdmin()) {
            throw new RuntimeException("Only admins can add books");
        }
        return new ResponseEntity<>(bookService.addBook(bookDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        if (!JwtHelper.isAdmin()) {
            throw new RuntimeException("Only admins can see all books");
        }
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        if (!JwtHelper.isAdmin()) {
            throw new RuntimeException("Only admins can see all books");
        }
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @RequestBody JsonPatch patch) {
        if (!JwtHelper.isAdmin()) {
            throw new RuntimeException("Only admins can see all books");
        }
        BookResponseDto updatedBook = bookService.updateBook(id, patch);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (!JwtHelper.isAdmin()) {
            throw new RuntimeException("Only admins can see all books");
        }
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.NO_CONTENT);
    }
}
