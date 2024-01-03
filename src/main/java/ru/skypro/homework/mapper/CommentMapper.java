package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;

import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(comment.getUser().getId());
        commentDto.setAuthorImage(comment.getUser().getAvatar().getFilePath());
        commentDto.setAuthorFirstName(comment.getUser().getFirstName());
        commentDto.setCreatedAt(comment.getTime());
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    default Comment commentDtoToComment(CommentDto commentDto, User user) {
        Comment comment = new Comment(user, commentDto.getCreatedAt(), commentDto.getText());
        return comment;
    }

    default CommentsDto commentsToCommentsDto(List<Comment> commentList) {
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentList.size());
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(commentToCommentDto(comment));
        }
        commentsDto.setResults(commentDtoList);
        return commentsDto;
    }

}
