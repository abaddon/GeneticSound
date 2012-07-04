/*
 * SimpleTableModel.java
 *
 * Created on 20 maggio 2006, 8.43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geneticsound;

import geneticsound.database.Melodia;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.table.*;
import java.util.Vector;
/**
 * carica i dati all'interno delle tabelle
 * @author marco
 */
public class SimpleTableModel extends AbstractTableModel{
    private String[] columnNames;
    private Object[][] cells;
    private SimpleDateFormat formatoData = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALY);
   
    
    /**
     * crea un modello di tabella
     * @param lista 
     */
    public SimpleTableModel( Vector lista/*, String[] columnNames*/){
        if(!lista.isEmpty()){
            SimpleTableModelMelodia(lista);
        }else{
            SimpleTableModelEmpty();
        }     
    }
    
    
    /**
     * tabella vuota
     */
    public void SimpleTableModelEmpty(){
        this.columnNames=new String[] {"","",""};
        cells=new Object[1][columnNames.length];
        for(int i=0; i<columnNames.length;i++)
            cells[0][i]="empty";
    }
    
    /**
     * crea modello per cliente
     * @param cliente 
     */
    public void SimpleTableModelMelodia(Vector<Melodia> melodie/*,String[] columnNames*/){
       
        this.columnNames=new String [] {"Nome", "Data", "note"};
        int rowCount;
        
        rowCount=melodie.size();
        cells=new Object[rowCount][columnNames.length];
        int counter =0;
        for(Melodia melodia: melodie){
            cells[counter][0]=melodia.getNome();
            cells[counter][1]=melodia.getData();
            cells[counter][2]=melodia.getNoteString();
            counter++;
        }
        
    }
    
    
    
    
    /**
     * ottiene il numero delle righe
     * @return 
     */
    public int getRowCount(){
        return cells.length;
    }
    /**
     * ottiene il numero delle colonne
     * @return 
     */
    public int getColumnCount(){
        return columnNames.length;
    }
    
    /**
     * ottiene il valore di una cella
     * @param r 
     * @param c 
     * @return 
     */
    public Object getValueAt(int r, int c){
        return cells[r][c];
    }
    /**
     * ottiene il nome di una colonna
     * @param c 
     * @return 
     */
    @Override
    public String getColumnName(int c){
        return columnNames[c];
    }
    
    /**
     * ottine la classe di una colonna
     * @param c 
     * @return 
     */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    
    
    
    
    
}
