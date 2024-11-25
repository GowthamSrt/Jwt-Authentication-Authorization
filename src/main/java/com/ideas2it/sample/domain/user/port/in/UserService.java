package com.ideas2it.sample.domain.user.port.in;

import com.ideas2it.sample.domain.book.dto.BookResponseDto;

import java.util.List;

public interface UserService {
    void borrowBook(Long userId, Long bookId);
    void returnBook(Long userId, Long bookId);
    List<BookResponseDto> viewBorrowedBooks(Long userId);
    BookResponseDto getBookByName(String name);

}
