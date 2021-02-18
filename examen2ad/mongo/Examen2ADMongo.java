/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen2ad.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;


import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    
    public static MongoCollection<Document> getCollection(String name){
        return mongoDB.getCollection(name);
    }
    
    public static ArrayList<Reserva> lerReservas() throws SQLException{
        
        Statement st = orclDB.createStatement();
        ResultSet rs1 = st.executeQuery("select * from reservas");
        ArrayList<Reserva> rs = new ArrayList();
        Reserva r;
        while(rs1.next()){
            r=new Reserva(rs1.getInt("codr"),rs1.getString("dni"),rs1.getInt("idvooida"),rs1.getInt("idvoovolta"));
            rs.add(r);
            /*System.out.println("Codr:" + rs1.getInt("codr"));
            System.out.println("DNI:" + rs1.getString("dni"));
            System.out.println("vooIda:" + rs1.getInt("idvooida"));
            System.out.println("vooVolta:" + rs1.getInt("idvoovolta"));*/
        }
        System.out.println(rs);
        return rs;
    }
    
    public static void lerReservasMongo(ArrayList<Reserva> rs){
        
        MongoCollection<Document> col = getCollection("pasaxeiros");
        for(Reserva r : rs){
            col.updateOne(new BasicDBObject("_id", r.getDNI()),new BasicDBObject("$inc", new BasicDBObject("nreservas",1)));
        }
        
        /*BasicDBObject query = new BasicDBObject("_id", "361a");
        cur = col.find(query).iterator();
        while(cur.hasNext()){
            doc=cur.next();
            System.out.println(doc.getString("_id"));
        }*/
        
    }
    
    public static void confirmarVuelo(ArrayList<Reserva> rs) throws SQLException{
        MongoCollection<Document> col = getCollection("voos");
        PreparedStatement pst = orclDB.prepareStatement("insert into confirmadas values(?,?,?,?,?)");
        MongoCursor<Document> cur;
        MongoCursor<Document> cur2;
        Document doc;
        Document doc2;
        double sum=0;
        for(Reserva r : rs){
            doc = col.find(Filters.eq("_id", r.getIdvueloIda())).first();
            doc2 = col.find(Filters.eq("_id", r.getIdvueloVuelta())).first();
            pst.setInt(1, r.getCodigo());
            pst.setString(2, r.getDNI());
            System.out.println("Documento Origen:" + doc.getString("orixe"));
            System.out.println("Documento Destino:" + doc.getString("destino"));
            pst.setString(3, doc.getString("orixe"));
            pst.setString(4, doc.getString("destino"));
            sum=sum+doc.getDouble("prezo") + doc2.getDouble("prezo");
            pst.setDouble(5, sum);
            if(pst.executeUpdate() > 0){
                System.out.println("Ok");
            }
            sum=0;
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        /*Scanner sc = new Scanner(System.in);
        String nombre = sc.nextLine();
        String colec = sc.nextLine();*/
        connectMongoClient();
        connectMongoDB("internacional");
        //clientMongo.close();
        ArrayList<Reserva> list = new ArrayList();
        try{
            getSQLConnection();
            list=lerReservas();
        }catch(SQLException e){
            e.printStackTrace();
        }
        lerReservasMongo(list);
        try{
            confirmarVuelo(list);
            orclDB.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    
        clientMongo.close();
        
    }
    
}
