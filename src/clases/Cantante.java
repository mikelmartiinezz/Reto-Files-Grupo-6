package clases;

public class Cantante extends Persona {

	  private String codigoCant; // autogenerado
	    private Genero genero;

	    public Cantante(String nombre, String apellido, String dni, String email, String codigoCant, Genero genero) {
	        super(nombre, apellido, dni, email);
	        this.codigoCant = codigoCant;
	        this.genero = genero;
	    }

		public String getCodigoCant() {
			return codigoCant;
		}

		public void setCodigoCant(String codigoCant) {
			this.codigoCant = codigoCant;
		}

		public Genero getGenero() {
			return genero;
		}

		public void setGenero(Genero genero) {
			this.genero = genero;
		}
	    
		
		 @Override
		public String toString() {
			return "Cantante [codigoCant=" + codigoCant + ", genero=" + genero + "]";
		}

		@Override
		    public String getDescripcion() {
		        return "Cantante - Género: " + genero;
		    }
	

}
