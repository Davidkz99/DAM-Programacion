package dam.tema8.proyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Clase para habilitar la conexión a nuestra base de datos.
 * @author David
 *
 */
public class DataBaseConnection {
	private Connection connection;
	public boolean connect() {		
		try {
			//cargar el driver
			DriverManager.registerDriver (new com.mysql.cj.jdbc.Driver());			
			
			//crear un objeto de conexión
			this.connection = 
					DriverManager.getConnection("jdbc:mysql://localhost/anime?user=root&password=root");			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return connection==null?false:true;
	}
	public boolean disconnect() {
		try {
			this.connection.close();
			return true;
		} catch (SQLException e) {			
			return false;
		}
	}
	public Connection getConnection() {
		return this.connection;
	}
	public boolean isConnected() {
		try {
			return !this.connection.isClosed();
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
	}
}
