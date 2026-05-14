package mn.icode.repository;

import mn.icode.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query("SELECT f FROM Film f WHERE "
            + "LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "ORDER BY f.title")
    List<Film> searchByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM Film f WHERE f.rating =  CAST(:rating AS mpaa_rating) ORDER BY f.title", nativeQuery = true)
    List<Film> findByRating(@Param("rating") String rating);

    @Query("SELECT f FROM Film f WHERE "
            + "f.rentalDuration BETWEEN :minDays AND :maxDays "
            + "ORDER BY f.rentalDuration")
    List<Film> findByRentalDurationBetween(@Param("minDays") Integer min,
            @Param("maxDays") Integer max);

    @Query("SELECT f FROM Film f WHERE f.filmId NOT IN "
            + "(SELECT DISTINCT i.filmId FROM Inventory i WHERE i.filmId IS NOT NULL) "
            + "ORDER BY f.title")
    List<Film> findNotExistedInInventory();

    @Query(value = "SELECT f.*, COUNT(r.rental_id) as rental_count "
            + "FROM film f "
            + "JOIN inventory i ON f.film_id = i.film_id "
            + "JOIN rental r ON i.inventory_id = r.inventory_id "
            + "GROUP BY f.film_id "
            + "ORDER BY rental_count DESC "
            + "LIMIT :limit", nativeQuery = true)
    List<Film> findTopRented(@Param("limit") int limit);
    List<Film> findByTitleContainingIgnoreCase(String keyword);
    List<Film> findByRentalRateLessThan(Double maxRate);
    List<Film> findByLengthBetween(Integer min, Integer max);
}
