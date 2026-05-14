package mn.icode.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mn.icode.model.Film;
import mn.icode.model.RentalCount;
import mn.icode.repository.FilmRepository;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RestController
public class FilmController {
    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping("/films")
    public ResponseEntity<List<Film>> getFilms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size

    ) {
        int offset = (page - 1) * size;
        return ResponseEntity.ok(filmRepository.findAll(size, offset));
    }

    @GetMapping("/films/search")
    public ResponseEntity<List<Film>> search(
            @RequestParam(required = false) String title) {
        List<Film> results = filmRepository.search(title);
        return results.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(results);
    }

    @GetMapping("/films/not-in-inventory")
    public ResponseEntity<List<Film>> getNonInventory(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        int offset = (page - 1) * size;
        return ResponseEntity.ok(filmRepository.notExist(size, offset));
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") int id) {
        return filmRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/films/top-rented")
    public ResponseEntity<List<RentalCount>> getTopFilms(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(filmRepository.findTopRented(limit));
    }

    // @DeleteMapping("/films/{id}")
    // public ResponseEntity<Optional<Film>> deletefilm(@PathVariable int id){
    // Optional <Film> film = filmRepository.findById(id);
    // if (film == null){
    // return ResponseEntity.notFound().build();
    // }
    // filmRepository.delete(id);
    // return ResponseEntity.ok(film);
    // }

    @DeleteMapping("/films/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable int id) {
        try {
            if (filmRepository.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            filmRepository.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Энд алдааг барьж авна
            return ResponseEntity.status(500)
                    .body("Энэ кино өөр өгөгдөлтэй холбоотой тул устгах боломжгүй (FK Constraint).");
        }
    }

    @GetMapping("/search")
    public List<Film> search(
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double maxRate,
            @RequestParam(required = false) Integer minLength,
            @RequestParam(required = false) Integer maxLength) {

        if (rating != null)
            return filmRepository.findByRating(rating); 
        if (keyword != null)
            return filmRepository.findBycase(keyword); 
        if (maxRate != null)
            return filmRepository.findByRentalRate(maxRate); 
        if (minLength != null && maxLength != null)
            return filmRepository.findByLengthBetween(minLength, maxLength); 
        return filmRepository.findAll(100, 0);
    }

}
