package mn.icode.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mn.icode.model.Category;
import mn.icode.model.FilmTitle;
import mn.icode.repository.CategoryRepository;

@Controller
@RequestMapping("/api")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(
            CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int offset = (page - 1) * size;
        return ResponseEntity.ok(categoryRepository.findAll(size, offset));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") int id) {
        return categoryRepository.findCategoryById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categories/{id}/films")
    public ResponseEntity<List<FilmTitle>> filmsByCategory(
                                        @PathVariable("id") int categoryId){
        List<FilmTitle> films = categoryRepository.filmsByCategory(categoryId);
        return ResponseEntity.ok(films);
    }
}
