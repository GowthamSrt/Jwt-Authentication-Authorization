package com.ideas2it.sample.domain.book.port.in;

import com.github.fge.jsonpatch.JsonPatch;
import com.ideas2it.sample.domain.book.dto.BookDto;
import com.ideas2it.sample.domain.book.dto.BookResponseDto;

import java.util.List;

public interface BookService {

    BookResponseDto addBook(BookDto bookDto);
    List<BookResponseDto> getAllBooks();
    BookResponseDto getBookById(Long id);
    BookResponseDto updateBook(Long id, JsonPatch patch);
    String deleteBook(Long id);
}
