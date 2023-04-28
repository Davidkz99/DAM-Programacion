package dam.tema8.proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dam.tema5.actividades.NonNull;
import dam.tema8.Book;
import dam.tema8.Drug;

public class DataBaseManager {
	private Connection connection=null;
	private Statement statement=null;
	//	private boolean drugsUpdated=false;
	//	private ArrayList<Drug> drugsData;
	/**
	 * Constructor especializado en inicializar objetos
	 * de tipo DatabaseManager a partir de un objeto de conexión
	 * que no puede ser nulo
	 * @param connection Objeto de conexión
	 */
	public DataBaseManager(@NonNull Connection connection) {
		this.connection = connection;
		try {
			this.statement = connection.createStatement();
			//this.drugsData = new ArrayList<Drug>();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}

	public ArrayList<Serie> getSeries(){
		ArrayList<Serie> series = null;
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			PreparedStatement psSerie = this.connection.prepareStatement("SELECT id,nombre,numEpisodios,id_estudio FROM series");
			ResultSet rsSerie = psSerie.executeQuery();
			series = new ArrayList<Serie>();
			while(rsSerie.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id=?");
				psEstudio.setInt(1, rsSerie.getInt(4));
				
				rsEstudio = psEstudio.executeQuery();
				
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(0), rsEstudio.getString(1));
				}
				
				series.add(new Serie(rsSerie.getInt(1),
									rsSerie.getString(2),
									rsSerie.getInt(3),
									estudio));
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return series;
	}
}