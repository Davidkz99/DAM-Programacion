package dam.tema8.proyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para habilitar la conexión a nuestra base de datos.
 * @author David
 *
 */
public class Conexion {
	private Connection connection = null;
	private String connectionString;
	
	public Conexion(String connectionString) {		
		//guarda los datos de conexión
		this.connectionString = connectionString;
		try {
			//registrar el controlador
			DriverManager.registerDriver (new com.mysql.cj.jdbc.Driver());
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	/**
	 * Guarda el objeto de conexión
	 * @param connection
	 * @return Si es distinto de null devuelve true
	 */
	public boolean conectar(Connection connection) {		
		this.connection = connection;
		return this.connection==null?false:true;
	}

	public boolean disconnect() {
		try {
			if(this.connection==null) return true;
			this.connection.close();
			return true;
		} catch (SQLException e) {			
			return false;
		}
	}
	public Connection getConnection() {
		return this.connection;
	}
	public boolean estaConectado() {
		try {
			return !this.connection.isClosed();
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
	}
	public String getConnectionString() {
		return this.connectionString;
	}
}
