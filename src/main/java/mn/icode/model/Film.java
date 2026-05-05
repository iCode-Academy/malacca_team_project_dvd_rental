package mn.icode.model;

import java.math.BigDecimal;

public class Film {
    private Integer filmId;
    private String title;
    private String rating;
    private BigDecimal rentalRate;

    public Integer getFilmId() {
        return filmId;
    }

    public String getRating() {
        return rating;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public String getTitle() {
        return title;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setRatingRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public void setTitle(String title) {
        this.title = title;
    }}