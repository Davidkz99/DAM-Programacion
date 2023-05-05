package dam.tema8.proyecto;

/**
 * Clase para gestionar las búsquedas en una base de datos.
 * @author David
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import dam.tema5.actividades.NonNull;
import dam.tema8.Book;

public class Gestion {
	private Connection connection=null;
	private Statement statement=null;
	/**
	 * Constructor especializado en inicializar objetos
	 * de tipo DatabaseManager a partir de un objeto de conexión
	 * que no puede ser nulo
	 * @param connection Objeto de conexión
	 */
	public Gestion(@NonNull Connection connection) {
		this.connection = connection;
		try {
			this.statement = connection.createStatement();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}

	//Método para poder guardar en un ArrayList todas las series
	public ArrayList<Serie> getSeries(){
		ArrayList<Serie> series = null;
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			PreparedStatement psSerie = this.connection.prepareStatement("SELECT id_serie, nombre_serie, numero_episodios, id_estudio FROM serie");
			ResultSet rsSerie = psSerie.executeQuery();
			series = new ArrayList<Serie>();
			while(rsSerie.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));

				rsEstudio = psEstudio.executeQuery();

				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				series.add(new Serie(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						estudio));
			}
			this.statement.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return series;
	}

	//Método para poner guardar en un ArrayList todas las películas
	public ArrayList<Pelicula> getPeliculas(){
		ArrayList<Pelicula> series = null;
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			PreparedStatement psSerie = this.connection.prepareStatement("SELECT id_pelicula, nombre_pelicula, duracion_minutos, id_estudio FROM pelicula");
			ResultSet rsSerie = psSerie.executeQuery();
			series = new ArrayList<Pelicula>();
			while(rsSerie.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));

				rsEstudio = psEstudio.executeQuery();

				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				series.add(new Pelicula(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						estudio));
			}
			this.statement.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return series;
	}
	
	//Método para mostrar la tabla Serie filtrada por un campo
//	public ArrayList<Serie> getSeries(HashMap<String,String> filter){
//		ArrayList<Serie> series = null;
//		Estudio estudio = null;
//		PreparedStatement psEstudio=null;
//		ResultSet rsEstudio;
//		int i=0, type=Types.VARCHAR;
//		String whereData="";
//		try {
//			for(String key:filter.keySet()) {
//				whereData += key + "=? AND ";
//				//Busco los campos que me pasan para el where en los campos que tiene la tabla series
//				//si algun campo no está, el metodo termina, devolviendo null;
//				if (Serie.CAMPOS.indexOf(key)< 0) {
//					return series;
//				} else {
//					return null;
//				}
//			}
//			
//			whereData = whereData.substring(0, whereData.length()-5);
//			PreparedStatement psSerie = this.connection.prepareStatement("SELECT id_serie, nombre_serie, "
//					+ "numero_episodios, id_estudio FROM serie WHERE " + whereData);
//	
//			for(Object value:filter.values()) {
//				if(value instanceof Integer) {
//					type = Types.INTEGER;
//				}else if(value instanceof Float) {
//					type = Types.FLOAT;
//				}else if(value instanceof Double) {
//					type = Types.DOUBLE;
//				}else if(value instanceof String) {
//					type = Types.VARCHAR;
//				}
//				psSerie.setObject(++i, value, type);				
//			}
//			
//			ResultSet rsSerie = psSerie.executeQuery();
//			series = new ArrayList<Serie>();
//			while(rsSerie.next()) {
//				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where " + whereData);
//				psEstudio.setInt(1, rsSerie.getInt(4));
//
//				rsEstudio = psEstudio.executeQuery();
//
//				if(rsEstudio.next()) {
//					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
//				}
//				series.add(new Serie(rsSerie.getInt(1),
//						rsSerie.getString(2),
//						rsSerie.getInt(3),
//						estudio));
//			}
//			this.statement.close();
//		} catch (SQLException e) {			
//			e.printStackTrace();
//		}
//		return series;
//	}
	
	//Método para obtener los datos de una consulta de manera ordenada por alguno de los campos seleccionados
	
	//Método para modificar registros en una tabla
	
	//Método para añadir registros a la tabla de uno en uno
	
	//Método para eliminar registros a la tabla de uno en uno

}