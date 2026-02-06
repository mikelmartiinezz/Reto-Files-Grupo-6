package principal;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clases.*;
import util.*;

public class MainReto {

	public static void main(String[] args) {
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

	// -------------------- MENÚ --------------------
	private static void mostrarMenu() {
		System.out.println("\nMENÚ PRINCIPAL");
		System.out.println("1. Añadir");
		System.out.println("2. Listado de grupos por nombre");
		System.out.println("3. Listado de festivales por nombre");
		System.out.println("4. Listado de conciertos por fecha");
		System.out.println("5. Eliminar festival");
		System.out.println("6. Listado detallado de grupos");
		System.out.println("7. Consultar staff por DNI");
		System.out.println("8. Modificar información");
		System.out.println("9. Salir del programa");
		System.out.print("Seleccione una opción: ");
	}

	private static void menuAnadir() {
		int opc;
		do {
			System.out.println("\nMENÚ AÑADIR");
			System.out.println("1. Añadir grupo");
			System.out.println("2. Añadir festival");
			System.out.println("3. Añadir concierto");
			System.out.println("4. Volver al menú principal");
			System.out.print("Seleccione una opción: ");

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

	// -------------------- AÑADIR --------------------
	private static void anadirGrupo() {
		try {
			ArrayList<Grupo> listaGrupos = new ArrayList<>();
			File fichero = new File("grupos.obj");

			// Leer los grupos existentes
			if (fichero.exists()) {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
					while (true) {
						try {
							Grupo g = (Grupo) ois.readObject();
							listaGrupos.add(g);
						} catch (EOFException e) {
							break;
						}
					}
				}
			}

			// Datos del nuevo grupo
			String nombreGrupo = Utilidades.introducirCadena("Introduce el nombre del grupo:");
			String codGrupo = String.format("G%02d", listaGrupos.size() + 1);
			HashMap<String, Persona> personas = new HashMap<>();
			char seguir;

			do {
				System.out.println("\n1. Añadir Cantante");
				System.out.println("2. Añadir Staff");
				int opcion = Utilidades.leerInt(1, 2);

				String nombre = Utilidades.introducirCadena("Nombre:");
				String apellido = Utilidades.introducirCadena("Apellido:");

				// Validar DNI
				String dni;
				do {
					dni = Utilidades.introducirCadena("DNI:");
					try {
						comprobarDni(dni);
						break;
					} catch (DniException e) {
						System.out.println(e.getMessage());
					}
				} while (true);

				// Validar Email
				String email;
				do {
					email = Utilidades.introducirCadena("Email:");
					try {
						validarEmail(email);
						break;
					} catch (EmailException e) {
						System.out.println(e.getMessage());
					}
				} while (true);

				if (opcion == 1) {
					// Cantante
					int numCantantes = (int) personas.values().stream().filter(p -> p instanceof Cantante).count();
					String codCant = String.format("C%02d", numCantantes + 1);

					Genero genero;
					do {
						try {
							String gen = Utilidades.introducirCadena("Género (POP / ROCK / REGGAETON):");
							genero = Genero.valueOf(gen.toUpperCase());
							break;
						} catch (IllegalArgumentException e) {
							System.out.println("Error: el género debe ser POP, ROCK o REGGAETON");
						}
					} while (true);

					Cantante c = new Cantante(nombre, apellido, dni, email, codCant, genero);
					personas.put(dni, c);

				} else {
					// Staff
					Tipo tipo;
					do {
						try {
							String tipoStr = Utilidades.introducirCadena("Tipo (MANAGER / TECNICO / MEDICO):");
							tipo = Tipo.valueOf(tipoStr.toUpperCase());
							break;
						} catch (IllegalArgumentException e) {
							System.out.println("Error: el tipo debe ser MANAGER, TECNICO o MEDICO");
						}
					} while (true);

					LocalDate fecha;
					do {
						System.out.println("Fecha inicio (dd/MM/yyyy):");
						fecha = Utilidades.leerFechaDMA();
						if (!fecha.isAfter(LocalDate.now()))
							break;
						System.out.println("Error: la fecha no puede ser futura.");
					} while (true);

					Staff s = new Staff(nombre, apellido, dni, email, tipo, fecha);
					personas.put(dni, s);
				}

				seguir = Utilidades.leerChar("¿Añadir otra persona? (S/N):");

			} while (seguir == 'S' || seguir == 's');

			Grupo nuevoGrupo = new Grupo(codGrupo, nombreGrupo, personas);
			listaGrupos.add(nuevoGrupo);

			// Guardar todos los grupos objeto por objeto
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
				for (Grupo g : listaGrupos) {
					oos.writeObject(g);
				}
			}

			System.out.println("Grupo añadido correctamente.");

		} catch (Exception e) {
			System.out.println("Error al añadir el grupo.");
			e.printStackTrace();
		}
	}

	private static void anadirFestival() {
		try {
			File ficheroFest = new File("festival.obj");
			File ficheroGrupos = new File("grupos.obj");

			if (!ficheroGrupos.exists()) {
				System.out.println("No hay grupos registrados.");
				return;
			}

			ArrayList<Festival> listaFestivales = new ArrayList<>();
			if (ficheroFest.exists()) {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroFest))) {
					while (true) {
						try {
							listaFestivales.add((Festival) ois.readObject());
						} catch (EOFException e) {
							break;
						}
					}
				}
			}

