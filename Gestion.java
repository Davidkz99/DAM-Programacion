package dam.tema8.proyecto;
/**
 * Clase para gestionar las búsquedas en una base de datos.
 * @author David
 */
import java.rmi.AlreadyBoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import dam.tema5.actividades.NonNull;

public class Gestion {
	private Connection connection=null;
	private Statement statement=null;
	private PreparedStatement pStatement=null;
	
	//Apartado 1
	/**
	 * Constructor especializado en inicializar objetos
	 * de tipo DatabaseManager a partir de un objeto de conexión
	 * que no puede ser nulo.
	 * @param Objeto de tipo conexión.
	 */
	public Gestion(@NonNull Connection connection) {
		this.connection = connection;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido conectar.");
				System.exit(0);
			}
			this.statement = connection.createStatement();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		System.out.print("La conexión a la base de datos es: ");
	}

	//Apartado 2.1
	//Se debe poder consultar los datos de cualquiera de las tablas de la bbdd

	/**
	 * Método especializado para poder guardar en un ArrayList 
	 * todos los estudios pertenecientes a la base de datos.
	 * @return Todos los estudios que pertenezcan a la base de datos.
	 */
	public ArrayList<Estudio> getEstudios() {
		ArrayList<Estudio> estudios = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			psEstudio = this.connection.prepareStatement("SELECT id_estudio, nombre_estudio FROM estudio");
			rsEstudio = psEstudio.executeQuery();
			estudios = new ArrayList<Estudio>();
			while(rsEstudio.next()) {
				estudios.add(new Estudio(rsEstudio.getInt(1),
						rsEstudio.getString(2)));
			}
			this.statement.close();
//			this.connection.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		System.out.println("Mostrando todos los estudios:");
		return estudios;
	}

	/**
	 * Método especializado para poder guardar en un ArrayList 
	 * todas las series pertenecientes a la base de datos.
	 * @return Todas las series que pertenezcan a la base de datos.
	 */
	public ArrayList<Serie> getSeries(){
		ArrayList<Serie> series = null;
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
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
		System.out.println("Mostrando todas las series:");
		return series;
	}

	/**
	 * Método especializado para poner guardar en un ArrayList 
	 * todas las películas pertenecientes a la base de datos.
	 * @return Todas las películas que pertenezcan a la base de datos.
	 */
	public ArrayList<Pelicula> getPeliculas(){
		ArrayList<Pelicula> peliculas = null;
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			PreparedStatement psSerie = this.connection.prepareStatement("SELECT id_pelicula, nombre_pelicula, duracion_minutos, id_estudio FROM pelicula");
			ResultSet rsSerie = psSerie.executeQuery();
			peliculas = new ArrayList<Pelicula>();
			while(rsSerie.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));
				rsEstudio = psEstudio.executeQuery();
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				peliculas.add(new Pelicula(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						estudio));
			}
			this.statement.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		System.out.println("Mostrando todas las películas:");
		return peliculas;
	}

	//Apartado 2.2
	//Método para mostrar la tabla Serie filtrada por al menos 2 campos
	/**
	 * Método especializado para poder mostrar un ArrayList
	 * de series de la base de datos filtradas al menos por dos campos.
	 * @param Objeto de tipo HashMap que constituye de:
	 * 		  String: Columna de la tabla por la que desees filtrar.
	 * 		  Object: Valor ubicado en la columna especificada.
	 * @return Todas las series filtradas según el tipo de filtrado que obtenga de objeto de tipo HashMap.
	 * @throws Excepción debido a que es posible que los valores que se han insertado
	 * en el objeto de tipo HashMap no coinciden con los de la tabla de la base de datos.
	 */
	public ArrayList<Serie> getSeries(HashMap<String,Object> filter) throws Exception{
		ArrayList<Serie> seriesFiltradas = null;
		int i=0, type=Types.VARCHAR;
		String whereData="";
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			for(String key:filter.keySet()) {
				whereData += key + "=? AND ";
			}
			whereData = whereData.substring(0, whereData.length()-5);
			PreparedStatement ps = this.connection.
					prepareStatement("SELECT id_serie, nombre_serie, numero_episodios, id_estudio FROM serie WHERE " + whereData);

			for(Object value:filter.values()) {
				if(value instanceof Integer) {
					type = Types.INTEGER;
				}else if(value instanceof Float) {
					type = Types.FLOAT;
				}else if(value instanceof Double) {
					type = Types.DOUBLE;
				}else if(value instanceof String) {
					type = Types.VARCHAR;
				}
				ps.setObject(++i, value, type);				
			}

			ResultSet rs = ps.executeQuery();
			seriesFiltradas = new ArrayList<Serie>();
			while(rs.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rs.getInt(4));
				rsEstudio = psEstudio.executeQuery();
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				seriesFiltradas.add(new Serie(rs.getInt(1),
						rs.getString(2),
						rs.getInt(3),
						estudio));
			}
		} catch (Exception e) {			
			throw new Exception("Error al filtrar la tabla. Es posible que una o varias columnas que has"
					+ " insertado no coincidan con las de la base de datos.");
		}
		System.out.println("Mostrando series filtradas:");
		return seriesFiltradas;
	}

	/**
	 * Método especializado para poder mostrar un ArrayList
	 * de películas de la base de datos filtradas al menos por dos campos.
	 * @param Objeto de tipo HashMap que constituye de:
	 * 		  String: Columna de la tabla por la que desees filtrar.
	 * 		  Object: Valor ubicado en la columna especificada.
	 * @return Todas las películas filtradas según el tipo de filtrado que obtenga de objeto de tipo HashMap.
	 * @throws Excepción debido a que es posible que los valores que se han insertado
	 * en el objeto de tipo HashMap no coinciden con los de la tabla de la base de datos.
	 */
	public ArrayList<Pelicula> getPeliculas(HashMap<String,Object> filter) throws Exception{
		ArrayList<Pelicula> peliculasFiltradas = null;
		int i=0, type=Types.VARCHAR;
		String whereData="";
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			for(String key:filter.keySet()) {
				whereData += key + "=? AND ";
			}
			whereData = whereData.substring(0, whereData.length()-5);
			PreparedStatement ps = this.connection.
					prepareStatement("SELECT id_pelicula, nombre_pelicula, duracion_minutos, id_estudio FROM pelicula WHERE " + whereData);

			for(Object value:filter.values()) {
				if(value instanceof Integer) {
					type = Types.INTEGER;
				}else if(value instanceof Float) {
					type = Types.FLOAT;
				}else if(value instanceof Double) {
					type = Types.DOUBLE;
				}else if(value instanceof String) {
					type = Types.VARCHAR;
				}
				ps.setObject(++i, value, type);				
			}

			ResultSet rs = ps.executeQuery();
			peliculasFiltradas = new ArrayList<Pelicula>();
			while(rs.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rs.getInt(4));
				rsEstudio = psEstudio.executeQuery();
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				peliculasFiltradas.add(new Pelicula(rs.getInt(1),
						rs.getString(2),
						rs.getInt(3),
						estudio));
			}
		} catch (Exception e) {			
			throw new Exception("Error al filtrar la tabla. Es posible que una o varias columnas que has"
					+ " insertado no coincidan con las de la base de datos.");
		}
		System.out.println("Mostrando películas filtradas:");
		return peliculasFiltradas;
	}

	//Apartado 2.3
	//Se pueden obtener los datos de una consulta de manera ordenada por alguno de los campos seleccionados
	/**
	 * 
	 * @param Id del estudio que se tiene de referencia para buscar películas
	 * @param Objeto de tipo Ordenacion que contendrá 
	 * el número de la columna que queremos obtener seguido 
	 * de si lo queremos mostrar de manera ascendente o descendente
	 * @return Todas las series que coincidan con la id del estudio aplicando el objeto de tipo Ordenación.
	 * @throws Excepción indicando que el número de columnas puede ser erróneo en caso de que
	 * insertemos un número superior a las existentes en la base de datos. También puede significar
	 * que hayamos escrito mal el método de ordenación, ya que debe ser "ASC" o "DESC".
	 */
	public ArrayList<Serie> getSeries(String id_estudio, Ordenacion... columnOrder) throws Exception{
		ArrayList<Serie> seriesOrdenadas = new ArrayList<Serie>();
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		String ordenacion=" ORDER BY ";
		String consulta = "SELECT id_serie, nombre_serie, numero_episodios, id_estudio FROM serie WHERE id_estudio=" + id_estudio + "";
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			//creamos un objeto de consulta 
			this.statement = this.connection.createStatement();

			//Asegurar la ejecución de la consulta
			//si no hay parámetros para la ordenación							
			//Recoger TODAS las columnas de ordenación e 
			//incluirlas en la consulta
			for(Ordenacion column:columnOrder) {
				ordenacion+=column.getIndex() + " " + column.getOrder() + ",";
			}
			if(ordenacion.length()>10) {
				ordenacion = ordenacion.substring(0,ordenacion.length()-1);
				consulta+=ordenacion;
			}

			//Aquí hacer un bucle para recorrer el column order 
			//y que el valor que le pase no sea mayor que el numero de columnas existentes

			//obtengo el conjunto de resultados porque se que
			//la sentencia ejecutada los ha producido (SELECT			
			ResultSet resultSet = statement.executeQuery(consulta);
			while(resultSet.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, resultSet.getInt(4));
				rsEstudio = psEstudio.executeQuery();
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				seriesOrdenadas.add(new Serie(resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getInt(3),
						estudio));
			}
			this.statement.close();
		} catch (Exception e) {			
			throw new Exception("El número de columnas o el método de ordenación es erróneo. Vuelve a intentarlo.");
		}
		System.out.println("Mostrando series ordenadas:");
		return seriesOrdenadas;
	}

	/**
	 * 
	 * @param Id del estudio que se tiene de referencia para buscar series
	 * @param Objeto de tipo Ordenacion que contendrá 
	 * el número de la columna que queremos obtener seguido 
	 * de si lo queremos mostrar de manera ascendente o descendente
	 * @return Todas las películas que coincidan con la id del estudio aplicando el objeto de tipo Ordenación.
	 * @throws Excepción indicando que el número de columnas puede ser erróneo en caso de que
	 * insertemos un número superior a las existentes en la base de datos. También puede significar
	 * que hayamos escrito mal el método de ordenación, ya que debe ser "ASC" o "DESC".
	 */
	public ArrayList<Pelicula> getPeliculas(String id_estudio, Ordenacion... columnOrder) throws Exception{
		ArrayList<Pelicula> peliculasOrdenadas = new ArrayList<Pelicula>();
		Estudio estudio = null;
		PreparedStatement psEstudio=null;
		ResultSet rsEstudio;
		String ordenacion=" ORDER BY ";
		String consulta = "SELECT id_serie, nombre_pelicula, duracion_minutos, id_estudio FROM pelicula WHERE id_estudio=" + id_estudio + "";
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			//creamos un objeto de consulta 
			this.statement = this.connection.createStatement();

			//Asegurar la ejecución de la consulta
			//si no hay parámetros para la ordenación							
			//Recoger TODAS las columnas de ordenación e 
			//incluirlas en la consulta
			for(Ordenacion column:columnOrder) {
				ordenacion+=column.getIndex() + " " + column.getOrder() + ",";
			}
			if(ordenacion.length()>10) {
				ordenacion = ordenacion.substring(0,ordenacion.length()-1);
				consulta+=ordenacion;
			}

			//Aquí hacer un bucle para recorrer el column order 
			//y que el valor que le pase no sea mayor que el numero de columnas existentes

			//obtengo el conjunto de resultados porque se que
			//la sentencia ejecutada los ha producido (SELECT			
			ResultSet resultSet = statement.executeQuery(consulta);
			while(resultSet.next()) {
				psEstudio = this.connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, resultSet.getInt(4));
				rsEstudio = psEstudio.executeQuery();
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				peliculasOrdenadas.add(new Pelicula(resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getInt(3),
						estudio));
			}
			this.statement.close();
		} catch (Exception e) {			
			throw new Exception("El número de columnas o el método de ordenación es erróneo. Vuelve a intentarlo.");
		}
		System.out.println("Mostrando series ordenadas:");
		return peliculasOrdenadas;
	}

	//Apartado 2.4
	//Se debe poder modificar cualquiera de los registros de una tabla.
	/**
	 * Método especializado para editar un registro de tipo estudio
	 * ubicado en la tabla de la base de datos.
	 * @param Objeto de tipo estudio que vamos a editar.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean editar(Estudio estudio) {
		boolean editado = false;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("UPDATE estudio SET nombre_estudio= '" + 
					estudio.getNombre() + "' WHERE id_estudio=" + estudio.getId());
			editado= this.pStatement.executeUpdate()>0;			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.print("Editar estudio: ");
		return editado;
	}

	/**
	 * Método especializado para editar un registro de tipo serie
	 * ubicado en la tabla de la base de datos.
	 * @param Objeto de tipo serie que vamos a editar.
	 * @return Mensaje validando o no la acción del método.
	 * @throws Excepción de tipo NullPointerException debido a que el objeto contiene una id de estudio
	 * que no coincide en la base de datos.
	 */
	public boolean editar(Serie serie) throws Exception{
		boolean editado = false;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("UPDATE serie SET nombre_serie= '" + serie.getNombre() +
					"',numero_episodios= " + serie.getNumEpisodios() + ",id_estudio= " + serie.getId_estudio() +
					" WHERE id_serie=" + serie.getId());
			editado= this.pStatement.executeUpdate()>0;			
		}catch(SQLException e) {
			throw new NullPointerException("Error al editar un registro. "
					+ "Se está intentando editar un registro cuya id de estudio no existe en la base de datos.");
		}
		System.out.print("Editar serie: ");
		return editado;
	}

	/**
	 * Método especializado para editar un registro de tipo película
	 * ubicado en la tabla de la base de datos.
	 * @param Objeto de tipo película que vamos a editar.
	 * @return Mensaje validando o no la acción del método.
	 * @throws Excepción de tipo NullPointerException debido a que el objeto contiene una id de estudio
	 * que no coincide en la base de datos.
	 */
	public boolean editar(Pelicula pelicula) throws Exception {
		boolean editado = false;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("UPDATE pelicula SET nombre_pelicula= '" + pelicula.getNombre() +
					"',duracion_minutos= " + pelicula.getDuracionMinutos() + ",id_estudio= " + pelicula.getId_estudio() +
					" WHERE id_pelicula=" + pelicula.getId());
			editado= this.pStatement.executeUpdate()>0;			
		}catch(SQLException e) {
			throw new NullPointerException("Error al editar un registro. "
					+ "Se está intentando editar un registro cuya id de estudio no existe en la base de datos.");
		}
		System.out.print("Editar película: ");
		return editado;
	}

	//Apartado 2.5
	//Se debe poder añadir nuevos registros a una tabla, de uno en uno o varios.
	/**
	 * Método especializado para añadir registros de tipo estudio
	 * a su correspondiente tabla ubicada en la base de datos.
	 * @param Objeto de tipo estudio para que pueda añadirlo.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean crear(Estudio estudio) throws Exception {
		boolean añadido=false;
		try {			
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("INSERT INTO estudio (id_estudio, "
					+ "nombre_estudio) VALUES(?,?)");
			this.pStatement.setInt(1, estudio.getId());
			this.pStatement.setString(2, estudio.getNombre());
			añadido = this.pStatement.executeUpdate()>0;
		}catch(SQLException e) {
			throw new AlreadyBoundException("Error al añadir un registro. "
					+ "Constructor del estudio incorrecto o id ya existente en la base de datos.");
		}
		System.out.print("Crear estudio: ");
		return añadido;
	}

	/**
	 * Método especializado para añadir registros de tipo serie
	 * a su correspondiente tabla ubicada en la base de datos.
	 * @param Objeto de tipo serie para que pueda añadirlo.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean crear(Serie serie) throws Exception {
		boolean añadido=false;
		try {			
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("INSERT INTO serie (id_serie, nombre_serie,"
					+ "numero_episodios, id_estudio) VALUES(?,?,?,?)");
			this.pStatement.setInt(1, serie.getId());
			this.pStatement.setString(2, serie.getNombre());
			this.pStatement.setInt(3, serie.getNumEpisodios());
			this.pStatement.setInt(4, serie.getId_estudio());

			añadido = this.pStatement.executeUpdate()>0;
		}catch(SQLException e) {
			throw new AlreadyBoundException("Error al añadir un registro. "
					+ "Constructor del estudio incorrecto o id ya existente en la base de datos.");
		}
		System.out.print("Crear serie: ");
		return añadido;
	}

	/**
	 * Método especializado para añadir registros de tipo película
	 * a su correspondiente tabla ubicada en la base de datos.
	 * @param Objeto de tipo película que vamos a añadir.
	 * @return Mensaje validando o no la acción del método.
	 * @throws Excepción de tipo AlreadyBoundException 
	 */
	public boolean crear(Pelicula pelicula) throws Exception {
		boolean añadido=false;
		try {			
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("INSERT INTO pelicula (id_pelicula, nombre_pelicula,"
					+ "duracion_minutos, id_estudio) VALUES(?,?,?,?)");
			this.pStatement.setInt(1, pelicula.getId());
			this.pStatement.setString(2, pelicula.getNombre());
			this.pStatement.setInt(3, pelicula.getDuracionMinutos());
			this.pStatement.setInt(4, pelicula.getId_estudio());

			añadido = this.pStatement.executeUpdate()>0;
		}catch(SQLException e) {
			throw new AlreadyBoundException("Error al añadir un registro. "
					+ "Constructor del estudio incorrecto o id ya existente en la base de datos.");
		}
		System.out.print("Crear película: ");
		return añadido;
	}

	//Apartado 2.6
	//Se debe poder eliminar registros de una tabla, de uno en uno o varios.
	/**
	 * Método especializado para eliminar de uno en uno
	 * registros de tipo estudio ubicados en la base de datos.
	 * @param Id al que esté asignado el objeto de tipo estudio.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean borrarEstudio(int id) {
		boolean borrado=false;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("DELETE FROM estudio WHERE id_estudio=" + id);
			borrado = this.pStatement.executeUpdate()>0;
		}catch(SQLException e) {
			e.printStackTrace();
			return borrado;
		}
		System.out.print("Borrar estudio: ");
		return borrado;			
	}

	/**
	 * Método especializado para eliminar de uno en uno
	 * registros de tipo serie ubicados en la base de datos.
	 * @param Id al que esté asignado el objeto de tipo serie.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean borrarSerie(int id) {
		boolean borrado=false;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("DELETE FROM serie WHERE id_serie=" + id);
			borrado = this.pStatement.executeUpdate()>0;	
		}catch(SQLException e) {
			e.printStackTrace();
			return borrado;
		}
		System.out.print("Borrar serie: ");
		return borrado;			
	}

	/**
	 * Método especializado para eliminar de uno en uno
	 * registros de tipo película ubicados en la base de datos.
	 * @param Id al que esté asignado el objeto de tipo película.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean borrarPelicula(int id) {
		boolean borrado=false;
		try {
			if(this.connection.isClosed()) {
				System.out.println("No se ha podido realizar la consulta.");
				System.exit(0);
			}
			this.pStatement = this.connection.prepareStatement("DELETE FROM pelicula WHERE id_pelicula=" + id);
			borrado = this.pStatement.executeUpdate()>0;
		}catch(SQLException e) {
			e.printStackTrace();
			return borrado;
		}
		System.out.print("Borrar película: ");
		return borrado;			
	}

}