package com.thoorigai.capabilityapi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record Product(
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
