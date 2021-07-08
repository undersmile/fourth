package club.guoshizhan.mapper;

import club.guoshizhan.PO.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// JpaSpecificationExecutor 接口用于组合查询，使用处为博客的条件查询
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b order by b.views desc")
    List<Blog> findNewest(Pageable pageable);

    // ?1 表示查询的字符串和方法的中的第一个参数相对应，?2 表示与方法中的第二个参数相对应，以此类推
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1 or b.description like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.createTime,'%Y') as year from Blog b group by function('date_format',b.createTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.createTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

}
