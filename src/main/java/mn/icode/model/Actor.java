package mn.icode.model;

import jakarta.persistence.*; // Энийг нэмэх хэрэгтэй

@Entity // Энэ класс өгөгдлийн сангийн хүснэгт гэдгийг заана
@Table(name = "actor") // Өгөгдлийн сан дээрх хүснэгтийн нэр
public class Actor {

    @Id // Primary Key гэдгийг заана
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматаар нэмэгдэх (Auto-increment)
    @Column(name = "actor_id") // Өгөгдлийн сангийн баганын нэр
    private Integer actorId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // Хоосон constructor заавал байх ёстой (Hibernate-д зориулж)
    public Actor() {}

    // --- Getters and Setters ---
    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}