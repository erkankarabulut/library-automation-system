package main.java.repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProductInfoRepository extends main.java.repository.BaseRepository {
    public String addProduct(HashMap<String, String> values) throws SQLException {
        String result = "Ok";
        int urunId = -1;
        this.connect();
        this.createStatement();
        int typeValue;
        String type = values.get("type");
        if(type.equals("Book"))
            typeValue = 1;
        else if(type.equals("Journal"))
            typeValue = 2;
        else
            typeValue = -1;

        Set valueSet = values.entrySet();
        Iterator valueSetIterator = valueSet.iterator();

        statement.executeUpdate("INSERT INTO Urunler (yerBilgisi, urunTipi, durum) " +
                "VALUES('" + values.get("yerId") + "'," + typeValue + ", 0 )");
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
}