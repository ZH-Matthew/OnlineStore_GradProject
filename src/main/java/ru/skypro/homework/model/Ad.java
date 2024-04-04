package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
/**
 * <b> Модель объявления </b> <p>
 * Связана с {@link Image} через OneToOne, <p> и с {@link User} через ManyToOne
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    private int price;

    private String title;

    @OneToOne
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
}
