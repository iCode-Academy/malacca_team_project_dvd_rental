package mn.icode.model;

public class CategoryStats {
    private String name;
    private Integer filmCount;
    private Double aveRentRate;
    private Integer totalRentals;

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
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
