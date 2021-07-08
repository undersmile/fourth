package club.guoshizhan.service.impl;

import club.guoshizhan.PO.Blog;
import club.guoshizhan.VO.BlogQuery;
import club.guoshizhan.handler.NotFoundException;
import club.guoshizhan.mapper.BlogRepository;
import club.guoshizhan.service.IBlogService;
import club.guoshizhan.utils.MarkdownUtils;
import club.guoshizhan.utils.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements IBlogService {

    @Autowired
    private BlogRepository repository;

    @Override
    public Blog getBlog(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = repository.findById(id).get();
        if (blog == null) {
            throw new NotFoundException("该博客找不到了~~~");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        repository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return repository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 查询条件：博客的标题
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.get("title"), "%" + blog.getTitle() + "%"));
                }
                // 查询条件：博客的分类
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.get("category").get("id"), blog.getTypeId()));
                }
                // 查询条件：是否为推荐文章
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return repository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                // 关联 Blog 对象里面的 tags 属性
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return repository.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return repository.findTop(pageable);
    }

    @Override
    public List<Blog> listNewestBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        Pageable pageable = PageRequest.of(0, size, sort);
        return repository.findNewest(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        // 通过年份分组获得每一年的博客
        List<String> years = repository.findGroupYear();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            System.out.println(year);
            map.put(year, repository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return repository.count();
    }

    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return repository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = repository.findById(id).get();
        if (b == null) {
            throw new NotFoundException("该博客不存在！");
        }
        // 把 blog 对象里面的赋值给 b 对象
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return repository.save(b);
    }

    @Override
    public void deleteBlog(Long id) {
        repository.deleteById(id);
    }

}
