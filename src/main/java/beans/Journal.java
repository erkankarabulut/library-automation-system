package main.java.beans;

public class Journal extends Product {

    private String name;

    public Journal(String name, Integer pageCount, String status, String place, String kategoriIsim,
                   String publisher, String language, String publishDate, String ID) {
        this.name = name;
        this.pageCount = pageCount;
        this.status = status;
        this.place = place;
        this.kategoriIsim = kategoriIsim;
        this.publisher = publisher;
        this.language = language;
        this.publishDate = publishDate;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