			// Leer grupos
			ArrayList<Grupo> listaGrupos = new ArrayList<>();
			try (ObjectInputStream oisG = new ObjectInputStream(new FileInputStream(ficheroGrupos))) {
				while (true) {
					try {
						listaGrupos.add((Grupo) oisG.readObject());
					} catch (EOFException e) {
						break;
					}
				}
			}

			if (listaGrupos.size() < 2) {
				System.out.println("Se necesitan al menos 2 grupos para crear un festival.");
				return;
			}

			String nombreFestival = Utilidades.introducirCadena("Nombre del festival:");
			String codFestival = nombreFestival.substring(0, 3).toUpperCase()
					+ String.format("%02d", listaFestivales.size() + 1);

			TreeMap<LocalDate, Concierto> conciertos = new TreeMap<>();
			char seguir = 0;
			do {
				System.out.println("Fecha del concierto (dd/MM/yyyy):");
				LocalDate fecha = Utilidades.leerFechaDMA();
				if (conciertos.containsKey(fecha)) {
					System.out.println("Error: ya existe un concierto en esa fecha.");
					continue;
				}

				// Mostrar grupos
				for (int i = 0; i < listaGrupos.size(); i++)
					System.out.println((i + 1) + ". " + listaGrupos.get(i).getNombGrupo());

				System.out.println("Selecciona grupo 1:");
				int g1 = Utilidades.leerInt(1, listaGrupos.size()) - 1;

				int g2;
				do {
					System.out.println("Selecciona grupo 2 (distinto del primero):");
					g2 = Utilidades.leerInt(1, listaGrupos.size()) - 1;
				} while (g1 == g2);

				String codConcierto = fecha.toString().replace("-", "")
						+ listaGrupos.get(g1).getNombGrupo().substring(0, 3).toUpperCase();
				Concierto c = new Concierto(codConcierto, fecha, listaGrupos.get(g1), listaGrupos.get(g2));
				conciertos.put(fecha, c);

				seguir = Utilidades.leerChar("¿Añadir otro concierto? (S/N):");
			} while (seguir == 'S' || seguir == 's');

			Festival f = new Festival(nombreFestival, codFestival, conciertos);
			listaFestivales.add(f);

