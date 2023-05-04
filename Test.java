package dam.tema8.proyecto;
/**
 * Clase test para poner a prueba la conexión y gestión de la base de datos.
 * @author David
 */
import java.util.ArrayList;

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
			
			//Mostrar todas las películas con todos sus atributos
			ArrayList<Pelicula> peliculas = databaseManager.getPeliculas();
			for (Pelicula pelicula : peliculas) {
				System.out.println(pelicula);
			}
		}
	}
}