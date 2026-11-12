package com.thoorigai.capabilityapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
public class CategoryController {
    private final SheetService sheetService;

    public CategoryController(SheetService sheetService) {
        this.sheetService = sheetService;
    }

    @GetMapping("/categories")
    public List<Category> getCategories() throws Exception {
        return sheetService.getCategories();
    }
}
