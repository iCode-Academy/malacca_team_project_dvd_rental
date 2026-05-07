package mn.icode.model;

public class FilmCategory {
    private Integer filmId_of_FC;
    private Integer categoryId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getFilmId_of_FC() {
        return filmId_of_FC;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setFilmId_of_FC(Integer filmId_of_FC) {
        this.filmId_of_FC = filmId_of_FC;
    }
    
    
}
