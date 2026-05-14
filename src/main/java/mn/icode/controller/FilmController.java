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

    @GetMapping("/films")
    public ResponseEntity<List<Film>> getFilms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("title"));
        List<Film> films = filmRepository.findAll(pageable).getContent();
        return ResponseEntity.ok(films);
    }

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
            System.out.println("Max rate joined");
            return ResponseEntity.ok(filmRepository.findByRentalRateLessThan(maxRate));
        }

        if (minLength != null && maxLength != null) {
            return ResponseEntity.ok(filmRepository.findByLengthBetween(minLength, maxLength));
        }

        return ResponseEntity.ok(filmRepository.findAll());
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
