package mn.icode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mn.icode.model.Category;
import mn.icode.model.FilmStats;
import mn.icode.model.FilmTitle;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> findAll(int limit, int offset) {
        String sql = """
            SELECT  category_id, name
                from category  
                order by name 
                limit ? offset ?
                """;
        return jdbcTemplate.query(sql, categoryRowMapper(), limit, offset);
    }

    // find category id
    public Optional<Category> findCategoryById(int filmId) {
        String sql = """
            select category_id, name from category
            where category_id = ?
            """;
        try {
            Category x = jdbcTemplate.queryForObject(sql, categoryRowMapper(), filmId);
            return Optional.ofNullable(x);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    // get films by category id
    public List<FilmTitle> filmsByCategory(int categoryId) {
        String sql = """
                select f.title, f.release_year from film f
                inner join film_category fc on fc.film_id = f.film_id
                inner join category c on c.category_id = fc.category_id
                where c.category_id = ? 
                """;
        return jdbcTemplate.query(sql, filmsByCategoryRowMapper(), categoryId);
    }

    public List<FilmStats> categoryStats() {
        String sql = """
                select c."name" as category, COUNT(f.film_id) as film_count, round(AVG(f.rental_rate), 2) as average_rental_rate, COUNT(r.rental_id) AS total_rentals 
                from category c
                inner join film_category fc on c.category_id = fc.category_id
                inner join film f on f.film_id = fc.film_id
                left join inventory i on f.film_id = i.film_id
                left join rental r on i.inventory_id = r.inventory_id
                group by c."name"
                order by total_rentals desc
                """;
        return jdbcTemplate.query(sql, categoryStatsRowMapper());
    }

    public Category create(Category category) {
        String sql = """
                insert into category (category_id, name)
                values (?, ?)
                """;

        jdbcTemplate.update(sql, category.getCategoryId(), category.getName());
        return category;
    }

    public int delete(int id) {
        String sql = """
                    delete from category 
                    where category_id = ?
            """;
        return jdbcTemplate.update(sql, id);
    }

    public int update(int id, Category category) {
        String sql = """
                update category set name = ?
                where category_id = ?
                """;
        return jdbcTemplate.update(sql, category.getName(), id);
    }

    // rowmappers   
    private RowMapper<Category> categoryRowMapper() {
        return (rs, rowNumb) -> {
            Category c = new Category();
            c.setCategoryId(rs.getInt("category_id"));
            c.setName(rs.getString("name"));

            return c;
        };
    }

    private RowMapper<FilmTitle> filmsByCategoryRowMapper() {
        return (rs, rowNumb) -> {
            FilmTitle c = new FilmTitle();
            c.setTitle(rs.getString("title"));
            c.setReleaseYear(rs.getInt("release_year"));
            return c;
        };
    }

    private RowMapper<FilmStats> categoryStatsRowMapper() {
        return (rs, rowNumb) -> {
            FilmStats fs = new FilmStats();
            fs.setCategory(rs.getString("category"));
            fs.setFilmCount(rs.getInt("film_count"));
            fs.setAveRentRate(rs.getDouble("average_rental_rate"));
            fs.setTotalRentals(rs.getInt("total_rentals"));
            return fs;
        };
    }

}
