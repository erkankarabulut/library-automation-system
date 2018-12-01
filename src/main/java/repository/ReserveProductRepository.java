package main.java.repository;

import java.sql.SQLException;

public class ReserveProductRepository extends BaseRepository {

    public Integer getStatus(String productID){
        Integer status = null;

        try{
            this.connect();
            this.createStatement();

            resultSet = statement.executeQuery("select durum as durum from Urunler where urunID = " +
                     productID);
            resultSet.next();
            status = resultSet.getInt("durum");

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return status;
    }

    public void reserve(String userIdorName, Boolean isID, String productID){

        try{
            this.connect();
            this.createStatement();

            String ID = null;
            if(isID){
                ID = userIdorName;
            }else{
                resultSet = statement.executeQuery("select kullaniciId as id from Kullanicilar " +
                        "where username = '" + userIdorName + "'");
                resultSet.next();
                ID = resultSet.getString("id");
            }

            statement.executeUpdate("insert into RezerveEdilenler (urunId, kullaniciId)" +
                    "values(" + productID + ", '" + ID + "')");
            statement.executeUpdate("update Urunler set durum = 2 where urunID = " +
                    productID);

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}

