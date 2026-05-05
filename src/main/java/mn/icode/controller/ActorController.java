package mn.icode.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mn.icode.model.Actor;
import mn.icode.repository.ActorRepository;

@Controller
@RequestMapping("/api")
public class ActorController {
    private final ActorRepository actorRepository;

    public ActorController(ActorRepository actorRepository){
        this.actorRepository = actorRepository;
    }
     @GetMapping("/actors")
    public ResponseEntity<List<Actor>> getActors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size

    ) {
        // int offset = (page - 1) * size;
        return ResponseEntity.ok(actorRepository.findAll(size));
    }
}
