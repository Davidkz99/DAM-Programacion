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
		System.out.println("Mostrando todos los estudios:");
		for (Estudio estudio : estudios) {
			System.out.println(estudio);
		}
		System.out.println();

		//Mostrar todas las series con todos sus atributos
		ArrayList<Serie> series = databaseManager.getSeries();
		System.out.println("Mostrando todos las series:");
		for (Serie serie : series) {
			System.out.println(serie);
		}
		System.out.println();

		//Mostrar todas las películas con todos sus atributos
		ArrayList<Pelicula> peliculas = databaseManager.getPeliculas();
		System.out.println("Mostrando todos las películas:");
		for (Pelicula pelicula : peliculas) {
			System.out.println(pelicula);
		}
		System.out.println();

		//Se debe poder filtrar los datos de cualquiera de las tablas de la bbdd, al menos por 2 campos
		HashMap<String, Object> filtradoEstudios = new HashMap<String, Object>();
		filtradoEstudios.put("nombre_estudio", "Bones");
		ArrayList<Estudio> estudiosFiltrados = databaseManager.getEstudios(filtradoEstudios);
		System.out.println("Mostrando estudios filtrados:");
		for (Estudio estudio : estudiosFiltrados) {
			System.out.println(estudio);
		}
		System.out.println();
		
		HashMap<String, Object> filtradoSeries = new HashMap<String, Object>();
		filtradoSeries.put("id_estudio", 1);
		filtradoSeries.put("numero_episodios", 37);
		ArrayList<Serie> seriesFiltradas = databaseManager.getSeries(filtradoSeries);
		System.out.println("Mostrando series filtradas:");
		for (Serie serie : seriesFiltradas) {
			System.out.println(serie);
		}
		System.out.println();
		
		HashMap<String, Object> filtradoPeliculas = new HashMap<String, Object>();
		filtradoPeliculas.put("id_estudio", 2);
		filtradoPeliculas.put("duracion_minutos", 140);
		ArrayList<Pelicula> peliculasFiltradas = databaseManager.getPeliculas(filtradoPeliculas);
		System.out.println("Mostrando películas filtradas:");
		for (Pelicula pelicula : peliculasFiltradas) {
			System.out.println(pelicula);
		}
		System.out.println();
		
	    //Se pueden obtener los datos de una consulta de manera ordenada por alguno de los campos seleccionados
		Ordenacion o1 = new Ordenacion(2, "ASC");
		ArrayList<Estudio> estudiosOrdenados = databaseManager.getEstudios(o1);
		System.out.println("Mostrando estudios ordenadas:");
		for (Estudio estudio : estudiosOrdenados) {
			System.out.println(estudio);
		}
		System.out.println();
		
		ArrayList<Serie> seriesOrdenadas = databaseManager.getSeries("1", o1);
		System.out.println("Mostrando series ordenadas:");
		for (Serie serie : seriesOrdenadas) {
			System.out.println(serie);
		}
		System.out.println();
		
		ArrayList<Pelicula> peliculasOrdenadas = databaseManager.getPeliculas("1", o1);
		System.out.println("Mostrando películas ordenadas:");
		for (Pelicula pelicula : peliculasOrdenadas) {
			System.out.println(pelicula);
		}
		System.out.println();
		
		//Se debe poder modificar cualquiera de los registros de una tabla
		Estudio e1 = new Estudio(1, "Madhouse");
		Estudio e2 = new Estudio(2, "Kyoto Animation");
		Estudio e3 = new Estudio(3, "Bones");
		Estudio e4 = new Estudio(4, "Estudio de prueba");

		Pelicula pelicula = new Pelicula(12, "Película de prueba", 140, e1);
		Serie serie = new Serie(23, "Serie de prueba", 13, e2);
		
		//Añadir un estudio, serie o película a la base de datos.
//		boolean añadir = databaseManager.crear(serie);
//		System.out.println("Añadir:" + añadir);
//		System.out.println();

		//Eliminar un estudio de la base de datos.
//		boolean eliminarEstudio = databaseManager.borrarEstudio(4);
//		System.out.println("Eliminar estudio:" + eliminarEstudio);
//		System.out.println();

		//Eliminar una serie de la base de datos.
		boolean eliminarSerie = databaseManager.borrarSerie(23);
		System.out.println("Eliminar serie: " + eliminarSerie);
		System.out.println();

		//Eliminar una película de la base de datos.
//		boolean eliminarPelicula = databaseManager.borrarPelicula(12);
//		System.out.println("Eliminar película: " + eliminarPelicula);
//		System.out.println();

//		Editar un estudio, serie o película de la base de datos.
//		boolean editar = databaseManager.editar(serie);
//		System.out.println("Editar: " + editar);
//		System.out.println();

		
		//Exportar estudios a documento XML.
		String	ruta = "/home/usuario/Estudios.xml";
		System.out.println("La tabla estudio se ha exportado a documento XML.");
		databaseManager.exportarEstudios(ruta);
		System.out.println();
		
		//Exportar series a documento XML.
		ruta = "/home/usuario/Series.xml";
		System.out.println("La tabla serie se ha exportado a documento XML.");
		databaseManager.exportarSeries(ruta);
		System.out.println();

		//Exportar peliculas a documento XML.
		ruta = "/home/usuario/Peliculas.xml";
		System.out.println("La tabla película se ha exportado a documento XML.");
		databaseManager.exportarPeliculas(ruta);

	}
}