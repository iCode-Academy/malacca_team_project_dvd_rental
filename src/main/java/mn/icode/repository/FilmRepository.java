package mn.icode.repository;

import java.sql.SQLException;
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
                SELECT film_id, title, rating, rental_rate
                FROM film
                ORDER BY title
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, filmRowMapper(), limit, offset);
    }

    public List<Film> search(String title) {
        String sql = """
                SELECT film_id, title, rating, rental_rate
                FROM film
                WHERE LOWER(title) LIKE CONCAT('%', LOWER(?), '%')
                """;
        return jdbcTemplate.query(sql, filmRowMapper(), title);
    }

    public List<Film> notExist(int limit, int offset) {
        String sql = """
                SELECT film_id, title, rating, rental_rate
                FROM film f
                WHERE film_id NOT IN (
                    SELECT DISTINCT film_id
                    FROM inventory
                    WHERE film_id IS NOT NULL
                )
                ORDER BY title
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, filmRowMapper(), limit, offset);
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

    // 1. ЗАСВАРЛАСАН: detail field-үүд нэмэгдсэн
    public Optional<Film> findById(int filmId) {
        String sql = """
                SELECT film_id, title, rating, rental_rate,
                       description, rental_duration,
                       replacement_cost, special_features
                FROM film
                WHERE film_id = ?
                """;
        try {
            Film film = jdbcTemplate.queryForObject(sql, filmDetailRowMapper(), filmId);
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    // 2. ЗАСВАРЛАСАН: = дутуу байсан
    public int delete(int id) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // 3. НЭМЭГДСЭН: detail-д зориулсан RowMapper
    private RowMapper<Film> filmDetailRowMapper() {
        return (rs, rowNum) -> {
            Film f = new Film();
            f.setFilmId(rs.getInt("film_id"));
            f.setTitle(rs.getString("title"));
            f.setRating(rs.getString("rating"));
            f.setRentalRate(rs.getBigDecimal("rental_rate"));
            f.setDescription(rs.getString("description"));
            f.setRentalDuration(rs.getInt("rental_duration"));
            f.setReplacementCost(rs.getBigDecimal("replacement_cost"));
            f.setSpecialFeatures(rs.getString("special_features"));
            return f;
        };
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
            } catch (SQLException ex) {
                System.out.println(ex.toString());
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