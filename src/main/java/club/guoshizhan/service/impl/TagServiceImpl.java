package club.guoshizhan.service.impl;

import club.guoshizhan.PO.Tag;
import club.guoshizhan.handler.NotFoundException;
import club.guoshizhan.mapper.TagRepository;
import club.guoshizhan.service.ITagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private TagRepository repository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return repository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Tag getTagByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return repository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return repository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag(String ids) { // 1,2,3
        return repository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idsArray = ids.split(",");
            for (int i = 0; i < idsArray.length; i++) {
                list.add(new Long(idsArray[i]));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = repository.findById(id).get();
        if (t == null) {
            throw new NotFoundException("不存在该标签！！！");
        }
        BeanUtils.copyProperties(tag, t);
        return repository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        repository.deleteById(id);
    }

}
