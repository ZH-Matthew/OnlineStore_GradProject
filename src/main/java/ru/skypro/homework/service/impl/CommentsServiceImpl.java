package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Override
    public CommentsDto getComments(long id) {
        return null;
    }

    @Override
    public CommentDto addComment(long id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        return null;
    }

    @Override
    public void deleteComment(long adId, long commentId, Authentication authentication) {

    }

    @Override
    public CommentDto updateComment(long adId, long commentId, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        return null;
    }
}
