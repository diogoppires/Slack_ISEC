/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Utils;

/**
 *
 * @author hugoferreira
 */
import java.io.Serializable;
import java.sql.Timestamp;

public class DataRequest implements Serializable {
    Timestamp time;
    String dbName;

    public DataRequest(Timestamp time, String dbName) {
        this.time = time;
        this.dbName = dbName;
    }

    public Timestamp getTime() {
        return time;
    }

    public String dbName() {
        return dbName;
    }
    
    
 
}
