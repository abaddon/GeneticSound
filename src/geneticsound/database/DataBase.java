/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticsound.database;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author   abaddon
 */
public class DataBase {
    
    private final String CURRENT_DIR="/Users/abaddon/Documents/progetti/java/GeneticSound/database/";
    //private PreparedStatement insertMelodia =null;
    private Statement stmt;
    private Connection connection=null;
    
    private static DataBase istanza=null;
    
    private DataBase(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            //connection=DriverManager.getConnection("jdbc:derby:/Users/abaddon/Documents/progetti/java/GeneticSound/database/GeneticSoundDB;create=false");
            connection=DriverManager.getConnection("jdbc:derby:./database/GeneticSoundDB;create=false");
            connection.setAutoCommit(true);
            
           
            
//            stmt=connection.createStatement();
//            stmt.executeUpdate("INSERT INTO \"MELODIA\" VALUES ('pippo1','2000-12-12', 'ciao1')");
           
//            stmt.close();
            
        } catch (SQLException ex) {
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    /**
	 * @return
	 * @uml.property  name="istanza"
	 */
    public static DataBase getIstanza(){
        if(istanza==null)
            istanza=new DataBase();
        return istanza;
    }
    
    
    public boolean addMelodia(Melodia melodia){
        boolean result = true;
        try {
            //System.out.println(melodia.getNome()+"---"+melodia.getData().toString()+"---"+melodia.getNote());
            PreparedStatement insertMelodia= connection.prepareStatement("INSERT INTO \"MELODIA\" VALUES (?, ?, ?)");
            insertMelodia.setString(1, melodia.getNome());
            insertMelodia.setDate(2, melodia.getData());
            insertMelodia.setString(3, melodia.getNoteNum());
            insertMelodia.executeUpdate();
            insertMelodia.close();
        } catch (SQLException ex) {
            result=false;
            ex.printStackTrace();
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
           
            return result;
        }
    }
    
    public boolean removeMelodia(String nome){
        boolean result = true;
        try {
            System.out.println(nome);
            PreparedStatement removeMelodia = connection.prepareStatement("DELETE FROM \"MELODIA\" WHERE \"NOME\"=?");
            removeMelodia.setString(1, nome);
            removeMelodia.executeUpdate();
        } catch (SQLException ex) {
            result=false;
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return result;
        }
            
    }
    
    public ResultSet getAllMelodie(){
        ResultSet result=null;
        try {
            PreparedStatement selectMelodie = connection.prepareStatement("SELECT * FROM \"MELODIA\"");
            result=selectMelodie.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return result;
        }
    }
    
    public Vector<Melodia>getMelodie(ResultSet result){
        Vector<Melodia> lista=new Vector<Melodia>();
        try {
            while (result.next()) {
                Melodia melodia=new Melodia(result.getString(1),result.getDate(2),result.getString(3));
                lista.add(melodia);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return lista;
        }
    }
    
   public ResultSet getResultMelodia(String nome){
        ResultSet result=null;
        try {
            PreparedStatement selectMelodia = connection.prepareStatement("SELECT * FROM \"MELODIA\" WHERE \"NOME\"=? ");
            selectMelodia.setString(1, nome);
            
            result=selectMelodia.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return result;
        }
    }
   
   public Melodia getMelodia(String nome){
       Melodia melodia=null;
        try {
            ResultSet result = getResultMelodia(nome);
            result.next();
            melodia=new Melodia(result.getString(1), result.getDate(2), result.getString(3));
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return melodia;
        }
               
   }
    
    
    

}
