package main.java.repository;

import java.sql.*;

public abstract class BaseRepository {

    protected Statement statement;
    protected Connection connection;
    protected ResultSet resultSet;

    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://89.252.184.201:3306/pubtekno_LibSys",
                    "pubtekno_SystemAdmin","Systemadmin1?");
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void createStatement(){
        try {
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public boolean isMaxReason(String userID, Boolean isID){
        Boolean result = false;

        try {
            this.connect();
            this.createStatement();

            String ID = null;
            if(isID){
                ID = userID;
            }else{
                resultSet = statement.executeQuery("select kullaniciId as id from Kullanicilar " +
                        "where username = '" + userID + "'");
                resultSet.next();
                ID = resultSet.getString("id");
            }

            resultSet = statement.executeQuery("select kullaniciTipi as tip from Kullanicilar where " +
                    "kullaniciId = '" + ID + "'");
            resultSet.next();
            Integer tip = resultSet.getInt("tip");

            resultSet = statement.executeQuery("select maxUrun as max from KullaniciTipleri where " +
                    "kullaniciTipi = " + tip);
            resultSet.next();
            Integer max = resultSet.getInt("max");

            resultSet = statement.executeQuery("select count(*) as count from " +
                    "OduncAlinanlar where kullaniciId = '" + ID + "'");
            resultSet.next();
            Integer count = resultSet.getInt("count");

            resultSet = statement.executeQuery("select count(*) as count from " +
                    "RezerveEdilenler where kullaniciId = '" + ID + "'");
            resultSet.next();
            count += resultSet.getInt("count");
            if(count >= max){
                result = true;
            }

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    public boolean isTextbookReason(String userID, Boolean isId, String kategori){
        boolean result = false;

        try{
            this.connect();
            this.createStatement();

            String ID = null;
            if(isId){
                ID = userID;
            }else{
                resultSet = statement.executeQuery("select kullaniciId as id from Kullanicilar " +
                        "where username = '" + userID + "'");
                resultSet.next();
                ID = resultSet.getString("id");
            }

            resultSet = statement.executeQuery("select kullaniciTipi as tip from Kullanicilar where " +
                    "kullaniciId = '" + ID + "'");
            resultSet.next();
            if(resultSet.getInt("tip") != 2 && kategori.equals("Textbook")){
                result = true;
            }

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    public boolean isUserSuitable(String userID, Boolean isID, String category, Boolean isBook){

        Boolean result = true;

        try {
            this.connect();
            this.createStatement();
            Integer maxUrun;
            String id;

            if(isID){
                resultSet = statement.executeQuery("select maxUrun from KullaniciTipleri " +
                        "where kullaniciTipi = (select kullaniciTipi from Kullanicilar where " +
                        "kullaniciId = '" + userID + "')");
                resultSet.next();
                maxUrun = Integer.parseInt(resultSet.getString("maxUrun"));

                resultSet = statement.executeQuery("select count(*) as count from " +
                        "OduncAlinanlar where kullaniciId = '" + userID + "'");
                resultSet.next();
                Integer count = resultSet.getInt("count");

                resultSet = statement.executeQuery("select count(*) as count from " +
                        "RezerveEdilenler where kullaniciId = '" + userID + "'");
                resultSet.next();
                count += resultSet.getInt("count");

                if(count >= maxUrun){
                    result = false;
                }else if (category.equals("Textbook") && isBook){
                    resultSet = statement.executeQuery("select kullaniciTipi as tip from " +
                            "Kullanicilar where kullaniciId = '" + userID +"'");
                    resultSet.next();
                    if(resultSet.getInt("tip") != 2){
                        result = false;
                    }
                }

            }else {
                resultSet = statement.executeQuery("select maxUrun from KullaniciTipleri " +
                        "where kullaniciTipi = (select kullaniciTipi from Kullanicilar where " +
                        "username = '" + userID + "')");
                resultSet.next();
                maxUrun = Integer.parseInt(resultSet.getString("maxUrun"));

                resultSet = statement.executeQuery("select kullaniciId as id from Kullanicilar " +
                        "where username = '" + userID + "'");
                resultSet.next();
                //id = Integer.parseInt(resultSet.getString("id"));
                id = resultSet.getString("id");
                resultSet = statement.executeQuery("select count(*) as count from " +
                        "OduncAlinanlar where kullaniciId = '" + id + "'");
                resultSet.next();

                if(Integer.parseInt(resultSet.getString("count")) >= maxUrun){
                    result = false;
                }else if (category.equals("Textbook") && isBook){
                    resultSet = statement.executeQuery("select kullaniciTipi as tip from " +
                            "Kullanicilar where username = '" + userID + "'");
                    resultSet.next();
                    if(resultSet.getInt("tip") != 2){
                        result = false;
                    }
                }
            }

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }
}