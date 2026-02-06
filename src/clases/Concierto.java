package clases;

import java.io.Serializable;
import java.time.LocalDate;

public class Concierto implements Serializable{
	
	private String identificadorC;
	private LocalDate fecha;
	private Grupo grupoPrincipal;
	private Grupo grupoInvitado;
	
	public Concierto(String identificadorC, LocalDate fecha, Grupo grupoPrincipal, Grupo grupoInvitado) {
		super();
		this.identificadorC = identificadorC;
		this.fecha = fecha;
		this.grupoPrincipal = grupoPrincipal;
		this.grupoInvitado = grupoInvitado;
	}

	public String getIdentificadorC() {
		return identificadorC;
	}

	public void setIdentificadorC(String identificadorC) {
		this.identificadorC = identificadorC;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Grupo getGrupoPrincipal() {
		return grupoPrincipal;
	}

	public void setGrupoPrincipal(Grupo grupoPrincipal) {
		this.grupoPrincipal = grupoPrincipal;
	}

	public Grupo getGrupoInvitado() {
		return grupoInvitado;
	}

	public void setGrupoInvitado(Grupo grupoInvitado) {
		this.grupoInvitado = grupoInvitado;
	}
	
	private String generarIdentificador() {
        String letras = grupoPrincipal.getNombGrupo().substring(0, Math.min(3, grupoPrincipal.getNombGrupo().length())).toUpperCase();
        return fecha.toString() + letras;
    }

	@Override
	public String toString() {
		return "Concierto [identificadorC=" + identificadorC + ", fecha=" + fecha + "]";
	}

	
	
}
