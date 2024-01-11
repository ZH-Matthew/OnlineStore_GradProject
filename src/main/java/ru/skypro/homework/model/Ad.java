package ru.skypro.homework.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //id объявления
    private Integer price; //цена объявления
    private String title; //заголовок объявления
    private String description;

    @OneToOne
    private Image image; //ссылка на картинку объявления

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
}
