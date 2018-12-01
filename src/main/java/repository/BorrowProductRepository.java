package main.java.repository;

import com.sun.org.apache.xpath.internal.operations.Bool;
import main.java.beans.Book;

import java.sql.SQLException;

public class BorrowProductRepository extends BaseRepository {

    public boolean isProductSuitable(String id){

        Boolean result = true;
        this.connect();
        this.createStatement();

        try{

            resultSet = statement.executeQuery("select durum as status from Urunler where " +
                    "urunId = '" + id + "'");
            resultSet.next();

            if(Integer.parseInt(resultSet.getString("status")) != 0){
                result = false;
            }

            this.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }



    public void borrow(String productID, String userIdorName, Boolean isID){

        try {
            this.connect();
            this.createStatement();

            if(isID){
                statement.executeUpdate("insert into OduncAlinanlar (urunId, kullaniciId)" +
                        "values(" + productID + ", '" + userIdorName +"')");
            }else{
                resultSet = statement.executeQuery("select kullaniciId as id from Kullanicilar " +
                        "where username = '" + userIdorName + "'" );
                resultSet.next();
                String id = resultSet.getString("id");
                statement.executeUpdate("insert into OduncAlinanlar (urunId, kullaniciId)" +
                        "values(" + productID + ", '" + id +"')");
            }

            statement.executeUpdate("update Urunler set durum = 1 where urunId = " + productID);

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
