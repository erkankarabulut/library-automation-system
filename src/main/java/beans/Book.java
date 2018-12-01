package main.java.beans;

public class Book extends Product{

    private String author;
    private String title;

    public Book(String title, String author, Integer pageCount, String status, String place,
                String kategoriIsim, String publisher, String language, String publishDate,
                String ID){

        this.author = author;
        this.title = title;
        this.pageCount = pageCount;
        this.status = status;
        this.place = place;
        this.language = language;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.kategoriIsim = kategoriIsim;
        this.ID = ID;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pageCount=" + pageCount +
                ", status='" + status + '\'' +
                ", place='" + place + '\'' +
                ", kategoriIsim='" + kategoriIsim + '\'' +
                ", publisher='" + publisher + '\'' +
                ", language='" + language + '\'' +
                ", publishDate='" + publishDate + '\'' +
                '}';
    }
}
