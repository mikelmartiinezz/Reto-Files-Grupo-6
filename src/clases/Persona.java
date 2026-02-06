package clases;

import java.io.Serializable;

public abstract class Persona implements Serializable {

	 private String nombre;
	    private String apellido;
	    private String dni;
	    private String email;
	    
		public Persona(String nombre, String apellido, String dni, String email) {
			super();
			this.nombre = nombre;
			this.apellido = apellido;
			this.dni = dni;
			this.email = email;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getApellido() {
			return apellido;
		}

		public void setApellido(String apellido) {
			this.apellido = apellido;
		}

		public String getDni() {
			return dni;
		}

		public void setDni(String dni) {
			this.dni = dni;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
		
		 public abstract String getDescripcion();
		
		@Override
		public String toString() {
			return "Persona [nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni + ", email=" + email + "]";
		}
	    
	    
}
