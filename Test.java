package dam.tema8.proyecto;
/**
 * Clase test para poner a prueba la conexión y gestión de la base de datos.
 * @author David
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
	public static void main(String[] args) throws Exception {
		Conexion conexion = new Conexion();

		// 1. Gestión de la conexión a la bbdd, sólo hay un objeto de conexión instanciado.
		if (conexion.conectar("jdbc:mysql://localhost/Anime?user=root&password=usuario")) {
			Gestion databaseManager = new Gestion(conexion.getConnection());
			System.out.println(conexion.estaConectado());
			System.out.println();
			
			//Mostrar todos los estudios con todos sus atributos
			ArrayList<Estudio> estudios = databaseManager.getEstudios();
			for (Estudio estudio : estudios) {
				System.out.println(estudio);
			}
			System.out.println();

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
			System.out.println();
//
//			//Se debe poder filtrar los datos de cualquiera de las tablas de la bbdd, al menos por 2 campos
//			HashMap<String, Object> filtradoSeries = new HashMap<String, Object>();
//			filtradoSeries.put("id_estudio", 1);
//			filtradoSeries.put("numero_episodios", 37);
//			//No sé si realmente debo hacer el if aquí o hacer que sea el método quién muestre el syso.
//			ArrayList<Serie> seriesFiltradas = databaseManager.getSeries(filtradoSeries);
//			for (Serie serie : seriesFiltradas) {
//				System.out.println(serie);
//			}
//			System.out.println();
//			
//			HashMap<String, Object> filtradoPeliculas = new HashMap<String, Object>();
//			filtradoPeliculas.put("id_estudio", 2);
//			filtradoPeliculas.put("duracion_minutos", 140);
//			//No sé si realmente debo hacer el if aquí o hacer que sea el método quién muestre el syso.
//			ArrayList<Pelicula> peliculasFiltradas = databaseManager.getPeliculas(filtradoPeliculas);
//			for (Pelicula pelicula : peliculasFiltradas) {
//				System.out.println(pelicula);
//			}
//			System.out.println();
//			
//			//Se pueden obtener los datos de una consulta de manera ordenada por alguno de los campos seleccionados
			Ordenacion o1 = new Ordenacion(2, "ASC");
			ArrayList<Serie> seriesOrdenadas = databaseManager.getSeries("1", o1);
			for (Serie serie : seriesOrdenadas) {
				System.out.println(serie);
			}
			System.out.println();
			
			//Se debe poder modificar cualquiera de los registros de una tabla

			//			//Añadir un estudio, serie o película a la base de datos.
			//			Estudio e1 = new Estudio(1, "Madhouse");
			//			Estudio e2 = new Estudio(2, "Kyoto Animation");
			//			Estudio e3 = new Estudio(1, "Bones");
			//			Estudio e4 = new Estudio(5, "Bones");
			//
			//			Pelicula pelicula = new Pelicula(11, "Violet Evergarden: the Movie", 140, e2);
			//			Serie serie = new Serie(22, "Sound! Euphonium", 12, e2);
			//			boolean añadir = databaseManager.crear(pelicula);
			//			System.out.println("Añadir: " + añadir);
			//			System.out.println();

			//Eliminar un estudio de la base de datos.
			//			boolean eliminarEstudio = databaseManager.borrarEstudio(4);
			//			System.out.println("Eliminar estudio: " + eliminarEstudio);
			//			System.out.println();

			//Eliminar una serie de la base de datos.
			//			boolean eliminarSerie = databaseManager.borrarSerie(5);
			//			System.out.println("Eliminar serie: " + eliminarSerie);
			//			System.out.println();

			//Eliminar una película de la base de datos.
			//			boolean eliminarPelicula = databaseManager.borrarPelicula(11);
			//			System.out.println("Eliminar película: " + eliminarPelicula);
			//			System.out.println();

			//Editar un estudio, serie o película de la base de datos.
			//			boolean editar = databaseManager.editar(serie);
			//			System.out.println("Editar: " + editar);
			//			System.out.println();

		}
	}
}