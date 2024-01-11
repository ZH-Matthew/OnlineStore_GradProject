package ru.skypro.homework.mapper;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;

import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.util.ArrayList;
import java.util.List;

public interface CommentMapper {

    default CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setAuthorImage(comment.getAuthor().getAvatar().getFilePath());
        commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    default Comment commentDtoToComment(CommentDto commentDto, User user) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(commentDto.getCreatedAt());
        comment.setAuthor(user);
        return comment;
    }

    default CommentsDto commentsToCommentsDto(List<Comment> commentList) { //без обратного метода
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentList.size());
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(commentToCommentDto(comment));
        }
        commentsDto.setResults(commentDtoList);
        return commentsDto;
    }

    default CreateOrUpdateComment commentToCreateOrUpdateComment(Comment comment){
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText(comment.getText());
        return  createOrUpdateComment;
    }

    default Comment CreateOrUpdateCommentToComment(CreateOrUpdateComment createOrUpdateComment){
        Comment comment = new Comment();
        comment.setText(createOrUpdateComment.getText());
        return comment;
    }

}
