package mn.icode.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mn.icode.model.Film;

@Repository
public class FilmRepository {
    
    private final JdbcTemplate jdbcTemplate;

    public FilmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> findAll(int limit , int offset){
        String sql = """
            SELECT  film_id,title, rating, rental_rate
                from film  
                order by title 
                limit ? offset ?
                """;
        return jdbcTemplate.query(sql, filmRowMapper(), limit, offset);
    }



    public List<Film> search(String title){
        String sql = """
                SELECT  film_id, title, rating, rental_rate
                from film 
                where title like CONCAT('%', ?, '%')
                """;
        return jdbcTemplate.query(sql, filmRowMapper(),  title);
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNumb) -> {
            Film f = new Film();
            f.setFilmId(rs.getInt("film_id"));
            f.setTitle(rs.getString("title"));
            f.setRating(rs.getString("rating"));
            f.setRatingRate(rs.getBigDecimal("rental_rate"));
            return f;
        };
    }
    

}
