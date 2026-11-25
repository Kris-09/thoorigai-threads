package com.thoorigai.bffapi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProductIn(
    String id,
    String name,
    String description,
    BigDecimal price,
    String categoryId,
    String size,
    String color,
    int quantity,
    List<String> media,
    boolean featured,
    LocalDate createdAt
) {}
