package com.ideas2it.sample.domain.user.usecase;

import com.ideas2it.sample.adaptor.out.BookRepository;
import com.ideas2it.sample.adaptor.out.UserRepository;
import com.ideas2it.sample.domain.book.dto.BookResponseDto;
import com.ideas2it.sample.domain.book.mapper.BookMapper;
import com.ideas2it.sample.domain.book.model.Book;
import com.ideas2it.sample.domain.user.mapper.UserMapper;
import com.ideas2it.sample.domain.user.model.User;
import com.ideas2it.sample.domain.user.port.in.UserService;
import com.ideas2it.sample.infrastructure.exception.BookAlreadyBorrowedException;
import com.ideas2it.sample.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    @Override
    public void borrowBook(Long userId, Long bookId) {
        val user = findUserById(userId);
        val book = findBookByIdAndNotDeleted(bookId);
        if (user.getBorrowedBooks().contains(book)) {
            throw new BookAlreadyBorrowedException("This book is already borrowed by you!");
        }
        user.getBorrowedBooks().add(book);
        userRepository.save(user);
    }

    @Override
    public void returnBook(Long userId, Long bookId) {
        val user = findUserById(userId);
        val book = findBookByIdAndNotDeleted(bookId);
        if (!user.getBorrowedBooks().contains(book)) {
            throw new ResourceNotFoundException("You didn't borrowed this book!");
        }
        user.getBorrowedBooks().remove(book);
        userRepository.save(user);
    }

    @Override
    public List<BookResponseDto> viewBorrowedBooks(Long userId) {
        val user = findUserById(userId);
        return user.getBorrowedBooks().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDto getBookByName(String name) {
        val book = bookRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with name: " + name));
        return bookMapper.toDto(book);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Book findBookByIdAndNotDeleted(Long bookId) {
        return bookRepository.findByIdAndIsDeletedFalse(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }
}
