package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

/**
 * <b> Модель пользователя </b> <p>
 * Связана с {@link Avatar} через OneToOne
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private String phone;

    private Role role;
    @OneToOne
    private Avatar avatar;
}

