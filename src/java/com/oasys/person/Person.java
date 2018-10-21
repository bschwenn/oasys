package java.com.oasys.person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Table(name = "person")
public class Person {

    private @Id @GeneratedValue Long id;

    private String email;
    private String name;
    private int graduationYear;
    private String photoPath;
    private JSON links;

    public Person(String name, int age, int years) {
        this.name = name;
        this.age = age;
        this.years = years;
    }
}