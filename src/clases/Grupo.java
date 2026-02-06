package clases;

import java.io.Serializable;
import java.util.HashMap;

public class Grupo implements Serializable, Comparable <Grupo> {

	private String CodGrup;
	private String NombGrupo;
	private HashMap<String, Persona> personas;
	
	public Grupo(String codGrup, String nombGrupo, HashMap<String, Persona> personas) {
		super();
		CodGrup = codGrup;
		NombGrupo = nombGrupo;
		this.personas = personas;
	}

	public String getCodGrup() {
		return CodGrup;
	}

	public void setCodGrup(String codGrup) {
		CodGrup = codGrup;
	}

	public String getNombGrupo() {
		return NombGrupo;
	}

	public void setNombGrupo(String nombGrupo) {
		NombGrupo = nombGrupo;
	}

	public HashMap<String, Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(HashMap<String, Persona> personas) {
		this.personas = personas;
	}
	
	// Añadir persona
    public void agregarPersona(Persona persona) {
        this.personas.put(persona.getDni(), persona);
    }

	@Override
	public String toString() {
		return "Grupo [CodGrup=" + CodGrup + ", NombGrupo=" + NombGrupo + "]";
	}
	
	@Override
	public int compareTo(Grupo otro) {
	    return this.NombGrupo.compareToIgnoreCase(otro.NombGrupo);
	}

	
}
