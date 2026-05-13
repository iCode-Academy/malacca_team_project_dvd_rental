package mn.icode.model;

public class FilmStats {
    private String category;
    private Integer filmCount;
    private Double aveRentRate;
    private Integer totalRentals;

    public String getCategory() {
        return category;
    }

    public Integer getFilmCount() {
        return filmCount;
    }

    public Double getAveRentRate() {
        return aveRentRate;
    }

    public Integer getTotalRentals() {
        return totalRentals;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFilmCount(Integer filmCount) {
        this.filmCount = filmCount;
    }

    public void setAveRentRate(Double aveRentRate) {
        this.aveRentRate = aveRentRate;
    }

    public void setTotalRentals(Integer totalRentals) {
        this.totalRentals = totalRentals;
    } 
    
}
