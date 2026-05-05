package mn.icode.model;

public class Actor {
    private Integer actorId;
    private String firstName;
    private String lastName;
    private String fullName;

    public Integer getActorId() {
        return actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
