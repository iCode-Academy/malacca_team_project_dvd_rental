package mn.icode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mn.icode.model.Film;
import mn.icode.model.RentalCount;

@Repository
public class FilmRepository {

    private final JdbcTemplate jdbcTemplate;

    public FilmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> findAll(int limit, int offset) {
        String sql = """
            SELECT  film_id,title, rating, rental_rate
                from film  
                order by title 
                limit ? offset ?
                """;
        return jdbcTemplate.query(sql, filmRowMapper(), limit, offset);
    }

    public List<Film> search(String title) {
        String sql = """
                SELECT  film_id, title, rating, rental_rate
                from film 
                where title like CONCAT('%', ?, '%')
                """;
        return jdbcTemplate.query(sql, filmRowMapper(), title);
    }

    public List<RentalCount> findTopRented(int limit) {
        String sql = """
                    SELECT f.film_id, f.title, f.rating, f.rental_rate,
                           COUNT(r.rental_id) AS rental_count
                    FROM film f
                    JOIN inventory i ON f.film_id = i.film_id
                    JOIN rental r    ON i.inventory_id = r.inventory_id
                    GROUP BY f.film_id, f.title, f.rating, f.rental_rate
                    ORDER BY rental_count DESC
                    LIMIT ?
                 """;
        return jdbcTemplate.query(sql, rentalRowMapper(), limit);
    }
       public Optional<Film> findById(int filmId) {
        String sql = """
                        select film_id, title, rating, rental_rate
                        from film
                        where film_id = ?
                     """;
        try {
            Film film = jdbcTemplate.queryForObject(sql, filmRowMapper(), filmId);
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    private RowMapper<RentalCount> rentalRowMapper() {
        return (rs, rowNum) -> {
            RentalCount f = new RentalCount();
            f.setFilmId(rs.getInt("film_id"));
            f.setTitle(rs.getString("title"));
            f.setRating(rs.getString("rating"));
            f.setRentalRate(rs.getBigDecimal("rental_rate")); 
            try {
                f.setRentalCount(rs.getInt("rental_count"));
            } catch (Exception ignored) {
            }
            return f;
        };
    }  

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film f = new Film();
            f.setFilmId(rs.getInt("film_id"));
            f.setTitle(rs.getString("title"));
            f.setRating(rs.getString("rating"));
            f.setRentalRate(rs.getBigDecimal("rental_rate"));
            return f;
        };
    }

}
