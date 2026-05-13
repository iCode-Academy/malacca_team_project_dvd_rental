package mn.icode.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            @PathVariable("id") int categoryId) {
        List<FilmTitle> films = categoryRepository.filmsByCategory(categoryId);
        return ResponseEntity.ok(films);
    }

    @PostMapping
    @RequestMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        if (category.getCategoryId() == null || category.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        Category created = categoryRepository.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
      @GetMapping("/categories/search")
    public ResponseEntity<List<Category>> search(
            @RequestParam(required = false) String name) {
        List<Category> results = categoryRepository.search(name);
        return results.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(results);
    }
    @DeleteMapping("/categories/dlt={id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        Optional<Category> category = categoryRepository.findCategoryById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        int rows = categoryRepository.delete(id);
        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/categories/{id:\\d+}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") int id, @RequestBody Category category) {
        int rows = categoryRepository.update(id, category);
        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

}