			// Guardar festivales objeto por objeto
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroFest))) {
				for (Festival fest : listaFestivales)
					oos.writeObject(fest);
			}

			System.out.println("Festival añadido correctamente.");

		} catch (Exception e) {
			System.out.println("Error al añadir el festival.");
			e.printStackTrace();
		}
	}

	private static void anadirConciertoAFestival() {
		try {
			File ficheroFest = new File("festival.obj");
			File ficheroGrupos = new File("grupos.obj");

			if (!ficheroFest.exists() || !ficheroGrupos.exists()) {
				System.out.println("No hay festivales o grupos registrados.");
				return;
			}

			ArrayList<Festival> listaFestivales = new ArrayList<>();
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroFest))) {
				while (true) {
					try {
						listaFestivales.add((Festival) ois.readObject());
					} catch (EOFException e) {
						break;
					}
				}
			}

			if (listaFestivales.isEmpty()) {
				System.out.println("No hay festivales.");
				return;
			}

			// Elegir festival
			for (int i = 0; i < listaFestivales.size(); i++)
				System.out.println((i + 1) + ". " + listaFestivales.get(i).getNombreFestival());
			int posFestival = Utilidades.leerInt(1, listaFestivales.size()) - 1;
			Festival festival = listaFestivales.get(posFestival);

			ArrayList<Grupo> listaGrupos = new ArrayList<>();
			try (ObjectInputStream oisG = new ObjectInputStream(new FileInputStream(ficheroGrupos))) {
				while (true) {
					try {
						listaGrupos.add((Grupo) oisG.readObject());
					} catch (EOFException e) {
						break;
					}
				}
			}

			if (listaGrupos.size() < 2) {
				System.out.println("Se necesitan al menos 2 grupos.");
				return;
			}

			char seguir = 0;
			do {
				System.out.println("Fecha del concierto (dd/MM/yyyy):");
				LocalDate fecha = Utilidades.leerFechaDMA();
				if (festival.getConciertos().containsKey(fecha)) {
					System.out.println("Error: ya existe un concierto en esa fecha.");
					continue;
				}

				// Mostrar grupos
				for (int i = 0; i < listaGrupos.size(); i++)
					System.out.println((i + 1) + ". " + listaGrupos.get(i).getNombGrupo());
				System.out.println("Selecciona GRUPO PRINCIPAL:");
				int g1 = Utilidades.leerInt(1, listaGrupos.size()) - 1;

				int g2;
				do {
					System.out.println("Selecciona GRUPO INVITADO:");
					g2 = Utilidades.leerInt(1, listaGrupos.size()) - 1;
				} while (g1 == g2);

				String codConcierto = fecha.toString().replace("-", "")
						+ listaGrupos.get(g1).getNombGrupo().substring(0, 3).toUpperCase();
				festival.getConciertos().put(fecha,
						new Concierto(codConcierto, fecha, listaGrupos.get(g1), listaGrupos.get(g2)));

				System.out.println("Concierto añadido correctamente.");
				seguir = Utilidades.leerChar("¿Añadir otro concierto a este festival? (S/N):");

			} while (seguir == 'S' || seguir == 's');

			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroFest))) {
				for (Festival f : listaFestivales)
					oos.writeObject(f);
			}

		} catch (Exception e) {
			System.out.println("Error al añadir el concierto.");
			e.printStackTrace();
		}
	}

	// -------------------- LISTAR GRUPOS POR NOMBRE --------------------
	private static void listarGruposPorNombre() {
		File ficheroGrupos = new File("grupos.obj");
		if (!ficheroGrupos.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		ArrayList<Grupo> listaGrupos = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroGrupos))) {
			while (true) {
				try {
					listaGrupos.add((Grupo) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer el fichero de grupos.");
			e.printStackTrace();
			return;
		}

		if (listaGrupos.isEmpty()) {
			System.out.println("No hay grupos para mostrar.");
			return;
		}

		Collections.sort(listaGrupos);

		System.out.println("LISTADO DE GRUPOS (ordenados por nombre)");
		System.out.println("--------------------------------------");
		for (Grupo g : listaGrupos) {
			System.out.println("Código: " + g.getCodGrup());
			System.out.println("Nombre: " + g.getNombGrupo());
			System.out.println("Personas en el grupo: " + g.getPersonas().size());
			System.out.println("--------------------------------------");
		}
	}

	// -------------------- LISTAR FESTIVALES POR NOMBRE --------------------
	private static void listarFestivalesPorNombre() {
		File ficheroFest = new File("festival.obj");
		if (!ficheroFest.exists()) {
			System.out.println("No hay festivales registrados.");
			return;
		}

		ArrayList<Festival> listaFestivales = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroFest))) {
			while (true) {
				try {
					listaFestivales.add((Festival) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer el fichero de festivales.");
			e.printStackTrace();
			return;
		}

		if (listaFestivales.isEmpty()) {
			System.out.println("No hay festivales para mostrar.");
			return;
		}

		Collections.sort(listaFestivales);

		System.out.println("LISTADO DE FESTIVALES (ordenados por nombre)");
		System.out.println("--------------------------------------");
		for (Festival f : listaFestivales) {
			System.out.println("Código: " + f.getCodFestival());
			System.out.println("Nombre: " + f.getNombreFestival());
			System.out.println("----------------");
		}
	}

	// -------------------- LISTAR DETALLADO DE GRUPOS --------------------
	private static void listarGruposDetallado() {
		File fichero = new File("grupos.obj");
		if (!fichero.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		ArrayList<Grupo> listaGrupos = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
			while (true) {
				try {
					listaGrupos.add((Grupo) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer los grupos.");
			e.printStackTrace();
			return;
		}

		if (listaGrupos.isEmpty()) {
			System.out.println("No hay grupos para mostrar.");
			return;
		}

		System.out.println("LISTADO DETALLADO DE GRUPOS");
		System.out.println("-------------------------------");
		for (Grupo g : listaGrupos) {
			System.out.println("Grupo: " + g.getCodGrup() + " - " + g.getNombGrupo());
			if (g.getPersonas().isEmpty()) {
				System.out.println("  (Sin personas)");
			} else {
				for (Persona p : g.getPersonas().values()) {
					if (p instanceof Cantante) {
						Cantante c = (Cantante) p;
						System.out.println("  Cantante:");
						System.out.println("    Nombre: " + c.getNombre() + " " + c.getApellido());
						System.out.println("    DNI: " + c.getDni());
						System.out.println("    Email: " + c.getEmail());
						System.out.println("    Género: " + c.getGenero());
					} else if (p instanceof Staff) {
						Staff s = (Staff) p;
						System.out.println("  Staff:");
						System.out.println("    Nombre: " + s.getNombre() + " " + s.getApellido());
						System.out.println("    DNI: " + s.getDni());
						System.out.println("    Email: " + s.getEmail());
						System.out.println("    Tipo: " + s.getTipo());
						System.out.println("    Fecha inicio: " + s.getFechaInicioPuesto());
						System.out.println("    Años en el puesto: " + s.calcularAniosPuesto());
					}
				}
			}
			System.out.println("----------------------------------");
		}
	}

	// -------------------- LISTAR CONCIERTOS POR FECHA --------------------
	private static void listarConciertosPorFecha() {
		File ficheroFest = new File("festival.obj");
		if (!ficheroFest.exists()) {
			System.out.println("No hay festivales registrados.");
			return;
		}

		ArrayList<Festival> listaFestivales = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroFest))) {
			while (true) {
				try {
					listaFestivales.add((Festival) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer los festivales.");
			e.printStackTrace();
			return;
		}

		TreeMap<LocalDate, Concierto> conciertosOrdenados = new TreeMap<>();
		for (Festival f : listaFestivales) {
			conciertosOrdenados.putAll(f.getConciertos());
		}

		if (conciertosOrdenados.isEmpty()) {
			System.out.println("No hay conciertos registrados.");
			return;
		}

		System.out.println("LISTADO DE CONCIERTOS ORDENADOS POR FECHA");
		System.out.println("---------------------------------------");
		for (Map.Entry<LocalDate, Concierto> entry : conciertosOrdenados.entrySet()) {
			Concierto c = entry.getValue();
			System.out.println("Fecha: " + entry.getKey());
			System.out.println("Grupo principal: " + c.getGrupoPrincipal().getNombGrupo());
			System.out.println("Grupo invitado: " + c.getGrupoInvitado().getNombGrupo());
			System.out.println("---------------------------------------");
		}
	}

	// -------------------- ELIMINAR FESTIVAL --------------------
	private static void eliminarFestival() {
		File ficheroFest = new File("festival.obj");
		if (!ficheroFest.exists()) {
			System.out.println("No hay festivales registrados.");
			return;
		}

		ArrayList<Festival> listaFestivales = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroFest))) {
			while (true) {
				try {
					listaFestivales.add((Festival) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer festivales.");
			e.printStackTrace();
			return;
		}

		String codFestival = Utilidades.introducirCadena("Introduce el código del festival a eliminar:");
		boolean eliminado = listaFestivales.removeIf(f -> f.getCodFestival().equalsIgnoreCase(codFestival));

		if (eliminado) {
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroFest))) {
				for (Festival f : listaFestivales)
					oos.writeObject(f);
			} catch (Exception e) {
				System.out.println("Error al guardar festivales.");
				e.printStackTrace();
				return;
			}
			System.out.println("Festival eliminado correctamente.");
		} else {
			System.out.println("No existe ningún festival con ese código.");
		}
	}

	// -------------------- CONSULTAR STAFF POR DNI --------------------
	private static void consultarStaffPorDni() {
		File ficheroGrupos = new File("grupos.obj");
		if (!ficheroGrupos.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		ArrayList<Grupo> listaGrupos = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheroGrupos))) {
			while (true) {
				try {
					listaGrupos.add((Grupo) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer grupos.");
			e.printStackTrace();
			return;
		}

		String dniBuscado = Utilidades.introducirCadena("Introducir DNI del Staff: ");
		boolean encontrado = false;
		for (Grupo g : listaGrupos) {
			for (Persona p : g.getPersonas().values()) {
				if (p instanceof Staff && p.getDni().equalsIgnoreCase(dniBuscado)) {
					Staff s = (Staff) p;
					System.out.println("STAFF ENCONTRADO");
					System.out.println("----------------");
					System.out.println("Grupo: " + g.getNombGrupo());
					System.out.println("Puesto: " + s.getTipo());
					System.out.println("Años en el puesto: " + s.calcularAniosPuesto());
					encontrado = true;
					break;
				}
			}
			if (encontrado)
				break;
		}

		if (!encontrado)
			System.out.println("No existe ningún staff con ese DNI.");
	}

	// -------------------- MODIFICAR CANTANTE --------------------
	private static void modificarCantante() {
		File fichero = new File("grupos.obj");
		if (!fichero.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		ArrayList<Grupo> listaGrupos = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
			while (true) {
				try {
					listaGrupos.add((Grupo) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer los grupos.");
			e.printStackTrace();
			return;
		}

		String dni = Utilidades.introducirCadena("Introduce DNI del cantante:");
		Cantante cantante = null;
		Grupo grupoCantante = null;

		for (Grupo g : listaGrupos) {
			if (g.getPersonas().containsKey(dni) && g.getPersonas().get(dni) instanceof Cantante) {
				cantante = (Cantante) g.getPersonas().get(dni);
				grupoCantante = g;
				break;
			}
		}

		if (cantante == null) {
			System.out.println("No existe ningún cantante con ese DNI.");
			return;
		}

		// Mostrar datos actuales
		System.out.println("Datos actuales:");
		System.out.println("Nombre: " + cantante.getNombre());
		System.out.println("Género: " + cantante.getGenero());

		// Modificar nombre
		String nuevoNombre;
		boolean repetido;
		do {
			nuevoNombre = Utilidades.introducirCadena("Nuevo nombre:");
			repetido = false;
			for (Persona p : grupoCantante.getPersonas().values()) {
				if (p instanceof Cantante && !p.getDni().equals(dni) && p.getNombre().equalsIgnoreCase(nuevoNombre)) {
					repetido = true;
					System.out.println("Error: ya existe otro cantante con ese nombre en el grupo.");
				}
			}
		} while (repetido);
		cantante.setNombre(nuevoNombre);

		// Modificar género
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

		// Guardar cambios objeto por objeto
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
			for (Grupo g : listaGrupos) {
				oos.writeObject(g);
			}
		} catch (Exception e) {
			System.out.println("Error al guardar cambios.");
			e.printStackTrace();
			return;
		}

		System.out.println("Cantante modificado correctamente.");
	}

	// -------------------- MODIFICAR STAFF --------------------
	private static void modificarStaff() {
		File fichero = new File("grupos.obj");
		if (!fichero.exists()) {
			System.out.println("No hay grupos registrados.");
			return;
		}

		ArrayList<Grupo> listaGrupos = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
			while (true) {
				try {
					listaGrupos.add((Grupo) ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer los grupos.");
			e.printStackTrace();
			return;
		}

		String dni = Utilidades.introducirCadena("Introduce DNI del staff:");
		Staff staff = null;

		for (Grupo g : listaGrupos) {
			if (g.getPersonas().containsKey(dni) && g.getPersonas().get(dni) instanceof Staff) {
				staff = (Staff) g.getPersonas().get(dni);
				break;
			}
		}

		if (staff == null) {
			System.out.println("No existe ningún staff con ese DNI.");
			return;
		}

		// Mostrar datos actuales
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

		// Guardar cambios objeto por objeto
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
			for (Grupo g : listaGrupos) {
				oos.writeObject(g);
			}
		} catch (Exception e) {
			System.out.println("Error al guardar cambios.");
			e.printStackTrace();
			return;
		}

		System.out.println("Staff modificado correctamente.");
	}

	private static void validarEmail(String email) throws EmailException {
		String Patron_email = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

		if (!email.matches(Patron_email)) {
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

			// Guardar cada grupo individualmente
			ObjectOutputStream oosGrupos = new ObjectOutputStream(new FileOutputStream("grupos.obj"));
			oosGrupos.writeObject(grupo1);
			oosGrupos.writeObject(grupo2);
			oosGrupos.close();

			// ------------------- FESTIVALES -------------------
			TreeMap<LocalDate, Concierto> conciertos1 = new TreeMap<>();
			conciertos1.put(LocalDate.of(2026, 6, 10),
					new Concierto("20260610THE", LocalDate.of(2026, 6, 10), grupo1, grupo2));
			Festival festival1 = new Festival("SummerFest", "SUM01", conciertos1);

			TreeMap<LocalDate, Concierto> conciertos2 = new TreeMap<>();
			conciertos2.put(LocalDate.of(2026, 7, 5),
					new Concierto("20260705REG", LocalDate.of(2026, 7, 5), grupo2, grupo1));
			Festival festival2 = new Festival("MusicDays", "MUS01", conciertos2);

			// Guardar cada festival individualmente
			ObjectOutputStream oosFest = new ObjectOutputStream(new FileOutputStream("festival.obj"));
			oosFest.writeObject(festival1);
			oosFest.writeObject(festival2);
			oosFest.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}