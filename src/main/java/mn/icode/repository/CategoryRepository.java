package mn.icode.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mn.icode.model.Category;

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
    private RowMapper<Category> categoryRowMapper() {
        return (rs, rowNumb) -> {
            Category c = new Category();
            c.setCategoryId(rs.getInt("category_id"));
            c.setName(rs.getString("name"));

            return c;
        };
    }
}
