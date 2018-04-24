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

                    System.out.println("Type: " + usertype);

                    statement.executeUpdate("insert into Kullanicilar (kullaniciId," +
                            "KullaniciTipi, Username, UserPassword) values('" +
                            id + "'," + usertype + ",'" +
                            values.get("username") + "','" + encryptedPsw + "')");

                    String sql = "insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','email','"
                            + values.get("email") + "')";
                    System.out.println("geldi" + sql);
                    statement.executeUpdate(sql);
                    statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','tel','"
                            + values.get("tel") + "')");
                    statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','gender','"
                            + values.get("gender") + "')");
                    statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','name','"
                            + values.get("name") + "')");
                    statement.executeUpdate("insert into KullaniciBilgileri (kullaniciId," +
                            "BilgiTipi, BilgiDegeri) values('" + id + "','surname','"
                            + values.get("surname") + "')");
                }
            }
        }

        this.closeConnection();
        return result;
    }

}
