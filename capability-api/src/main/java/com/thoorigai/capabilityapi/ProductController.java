package com.thoorigai.capabilityapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController 
public class ProductController {
    @GetMapping("/products")
    public List<Product> getProducts() {
        return List.of(
            new Product(
                "1",
                "Saree",
                "This is a unique saree",
                new BigDecimal("50.00"),
                "1",
                "L",
                "Blue",
                1,
                List.of("https://example.com/saree1.jpg", "https://example.com/saree2.jpg"),
                true,
                LocalDate.of(2026, 9, 9)
            ),
            new Product(
                "2",
                "Chudidar",
                "This is a custom made chudidar",
                new BigDecimal("40.00"),
                "1",
                "M",
                "Green",
                1,
                List.of("https://example.com/chudidar1.jpg", "https://example.com/chudidar2.jpg"),
                false,
                LocalDate.of(2026, 9, 9)
            )
        );
    }

}
