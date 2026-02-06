package principal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clases.*;
import util.*;

public class MainReto {

	public static void main(String[] args) {
		// Comprobar si los ficheros existen
		File ficheroGrupos = new File("grupos.obj");
		File ficheroFestivales = new File("festival.obj");

		if (!ficheroGrupos.exists() || !ficheroFestivales.exists()) {
			precargarDatos();
			System.out.println("Datos precargados correctamente.");
		} else {
			System.out.println("Ficheros existentes, se cargará la información desde ellos.");
		}

		int opc;

		do {
			mostrarMenu();
			opc = Utilidades.leerInt();

			switch (opc) {

			case 1:
				menuAnadir();
				break;
			case 2:
				listarGruposPorNombre();
				break;
			case 3:
				listarFestivalesPorNombre();
				break;
			case 4:
				listarConciertosPorFecha();
				break;
			case 5:
				eliminarFestival();
				break;
			case 6:
				listarGruposDetallado();
				break;
			case 7:
				consultarStaffPorDni();
				break;
			case 8:
				modificarInformacion();
				break;

			case 9:
				System.out.println("Saliendo del programa...");
				break;

			default:
				System.out.println("Opción incorrecta");
			}

		} while (opc != 9);
	}

	private static void listarGruposDetallado() {
		File fichero = new File("grupos.obj");

	    if (!fichero.exists()) {
	        System.out.println("No hay grupos registrados.");
	        return;
	    }

	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {

	        Grupo[] grupos = (Grupo[]) ois.readObject();

	        if (grupos.length == 0) {
	            System.out.println("No hay grupos para mostrar.");
	            return;
	        }

	        System.out.println("LISTADO DETALLADO DE GRUPOS");
	        System.out.println("-------------------------------");

	        for (Grupo g : grupos) {
	            System.out.println("Grupo: " + g.getCodGrup() + " - " + g.getNombGrupo());

	            if (g.getPersonas().isEmpty()) {
	                System.out.println("  (Sin personas)");
	            } else {

	                for (Persona p : g.getPersonas().values()) {

	                    if (p instanceof Cantante) {
	                        Cantante c = (Cantante) p;
	                        System.out.println("  Cantante:");
	                        System.out.println("  	Nombre: " + c.getNombre() + " " + c.getApellido());
	                        System.out.println("  	DNI: " + c.getDni());
	                        System.out.println("  	Email: " + c.getEmail());
	                        System.out.println("  	Género: " + c.getGenero());

	                    } else if (p instanceof Staff) {
	                        Staff s = (Staff) p;
	                        System.out.println("  Staff:");
	                        System.out.println("  	Nombre: " + s.getNombre() + " " + s.getApellido());
	                        System.out.println("  	DNI: " + s.getDni());
	                        System.out.println("  	Email: " + s.getEmail());
	                        System.out.println("  	Tipo: " + s.getTipo());
	                        System.out.println("  	Fecha inicio: " + s.getFechaInicioPuesto());
	                        System.out.println("  	Años en el puesto: " + s.calcularAniosPuesto());
	                    }
	                }
	            }

	            System.out.println("----------------------------------");
	        }

	    } catch (Exception e) {
	        System.out.println("Error al leer los grupos.");
	        e.printStackTrace();
	    }
		
	}

	private static void modificarInformacion() {
	    int opc;

	    do {
	        System.out.println("MENÚ MODIFICAR INFORMACIÓN");
	        System.out.println("1. Modificar cantante");
	        System.out.println("2. Modificar staff");
	        System.out.println("3. Volver");
	        opc = Utilidades.leerInt(1, 3);

	        switch (opc) {
	        case 1:
	            modificarCantante();
	            break;
	        case 2:
	            modificarStaff();
	            break;
	        case 3:
	            System.out.println("Volviendo...");
	            break;
	        }
	    } while (opc != 3);
	}


	private static void modificarStaff() {

	    File fichero = new File("grupos.obj");

	    if (!fichero.exists()) {
	        System.out.println("No hay grupos registrados.");
	        return;
	    }

	    try {
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));
	        Grupo[] grupos = (Grupo[]) ois.readObject();
	        ois.close();

	        String dni = Utilidades.introducirCadena("Introduce DNI del staff:");

