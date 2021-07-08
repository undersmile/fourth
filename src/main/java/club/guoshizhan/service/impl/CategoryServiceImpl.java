package club.guoshizhan.service.impl;

import club.guoshizhan.PO.Category;
import club.guoshizhan.handler.NotFoundException;
import club.guoshizhan.mapper.CategoryRepository;
import club.guoshizhan.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    @Transactional
    @Override
    public Category getCategory(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Category getCategoryByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Category> listCategory(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Category> listCategory() {
        return repository.findAll();
    }

    // 博客分类的首页数据展示
    @Override
    public List<Category> listCategoryTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return repository.findTop(pageable);
    }

    @Transactional
    @Override
    public Category updateCategory(Long id, Category category) {
        Category c = repository.findById(id).get();
        if (c == null) {
            throw new NotFoundException("不存在此分类！");
        }
        // 把 category 对象的值给 c 对象
        BeanUtils.copyProperties(category, c);
        return repository.save(c);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }

}
