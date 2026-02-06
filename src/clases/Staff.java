package clases;

import java.time.LocalDate;
import java.time.Period;

public class Staff extends Persona{

    private Tipo tipo;
    private LocalDate fechaInicioPuesto;

    public Staff(String nombre, String apellido, String dni, String email, Tipo tipo, LocalDate fechaInicioPuesto) {
        super(nombre, apellido, dni, email);
        this.tipo = tipo;
        this.fechaInicioPuesto = fechaInicioPuesto;
    }

    
	public Tipo getTipo() {
		return tipo;
	}


	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}


	public LocalDate getFechaInicioPuesto() {
		return fechaInicioPuesto;
	}


	public void setFechaInicioPuesto(LocalDate fechaInicioPuesto) {
		this.fechaInicioPuesto = fechaInicioPuesto;
	}


	public int calcularAniosPuesto() {
	    LocalDate hoy = LocalDate.now();
	    int anios = hoy.getYear() - fechaInicioPuesto.getYear();
	    return anios;
	}

    @Override
    public String getDescripcion() {
        return "Staff - " + tipo + ", " + calcularAniosPuesto() + " años en el puesto";
    }
}
