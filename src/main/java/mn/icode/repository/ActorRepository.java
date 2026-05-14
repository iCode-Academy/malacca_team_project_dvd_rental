package mn.icode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mn.icode.model.Actor;
import mn.icode.model.Film;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>{

    @Override
    List<Actor> findAll();

    List<Film> findByActor(int actorId);

    Optional<Actor> findById(int id);

    List<Actor> findByFirstNameContainingIgnoreCase(String firstName);
    List<Actor> findByLastNameContainingIgnoreCase(String firstName);


    int update(int id, Actor actor);

    boolean deleteById(int id);

    Actor create(Actor actor);

}