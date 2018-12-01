package main.java.repository;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;
import java.util.HashMap;

public class UserInfoRepository extends BaseRepository {

    String kartID;

    public boolean checkUser(String userNameOrID, String userPassword, Boolean idLogin)
            throws SQLException {
        Boolean result = false;
        String encryptedPsw = DigestUtils.shaHex(userPassword);

        this.connect();
        this.createStatement();

        if(idLogin){
            resultSet = this.statement.executeQuery("select * from Kullanicilar\n" +
                    "where kullaniciId = '" + userNameOrID + "'");
        }else{
            resultSet = this.statement.executeQuery("select * from Kullanicilar where " +
                    "Username = '" + userNameOrID + "' and UserPassword = '" + encryptedPsw + "'");
        }

        if(resultSet.next()){
            result = true;
        }

        return result;
    }


    public String registerUser(HashMap<String, String> values) throws SQLException {
        String result = "Ok";
        this.connect();
        this.createStatement();

        resultSet = statement.executeQuery("select * from Kullanicilar where kullaniciId = '" +
                values.get("id") +"'");
        if(resultSet.next()){
            result = "Warning: There is already a user with the entered Card ID ..!";
        }else{
            resultSet = statement.executeQuery("select * from Kullanicilar where " +
                    "Username = '" + values.get("username") + "'");
            if (resultSet.next()){
                result = "Warning: There is already a user with the entered Username ..!";
            }else{
                resultSet = statement.executeQuery("select * from KullaniciBilgileri " +
                        "where BilgiTipi = 'email' and BilgiDegeri = " +
                        "'" + values.get("email") + "'");
                if(resultSet.next()){
                    result = "Warning: There is already a user with entered email ..!";
                }else{
                    String encryptedPsw = DigestUtils.shaHex(values.get("password"));
                    String id = values.get("id");

                    resultSet = statement.executeQuery("select kullaniciTipi as type from " +
                            "KullaniciTipleri where kullaniciTipiAdi = '" +
                    values.get("usertype") + "'");
                    resultSet.next();
                    Integer usertype = resultSet.getInt("type");

                    statement.executeUpdate("insert into Kullanicilar (kullaniciId," +
                            "KullaniciTipi, Username, UserPassword) values('" +
                            id + "'," + usertype + ",'" +
                            values.get("username") + "','" + encryptedPsw + "')");

                    statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','email','"
                            + values.get("email") + "')");
                    if(values.get("tel").toString().length()!=0)
                        statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                                "BilgiTipi, BilgiDegeri) values('" + id + "','tel','"
                                + values.get("tel") + "')");
                    if(values.get("gender").toString().length()!=0)
                        statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','gender','"
                            + values.get("gender") + "')");
                    if(values.get("name").toString().length()!=0)
                        statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','name','"
                            + values.get("name") + "')");
                    if(values.get("surname").toString().length()!=0)
                        statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','surname','"
                            + values.get("surname") + "')");
                }
            }
        }

        this.closeConnection();
        return result;
    }

    public boolean checkUsernameId(String username, String ID){
        try{

            this.connect();
            this.createStatement();

            resultSet = statement.executeQuery("select * from Kullanicilar where " +
                    "Username = '" + username + "' and kullaniciId = '" + ID + "'");

            if(resultSet.next()){
                return true;
            }

            this.closeConnection();

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public void changePassword(String id, String psw){

        try {

            this.connect();
            this.createStatement();

            String newPsw = DigestUtils.shaHex(psw);
            statement.executeUpdate("update Kullanicilar set userPassword = '"
            + newPsw + "' where kullaniciId = '" + id + "'");

            this.closeConnection();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public boolean checkAuthorization(String id, boolean isID){
        boolean result = true;

        try {
            this.connect();
            this.createStatement();

            if(isID){
                resultSet = statement.executeQuery("select kullaniciTipi as Tip from " +
                        "Kullanicilar where kullaniciId = '" + id + "'");
                resultSet.next();
                if(resultSet.getInt("Tip") != 0){
                    result = false;
                }
            }else{
                String ID = null;
                resultSet = statement.executeQuery("select kullaniciId as id from Kullanicilar " +
                        "where username = '" + id + "'");
                resultSet.next();
                ID = resultSet.getString("id");

                resultSet = statement.executeQuery("select kullaniciTipi as Tip from " +
                        "Kullanicilar where kullaniciId = '" + ID + "'");
                resultSet.next();
                if(resultSet.getInt("Tip") != 0){
                    result = false;
                }
            }

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return  result;
    }

    public boolean checkIfUserAlreadyHave(String userID, Boolean isID, String productID){
        Boolean result = true;

        try {
            this.connect();
            this.createStatement();

            String ID = null;
            if(!isID){
                resultSet = statement.executeQuery("select kullaniciId as id from Kullanicilar where " +
                        "username = '" + userID + "'");
                resultSet.next();
                ID = resultSet.getString("id");
            }else{
                ID = userID;
            }

            resultSet = statement.executeQuery("select * from OduncAlinanlar where kullaniciId = " +
                    "'" + ID + "' and urunId = '" + productID + "'");
            if(resultSet.next()){
                result = false;
            }

            resultSet = statement.executeQuery("select * from RezerveEdilenler where kullaniciId = " +
                    "'" + ID + "' and urunId = '" + productID + "'");
            if(resultSet.next()){
                result = false;
            }

            this.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }
}
