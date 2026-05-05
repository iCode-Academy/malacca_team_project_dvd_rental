package mn.icode.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mn.icode.model.Film;
import mn.icode.repository.FilmRepository;

@Controller
@RequestMapping("/api")
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
}
