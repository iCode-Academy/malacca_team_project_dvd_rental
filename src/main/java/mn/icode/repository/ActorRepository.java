package mn.icode.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mn.icode.model.Actor;

@Repository
public class ActorRepository {

    private final JdbcTemplate jdbcTemplate;

    public ActorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Actor> findAll(int limit) {
        String sql = """
            SELECT  title, rating
            FROM film f
            INNER JOIN film_actor fc ON fc.film_id = f.film_id
            INNER JOIN actor a ON a.actor_id = fc.actor_id
            where fc.actor_id=?
            ORDER BY f.title ASC;
                """;
        return jdbcTemplate.query(sql, actorRowMapper());
    }

    private RowMapper<Actor> actorRowMapper() {
        return (rs, rowNum) -> {
            Actor a = new Actor();
            f.setTitle(rs.getTitle("title"));
            f.setRating(rs.getRating("rating"));
            return a;
        };
    }
}
