package com.ideas2it.sample.adaptor.in;

import com.github.fge.jsonpatch.JsonPatch;
import com.ideas2it.sample.infrastructure.util.UserChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ideas2it.sample.domain.book.dto.BookDto;
import com.ideas2it.sample.domain.book.dto.BookResponseDto;
import com.ideas2it.sample.domain.book.port.in.BookService;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserChecker userChecker;

    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(@RequestBody BookDto bookDto) {
        userChecker.ensureAdminAccess();
        return new ResponseEntity<>(bookService.addBook(bookDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        userChecker.ensureAdminAccess();
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        userChecker.ensureAdminAccess();
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @RequestBody JsonPatch patch) {
        userChecker.ensureAdminAccess();
        BookResponseDto updatedBook = bookService.updateBook(id, patch);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        userChecker.ensureAdminAccess();
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.NO_CONTENT);
    }
}
