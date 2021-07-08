package club.guoshizhan.service;

import club.guoshizhan.PO.Comment;

import java.util.List;

public interface ICommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

}
