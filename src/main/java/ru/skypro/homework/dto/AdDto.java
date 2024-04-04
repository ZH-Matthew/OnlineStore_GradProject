package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class AdDto {
    private Long author; //id автора объявления

    private String image;//ссылка на картинку объявления !

    private Long pk; //id объявления

    private Integer price;//цена объявления !

    private String title;//заголовок объявления !
}
