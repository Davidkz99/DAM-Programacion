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
	
	/**
	 * Constructor de la clase Conexion.
	 * @param connectionString
	 */
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
	 * Método para establecer una conexión a la base de datos.
	 * 
	 * @param connection La conexión a establecer.
	 * @return true si la conexión se estableció correctamente, false en caso contrario.
	 */
	public boolean conectar(Connection connection) {		
		this.connection = connection;
		return this.connection==null?false:true;
	}

	/**
	 * Método para cerrar la conexión a la base de datos.
	 * 
	 * @return true si la conexión se cerró correctamente, false en caso contrario.
	 */
	public boolean disconnect() {
		try {
			if(this.connection==null) return true;
			this.connection.close();
			return true;
		} catch (SQLException e) {			
			return false;
		}
	}
	/**
	 * Método getter para obtener la conexión actual.
	 * 
	 * @return La conexión actual.
	 */
	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * Método para verificar si hay una conexión establecida.
	 * 
	 * @return true si hay una conexión establecida, false en caso contrario.
	 */
	public boolean estaConectado() {
		try {
			return !this.connection.isClosed();
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Método para obtener la cadena de conexión a la base de datos.
	 * 
	 * @return La cadena de conexión a la base de datos.
	 */
	public String getConnectionString() {
		return this.connectionString;
	}
}
