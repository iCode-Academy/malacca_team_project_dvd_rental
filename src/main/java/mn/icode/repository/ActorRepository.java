package mn.icode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mn.icode.model.Actor;
import mn.icode.model.Film;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>{

    @Query("SELECT actor_id, first_name, last_name FROM actor ORDER BY actor_id ASC")
    @Override
    List<Actor> findAll();
    
    @Query("SELECT actor_id, first_name, last_name from actor where actor_id")
    Optional<Actor> findById(int id);
    
    // @Query("SELECT f.film_id, f.title, f.rating, f.rental_rate FROM Film f inner join f.actors a WHERE a.actorId = :actorId")
    // List<Film> findByActor(@Param("actorId") int actorId);


    List<Actor> findByFirstNameContainingIgnoreCase(String firstName);
    List<Actor> findByLastNameContainingIgnoreCase(String firstName);


    int update(int id, Actor actor);

    boolean deleteById(int id);

    Actor create(Actor actor);

}