package com.thoorigai.capabilityapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


@RestController 
public class ProductController {
    private final SheetService sheetService;

    public ProductController(SheetService sheetService) {
        this.sheetService = sheetService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() throws Exception {
        return sheetService.getProducts();
    }

}
