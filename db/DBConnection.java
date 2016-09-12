/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrator
 */
public class DBConnection {
    
    private String host = "localhost";
    private String sche = "ivysql";
    private String user = "root";
    private String pass = "optimizacion";

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            StringBuilder sb = new StringBuilder();
            sb.append("jdbc:mysql://");
            sb.append(host);
            sb.append("/");
            sb.append(sche);
            sb.append("?user=");
            sb.append(user);
            sb.append("&password=");
            sb.append(pass);
            return DriverManager.getConnection(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    static public void main(String[] args){
        try {
            ResultSet rs = (new DBConnection()).getConnection().createStatement().executeQuery("Select * from scop_class");
            while(rs.next()){
                System.out.println(rs.getString("sccs"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
