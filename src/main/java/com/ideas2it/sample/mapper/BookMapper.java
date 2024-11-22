package com.ideas2it.sample.mapper;

import com.ideas2it.sample.dto.BookDto;
import com.ideas2it.sample.dto.BookResponseDto;
import com.ideas2it.sample.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookDto bookDto) {
        return Book.builder()
                .name(bookDto.getName())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                .build();
    }

    public BookResponseDto toDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .price(book.getPrice())
                .build();
    }

    public BookDto toBookDto(Book book) {
        return BookDto.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .price(book.getPrice())
                .build();
    }
}
