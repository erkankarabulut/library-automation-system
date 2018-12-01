package main.java.repository;

import main.java.beans.Book;
import main.java.beans.Journal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductInfoRepository extends main.java.repository.BaseRepository {

    public String addProduct(HashMap<String, String> values) throws SQLException {

        this.connect();
        this.createStatement();

        String result = "Ok";
        String type = values.get("type");
        int urunId = -1;
        int typeValue;

        if(type.equals("Book"))
            typeValue = 1;
        else if(type.equals("Journal"))
            typeValue = 2;
        else
            typeValue = -1;

        Set valueSet = values.entrySet();
        Iterator valueSetIterator = valueSet.iterator();

        resultSet = statement.executeQuery("select kategoriId as kategoriId from UrunKategorileri where "
                + "kategoriIsim = '" + values.get("categoryName") + "'");
        resultSet.next();
        Integer kategoriId = resultSet.getInt("kategoriId");

        statement.executeUpdate("INSERT INTO Urunler (yerBilgisi, urunTipi, durum, kategori) " +
                "VALUES('" + values.get("yerId") + "'," + typeValue + ", 0, " + kategoriId +" )");
        resultSet = statement.executeQuery("SELECT MAX(urunId) as max FROM Urunler");

        if(resultSet.next())
            urunId = Integer.parseInt(resultSet.getString("max"));

        valueSetIterator.next();
        valueSetIterator.next();
        while(valueSetIterator.hasNext()) {
            Map.Entry me = (Map.Entry)valueSetIterator.next();
            if(me.getValue().toString().length()!=0)
                statement.executeUpdate("INSERT INTO UrunBilgileri (urunId, bilgiTipi, bilgiDegeri) " +
                        "VALUES (" + urunId + ", '" + me.getKey() + "', '" + me.getValue() + "')");

        }

        return result;
    }

    public ArrayList<Journal> getAllJournals(int productType) throws SQLException {

        ArrayList<Journal> journals = new ArrayList<>();

        this.connect();
        this.createStatement();

        String sql = "select kategoriIsim, author, title, publisher, page, language, publishDate,\n" +
                "yerBilgisi, durum, tablo.urunTipi, tablo.urunId\n" +
                "from (select max(author) as author, max(title) as title, urunId as urunId, \n" +
                "max(publisher) as publisher, max(page) as page, kategori,\n" +
                "max(language) as language, max(publishDate) as publishDate, \n" +
                "yerBilgisi as yerBilgisi, durum as durum, urunAdi as urunTipi\n" +
                "from(select author, title, publisher, page, language, publishDate,\n" +
                "tablo2.urunId, kategori, yerBilgisi, durum, urunAdi from (\n" +
                "select case when bilgiTipi = 'author' then bilgiDegeri end as author, \n" +
                "case when bilgiTipi = 'title' then bilgidegeri end as title,\n" +
                "case when bilgiTipi = 'publisher' then bilgidegeri end as publisher,\n" +
                "case when bilgiTipi = 'numOfPages' then bilgidegeri end as page,\n" +
                "case when bilgiTipi = 'language' then bilgidegeri end as language,\n" +
                "case when bilgiTipi = 'publishDate' then bilgidegeri end as publishDate,\n" +
                "Urunler.urunId, kategori, yerBilgisi, durum, urunTipi from Urunler inner join UrunBilgileri on\n" +
                "Urunler.urunId = UrunBilgileri.urunId where Urunler.urunTipi = " + productType + ")tablo2 left join\n" +
                "UrunTipleri on UrunTipleri.urunTipi = tablo2.urunTipi) tablo \n" +
                "group by urunId\n" +
                ")tablo inner join UrunKategorileri on tablo.kategori = UrunKategorileri.kategoriId";

        resultSet = statement.executeQuery(sql);
        journals = getJournals();

        this.closeConnection();
        return journals;
    }

    public ArrayList<Book> getAllBooks(int productType) throws SQLException {

        ArrayList<Book> books = new ArrayList<>();

        this.connect();
        this.createStatement();

        String sql = "select kategoriIsim, author, title, publisher, page, language, publishDate,\n" +
                "yerBilgisi, durum, tablo.urunTipi, tablo.urunId\n" +
                "from (select max(author) as author, max(title) as title, urunId as urunId, \n" +
                "max(publisher) as publisher, max(page) as page, kategori,\n" +
                "max(language) as language, max(publishDate) as publishDate, \n" +
                "yerBilgisi as yerBilgisi, durum as durum, urunAdi as urunTipi\n" +
                "from(select author, title, publisher, page, language, publishDate,\n" +
                "tablo2.urunId, kategori, yerBilgisi, durum, urunAdi from (\n" +
                "select case when bilgiTipi = 'author' then bilgiDegeri end as author, \n" +
                "case when bilgiTipi = 'title' then bilgidegeri end as title,\n" +
                "case when bilgiTipi = 'publisher' then bilgidegeri end as publisher,\n" +
                "case when bilgiTipi = 'numOfPages' then bilgidegeri end as page,\n" +
                "case when bilgiTipi = 'language' then bilgidegeri end as language,\n" +
                "case when bilgiTipi = 'publishDate' then bilgidegeri end as publishDate,\n" +
                "Urunler.urunId, kategori, yerBilgisi, durum, urunTipi from Urunler inner join UrunBilgileri on\n" +
                "Urunler.urunId = UrunBilgileri.urunId where Urunler.urunTipi = " + productType + ")tablo2 left join\n" +
                "UrunTipleri on UrunTipleri.urunTipi = tablo2.urunTipi) tablo \n" +
                "group by urunId\n" +
                ")tablo inner join UrunKategorileri on tablo.kategori = UrunKategorileri.kategoriId";

        resultSet = statement.executeQuery(sql);
        books = getBooks();

        this.closeConnection();
        return books;
    }

    public ArrayList<Book> getBooks(){

        ArrayList<Book> books = new ArrayList<>();

        try{

            while (resultSet.next()){

                String status           = null;
                String title            = resultSet.getString("title");
                String author           = resultSet.getString("author");
                Integer statusValue     = resultSet.getInt("durum");
                Integer page            = resultSet.getInt("page");
                String publisher        = resultSet.getString("publisher");
                String publishDate      = resultSet.getString("publishDate");
                String language         = resultSet.getString("language");
                String kategoriIsim     = resultSet.getString("kategoriIsim");
                String place            = resultSet.getString("yerBilgisi");
                String ID               = resultSet.getString("urunId");

                if(statusValue == 0){
                    status = "On the Shelf";
                }else if (statusValue == 1){
                    status = "Taken";
                }else if (statusValue == 2){
                    status = "Reserved";
                }

                Book book = new Book(title, author, page, status, place, kategoriIsim, publisher,
                        language, publishDate, ID);

                books.add(book);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return books;

    }

    public ArrayList<Journal> getJournals(){

        ArrayList<Journal> journals = new ArrayList<>();

        try{

            while (resultSet.next()){

                String status           = null;
                String title            = resultSet.getString("title");
                Integer statusValue     = resultSet.getInt("durum");
                Integer page            = resultSet.getInt("page");
                String publisher        = resultSet.getString("publisher");
                String publishDate      = resultSet.getString("publishDate");
                String language         = resultSet.getString("language");
                String kategoriIsim     = resultSet.getString("kategoriIsim");
                String place            = resultSet.getString("yerBilgisi");
                String ID               = resultSet.getString("urunId");

                if(statusValue == 0){
                    status = "On the Shelf";
                }else if (statusValue == 1){
                    status = "Taken";
                }else if (statusValue == 2){
                    status = "Reserved";
                }

                Journal journal = new Journal(title, page, status, place, kategoriIsim, publisher,
                        language, publishDate, ID);

                journals.add(journal);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return journals;

    }

    public ArrayList<Book> getSearchedBooks(String value, Integer productType){
        ArrayList<Book> books = new ArrayList<>();

        try{

            this.connect();
            this.createStatement();

            String sql = "select kategoriIsim, author, title, publisher, page, language, publishDate,\n" +
                    "yerBilgisi, durum, tablo.urunTipi, tablo.urunId\n" +
                    "from (select max(author) as author, max(title) as title, urunId as urunId, \n" +
                    "max(publisher) as publisher, max(page) as page, kategori,\n" +
                    "max(language) as language, max(publishDate) as publishDate, \n" +
                    "yerBilgisi as yerBilgisi, durum as durum, urunAdi as urunTipi\n" +
                    "from(select author, title, publisher, page, language, publishDate,\n" +
                    "tablo2.urunId, kategori, yerBilgisi, durum, urunAdi from (\n" +
                    "select case when bilgiTipi = 'author' then bilgiDegeri end as author, \n" +
                    "case when bilgiTipi = 'title' then bilgidegeri end as title,\n" +
                    "case when bilgiTipi = 'publisher' then bilgidegeri end as publisher,\n" +
                    "case when bilgiTipi = 'numOfPages' then bilgidegeri end as page,\n" +
                    "case when bilgiTipi = 'language' then bilgidegeri end as language,\n" +
                    "case when bilgiTipi = 'publishDate' then bilgidegeri end as publishDate,\n" +
                    "Urunler.urunId, kategori, yerBilgisi, durum, urunTipi from Urunler inner join UrunBilgileri on\n" +
                    "Urunler.urunId = UrunBilgileri.urunId where Urunler.urunTipi = 1)tablo2 left join\n" +
                    "UrunTipleri on UrunTipleri.urunTipi = tablo2.urunTipi) tablo \n" +
                    "group by urunId\n" +
                    ")tablo inner join UrunKategorileri on tablo.kategori = UrunKategorileri.kategoriId having title like '%"+ value + "%' " +
                    "or author like '%" + value + "%' ";

            resultSet = statement.executeQuery(sql);

            books = getBooks();

            this.closeConnection();

        }catch (SQLException e){
            e.printStackTrace();
        }

        return books;
    }

    public ArrayList<Journal> getSearchedJournals(String value, Integer productType){
        ArrayList<Journal> journals = new ArrayList<>();

        try{

            this.connect();
            this.createStatement();

            String sql = "select kategoriIsim, author, title, publisher, page, language, publishDate,\n" +
                    "yerBilgisi, durum, tablo.urunTipi, tablo.urunId\n" +
                    "from (select max(author) as author, max(title) as title, urunId as urunId, \n" +
                    "max(publisher) as publisher, max(page) as page, kategori,\n" +
                    "max(language) as language, max(publishDate) as publishDate, \n" +
                    "yerBilgisi as yerBilgisi, durum as durum, urunAdi as urunTipi\n" +
                    "from(select author, title, publisher, page, language, publishDate,\n" +
                    "tablo2.urunId, kategori, yerBilgisi, durum, urunAdi from (\n" +
                    "select case when bilgiTipi = 'author' then bilgiDegeri end as author, \n" +
                    "case when bilgiTipi = 'title' then bilgidegeri end as title,\n" +
                    "case when bilgiTipi = 'publisher' then bilgidegeri end as publisher,\n" +
                    "case when bilgiTipi = 'numOfPages' then bilgidegeri end as page,\n" +
                    "case when bilgiTipi = 'language' then bilgidegeri end as language,\n" +
                    "case when bilgiTipi = 'publishDate' then bilgidegeri end as publishDate,\n" +
                    "Urunler.urunId, kategori, yerBilgisi, durum, urunTipi from Urunler inner join UrunBilgileri on\n" +
                    "Urunler.urunId = UrunBilgileri.urunId where Urunler.urunTipi = 1)tablo2 left join\n" +
                    "UrunTipleri on UrunTipleri.urunTipi = tablo2.urunTipi) tablo \n" +
                    "group by urunId\n" +
                    ")tablo inner join UrunKategorileri on tablo.kategori = UrunKategorileri.kategoriId having title like '%"+ value + "%' " +
                    "or author like '%" + value + "%' ";

            resultSet = statement.executeQuery(sql);

            journals = getJournals();

            this.closeConnection();

        }catch (SQLException e){
            e.printStackTrace();
        }

        return journals;
    }

    public ArrayList<String> getCategories(int productType){
        ArrayList<String> categoryList = new ArrayList<>();

        try{
            this.connect();
            this.createStatement();

            resultSet = statement.executeQuery("select kategoriIsim as kategori from " +
                    "UrunKategorileri where urunTipi = " + productType);
            while (resultSet.next()){
                categoryList.add(resultSet.getString("kategori"));
            }

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return categoryList;
    }

}