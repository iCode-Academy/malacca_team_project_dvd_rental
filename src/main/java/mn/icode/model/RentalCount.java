package mn.icode.model;

import java.math.BigDecimal;

public class RentalCount {

    private Integer filmId;
    private String title;
    private String rating;
    private BigDecimal rentalRate;
    private BigDecimal ratingRate;
    private int rentalCount;

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public BigDecimal getRatingRate() {
        return ratingRate;
    }

    public void setRatingRate(BigDecimal ratingRate) {
        this.ratingRate = ratingRate;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

}
