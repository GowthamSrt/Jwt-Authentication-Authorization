package com.ideas2it.sample.domain.book.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.ideas2it.sample.domain.book.dto.BookDto;
import com.ideas2it.sample.domain.book.dto.BookResponseDto;
import com.ideas2it.sample.infrastructure.exception.AlreadyExistsException;
import com.ideas2it.sample.infrastructure.exception.ResourceNotFoundException;
import com.ideas2it.sample.domain.book.mapper.BookMapper;
import com.ideas2it.sample.domain.book.model.Book;
import com.ideas2it.sample.adaptor.out.BookRepository;
import com.ideas2it.sample.domain.book.port.in.BookService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LogManager.getLogger(BookServiceImpl.class);

    @Override
    public BookResponseDto addBook(BookDto bookDto) {
        bookAlreadyExists(bookDto.getName());
        Book book = bookMapper.toEntity(bookDto);
        bookRepository.save(book);
        LOGGER.info(bookDto.getName() + " : Book added successfully");
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAllByIsDeletedFalse()
                .stream()
                .map(bookMapper :: toDto)
                .toList();
    }

    @Override
    public BookResponseDto getBookById(Long id) {
        return bookRepository.findByIdAndIsDeletedFalse(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));
    }

    @Override
    public String deleteBook(Long id) {

        val book = bookRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " is not found"));
        book.setDeleted(true);
        bookRepository.save(book);
        LOGGER.info("Book with id " + id + "removed successfully");
        return "Book removed successfully";
    }

    @Override
    public BookResponseDto updateBook(Long id, JsonPatch patch) {
        Book bookExists = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));
        BookDto bookDto = bookMapper.toBookDto(bookExists);
        BookDto patchedDto = applyPatchToBookDto(patch, bookDto);
        Book patchedBook = bookMapper.toEntity(patchedDto);
        patchedBook.setId(id);
        Book updatedBook = bookRepository.save(patchedBook);
        LOGGER.info("Book with " + id + " : updated successfully");
        return bookMapper.toDto(updatedBook);

    }

    private BookDto applyPatchToBookDto(JsonPatch patch, BookDto bookDto) {
        try {
            JsonNode bookNode = objectMapper.convertValue(bookDto, JsonNode.class);
            JsonNode patchedNode = patch.apply(bookNode);
            return objectMapper.treeToValue(patchedNode, BookDto.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            LOGGER.warn("Patch apply failed");
            throw new RuntimeException("Failed to apply Json patch to BookDto");
        }
    }

    private void bookAlreadyExists(String name) {
        if (bookRepository.findByNameAndIsDeletedFalse(name).isPresent()) {
            LOGGER.warn("The Book is already exists with NAME : " + name);
            throw new AlreadyExistsException("Book already exists!");
        }
    }
}
