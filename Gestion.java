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
import java.util.ArrayList;

import dam.tema5.actividades.NonNull;

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
		ArrayList<Serie> serie = null;
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			PreparedStatement psSerie = this.connection.prepareStatement("SELECT id_serie, nombre_serie, numero_episodios, id_estudio FROM serie");
			ResultSet rsSerie = psSerie.executeQuery();
			serie = new ArrayList<Serie>();
			while(rsSerie.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));

				rsEstudio = psEstudio.executeQuery();

				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}

				serie.add(new Serie(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						estudio));
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return serie;
	}
	
	//Método para poner guardar en un ArrayList todas las películas
	public ArrayList<Pelicula> getPeliculas(){
		ArrayList<Pelicula> serie = null;
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			PreparedStatement psSerie = this.connection.prepareStatement("SELECT id_pelicula, nombre_pelicula, duracion_minutos, id_estudio FROM pelicula");
			ResultSet rsSerie = psSerie.executeQuery();
			serie = new ArrayList<Pelicula>();
			while(rsSerie.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));

				rsEstudio = psEstudio.executeQuery();

				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}

				serie.add(new Pelicula(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						estudio));
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return serie;
	}
	
	
}