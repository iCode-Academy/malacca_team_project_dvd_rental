package mn.icode.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "film")
public class Film {

    public Film() {
    }

    @Id
    @Column(name = "film_id")
    private Integer filmId;

    @Column(name = "title")
    private String title;

    @Column(name = "rating", columnDefinition = "mpaa_rating")
    private String rating;

    @Column(name = "rental_rate")
    private BigDecimal rentalRate;

    @Column(name = "description")
    private String description;

    @Column(name = "rental_duration")
    private Integer rentalDuration;

    @Column(name = "replacement_cost")
    private BigDecimal replacementCost;

    @Column(name = "special_features")
    private String specialFeatures;

    @Transient
    private int rentalCount;

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(Integer rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }
}
