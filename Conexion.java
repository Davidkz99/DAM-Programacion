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
	private Connection connection;
	public boolean conectar(String url) {		
		try {
			//cargar el driver
			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());			

			//crear un objeto de conexión
			this.connection = DriverManager.getConnection(url);		
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return connection==null?false:true;
	}

	public boolean desconectar() {
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

	public boolean estaConectado() {
		try {
			return !this.connection.isClosed();
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
	}
}
