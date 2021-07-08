package club.guoshizhan.PO;

public class OneSentence {

    private String id;
    private String content;
    private String author;
    private String assign_date;
    private String[] origin_img_urls;
    private String TrackObject;
    private String joinNum;
    private String share_url;
    private String posterImgUrls;
    private String shareUrls;
    private String dailyAudioUrls;
    private String shareImgUrls;
    private String translation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(String assign_date) {
        this.assign_date = assign_date;
    }

    public String[] getOrigin_img_urls() {
        return origin_img_urls;
    }

    public void setOrigin_img_urls(String[] origin_img_urls) {
        this.origin_img_urls = origin_img_urls;
    }

    public String getTrackObject() {
        return TrackObject;
    }

    public void setTrackObject(String trackObject) {
        TrackObject = trackObject;
    }

    public String getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(String joinNum) {
        this.joinNum = joinNum;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getPosterImgUrls() {
        return posterImgUrls;
    }

    public void setPosterImgUrls(String posterImgUrls) {
        this.posterImgUrls = posterImgUrls;
    }

    public String getShareUrls() {
        return shareUrls;
    }

    public void setShareUrls(String shareUrls) {
        this.shareUrls = shareUrls;
    }

    public String getDailyAudioUrls() {
        return dailyAudioUrls;
    }

    public void setDailyAudioUrls(String dailyAudioUrls) {
        this.dailyAudioUrls = dailyAudioUrls;
    }

    public String getShareImgUrls() {
        return shareImgUrls;
    }

    public void setShareImgUrls(String shareImgUrls) {
        this.shareImgUrls = shareImgUrls;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "OneSentence{" +
                "id='" + id + '\'' +
                "assign_date='" + assign_date + '\'' +
                "origin_img_urls='" + origin_img_urls[0] + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}