	        Staff staff = null;

	        for (Grupo g : grupos) {
	            if (g.getPersonas().containsKey(dni) &&
	                g.getPersonas().get(dni) instanceof Staff) {

	                staff = (Staff) g.getPersonas().get(dni);
	                break;
	            }
	        }

	        if (staff == null) {
	            System.out.println("No existe ningún staff con ese DNI.");
	            return;
	        }

	        System.out.println("Datos actuales:");
	        System.out.println("Nombre: " + staff.getNombre());
	        System.out.println("Tipo: " + staff.getTipo());

	        // Modificar nombre
	        String nuevoNombre = Utilidades.introducirCadena("Nuevo nombre:");
	        staff.setNombre(nuevoNombre);

	        // Modificar tipo
	        Tipo nuevoTipo = null;
	        boolean valido = false;

	        do {
	            try {
	                String tipoStr = Utilidades.introducirCadena("Nuevo tipo (MANAGER / TECNICO / MEDICO):");
	                nuevoTipo = Tipo.valueOf(tipoStr.toUpperCase());
	                valido = true;
	            } catch (IllegalArgumentException e) {
	                System.out.println("Tipo incorrecto.");
	            }
	        } while (!valido);

	        staff.setTipo(nuevoTipo);

	        // Guardar cambios
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
	        oos.writeObject(grupos);
	        oos.close();

