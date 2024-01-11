package ru.skypro.homework.model;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "authorities")
@Data
public class Authorities { //насколько это здесь нужно?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User username;

    @Column
    private String authority;

}
