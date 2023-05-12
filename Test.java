package dam.tema8.proyecto;
/**
 * Clase test para poner a prueba la conexión y gestión de la base de datos.
 * @author David
 */
import java.util.ArrayList;

import dam.tema8.proyecto.copia.Conexion;
import dam.tema8.proyecto.copia.Estudio;
import dam.tema8.proyecto.copia.Gestion;
import dam.tema8.proyecto.copia.Pelicula;
import dam.tema8.proyecto.copia.Serie;

public class Test {
	public static void main(String[] args) {
		Conexion conexion = new Conexion();

		if (conexion.conectar("jdbc:mysql://localhost/Anime?user=root&password=usuario")) {
			Gestion databaseManager = new Gestion(conexion.getConnection());
			System.out.println(conexion.estaConectado());
			
			//Mostrar todas las series con todos sus atributos
			ArrayList<Serie> series = databaseManager.getSeries();			
			for (Serie serie : series) {
				System.out.println(serie);
			}
			System.out.println();
//			
			//Mostrar todas las películas con todos sus atributos
			ArrayList<Pelicula> peliculas = databaseManager.getPeliculas();
			for (Pelicula pelicula : peliculas) {
				System.out.println(pelicula);
			}
			System.out.println();
			
			//Añadir una serie a la base de datos. 
			//EDITAR LA QUERY DE AÑADIR PARA QUE AÑADA CON LA ID QUE YO LE DÉ, NO LA DE AUTOINCREMENT
			Estudio e1 = new Estudio(3);
			Serie s1 = new Serie(4, "My Hero Academia", 12, e1);
			boolean añadirSerie = databaseManager.crearSerie(s1);
			if (añadirSerie == true) {
				System.out.println("Se ha añadido la serie");
			} else {
				System.out.println("No se ha podido añadir la serie");
			}
			System.out.println();
			//Mostrar una tabla filtrada por al menos por 2 campos
//			ArrayList<Serie> seriesFiltradas = databaseManager.getSeries("nombre_serie", "Death Note");
		}
	}
}