	        System.out.println("Staff modificado correctamente.");

	    } catch (Exception e) {
	        System.out.println("Error al modificar staff.");
	        e.printStackTrace();
	    }
	}


	private static void modificarCantante() {

	    File fichero = new File("grupos.obj");

	    if (!fichero.exists()) {
	        System.out.println("No hay grupos registrados.");
	        return;
	    }

	    try {
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));
	        Grupo[] grupos = (Grupo[]) ois.readObject();
	        ois.close();

	        String dni = Utilidades.introducirCadena("Introduce DNI del cantante:");

	        Cantante cantante = null;
	        Grupo grupoCantante = null;

	        // Buscar cantante
	        for (Grupo g : grupos) {
	            if (g.getPersonas().containsKey(dni) &&
	                g.getPersonas().get(dni) instanceof Cantante) {

	                cantante = (Cantante) g.getPersonas().get(dni);
	                grupoCantante = g;
	                break;
	            }
	        }

	        if (cantante == null) {
	            System.out.println("No existe ningún cantante con ese DNI.");
	            return;
	        }

	        System.out.println("Datos actuales:");
	        System.out.println("Nombre: " + cantante.getNombre());
	        System.out.println("Género: " + cantante.getGenero());

	        // ---------- MODIFICAR NOMBRE ----------
	        String nuevoNombre;
	        boolean repetido;

	        do {
	            nuevoNombre = Utilidades.introducirCadena("Nuevo nombre:");
	            repetido = false;

	            for (Persona p : grupoCantante.getPersonas().values()) {
	                if (p instanceof Cantante &&
	                    p.getNombre().equalsIgnoreCase(nuevoNombre) &&
	                    p != cantante) {
	                    repetido = true;
	                    System.out.println("Error: ya existe otro cantante con ese nombre en el grupo.");
	                }
	            }
	        } while (repetido);

	        cantante.setNombre(nuevoNombre);

	        // ---------- MODIFICAR GÉNERO ----------
	        Genero nuevoGenero = null;
	        boolean valido = false;

	        do {
	            try {
	                String gen = Utilidades.introducirCadena("Nuevo género (POP / ROCK / REGGAETON):");
	                nuevoGenero = Genero.valueOf(gen.toUpperCase());
	                valido = true;
	            } catch (IllegalArgumentException e) {
	                System.out.println("Género incorrecto.");
	            }
	        } while (!valido);

	        cantante.setGenero(nuevoGenero);

	        // Guardar cambios
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
	        oos.writeObject(grupos);
	        oos.close();

	        System.out.println("Cantante modificado correctamente.");

	    } catch (Exception e) {
	        System.out.println("Error al modificar cantante.");
	        e.printStackTrace();
	    }
	}

	private static void consultarStaffPorDni() {
		File ficheroGrupos = new File("grupos.obj");
		if (!ficheroGrupos.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		String dniBuscado = Utilidades.introducirCadena("Introducir dni del Staff: ");

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroGrupos))) {
			Grupo[] grupos = (Grupo[]) ois.readObject();

			boolean encontrado = false;

			for (Grupo g : grupos) {
				for (Persona p : g.getPersonas().values()) {// devuelve personas staff y cantantes.

					if (p instanceof Staff && p.getDni().equals(dniBuscado)) {
						Staff s = (Staff) p;
						System.out.println("STAFF ENCONTRADO");
						System.out.println("----------------");
						System.out.println("Grupo: " + g.getNombGrupo());
						System.out.println("Puesto: " + s.getTipo());
						System.out.println("Años en el puesto: " + s.calcularAniosPuesto());
						encontrado = true;
					}
				}
			}
			if (!encontrado) {
				System.out.println("No existe ningún staff con ese DNI.");
			}
		} catch (Exception e) {
			System.out.println("Error al buscar el staff.");
		}

	}

		private static void eliminarFestival() {
		    File fichero = new File("festival.obj");

		    if (!fichero.exists()) {
		        System.out.println("No hay festivales registrados.");
		        return;
		    }

		    try {
		        // Leer festivales del fichero
		        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));
		        Festival[] festivalesArray = (Festival[]) ois.readObject();
		        ois.close();

		        ArrayList<Festival> listaFestivales = new ArrayList<>(Arrays.asList(festivalesArray));

		        // Pedir código del festival
		        String codFestival = Utilidades.introducirCadena("Introduce el código del festival a eliminar:");

		        boolean eliminado = false;

		        for (int i = 0; i < listaFestivales.size(); i++) {
		            if (listaFestivales.get(i).getCodFestival().equalsIgnoreCase(codFestival)) {
		                listaFestivales.remove(i);
		                eliminado = true;
		                break;
		            }
		        }

		        if (eliminado) {
		            // Guardar de nuevo en el fichero
		            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
		            oos.writeObject(listaFestivales.toArray(new Festival[0]));
		            oos.close();

		            System.out.println("Festival eliminado correctamente.");
		        } else {
		            System.out.println("Error: el festival no existe.");
		        }

		    } catch (Exception e) {
		        System.out.println("Error al eliminar el festival.");
		        e.printStackTrace();
		    }
	}

		private static void listarConciertosPorFecha() {

		    File fichFesti = new File("festival.obj");

		    if (!fichFesti.exists()) {
		        System.out.println("No hay conciertos registrados.");
		        return;
		    }

		    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichFesti))) {

		        Festival[] festivales = (Festival[]) ois.readObject();

		        if (festivales.length == 0) {
		            System.out.println("No hay conciertos para mostrar.");
		            return;
		        }

		        // TreeMap para ordenar conciertos por fecha
		        TreeMap<LocalDate, Concierto> conciertosOrdenados = new TreeMap<>();

		        // Recorrer festivales y agregar todos los conciertos al TreeMap
		        for (Festival f : festivales) {
		            for (LocalDate fecha : f.getConciertos().keySet()) {
		                conciertosOrdenados.put(fecha, f.getConciertos().get(fecha));
		            }
		        }

		        if (conciertosOrdenados.isEmpty()) {
		            System.out.println("No hay conciertos registrados.");
		            return;
		        }

		        System.out.println("LISTADO DE CONCIERTOS ORDENADOS POR FECHA");
		        System.out.println("---------------------------------------");

		        // Mostrar solo los conciertos: fecha, código y los dos grupos
		        for (LocalDate fecha : conciertosOrdenados.keySet()) {
		            Concierto c = conciertosOrdenados.get(fecha);
		            System.out.println("Fecha: " + fecha);
		            System.out.println("Grupo 1: " + c.getGrupoPrincipal().getNombGrupo());
		            System.out.println("Grupo 2: " + c.getGrupoInvitado().getNombGrupo());
		            System.out.println("---------------------------------------");
		        }

		    } catch (Exception e) {
		        System.out.println("Error al listar los conciertos.");
		        e.printStackTrace();
		    }
		}


	private static void listarFestivalesPorNombre() {
		File ficheroGrupos = new File("festival.obj");

		if (!ficheroGrupos.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroGrupos))) {
			Festival[] gruposArray = (Festival[]) ois.readObject();

			if (gruposArray.length == 0) {
				System.out.println("No hay grupos para mostrar.");
				return;
			}

			// Pasamos a ArrayList
			ArrayList<Festival> festivales = new ArrayList<>();
			for (Festival g : gruposArray) {
				festivales.add(g);
			}

			Collections.sort(festivales);


			System.out.println("LISTADO DE FESTIVALES (ordenados por nombre)");
			System.out.println("--------------------------------------");

			for (Festival f : festivales) {
				System.out.println("Código: " + f.getCodFestival());
				System.out.println("Nombre: " + f.getNombreFestival());
				System.out.println("----------------");
			
			}

		} catch (Exception e) {
			System.out.println("Error al leer el fichero de grupos.");
			e.printStackTrace();
		}
	}

	private static void listarGruposPorNombre() {
		File ficheroGrupos = new File("grupos.obj");

		if (!ficheroGrupos.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroGrupos))) {
			Grupo[] gruposArray = (Grupo[]) ois.readObject();

			if (gruposArray.length == 0) {
				System.out.println("No hay grupos para mostrar.");
				return;
			}

			// Pasamos a ArrayList
			ArrayList<Grupo> grupos = new ArrayList<>();
			for (Grupo g : gruposArray) {
				grupos.add(g);
			}

			Collections.sort(grupos);


			System.out.println("LISTADO DE GRUPOS (ordenados por nombre)");
			System.out.println("--------------------------------------");

			for (Grupo g : grupos) {
				System.out.println("Código: " + g.getCodGrup());
				System.out.println("Nombre: " + g.getNombGrupo());
				System.out.println("Personas en el grupo: " + g.getPersonas().size());
				System.out.println("--------------------------------------");
			}

		} catch (Exception e) {
			System.out.println("Error al leer el fichero de grupos.");
			e.printStackTrace();
		}
	}

	private static void menuAnadir() {
		int opc;

		do {
			System.out.println("MENÚ AÑADIR");
			System.out.println("1. Añadir grupo");
			System.out.println("2. Añadir festival");
			System.out.println("3. Añadir concierto");
			System.out.println("4. Volver al menú principal");
			System.out.println("Seleccione una opción: ");

			opc = Utilidades.leerInt();

			switch (opc) {
			case 1:
				anadirGrupo();
				break;
			case 2:
				anadirFestival();
				break;
			case 3:
					anadirConciertoAFestival();
					break;
			case 4:
				System.out.println("Volviendo al menú principal...");
				break;
			default:
				System.out.println("Opción incorrecta");
			}

		} while (opc != 4);
	}
	
	private static void anadirConciertoAFestival() {

	    File ficheroFest = new File("festival.obj");
	    File ficheroGrupos = new File("grupos.obj");

	    if (!ficheroFest.exists() || !ficheroGrupos.exists()) {
	        System.out.println("No hay festivales o grupos registrados.");
	        return;
	    }

	    try {
	        // Leer festivales
	        ObjectInputStream oisFest = new ObjectInputStream(new FileInputStream(ficheroFest));
	        Festival[] festivales = (Festival[]) oisFest.readObject();
	        oisFest.close();

	        if (festivales.length == 0) {
	            System.out.println("No hay festivales disponibles.");
	            return;
	        }

	        // Mostrar festivales
	        System.out.println("Festivales disponibles:");
	        for (int i = 0; i < festivales.length; i++) {
	            System.out.println((i + 1) + ". " + festivales[i].getNombreFestival());
	        }

	        int posFestival = Utilidades.leerInt(1, festivales.length) - 1;
	        Festival festival = festivales[posFestival];

	        // Leer grupos
	        ObjectInputStream oisGrupos = new ObjectInputStream(new FileInputStream(ficheroGrupos));
	        Grupo[] grupos = (Grupo[]) oisGrupos.readObject();
	        oisGrupos.close();

	        if (grupos.length < 2) {
	            System.out.println("Se necesitan al menos 2 grupos.");
	            return;
	        }

	        char seguir;
	        do {
	            System.out.println("Fecha del concierto (dd/MM/yyyy):");
	            LocalDate fecha = Utilidades.leerFechaDMA();

	            if (festival.getConciertos().containsKey(fecha)) {
	                System.out.println("Error: ya existe un concierto en esa fecha.");
	                return;
	            }

	            System.out.println("Grupos disponibles:");
	            for (int i = 0; i < grupos.length; i++) {
	                System.out.println((i + 1) + ". " + grupos[i].getNombGrupo());
	            }

	            System.out.println("Selecciona el GRUPO PRINCIPAL:");
	            int gPrincipal = Utilidades.leerInt(1, grupos.length) - 1;

	            int gInvitado;
	            do {
	                System.out.println("Selecciona el GRUPO INVITADO:");
	                gInvitado = Utilidades.leerInt(1, grupos.length) - 1;
	            } while (gPrincipal == gInvitado);

	            String nombreGrupo = grupos[gPrincipal].getNombGrupo().toUpperCase();
	            String siglas = nombreGrupo.length() >= 3 
	                    ? nombreGrupo.substring(0, 3)
	                    : nombreGrupo;

	            String codConcierto = fecha.toString().replace("-", "") + siglas;


	            Concierto nuevo = new Concierto(
	                    codConcierto,
	                    fecha,
	                    grupos[gPrincipal],
	                    grupos[gInvitado]
	            );

	            festival.getConciertos().put(fecha, nuevo);

	            System.out.println("Concierto añadido correctamente.");
	            seguir = Utilidades.leerChar("¿Añadir otro concierto a este festival? (S/N)");

	        } while (seguir == 'S' || seguir == 's');

	        // Guardar festivales actualizados
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroFest));
	        oos.writeObject(festivales);
	        oos.close();

	    } catch (Exception e) {
	        System.out.println("Error al añadir el concierto.");
	        e.printStackTrace();
	    }
	}


		private static void anadirFestival() {
			try {
				ArrayList<Festival> listaFestivales = new ArrayList<>();
				File ficheroFest = new File("festival.obj");

				if (ficheroFest.exists()) {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroFest));
					Festival[] festivales = (Festival[]) ois.readObject();
					ois.close();

					for (Festival f : festivales) {
						listaFestivales.add(f);
					}
				}

				File ficheroGrupos = new File("grupos.obj");
				if (!ficheroGrupos.exists()) {
					System.out.println("No hay grupos registrados.");
					return;
				}

				ObjectInputStream oisGrupos = new ObjectInputStream(new FileInputStream(ficheroGrupos));
				Grupo[] grupos = (Grupo[]) oisGrupos.readObject();
				oisGrupos.close();

				if (grupos.length < 2) {
					System.out.println("Se necesitan al menos 2 grupos para crear un concierto.");
					return;

				}
				
				String nombreFestival = Utilidades.introducirCadena("Nombre del festival:");
				String codFestival = nombreFestival.substring(0, 3).toUpperCase()
				        + String.format("%02d", listaFestivales.size() + 1);


				TreeMap<LocalDate, Concierto> conciertos = new TreeMap<>();
				char seguir;
				do {
					System.out.println("Fecha del concierto (dd/MM/yyyy):");
					LocalDate fecha = Utilidades.leerFechaDMA();
					System.out.println("Grupos disponibles:");
					for (int i = 0; i < grupos.length; i++) {

						System.out.println((i + 1) + ". " + grupos[i].getNombGrupo());

					}

					System.out.println("Selecciona grupo 1:");
					int g1 = Utilidades.leerInt(1, grupos.length) - 1;
					int g2;

					do {
						System.out.println("Selecciona grupo 2 (distinto del primero):");
						g2 = Utilidades.leerInt(1, grupos.length) - 1;
					} while (g1 == g2);

					String codConcierto = fecha.toString().replace("-", "") +
							grupos[g1].getNombGrupo().substring(0, 3).toUpperCase();

					Concierto c = new Concierto(codConcierto, fecha, grupos[g1], grupos[g2]);
					if (conciertos.containsKey(fecha)) {
					    System.out.println("Error: ya existe un concierto en esa fecha.");
					} else {
					    conciertos.put(fecha, c);
					}

					seguir = Utilidades.leerChar("¿Añadir otro concierto? (S/N)");
				} while (seguir == 'S' || seguir == 's');
				Festival nuevoFestival = new Festival(nombreFestival, codFestival, conciertos);
				listaFestivales.add(nuevoFestival);

				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("festival.obj"));
				oos.writeObject(listaFestivales.toArray(new Festival[0]));
				oos.close();

				System.out.println("Festival añadido correctamente.");
			} catch (Exception e) {
				System.out.println("Error al añadir el festival.");
				e.printStackTrace();

			}
	}

	private static void anadirGrupo() {
	    try {

	        ArrayList<Grupo> listaGrupos = new ArrayList<>();
	        File fichero = new File("grupos.obj");

	        if (fichero.exists()) {
	            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));
	            Grupo[] grupos = (Grupo[]) ois.readObject();
	            ois.close();

	            for (Grupo g : grupos) {
	                listaGrupos.add(g);
	            }
	        }

	        String nombreGrupo = Utilidades.introducirCadena("Introduce el nombre del grupo:");
	        String codGrupo = String.format("G%02d", listaGrupos.size() + 1);

	        HashMap<String, Persona> personas = new HashMap<>();
	        char seguir;

	        do {
	            boolean dniValido = false;
	            boolean emailValido = false;

	            System.out.println("1. Añadir Cantante");
	            System.out.println("2. Añadir Staff");
	            int opcion = Utilidades.leerInt(1, 2);

	            String nombre = Utilidades.introducirCadena("Nombre:");
	            String apellido = Utilidades.introducirCadena("Apellido:");

	            String dni = "";
	            do {
	                dni = Utilidades.introducirCadena("DNI:");
	                try {
	                    comprobarDni(dni);
	                    dniValido = true;
	                } catch (DniException e) {
	                    System.out.println(e.getMessage());
	                }
	            } while (!dniValido);

	            String email = "";
	            do {
	                email = Utilidades.introducirCadena("Email:");
	                try {
	                    validarEmail(email);
	                    emailValido = true;
	                } catch (EmailException e) {
	                    System.out.println(e.getMessage());
	                }
	            } while (!emailValido);

	            if (opcion == 1) {

	            	int numCantantes = 0;
	            	for (Persona p : personas.values()) {
	            	    if (p instanceof Cantante) {
	            	        numCantantes++;
	            	    }
	            	}
	            	String codCant = String.format("C%02d", numCantantes + 1);

	                Genero genero = null;
	                boolean generoValido = false;

	                do {
	                    try {
	                        String gen = Utilidades.introducirCadena("Género (POP / ROCK / REGGAETON):");
	                        genero = Genero.valueOf(gen.toUpperCase());
	                        generoValido = true;
	                    } catch (IllegalArgumentException e) {
	                        System.out.println("Error: el género debe ser POP, ROCK o REGGAETON");
	                    }
	                } while (!generoValido);

	                Cantante c = new Cantante(nombre, apellido, dni, email, codCant, genero);
	                personas.put(dni, c);

	            } else {
	                Tipo tipo = null;
	                boolean tipoValido = false;

	                do {
	                    try {
	                        String tipoStr = Utilidades.introducirCadena( "Tipo (MANAGER / TECNICO / MEDICO):");
	                        tipo = Tipo.valueOf(tipoStr.toUpperCase());
	                        tipoValido = true;
	                    } catch (IllegalArgumentException e) {
	                        System.out.println("Error: el tipo debe ser MANAGER, TECNICO o MEDICO");
	                    }
	                } while (!tipoValido);

	                LocalDate fecha;
	                do {
	                    System.out.println("Fecha inicio (dd/MM/yyyy):");
	                    fecha = Utilidades.leerFechaDMA();

	                    if (fecha.isAfter(LocalDate.now())) {
	                        System.out.println("Error: la fecha no puede ser futura.");
	                    }
	                } while (fecha.isAfter(LocalDate.now()));


	                Staff s = new Staff(nombre, apellido, dni, email, tipo, fecha);
	                personas.put(dni, s);
	            }

	            seguir = Utilidades.leerChar("¿Añadir otra persona? (S/N)");

	        } while (seguir == 'S' || seguir == 's');

	        Grupo nuevoGrupo = new Grupo(codGrupo, nombreGrupo, personas);
	        listaGrupos.add(nuevoGrupo);

	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("grupos.obj"));
	        oos.writeObject(listaGrupos.toArray(new Grupo[0]));
	        oos.close();

	        System.out.println("Grupo añadido correctamente.");

	    } catch (Exception e) {
	        System.out.println("Error al añadir el grupo");
	        e.printStackTrace();
	    }
	}

	private static void validarEmail(String email) throws EmailException {
	    String Patron_email = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

	    if (!email.matches(Patron_email )) {
	        throw new EmailException("Formato de email incorrecto");
	    }
	}

	private static void comprobarDni(String dni) throws DniException {

		Pattern modelo = Pattern.compile("\\d{8}[A-HJ-NP-TV-Z]");
		Matcher matcher = modelo.matcher(dni);

		if (!matcher.matches()) {
			throw new DniException("Formato de DNI incorrecto");
		}
	}

	private static void mostrarMenu() {

		System.out.println("MENÚ PRINCIPAL");
		System.out.println("1. Añadir");
		System.out.println("2. Listado de grupos por nombre");
		System.out.println("3. Listado de festival por nombre");
		System.out.println("4. Listado de concierto por fecha");
		System.out.println("5. Eliminar festival");
		System.out.println("6. Listado detallado de grupos");
		System.out.println("7. Consultar staff por DNI");
		System.out.println("8. Modificar información");
		System.out.println("9. Salir del programa");
		System.out.println("Seleccione una opción: ");

	}

	public static void precargarDatos() {
		try {
			// ------------------- GRUPOS -------------------
			HashMap<String, Persona> personas1 = new HashMap<>();
			personas1.put("12345678A", new Cantante("Juan", "Pérez", "12345678A", "juan@mail.com", "C01", Genero.POP));
			personas1.put("87654321B", new Cantante("Ana", "García", "87654321B", "ana@mail.com", "C02", Genero.ROCK));
			personas1.put("11111111C", new Staff("Carlos", "Lopez", "11111111C", "carlos@mail.com", Tipo.MANAGER,
					LocalDate.of(2020, 5, 1)));
			personas1.put("22222222D", new Staff("Marta", "Santos", "22222222D", "marta@mail.com", Tipo.TECNICO,
					LocalDate.of(2021, 3, 15)));

			Grupo grupo1 = new Grupo("G01", "The Rockers", personas1);

			HashMap<String, Persona> personas2 = new HashMap<>();
			personas2.put("33333333E",
					new Cantante("Luis", "Martínez", "33333333E", "luis@mail.com", "C03", Genero.REGGAETON));
			personas2.put("44444444F", new Cantante("Sara", "Torres", "44444444F", "sara@mail.com", "C04", Genero.POP));
			personas2.put("55555555G",
					new Staff("Pedro", "Vega", "55555555G", "pedro@mail.com", Tipo.MEDICO, LocalDate.of(2022, 1, 20)));

			Grupo grupo2 = new Grupo("G02", "Reaggaeton Stars", personas2);

			// Guardar grupos en "grupos.obj"
			Grupo[] listaGrupos = { grupo1, grupo2 };
			ObjectOutputStream oosGrupos = new ObjectOutputStream(new FileOutputStream("grupos.obj"));
			oosGrupos.writeObject(listaGrupos);
			oosGrupos.close();

			// ------------------- FESTIVALES -------------------
			TreeMap<LocalDate, Concierto> conciertos1 = new TreeMap<>();
			conciertos1.put(LocalDate.of(2026, 6, 10),
					new Concierto("20260610THE", LocalDate.of(2024, 6, 10), grupo1, grupo2));
			Festival festival1 = new Festival("SummerFest", "SUM01", conciertos1);

			TreeMap<LocalDate, Concierto> conciertos2 = new TreeMap<>();
			conciertos2.put(LocalDate.of(2026, 7, 5),
					new Concierto("20260705REG", LocalDate.of(2024, 7, 5), grupo2, grupo1));
			Festival festival2 = new Festival("MusicDays", "MUS01", conciertos2);

			Festival[] listaFestivales = { festival1, festival2 };
			ObjectOutputStream oosFest = new ObjectOutputStream(new FileOutputStream("festival.obj"));
			oosFest.writeObject(listaFestivales);
			oosFest.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}