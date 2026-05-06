package mn.icode.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mn.icode.model.Actor;
import mn.icode.model.Film;
import mn.icode.repository.ActorRepository;

@RestController
@RequestMapping("/api")
public class ActorController {

    private final ActorRepository actorRepository;

    public ActorController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @GetMapping("/actors")
    public ResponseEntity<List<Actor>> getActorsName() {
        return ResponseEntity.ok(actorRepository.getActors());

    }

    @GetMapping("/actors/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable("id") int id) {
       return actorRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/actors/{id}/films")
    public ResponseEntity<List<Film>> getFilmsByActor(@PathVariable int id) {
        return ResponseEntity.ok(actorRepository.findFilmsByActorId(id));
    }

}
