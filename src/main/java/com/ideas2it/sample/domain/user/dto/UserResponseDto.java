package com.ideas2it.sample.domain.user.dto;

import com.ideas2it.sample.domain.book.dto.BookResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDto {
    private String username;
    private String email;
    private List<BookResponseDto> borrowedBooks;
}
