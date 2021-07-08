package club.guoshizhan.service.impl;

import club.guoshizhan.PO.Comment;
import club.guoshizhan.mapper.CommentRepository;
import club.guoshizhan.service.ICommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository repository;

    // 存放迭代找出的所有子代的集合
    private List<Comment> tempReplies = new ArrayList<>();

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        // 获取到 parentCommentId 为 null 的评论数据
        List<Comment> comments = repository.findByBlogIdAndParentCommentNull(blogId, sort);
//        List<Comment> comments = repository.findByBlogId(blogId);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(repository.findById(parentCommentId).get());
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return repository.save(comment);
    }


    /**
     * 循环每个顶级的评论节点
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        // commentView 里面存放了父级评论，通过 combineChildren 方法来遍历出这些父级评论的子级评论
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * @param comments root 根节点，blog 不为空的对象集合
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replies = comment.getReplyComments();
            for (Comment reply : replies) {
                // 循环迭代，找出子代，存放在 tempReplies 中
                recursively(reply);
            }
            // 修改顶级节点的 reply 集合为迭代处理后的集合
            comment.setReplyComments(tempReplies);
            // 清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    /**
     * 递归迭代，剥洋葱
     */
    private void recursively(Comment comment) {
        tempReplies.add(comment);    // 顶节点添加到临时存放集合
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replies = comment.getReplyComments();
            for (Comment reply : replies) {
                tempReplies.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }

}
