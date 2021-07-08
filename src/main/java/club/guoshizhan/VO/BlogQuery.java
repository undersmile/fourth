package club.guoshizhan.VO;

public class BlogQuery {

    private String title;        // 文章标题
    private Long typeId;         // 分类 Id
    private boolean recommend;   // 是否推荐

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

}
