package mn.icode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mn.icode.model.Category;
import mn.icode.model.FilmTitle;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> findAll(int limit , int offset){
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
    public int update(int id,Category category){
        String sql="""
                update category set name = ?
                where category_id = ?
                """;
        return jdbcTemplate.update(sql, category.getName(), id);
    }
    public List<Category> search(String name) {
       String sql = """
            SELECT  name
            FROM category
            WHERE LOWER(name) LIKE CONCAT('%', LOWER(?), '%')
            """;
        return jdbcTemplate.query(sql, categoryNameRowMapper(), name);
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

    private RowMapper<Category> categoryNameRowMapper(){
        return (rs, rowNumb) -> {
            Category c = new Category();
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

}
