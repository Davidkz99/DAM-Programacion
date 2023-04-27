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
	public boolean conectar() {		
		try {
			//cargar el driver
			DriverManager.registerDriver (new com.mysql.cj.jdbc.Driver());			

			//crear un objeto de conexión
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost/anime?user=root&password=root");			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return connection==null?false:true;
	}
	
}
