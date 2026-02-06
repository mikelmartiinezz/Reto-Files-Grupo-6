package clases;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeMap;

public class Festival implements Serializable, Comparable<Festival> {

	private String nombreFestival;
    private String codFestival; // 3 primeras letras + número incremental
    private TreeMap<LocalDate, Concierto> conciertos; // Ordenados por fecha
    
	public Festival(String nombreFestival, String codFestival, TreeMap<LocalDate, Concierto> conciertos) {
		super();
		this.nombreFestival = nombreFestival;
		this.codFestival = codFestival;
		this.conciertos = conciertos;
	}

	public String getNombreFestival() {
		return nombreFestival;
	}

	public void setNombreFestival(String nombreFestival) {
		this.nombreFestival = nombreFestival;
	}

	public String getCodFestival() {
		return codFestival;
	}

	public void setCodFestival(String codFestival) {
		this.codFestival = codFestival;
	}

	public TreeMap<LocalDate, Concierto> getConciertos() {
		return conciertos;
	}

	public void setConciertos(TreeMap<LocalDate, Concierto> conciertos) {
		this.conciertos = conciertos;
	}
	
	public void agregarConcierto(Concierto concierto) {
	    this.conciertos.put(concierto.getFecha(), concierto);
	}

	@Override
	public String toString() {
		return "Festival [nombreFestival=" + nombreFestival + ", codFestival=" + codFestival + "]";
	}
	
	@Override
    public int compareTo(Festival otro) {
        return this.nombreFestival.compareToIgnoreCase(otro.nombreFestival);
    }
	  
}
