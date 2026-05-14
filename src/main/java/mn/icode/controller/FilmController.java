package mn.icode.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import mn.icode.model.Film;
import mn.icode.repository.FilmRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
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
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("title"));
        List<Film> films = filmRepository.findAll(pageable).getContent();
        return ResponseEntity.ok(films);
    }

    @GetMapping("/films/search")
    public ResponseEntity<List<Film>> search(
            @RequestParam(required = false, name = "title") String title,
            @RequestParam(required = false, name = "rating") String rating,
            @RequestParam(required = false, name = "keyword") String keyword
    ) {
        if (rating != null && !rating.isEmpty()) {
            return ResponseEntity.ok(filmRepository.findByRating(rating));
        }
        String searchTerm = keyword != null ? keyword : title;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return ResponseEntity.ok(filmRepository.searchByKeyword(searchTerm));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/films/by-duration")
    public ResponseEntity<List<Film>> byRentalDuration(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        List<Film> results = filmRepository.findByRentalDurationBetween(min, max);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/films/not-in-inventory")
    public ResponseEntity<List<Film>> getNonInventory() {
        return ResponseEntity.ok(filmRepository.findNotExistedInInventory());
    }

    @GetMapping("/films/top-rented")
    public ResponseEntity<List<Film>> getTopFilms(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(filmRepository.findTopRented(limit));
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") int id) {
        return filmRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/films/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable int id) {
        try {
            if (filmRepository.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            filmRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Энэ кино өөр өгөгдөлтэй холбоотой тул устгах боломжгүй.");
        }
    }
}
