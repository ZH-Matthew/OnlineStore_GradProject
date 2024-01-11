package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Role role;

    //Далее блок колонок для jdbc
    private String username;//насколько здесь нужны эти поля?
    private String password;

    private Boolean enabled; //насколько здесь нужны эти поля?

    private Integer priority; //насколько здесь нужны эти поля?
    //-------------------------------
    @OneToOne
    private Avatar avatar;
}
