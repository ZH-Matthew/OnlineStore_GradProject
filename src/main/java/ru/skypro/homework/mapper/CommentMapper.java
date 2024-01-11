package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentMapper {
    public CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setAuthorImage(comment.getAuthor().getAvatar().getFilePath());
        commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    public Comment commentDtoToComment(CommentDto commentDto, User user) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(commentDto.getCreatedAt());
        comment.setAuthor(user);
        return comment;
    }

    public CommentsDto commentsToCommentsDto(List<Comment> commentList) { //без обратного метода
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentList.size());
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(commentToCommentDto(comment));
        }
        commentsDto.setResults(commentDtoList);
        return commentsDto;
    }

    public CreateOrUpdateComment commentToCreateOrUpdateComment(Comment comment){
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText(comment.getText());
        return  createOrUpdateComment;
    }

    public Comment CreateOrUpdateCommentToComment(CreateOrUpdateComment createOrUpdateComment){
        Comment comment = new Comment();
        comment.setText(createOrUpdateComment.getText());
        return comment;
    }
}
