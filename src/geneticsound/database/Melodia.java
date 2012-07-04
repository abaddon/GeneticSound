/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geneticsound.database;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author  abaddon
 */
@Entity
@Table(name = "MELODIA")
@NamedQueries({@NamedQuery(name = "Melodia.findByNome", query = "SELECT m FROM Melodia m WHERE m.nome = :nome"), @NamedQuery(name = "Melodia.findByData", query = "SELECT m FROM Melodia m WHERE m.data = :data"), @NamedQuery(name = "Melodia.findByNote", query = "SELECT m FROM Melodia m WHERE m.note = :note")})
public class Melodia implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
	 * @uml.property  name="nome"
	 */
    @Id
    @Column(name = "NOME", nullable = false)
    private String nome;
    /**
	 * @uml.property  name="data"
	 */
    @Column(name = "DATA", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "NOTE", nullable = false)
    private String note;
    private final String SEPARATORE="-";
    
    private SimpleDateFormat formatter = null;
    private static String[] pitchName=new String[]{"DO","DO#","RE","RE#","MI","FA","FA#","SOL","SOL#","LA","LA#","SI"};
    
    public Melodia() {
    }

    public Melodia(String nome) {
        this.nome = nome;
    }

    

    public Melodia(String nome, Date data, String note) {
        this.nome = nome;
        this.data = data;
        this.note = note;
    }
    
     public Melodia(String nome, java.util.Date data, int[] note) {
        this.nome = nome;
        this.note="";
        this.data = new Date(data.getTime());
        for(int i=0; i< note.length; i++)
            this.note+=note[i]+SEPARATORE;
        this.note=this.note.substring(0, this.note.length()-SEPARATORE.length());
    }

    /**
	 * @return
	 * @uml.property  name="nome"
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * @param nome
	 * @uml.property  name="nome"
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * @return
	 * @uml.property  name="data"
	 */
    public Date getData() {
        return data;
    }
    
    public String getDataFormatted(String format){
        formatter = new SimpleDateFormat(format);
        return formatter.format(data);
    }

    /**
	 * @param data
	 * @uml.property  name="data"
	 */
    public void setData(Date data) {
        this.data = data;
    }

    public String getNoteNum() {
        return note;
    }
    
    public String getNoteString(){
        String pitchesString="";
        //int[] lista=getNotetoInt();
        for(int nota: getNotetoInt()){
            int numOtt=(nota/12)-1;
            pitchesString+=pitchName[nota%12]+numOtt+" ";
        }
        return pitchesString;
    }
    
    public ArrayList<Integer> getNotetoInt(){
        
        getNoteNum().split(SEPARATORE);
        ArrayList<Integer> lista=new ArrayList<Integer>();
        for(int i=0; i<getNoteNum().split(SEPARATORE).length; i++){
            lista.add(Integer.valueOf(getNoteNum().split(SEPARATORE)[i]));
        }
        return lista;
    }
    
    public int[] getNoteToArrayInt(){
    	ArrayList<Integer>lista=getNotetoInt();
    	int result[]=new int[lista.size()];
        for(int i=0; i<lista.size(); i++){
        	result[i]=lista.get(i);
        }
        return result;
    }

    /**
	 * @param note
	 * @uml.property  name="note"
	 */
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nome != null ? nome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Melodia)) {
            return false;
        }
        Melodia other = (Melodia) object;
        if ((this.nome == null && other.nome != null) || (this.nome != null && !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "geneticsound.database.Melodia[nome=" + nome + "]";
    }

}
