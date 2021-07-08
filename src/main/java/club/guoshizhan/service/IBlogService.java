package club.guoshizhan.service;

import club.guoshizhan.PO.Blog;
import club.guoshizhan.VO.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IBlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    Page<Blog> listBlog(String query, Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    List<Blog> listNewestBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

}
