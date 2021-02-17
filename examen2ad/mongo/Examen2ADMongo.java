/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen2ad.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Scanner;



/**
 *
 * @author oracle
 */
public class Examen2ADMongo {

    public static MongoClient clientMongo;
    public static MongoDatabase mongoDB;
    public static MongoCollection<Document> collection;
    public static Connection orclDB = null;
    /**
     * @param args the command line arguments
     */
    
    public static void getSQLConnection() throws SQLException{
        String usuario = "hr";
        String password = "hr";
        String host = "localhost"; 
        String puerto = "1521";
        String sid = "orcl";
        String url = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;
        
           
            orclDB = DriverManager.getConnection(url);
    }
    
    public static void closeSQLConection() throws SQLException {
      orclDB.close();
    }
    
    public static void connectMongoClient(){
        clientMongo = new MongoClient("localhost");
    }
    
    public static void connectMongoDB(String name){
        mongoDB = clientMongo.getDatabase(name);
    }
    
    public static void getCollection(String name){
        collection = mongoDB.getCollection(name);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        String nombre = sc.nextLine();
        String colec = sc.nextLine();
        connectMongoClient();
        connectMongoDB(nombre);
        getCollection(colec);
        clientMongo.close();
    }
    
}
