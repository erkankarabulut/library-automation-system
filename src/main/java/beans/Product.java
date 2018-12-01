package main.java.beans;

public abstract class Product {

    protected Integer pageCount;
    protected String status;
    protected String place;
    protected String kategoriIsim;
    protected String publisher;
    protected String language;
    protected String publishDate;
    protected String ID;

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getKategoriIsim() {
        return kategoriIsim;
    }

    public void setKategoriIsim(String kategoriIsim) {
        this.kategoriIsim = kategoriIsim;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
