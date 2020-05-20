package ua.stepess.dnipro.orderservice.dto.book;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookItem {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private Long quantity;
    private List<Author> authors;
    private List<String> genres;
    private List<String> tags;
}
