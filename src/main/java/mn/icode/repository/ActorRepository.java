package mn.icode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mn.icode.model.Actor;
import mn.icode.model.Film;

@Repository
public class ActorRepository {

    private final JdbcTemplate jdbcTemplate;

    public ActorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Actor> getActors() {

    String sql = """
        SELECT 
            actor_id,
            first_name || ' ' || last_name AS full_name
        FROM actor
        ORDER BY actor_id ASC
        """;

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
        Actor actor = new Actor();
        actor.setActorId(rs.getInt("actor_id"));
        actor.setFullName(rs.getString("full_name"));
        return actor;
    });
}
    public List<Film> findFilmsByActorId(int actorId) {

        String sql = """
            SELECT f.film_id, f.title, f.rating
            FROM film f
            INNER JOIN film_actor fa ON f.film_id = fa.film_id
            WHERE fa.actor_id = ?
            ORDER BY f.title ASC
        """;

        return jdbcTemplate.query(sql, filmRowMapper(), actorId);
    }
    
    public Optional <Actor>findById(int actorId){
        String sql =
        """
                select actor_id, first_name, last_name
                from actor 
                where actor_id=?
                """;
                try {
                    Actor actor =jdbcTemplate.queryForObject(sql, actorRowMapper(),actorId);
                    return Optional.ofNullable(actor);
                } catch (EmptyResultDataAccessException ex) {
                    return Optional.empty();
                }

    }

     private RowMapper<Actor> actorRowMapper() {
        return (rs, rowNum) -> {
            Actor a = new Actor();
            a.setActorId(rs.getInt("actor_id"));
            a.setFirstName(rs.getString("first_name"));
            a.setLastName(rs.getString("last_name"));
            return a;
        };
    }    

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film f = new Film();
            f.setFilmId(rs.getInt("film_id"));
            f.setTitle(rs.getString("title"));
            f.setRating(rs.getString("rating"));
            return f;
        };
    }
}