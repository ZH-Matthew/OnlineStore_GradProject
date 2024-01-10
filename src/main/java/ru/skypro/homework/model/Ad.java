package ru.skypro.homework.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  //id объявления
    private Integer price; //цена объявления
    private String title; //заголовок объявления
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;

    @OneToOne
    private Image image; //ссылка на картинку объявления

    public Ad(Integer price, String title, String description, User user, Image image) {
        this.price = price;
        this.title = title;
        this.description = description;
        this.user = user;
        this.image = image;
    }
}