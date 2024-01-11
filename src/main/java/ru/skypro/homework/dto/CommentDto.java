package ru.skypro.homework.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long author;//id автора комментария

    private String authorImage;//ссылка на аватар автора комментария

    private String authorFirstName;//имя создателя комментария

    private LocalDateTime createdAt; //дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970

    private Long pk;//id комментария

    private String text;//текст комментария
}
