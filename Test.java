package dam.tema8.proyecto;

import dam.tema8.DatabaseConnection;
import dam.tema8.DatabaseManager;

public class Test {
	public static void main(String[] args) {
		DataBaseConnection conexion = new DataBaseConnection();

		if (conexion.conectar("jdbc:mysql://localhost/Anime?user=root&password=usuario")) {
			DatabaseManager databaseManager = new DatabaseManager(conexion.getConnection());

			System.out.println(conexion.estaConectado());
		}
	}
}