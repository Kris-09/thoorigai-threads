package com.thoorigai.bffapi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProductOut(
    String id,
    String name,
    String description,
    BigDecimal price,
    String categoryId,
    String categoryName,
    String size,
    String color,
    int quantity,
    List<String> media,
    boolean featured,
    LocalDate createdAt
) {}
