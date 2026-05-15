package mn.icode.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import mn.icode.model.Film;
import mn.icode.repository.FilmRepository;

@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RestController
public class FilmController {

    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    // ── GET /api/films — жагсаалт (хуудаслалттай) ─────────────────────────
    @GetMapping("/films")
    public ResponseEntity<List<Film>> getFilms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("title"));
        List<Film> films = filmRepository.findAll(pageable).getContent();
        return ResponseEntity.ok(films);
    }

    // ── GET /api/films/search — хайлт ─────────────────────────────────────
    @GetMapping("/films/search")
    public ResponseEntity<List<Film>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double maxRate,
            @RequestParam(required = false) Integer minLength,
            @RequestParam(required = false) Integer maxLength) {
        if (rating != null && !rating.isEmpty()) {
            return ResponseEntity.ok(filmRepository.findByRating(rating));
        }

        String searchTerm = (keyword != null) ? keyword : title;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return ResponseEntity.ok(filmRepository.searchByKeyword(searchTerm));
        }

        if (maxRate != null) {
            return ResponseEntity.ok(filmRepository.findByRentalRateLessThan(maxRate));
        }

        if (minLength != null && maxLength != null) {
            return ResponseEntity.ok(filmRepository.findByLengthBetween(minLength, maxLength));
        }

        return ResponseEntity.ok(filmRepository.findAll());
    }

    // ── GET /api/films/by-duration ─────────────────────────────────────────
    @GetMapping("/films/by-duration")
    public ResponseEntity<List<Film>> byRentalDuration(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        return ResponseEntity.ok(filmRepository.findByRentalDurationBetween(min, max));
    }

    // ── GET /api/films/not-in-inventory ───────────────────────────────────
    @GetMapping("/films/not-in-inventory")
    public ResponseEntity<List<Film>> getNonInventory() {
        return ResponseEntity.ok(filmRepository.findNotExistedInInventory());
    }

    // ── GET /api/films/top-rented ─────────────────────────────────────────
    @GetMapping("/films/top-rented")
    public ResponseEntity<List<Film>> getTopFilms(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(filmRepository.findTopRented(limit));
    }

    // ── GET /api/films/{id} — нэг film унших ──────────────────────────────
    @GetMapping("/films/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        return filmRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/films — шинэ film үүсгэх ───────────────────────────────
    @PostMapping("/films")
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        Film saved = filmRepository.save(film);
        return ResponseEntity.ok(saved);
    }

    // ── PUT /api/films/{id} — film засах ──────────────────────────────────
    @PutMapping("/films/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id,
                                           @RequestBody Film updated) {
        return filmRepository.findById(id).map(f -> {
            f.setTitle(updated.getTitle());
            f.setRating(updated.getRating());
            f.setRentalRate(updated.getRentalRate());
            f.setRentalDuration(updated.getRentalDuration());
            f.setReplacementCost(updated.getReplacementCost());
            f.setSpecialFeatures(updated.getSpecialFeatures());
            return ResponseEntity.ok(filmRepository.save(f));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/films/{id} — film устгах ──────────────────────────────
    @DeleteMapping("/films/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable("id") int id) {
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