package dam.tema8.proyecto;
/**
 * Clase test para poner a prueba la conexión y gestión de la base de datos.
 * @author David
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
	public static void main(String[] args) throws Exception {
		// 1. Gestión de la conexión a la bbdd, sólo hay un objeto de conexión instanciado.
		Conexion conexion = new Conexion("jdbc:mysql://localhost/Anime?user=root&password=usuario");
		Gestion databaseManager = new Gestion(conexion);

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

		//Se debe poder filtrar los datos de cualquiera de las tablas de la bbdd, al menos por 2 campos
		HashMap<String, Object> filtradoEstudios = new HashMap<String, Object>();
		filtradoEstudios.put("nombre_estudio", "Bones");
		ArrayList<Estudio> estudiosFiltrados = databaseManager.getEstudios(filtradoEstudios);
		for (Estudio estudio : estudiosFiltrados) {
			System.out.println(estudio);
		}
		System.out.println();
		
		HashMap<String, Object> filtradoSeries = new HashMap<String, Object>();
		filtradoSeries.put("id_estudio", 1);
		filtradoSeries.put("numero_episodios", 37);
		ArrayList<Serie> seriesFiltradas = databaseManager.getSeries(filtradoSeries);
		for (Serie serie : seriesFiltradas) {
			System.out.println(serie);
		}
		System.out.println();
		
		HashMap<String, Object> filtradoPeliculas = new HashMap<String, Object>();
		filtradoPeliculas.put("id_estudio", 2);
		filtradoPeliculas.put("duracion_minutos", 140);
		ArrayList<Pelicula> peliculasFiltradas = databaseManager.getPeliculas(filtradoPeliculas);
		for (Pelicula pelicula : peliculasFiltradas) {
			System.out.println(pelicula);
		}
		System.out.println();
		
	    //Se pueden obtener los datos de una consulta de manera ordenada por alguno de los campos seleccionados
		Ordenacion o1 = new Ordenacion(2, "ASC");
		ArrayList<Serie> seriesOrdenadas = databaseManager.getSeries("1", o1);
		for (Serie serie : seriesOrdenadas) {
			System.out.println(serie);
		}
		System.out.println();
		
		ArrayList<Pelicula> peliculasOrdenadas = databaseManager.getPeliculas("1", o1);
		for (Pelicula pelicula : peliculasOrdenadas) {
			System.out.println(pelicula);
		}
		System.out.println();
		
		//Se debe poder modificar cualquiera de los registros de una tabla
		Estudio e1 = new Estudio(1, "Madhouse");
		Estudio e2 = new Estudio(2, "Kyoto Animation");
		Estudio e3 = new Estudio(3, "Bones");
		Estudio e4 = new Estudio(4, "Mappa");

		Pelicula pelicula = new Pelicula(12, "Película", 140, e2);
		Serie serie = new Serie(23, "Serie de prueba", 12, e2);
		
		//Añadir un estudio, serie o película a la base de datos.
//		boolean añadir = databaseManager.crear(e4);
//		System.out.println(añadir);
//		System.out.println();

		//Eliminar un estudio de la base de datos.
//		boolean eliminarEstudio = databaseManager.borrarEstudio(4);
//		System.out.println(eliminarEstudio);
//		System.out.println();

		//Eliminar una serie de la base de datos.
//		boolean eliminarSerie = databaseManager.borrarSerie(23);
//		System.out.println(eliminarSerie);
//		System.out.println();

		//Eliminar una película de la base de datos.
//		boolean eliminarPelicula = databaseManager.borrarPelicula(12);
//		System.out.println(eliminarPelicula);
//		System.out.println();

//		Editar un estudio, serie o película de la base de datos.
//		boolean editar = databaseManager.editar(serie);
//		System.out.println(editar);
//		System.out.println();

		
		//Exportar estudios a documento XML.
		String	ruta = "/home/usuario/Estudios.xml";
		databaseManager.exportarEstudios(ruta);
		System.out.println();
		
		//Exportar series a documento XML.
		ruta = "/home/usuario/Series.xml";
		databaseManager.exportarSeries(ruta);
		System.out.println();

		//Exportar peliculas a documento XML.
		ruta = "/home/usuario/Peliculas.xml";
		databaseManager.exportarPeliculas(ruta);

	}
}