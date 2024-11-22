package com.ideas2it.sample.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto {
    private String name;
    private String author;
    private double price;
}
