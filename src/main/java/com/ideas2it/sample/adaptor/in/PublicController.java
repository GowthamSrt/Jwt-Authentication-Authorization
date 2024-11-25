package com.ideas2it.sample.adaptor.in;

import com.ideas2it.sample.domain.book.dto.BookResponseDto;
import com.ideas2it.sample.domain.book.mapper.BookMapper;
import com.ideas2it.sample.domain.book.port.in.BookService;
import com.ideas2it.sample.domain.user.port.in.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("public/books")
@RequiredArgsConstructor
public class PublicController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        val books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BookResponseDto> getBookByName(@PathVariable String name) {
        BookResponseDto book = userService.getBookByName(name);
        return ResponseEntity.ok(book);
    }
}
