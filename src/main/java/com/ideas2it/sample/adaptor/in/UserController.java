package com.ideas2it.sample.adaptor.in;

import com.ideas2it.sample.domain.book.dto.BookResponseDto;
import com.ideas2it.sample.domain.user.port.in.UserService;
import com.ideas2it.sample.infrastructure.util.UserChecker;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserChecker userChecker;

    @PostMapping("/{userId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        userChecker.ensureUserAccess();
        userService.borrowBook(userId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @GetMapping("/{userId}/borrowed-books")
    public ResponseEntity<List<BookResponseDto>> getAllBorrowedBooks(@PathVariable Long userId) {
        userChecker.ensureUserAccess();
        val books = userService.viewBorrowedBooks(userId);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/{userId}/books/{bookId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        userChecker.ensureUserAccess();
        userService.returnBook(userId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @GetMapping("/books/name/{name}")
    public ResponseEntity<BookResponseDto> getBookByName(@PathVariable String name) {
        userChecker.ensureUserAccess();
        val book = userService.getBookByName(name);
        return ResponseEntity.ok(book);
    }
}
