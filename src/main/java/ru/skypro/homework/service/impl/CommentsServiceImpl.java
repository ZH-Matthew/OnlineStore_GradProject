package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentsService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
/**
 * <b> Сервис для работы с комментариями объявлений </b> <p>
 * Содержит CRUD методы + внутренний метод проверки доступа к редактированию {@link #checkPermit(Comment, Authentication)}
 */
@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    /**
     * <b>Метод получения всех комментариев </b> <p>
     * Принцип работы:<p>
     * По ID объявления найти все комментарии к нему, на основе полученного листа собрать и вернуть DTO {@link CommentsDto}
     * @param id (long) ID объявления
     * @return {@link CommentsDto})
     */
    @Override
    public CommentsDto getComments(long id) {
        List<Comment> commentList = commentRepository.findCommentsByAdId(id);
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentList.size());
        commentsDto.setResults(commentList.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList()));
        return commentsDto;
    }

    /**
     * <b>Метод добавления комментария </b> <p>
     * Принцип работы:<p>
     * Найти объявление по ID, создать новый {@link Comment}, добавить в него: {@link Ad}, текст из {@link CreateOrUpdateComment},
     * дату и время написания коммента, пользователя {@link User} (запрошенного из аутентификации)
     * @param id ID объявления
     * @param createOrUpdateComment  DTO комментария содержащее только текст
     * @param authentication объект аутентификации с данными текущего пользователя
     * @return {@link CommentDto}
     */
    @Override
    public CommentDto addComment(long id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        Ad ad = adRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID " + id + " не найдено"));
        Comment comment = new Comment();
        comment.setText(createOrUpdateComment.getText());
        comment.setAd(ad);
        comment.setCreatedAt(LocalDateTime.now());
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        comment.setAuthor(user);
        commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    /**
     * <b>Метод удаления комментария </b> <p>
     * Принцип работы:<p>
     * Находим коммент по ID коммента, проверяем через {@link #checkPermit} доступ к редактированию, если все ок, удаляем коммент.
     * @param adId ID объявления
     * @param commentId - ID коммента
     * @param authentication объект аутентификации с данными текущего пользователя
     */
    //Метод использует аннотацию @Transactional, которая дает понять Spring что данный метод - это не поочередные
    //самостоятельные действия внутри, а единая транзакция с возможностью отката. Spring сам реализует запросы к БД
    //и "упаковку" в транзакции. Тем самым мы обезопасим наше удаление от непредвиденных сбоев и неточностей.
    @Override
    @Transactional
    public void deleteComment(long adId, long commentId, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с ID" + commentId + "не найден"));
        checkPermit(comment, authentication);
        commentRepository.delete(comment);
    }

    /**
     * <b>Метод изменения комментария </b> <p>
     * Принцип работы:<p>
     * Находим коммент по ID коммента, проверяем через {@link #checkPermit} доступ к редактированию, если все ок,
     * меняем текст коммента
     * @param adId ID объявления
     * @param commentId ID коммента
     * @param createOrUpdateComment DTO комментария содержащее только текст
     * @param authentication объект аутентификации с данными текущего пользователя
     * @return {@link CommentDto}
     */
    //Метод использует аннотацию @Transactional, которая дает понять Spring что данный метод - это не поочередные
    //самостоятельные действия внутри, а единая транзакция с возможностью отката. Spring сам реализует запросы к БД
    //и "упаковку" в транзакции. Тем самым мы обезопасим наше действие от непредвиденных сбоев и неточностей.
    @Override
    @Transactional
    public CommentDto updateComment(long adId, long commentId, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с ID" + commentId + "не найден"));
        checkPermit(comment, authentication);
        comment.setText(createOrUpdateComment.getText());
        return commentMapper.commentToCommentDto(commentRepository.save(comment));
    }
    /**
     * <b> Метод проверки доступа к редактированию объявления </b> <p>
     * Служебный внутренний метод принимающий на вход: <p> {@link Comment} и {@link Authentication} <p>
     * далее сравнивает автора коммента и текущего пользователя, а также проверяет, является ли пользователь Админом.
     * Если текущий пользователь не автор коммента и не админ, то будет выброшено  {@link AccessDeniedException}
     *
     * @param comment коммент
     * @param authentication объект аутентификации с данными текущего пользователя
     */
    public void checkPermit(Comment comment, Authentication authentication){
        if (!comment.getAuthor().getEmail().equals(authentication.getName()) && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AccessDeniedException("Вы не можете редактировать или удалять чужое объявление");
        }
    }
}
