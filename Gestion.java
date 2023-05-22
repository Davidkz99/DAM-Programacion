package dam.tema8.proyecto;
//Importación de paquetes para la gestión de la base de datos.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
//Importación de paquetes para la exportación de la base de datos
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
/**
 * Clase para gestionar una base de datos.
 * @author David
 */
public class Gestion {
	private Conexion conexion=null;
	//Apartado 1
	/**
	 * Constructor especializado en inicializar objetos de tipo Gestión 
	 * a partir de un objeto de conexión que no puede ser nulo.
	 * @param Objeto de tipo conexión.
	 */
	public Gestion(Conexion conexion) {
		this.conexion = conexion;
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
		Connection connection=null;
		PreparedStatement pStatement=null;
		ResultSet resultSet;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión.
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto.
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return estudios;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("SELECT id_estudio, nombre_estudio FROM estudio");
			//Realizo la query.
			resultSet = pStatement.executeQuery();
			estudios = new ArrayList<Estudio>();
			//Itero sobre los resultados y los voy agregando al array.
			while(resultSet.next()) {
				estudios.add(new Estudio(resultSet.getInt(1),
						resultSet.getString(2)));
			}
			//Cierro el flujo.
			resultSet.close();
			pStatement.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return estudios;
	}

	/**
	 * Método especializado para poder guardar en un ArrayList 
	 * todas las series pertenecientes a la base de datos.
	 * @return Todas las series que pertenezcan a la base de datos.
	 */
	public ArrayList<Serie> getSeries() {
		Estudio estudio = null;
		ArrayList<Serie> series = null;
		Connection connection=null;
		PreparedStatement psSerie = null;
		PreparedStatement psEstudio=null;
		ResultSet rsSerie;
		ResultSet rsEstudio;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return series;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			psSerie = connection.prepareStatement("SELECT id_serie, nombre_serie, "
					+ "numero_episodios, id_estudio FROM serie");
			//Realizo la query.
			rsSerie = psSerie.executeQuery();
			series = new ArrayList<Serie>();
			//Itero sobre los resultados a la vez que realizo una query secundaria en la que buscaré
			//y añadiré a la serie los valores que corresponden a cierto id_estudio.
			while(rsSerie.next()) {
				psEstudio = connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));
				rsEstudio = psEstudio.executeQuery();
	            //Verifico si se obtuvo un resultado de estudio.
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				//Añado al array una nueva serie
				series.add(new Serie(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						//El objeto estudio contendrá el id que coincida en las tablas serie y estudio,
						//y el nombre del estudio.
						estudio));
				//Cierro el flujo secundario
				psEstudio.close();
				rsEstudio.close();
			}
			//Cierro el flujo principal
			psSerie.close();
			rsSerie.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return series;
	}

	/**
	 * Método especializado para poner guardar en un ArrayList 
	 * todas las películas pertenecientes a la base de datos.
	 * @return Todas las películas que pertenezcan a la base de datos.
	 */
	public ArrayList<Pelicula> getPeliculas(){
		Estudio estudio = null;
		ArrayList<Pelicula> peliculas = null;
		Connection connection=null;
		PreparedStatement psPelicula = null;
		PreparedStatement psEstudio=null;
		ResultSet rsPelicula;
		ResultSet rsEstudio;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return peliculas;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			psPelicula = connection.prepareStatement("SELECT id_pelicula, nombre_pelicula, duracion_minutos, "
					+ "id_estudio FROM pelicula");
			//Realizo la query.
			rsPelicula = psPelicula.executeQuery();
			peliculas = new ArrayList<Pelicula>();
			//Itero sobre los resultados a la vez que realizo una query secundaria en la que buscaré
			//y añadiré a la película los valores que corresponden a cierto id_estudio.
			while(rsPelicula.next()) {
				psEstudio = connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsPelicula.getInt(4));
				rsEstudio = psEstudio.executeQuery();
	            //Verifico si se obtuvo un resultado de estudio.
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				//Añado al array una nueva película
				peliculas.add(new Pelicula(rsPelicula.getInt(1),
						rsPelicula.getString(2),
						rsPelicula.getInt(3),
						//El objeto estudio contendrá el id que coincida en las tablas película y estudio,
						//y el nombre del estudio.
						estudio));
				//Cierro el flujo secundario
				psEstudio.close();
				rsEstudio.close();
			}
			//Cierro el flujo principal
			psPelicula.close();
			rsPelicula.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return peliculas;
	}

	//Apartado 2.2
	/**
	 * Método especializado para poder guardar en un ArrayList 
	 * todos los estudios filtrados por un HashMap pertenecientes a la base de datos.
	 * @return Todos los estudios que pertenezcan a la base de datos.
	 */
	public ArrayList<Estudio> getEstudios(HashMap<String,Object> filter) {
		ArrayList<Estudio> estudiosFiltrados = null;
		int i=0, type=Types.VARCHAR;
		String whereData="";
		Connection connection=null;
		PreparedStatement pStatement = null;
		ResultSet rs;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return estudiosFiltrados;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Bucle for que permite guardar en un objeto de tipo String
			//la manera en la que voy a filtrar la búsqueda de resultados.
			for(String key:filter.keySet()) {
				whereData += key + "=? AND ";
			}
			//Al terminar el bucle, sobran cinco caracteres que son: " AND ", por lo que los quitaré.
			whereData = whereData.substring(0, whereData.length()-5);
			//Preparo la query.
			pStatement = connection.prepareStatement("SELECT id_estudio, nombre_estudio FROM estudio WHERE " + whereData);

			//Bucle for que me permite comprobar el tipo de dato que se encuentra
			//en el objeto de tipo HashMap. Si el valor es de alguno de algún tipo,
			//se asigna el tipo que corresponda a la variable 'type'.
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
				pStatement.setObject(++i, value, type);				
			}
			//Realizo la query.
			rs = pStatement.executeQuery();
			estudiosFiltrados = new ArrayList<Estudio>();
			//Itero sobre los resultados y los voy agregando al array.
			while(rs.next()) {
				estudiosFiltrados.add(new Estudio(rs.getInt(1),
						rs.getString(2)));
			}
			//Cierro el flujo.
			pStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return estudiosFiltrados;
	}

	/**
	 * Método especializado para poder mostrar un ArrayList
	 * de series de la base de datos filtradas al menos por dos campos.
	 * @param Objeto de tipo HashMap que constituye de:
	 * 		  String: Columna de la tabla por la que desees filtrar.
	 * 		  Object: Valor ubicado en la columna especificada.
	 * @return Todas las series filtradas según el tipo de filtrado que obtenga de objeto de tipo HashMap.
	 */
	public ArrayList<Serie> getSeries(HashMap<String,Object> filter) {
		ArrayList<Serie> seriesFiltradas = null;
		int i=0, type=Types.VARCHAR;
		String whereData="";
		Estudio estudio = null;
		Connection connection=null;
		PreparedStatement psSerie = null;
		PreparedStatement psEstudio=null;
		ResultSet rsSerie;
		ResultSet rsEstudio;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return seriesFiltradas;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Bucle for que permite recorrer el HashMap, guardándolo en un objeto de tipo String.
			for(String key:filter.keySet()) {
				whereData += key + "=? AND ";
			}
			//Al terminar el bucle, sobran cinco caracteres que son: " AND ", por lo que los quitaré.
			whereData = whereData.substring(0, whereData.length()-5);
			//Preparo la query.
			psSerie = connection.prepareStatement("SELECT id_serie, nombre_serie, numero_episodios, "
					+ "id_estudio FROM serie WHERE " + whereData);

			//Bucle para comprobar el tipo de dato que se encuentra en el objeto de tipo HashMap. 
			//Si el valor es de alguno de algún tipo, se asigna el tipo que corresponda a la variable 'type'.
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
				psSerie.setObject(++i, value, type);				
			}
			//Realizo la query.
			rsSerie = psSerie.executeQuery();
			seriesFiltradas = new ArrayList<Serie>();
			//Itero sobre los resultados a la vez que realizo una query secundaria en la que buscaré
			//y añadiré a la serie los valores que corresponden a cierto id_estudio.
			while(rsSerie.next()) {
				psEstudio = connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));
				rsEstudio = psEstudio.executeQuery();
	            //Verifico si se obtuvo un resultado de estudio.
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				//Añado al array una nueva serie
				seriesFiltradas.add(new Serie(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						//El objeto estudio contendrá el id que coincida en las tablas serie y estudio,
						//y el nombre del estudio.
						estudio));
				//Cierro el flujo secundario.
				psEstudio.close();
				rsEstudio.close();
			}
			//Cierro el flujo principal.
			psSerie.close();
			rsSerie.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return seriesFiltradas;
	}

	/**
	 * Método especializado para poder mostrar un ArrayList
	 * de películas de la base de datos filtradas al menos por dos campos.
	 * @param Objeto de tipo HashMap que constituye de:
	 * 		  String: Columna de la tabla por la que desees filtrar.
	 * 		  Object: Valor ubicado en la columna especificada.
	 * @return Todas las películas filtradas según el tipo de filtrado que obtenga de objeto de tipo HashMap.
	 */
	public ArrayList<Pelicula> getPeliculas(HashMap<String,Object> filter) {
		ArrayList<Pelicula> peliculasFiltradas = null;
		int i=0, type=Types.VARCHAR;
		String whereData="";
		Estudio estudio = null;
		Connection connection=null;
		PreparedStatement psPelicula = null;
		PreparedStatement psEstudio=null;
		ResultSet rsPelicula;
		ResultSet rsEstudio;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return peliculasFiltradas;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Bucle for que permite recorrer el HashMap, guardándolo en un objeto de tipo String.
			for(String key:filter.keySet()) {
				whereData += key + "=? AND ";
			}
			//Al terminar el bucle, sobran cinco caracteres que son: " AND ", por lo que los quitaré.
			whereData = whereData.substring(0, whereData.length()-5);
			//Preparo la query.
			psPelicula = connection.prepareStatement("SELECT id_pelicula, nombre_pelicula, duracion_minutos, "
					+ "id_estudio FROM pelicula WHERE " + whereData);
			
			//Bucle para comprobar el tipo de dato que se encuentra en el objeto de tipo HashMap. 
			//Si el valor es de alguno de algún tipo, se asigna el tipo que corresponda a la variable 'type'.
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
				psPelicula.setObject(++i, value, type);				
			}
			//Realizo la query.
			rsPelicula = psPelicula.executeQuery();
			peliculasFiltradas = new ArrayList<Pelicula>();
			//Itero sobre los resultados a la vez que realizo una query secundaria en la que buscaré
			//y añadiré a la película los valores que corresponden a cierto id_estudio.
			while(rsPelicula.next()) {
				psEstudio = connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsPelicula.getInt(4));
				rsEstudio = psEstudio.executeQuery();
	            //Verifico si se obtuvo un resultado de estudio.
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				//Añado al array una nueva película
				peliculasFiltradas.add(new Pelicula(rsPelicula.getInt(1),
						rsPelicula.getString(2),
						rsPelicula.getInt(3),
						//El objeto estudio contendrá el id que coincida en las tablas película y estudio,
						//y el nombre del estudio.
						estudio));
				//Cierro el flujo secundario
				psEstudio.close();
				rsEstudio.close();
			}
			//Cierro el flujo principal
			psPelicula.close();
			rsPelicula.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return peliculasFiltradas;
	}

	//Apartado 2.3
	//Se pueden obtener los datos de una consulta de manera ordenada por alguno de los campos seleccionados
	/**
	 * Método especializado para obtener todas los estudios de un estudio en concreto ordenadas.
	 * @param Objeto de tipo Ordenacion que contendrá 
	 * el número de la columna que queremos obtener seguido 
	 * de si lo queremos mostrar de manera ascendente o descendente
	 * @return Todas los estudios ordenados.
	 */
	public ArrayList<Estudio> getEstudios(Ordenacion... columnOrder) {
		ArrayList<Estudio> estudiosOrdenados = new ArrayList<Estudio>();
		String ordenacion=" ORDER BY ";
		String consulta = "SELECT id_estudio, nombre_estudio FROM estudio";
		Connection connection=null;
		PreparedStatement psEstudio=null;
		ResultSet resultSet;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return estudiosOrdenados;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Bucle for para aladir al objeto de tipo String 
			//el número de la columna y el método de ordenación
			for(Ordenacion column:columnOrder) {
				ordenacion+=column.getIndex() + " " + column.getOrder() + ",";
			}
			//Condición para comprobar la longitud final del string y concatenarlo a la consulta.
			if(ordenacion.length()>10) {
	            //Elimino la última coma de la cláusula de ordenación.
				ordenacion = ordenacion.substring(0,ordenacion.length()-1);
				consulta+=ordenacion;
			}
			//Preparo la query.
			psEstudio = connection.prepareStatement(consulta);
			//Realizo la query
			resultSet = psEstudio.executeQuery();
			//Itero sobre los resultados a la vez que realizo una query secundaria en la que buscaré
			//y añadiré a la serie los valores que corresponden a cierto id_estudio.
			while(resultSet.next()) {
				estudiosOrdenados.add(new Estudio(resultSet.getInt(1),
						resultSet.getString(2)));
			}
			//Cierro el flujo principal.
			resultSet.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return estudiosOrdenados;
	}
	
	/**
	 * Método especializado para obtener todas las series de un estudio en concreto ordenadas.
	 * @param Id del estudio que se tiene de referencia para buscar películas
	 * @param Objeto de tipo Ordenacion que contendrá 
	 * el número de la columna que queremos obtener seguido 
	 * de si lo queremos mostrar de manera ascendente o descendente
	 * @return Todas las series que coincidan con la id del estudio aplicando el objeto de tipo Ordenación.
	 */
	public ArrayList<Serie> getSeries(String id_estudio, Ordenacion... columnOrder) {
		ArrayList<Serie> seriesOrdenadas = new ArrayList<Serie>();
		Estudio estudio = null;
		String ordenacion=" ORDER BY ";
		String consulta = "SELECT id_serie, nombre_serie, numero_episodios, id_estudio FROM serie "
				+ "WHERE id_estudio=" + id_estudio + "";
		Connection connection=null;
		PreparedStatement psSerie=null;
		PreparedStatement psEstudio=null;
		ResultSet rsSerie;
		ResultSet rsEstudio;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return seriesOrdenadas;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Bucle for para aladir al objeto de tipo String 
			//el número de la columna y el método de ordenación
			for(Ordenacion column:columnOrder) {
				ordenacion+=column.getIndex() + " " + column.getOrder() + ",";
			}
			//Condición para comprobar la longitud final del string y concatenarlo a la consulta.
			if(ordenacion.length()>10) {
	            //Elimino la última coma de la cláusula de ordenación.
				ordenacion = ordenacion.substring(0,ordenacion.length()-1);
				consulta+=ordenacion;
			}
			//Preparo la query.
			psSerie = connection.prepareStatement(consulta);
			//Realizo la query
			rsSerie = psSerie.executeQuery();
			//Itero sobre los resultados a la vez que realizo una query secundaria en la que buscaré
			//y añadiré a la serie los valores que corresponden a cierto id_estudio.
			while(rsSerie.next()) {
				psEstudio = connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsSerie.getInt(4));
				rsEstudio = psEstudio.executeQuery();
	            //Verifico si se obtuvo un resultado de estudio.
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				//Añado al array una nueva serie
				seriesOrdenadas.add(new Serie(rsSerie.getInt(1),
						rsSerie.getString(2),
						rsSerie.getInt(3),
						//El objeto estudio contendrá el id que coincida en las tablas serie y estudio,
						//y el nombre del estudio.
						estudio));
				//Cierro el flujo secundario.
				psEstudio.close();
				rsEstudio.close();
			}
			//Cierro el flujo principal.
			psSerie.close();
			rsSerie.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		return seriesOrdenadas;
	}

	/**
	 * Método especializado para obtener todas las películas de un estudio en concreto ordenadas.
	 * @param Id del estudio que se tiene de referencia para buscar series
	 * @param Objeto de tipo Ordenacion que contendrá 
	 * el número de la columna que queremos obtener seguido 
	 * de si lo queremos mostrar de manera ascendente o descendente
	 * @return Todas las películas que coincidan con la id del estudio aplicando el objeto de tipo Ordenación.
	 */
	public ArrayList<Pelicula> getPeliculas(String id_estudio, Ordenacion... columnOrder) {
		ArrayList<Pelicula> peliculasOrdenadas = new ArrayList<Pelicula>();
		Estudio estudio = null;
		String ordenacion=" ORDER BY ";
		String consulta = "SELECT id_pelicula, nombre_pelicula, duracion_minutos, id_estudio FROM pelicula "
				+ "WHERE id_estudio=" + id_estudio + "";
		Connection connection=null;
		PreparedStatement psPelicula=null;
		PreparedStatement psEstudio=null;
		ResultSet rsPelicula;
		ResultSet rsEstudio;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo el array vacío.
			if(!this.conexion.estaConectado()) return peliculasOrdenadas;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Bucle for para aladir al objeto de tipo String 
			//el número de la columna y el método de ordenación
			for(Ordenacion column:columnOrder) {
				ordenacion+=column.getIndex() + " " + column.getOrder() + ",";
			}
			//Condición para comprobar la longitud final del string y concatenarlo a la consulta.
			if(ordenacion.length()>10) {
	            //Elimino la última coma de la cláusula de ordenación.
				ordenacion = ordenacion.substring(0,ordenacion.length()-1);
				consulta+=ordenacion;
			}
			//Preparo la query.
			psPelicula = connection.prepareStatement(consulta);
			//Realizo la query.
			rsPelicula = psPelicula.executeQuery();
			//Itero sobre los resultados a la vez que realizo una query secundaria en la que buscaré
			//y añadiré a la película los valores que corresponden a cierto id_estudio.
			while(rsPelicula.next()) {
				psEstudio = connection.prepareStatement("SELECT * FROM estudio where id_estudio=?");
				psEstudio.setInt(1, rsPelicula.getInt(4));
				rsEstudio = psEstudio.executeQuery();
	            //Verifico si se obtuvo un resultado de estudio.
				if(rsEstudio.next()) {
					estudio = new Estudio(rsEstudio.getInt(1), rsEstudio.getString(2));
				}
				//Añado al array una nueva película.
				peliculasOrdenadas.add(new Pelicula(rsPelicula.getInt(1),
						rsPelicula.getString(2),
						rsPelicula.getInt(3),
						//El objeto estudio contendrá el id que coincida en las tablas película y estudio,
						//y el nombre del estudio.
						estudio));
				//Cierro el flujo secundario.
				psEstudio.close();
				rsEstudio.close();
			}
			//Cierro el flujo principal.
			psPelicula.close();
			rsPelicula.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
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
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("UPDATE estudio SET nombre_estudio= '" + 
					estudio.getNombre() + "' WHERE id_estudio=" + estudio.getId());
			//Si se realizó la query, el valor es true.
			editado= pStatement.executeUpdate()>0;
			//Cierro el flujo
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
		return editado;
	}

	/**
	 * Método especializado para editar un registro de tipo serie
	 * ubicado en la tabla de la base de datos.
	 * @param Objeto de tipo serie que vamos a editar.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean editar(Serie serie) {
		boolean editado = false;
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("UPDATE serie SET nombre_serie= '" + serie.getNombre() +
					"',numero_episodios= " + serie.getNumEpisodios() + ",id_estudio= " + serie.getId_estudio() +
					" WHERE id_serie=" + serie.getId());
			//Si se realizó la query, el valor es true.
			editado= pStatement.executeUpdate()>0;
			//Cierro el flujo
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
		return editado;
	}

	/**
	 * Método especializado para editar un registro de tipo película
	 * ubicado en la tabla de la base de datos.
	 * @param Objeto de tipo película que vamos a editar.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean editar(Pelicula pelicula) {
		boolean editado = false;
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("UPDATE pelicula SET nombre_pelicula= '" + pelicula.getNombre() +
					"',duracion_minutos= " + pelicula.getDuracionMinutos() + ",id_estudio= " + pelicula.getId_estudio() +
					" WHERE id_pelicula=" + pelicula.getId());
			//Si se realizó la query, el valor es true.
			editado= pStatement.executeUpdate()>0;
			//Cierro el flujo.
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
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
	public boolean crear(Estudio estudio) {
		boolean añadido=false;
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("INSERT INTO estudio (id_estudio, "
					+ "nombre_estudio) VALUES(?,?)");
			//Guardo el resultado que corresponde al objeto que le he pasado al método según la columna.
			pStatement.setInt(1, estudio.getId());
			pStatement.setString(2, estudio.getNombre());
			//Si se realizó la query, el valor es true.
			añadido = pStatement.executeUpdate()>0;
			//Cierro el flujo.
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
		return añadido;
	}

	/**
	 * Método especializado para añadir registros de tipo serie
	 * a su correspondiente tabla ubicada en la base de datos.
	 * @param Objeto de tipo serie para que pueda añadirlo.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean crear(Serie serie) {
		boolean añadido=false;
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("INSERT INTO serie (id_serie, nombre_serie,"
					+ "numero_episodios, id_estudio) VALUES(?,?,?,?)");
			//Guardo el resultado que corresponde al objeto que le he pasado al método según la columna.
			pStatement.setInt(1, serie.getId());
			pStatement.setString(2, serie.getNombre());
			pStatement.setInt(3, serie.getNumEpisodios());
			pStatement.setInt(4, serie.getId_estudio());
			//Si se realizó la query, el valor es true.
			añadido = pStatement.executeUpdate()>0;
			//Cierro el flujo.
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
		return añadido;
	}

	/**
	 * Método especializado para añadir registros de tipo película
	 * a su correspondiente tabla ubicada en la base de datos.
	 * @param Objeto de tipo película que vamos a añadir.
	 * @return Mensaje validando o no la acción del método.
	 */
	public boolean crear(Pelicula pelicula) {
		boolean añadido=false;
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("INSERT INTO pelicula (id_pelicula, nombre_pelicula,"
					+ "duracion_minutos, id_estudio) VALUES(?,?,?,?)");
			//Guardo el resultado que corresponde al objeto que le he pasado al método según la columna.
			pStatement.setInt(1, pelicula.getId());
			pStatement.setString(2, pelicula.getNombre());
			pStatement.setInt(3, pelicula.getDuracionMinutos());
			pStatement.setInt(4, pelicula.getId_estudio());
			//Si se realizó la query, el valor es true.
			añadido = pStatement.executeUpdate()>0;
			//Cierro el flujo.
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
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
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("DELETE FROM estudio WHERE id_estudio=" + id);
			//Si se realizó la query, el valor es true.
			borrado = pStatement.executeUpdate()>0;
			//Cierro el flujo.
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
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
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("DELETE FROM serie WHERE id_serie=" + id);
			//Si se realizó la query, el valor es true.
			borrado = pStatement.executeUpdate()>0;
			//Cierro el flujo.
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
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
		Connection connection=null;
		PreparedStatement pStatement=null;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en la clase de conexión
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta, en este caso devuelvo false en el boolean.
			if(!this.conexion.estaConectado()) return false;
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Preparo la query.
			pStatement = connection.prepareStatement("DELETE FROM pelicula WHERE id_pelicula=" + id);
			//Si se realizó la query, el valor es true.
			borrado = pStatement.executeUpdate()>0;
			//Cierro el flujo.
			pStatement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
		//Devuelve falso o verdadero.
		return borrado;			
	}
	
	//Apartado 2.9
	//Se podrán exportar datos de una tabla desde hacia un fichero XML, 
	//no tiene porque ser todos los datos de la tabla.
	/**
	 * Método especializado en exportar la tabla de estudios de la base de datos.
	 * Podemos exportar toda la tabla indicando todas sus columnas
	 * o únicamente las columnas que queramos con la clase Element
	 * @param Ruta donde se ubica el archivo.xml
	 */
	public void exportarEstudios(String ruta) {
		Connection connection=null;
		DocumentBuilderFactory documentBuilderFactory;
		DocumentBuilder documentBuilder;
		Document document;
		Element raiz;
		PreparedStatement pStatement=null;
		ResultSet resultSet;
		DOMSource source;
		Transformer transformer;
		StreamResult result;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en el gestor de conexión.
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto.
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta.
			if(!this.conexion.estaConectado());
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Creo una instancia del DocumentBuilder.
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			//Creo un nuevo documento.
			document = documentBuilder.newDocument();
			//Creo el elemento raíz del documento.
			raiz = document.createElement("Estudios");
			//Con el método appendChild, podremos añadir la raíz al documento.
			document.appendChild(raiz);
			//Preparo la query.
			pStatement = connection.prepareStatement("SELECT * FROM estudio");
			//Realizo la query.
			resultSet = pStatement.executeQuery();
			//Mientras el resultset tenga registro, insertarlo en el documento.
			while (resultSet.next()) {
				//Creo un elemento al documento, que serán las filas.
				Element fila = document.createElement("fila");
				//El elemento raíz llamado película dispondrá de filas para clasificar las películas.
				raiz.appendChild(fila);
				//Obtengo el valor correspondiente a la columna indicada, de la tabla pelicula.
				String columna1 = resultSet.getString("id_estudio");
				//Creo un nuevo elemento en el documento, que será la etiqueta
				//que dentro contendrá el valor que le corresponde.
				Element element1 = document.createElement("id_estudio");
				//Asigno el valor al elemento en el documento.
				element1.appendChild(document.createTextNode(columna1));
				//Finalmente, añado la columna con su valor insertado.
				fila.appendChild(element1);
				String columna2 = resultSet.getString("nombre_estudio");
				Element element2 = document.createElement("nombre_estudio");
				element2.appendChild(document.createTextNode(columna2));
				fila.appendChild(element2);
			}
			//Cierro el flujo
			resultSet.close();
			pStatement.close();
			connection.close();
			//Creo el objeto Transformer para escribir el documento XML en un archivo.
			//Objeto de la clase DOMSource que intermediará entre el transformador y el árbol DOM.
			source = new DOMSource(document);
			//Creo una nueva instancia del transformador a través de la fábrica de transformadores.
			transformer = TransformerFactory.newInstance().newTransformer();
			//Condición para saber si el archivo que le pasamos al método contiene la extensión .xml
			if (ruta.contains(".xml")) {
				//Objeto de la clase StreamResult, que intermediará entre el transformador 
				//y el archivo de destino
				result = new StreamResult(new File(ruta));
				transformer.transform(source, result);
			} else {
				//Si no contiene la extensión .xml, se la añado.
				ruta  = ruta.concat(".xml");
				result = new StreamResult(new File(ruta));
				//Esbribo el contenido del documento XML en el archivo.
				transformer.transform(source, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
	}
	
	/**
	 * Método especializado en exportar la tabla de series de la base de datos.
	 * Podemos exportar toda la tabla indicando todas sus columnas
	 * o únicamente las columnas que queramos con la clase Element
	 * @param Ruta donde se ubica el archivo.xml
	 */
	public void exportarSeries(String ruta) {
		Connection connection=null;
		DocumentBuilderFactory documentBuilderFactory;
		DocumentBuilder documentBuilder;
		Document document;
		Element raiz;
		PreparedStatement pStatement=null;
		ResultSet resultSet;
		DOMSource source;
		Transformer transformer;
		StreamResult result;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en el gestor de conexión.
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto.
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta.
			if(!this.conexion.estaConectado());
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Creo una instancia del DocumentBuilder.
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			//Creo un nuevo documento.
			document = documentBuilder.newDocument();
			//Creo el elemento raíz del documento.
			raiz = document.createElement("Series");
			//Con el método appendChild, podremos añadir la raíz al documento.
			document.appendChild(raiz);
			//Preparo la query.
			pStatement = connection.prepareStatement("SELECT * FROM serie");
			//Realizo la query.
			resultSet = pStatement.executeQuery();
			//Mientras el resultset tenga registro, insertarlo en el documento.
			while (resultSet.next()) {
				//Creo un elemento al documento, que serán las filas.
				Element fila = document.createElement("fila");
				//El elemento raíz llamado película dispondrá de filas para clasificar las películas.
				raiz.appendChild(fila);
				//Obtengo el valor correspondiente a la columna indicada, de la tabla pelicula.
				String columna1 = resultSet.getString("id_serie");
				//Creo un nuevo elemento en el documento, que será la etiqueta
				//que dentro contendrá el valor que le corresponde.
				Element element1 = document.createElement("id_serie");
				//Asigno el valor al elemento en el documento.
				element1.appendChild(document.createTextNode(columna1));
				//Finalmente, añado la columna con su valor insertado.
				fila.appendChild(element1);
				String columna2 = resultSet.getString("nombre_serie");
				Element element2 = document.createElement("nombre_serie");
				element2.appendChild(document.createTextNode(columna2));
				fila.appendChild(element2);
				String columna3 = resultSet.getString("numero_episodios");
				Element element3 = document.createElement("numero_episodios");
				element3.appendChild(document.createTextNode(columna3));
				fila.appendChild(element3);
				String columna4 = resultSet.getString("id_estudio");
				Element element4 = document.createElement("id_estudio");
				element4.appendChild(document.createTextNode(columna4));
				fila.appendChild(element4);
			}
			//Cierro el flujo
			resultSet.close();
			pStatement.close();
			//Creo el objeto Transformer para escribir el documento XML en un archivo.
			//Objeto de la clase DOMSource que intermediará entre el transformador y el árbol DOM.
			source = new DOMSource(document);
			//Creo una nueva instancia del transformador a través de la fábrica de transformadores.
			transformer = TransformerFactory.newInstance().newTransformer();
			//Condición para saber si el archivo que le pasamos al método contiene la extensión .xml
			if (ruta.contains(".xml")) {
				//Objeto de la clase StreamResult, que intermediará entre el transformador 
				//y el archivo de destino
				result = new StreamResult(new File(ruta));
				transformer.transform(source, result);
			} else {
				//Si no contiene la extensión .xml, se la añado.
				ruta  = ruta.concat(".xml");
				result = new StreamResult(new File(ruta));
				//Esbribo el contenido del documento XML en el archivo.
				transformer.transform(source, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
	}
	
	/**
	 * Método especializado en exportar la tabla de películas de la base de datos.
	 * Podemos exportar toda la tabla indicando todas sus columnas
	 * o únicamente las columnas que queramos con la clase Element
	 * @param Ruta donde se ubica el archivo.xml
	 */
	public void exportarPeliculas(String ruta) {
		Connection connection=null;
		DocumentBuilderFactory documentBuilderFactory;
		DocumentBuilder documentBuilder;
		Document document;
		Element raiz;
		PreparedStatement pStatement=null;
		ResultSet resultSet;
		DOMSource source;
		Transformer transformer;
		StreamResult result;
		try {
			//Se abre la conexión usando la cadena de conexión guardada en el gestor de conexión.
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			//Se guarda el objeto de conexión una vez abierto.
			this.conexion.conectar(connection);
			//Compruebo si la conexión no está abierta.
			if(!this.conexion.estaConectado());
			//Obtengo la conexión establecida
			connection = this.conexion.getConnection();
			//Creo una instancia del DocumentBuilder.
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			//Creo un nuevo documento.
			document = documentBuilder.newDocument();
			//Creo el elemento raíz del documento.
			raiz = document.createElement("Películas");
			//Con el método appendChild, podremos añadir la raíz al documento.
			document.appendChild(raiz);
			//Preparo la query.
			pStatement = connection.prepareStatement("SELECT * FROM pelicula");
			//Realizo la query.
			resultSet = pStatement.executeQuery();
			//Mientras el resultset tenga registro, insertarlo en el documento.
			while (resultSet.next()) {
				//Creo un elemento al documento, que serán las filas.
				Element fila = document.createElement("fila");
				//El elemento raíz llamado película dispondrá de filas para clasificar las películas.
				raiz.appendChild(fila);
				//Obtengo el valor correspondiente a la columna indicada, de la tabla pelicula.
				String columna1 = resultSet.getString("id_pelicula");
				//Creo un nuevo elemento en el documento, que será la etiqueta
				//que dentro contendrá el valor que le corresponde.
				Element element1 = document.createElement("id_pelicula");
				//Asigno el valor al elemento en el documento.
				element1.appendChild(document.createTextNode(columna1));
				//Finalmente, añado la columna con su valor insertado.
				fila.appendChild(element1);
				String columna2 = resultSet.getString("nombre_pelicula");
				Element element2 = document.createElement("nombre_pelicula");
				element2.appendChild(document.createTextNode(columna2));
				fila.appendChild(element2);
				String columna3 = resultSet.getString("duracion_minutos");
				Element element3 = document.createElement("duracion_minutos");
				element3.appendChild(document.createTextNode(columna3));
				fila.appendChild(element3);
				String columna4 = resultSet.getString("id_estudio");
				Element element4 = document.createElement("id_estudio");
				element4.appendChild(document.createTextNode(columna4));
				fila.appendChild(element4);
			}
			//Cierro el flujo
			resultSet.close();
			pStatement.close();
			//Creo el objeto Transformer para escribir el documento XML en un archivo.
			//Objeto de la clase DOMSource que intermediará entre el transformador y el árbol DOM.
			source = new DOMSource(document);
			//Creo una nueva instancia del transformador a través de la fábrica de transformadores.
			transformer = TransformerFactory.newInstance().newTransformer();
			//Condición para saber si el archivo que le pasamos al método contiene la extensión .xml
			if (ruta.contains(".xml")) {
				//Objeto de la clase StreamResult, que intermediará entre el transformador 
				//y el archivo de destino
				result = new StreamResult(new File(ruta));
				transformer.transform(source, result);
			} else {
				//Si no contiene la extensión .xml, se la añado.
				ruta  = ruta.concat(".xml");
				result = new StreamResult(new File(ruta));
				//Esbribo el contenido del documento XML en el archivo.
				transformer.transform(source, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//Finalmente cierro la conexión.
			this.conexion.disconnect();
		}
	}
	
	/**
	 * Método para importar en una tabla existente los datos de una tabla guardados en un archivo xml.
	 * @param filePath
	 */
	public boolean importarEstudios(String filePath) {
		boolean importado = false;
		Connection connection=null;
		PreparedStatement pStatement;
        try {
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			this.conexion.conectar(connection);
			if(!this.conexion.estaConectado());
			connection = this.conexion.getConnection();
			//Creo una instancia del DocumentBuilder.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Creo un objeto de tipo Document que contiene la ruta.
            Document document = builder.parse(new File(filePath));
            //Obtengo una lista de elementos del documento.
            NodeList nodeList = document.getElementsByTagName("fila");
            //Itero sobre los elementos
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                //Obtengo los valores de las columnas del elemento.
                //El método getTextContent devuelve el contenido de texto del nodo actual.
                String columna1 = element.getElementsByTagName("id_estudio").item(0).getTextContent();
                String columna2 = element.getElementsByTagName("nombre_estudio").item(0).getTextContent();
                pStatement = connection.prepareStatement("INSERT INTO ImporteEstudio (id_estudio, "
                		+ "nombre_estudio) VALUES(?,?)");
                pStatement.setString(1, columna1);
                pStatement.setString(2, columna2);
                importado = pStatement.executeUpdate()>0;
                pStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.conexion.disconnect();
        }
		return importado;
    }
	
	public boolean importarSeries(String filePath) {
		boolean importado = false;
		Connection connection=null;
		PreparedStatement pStatement;
        try {
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			this.conexion.conectar(connection);
			if(!this.conexion.estaConectado());
			connection = this.conexion.getConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            NodeList nodeList = document.getElementsByTagName("fila");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String columna1 = element.getElementsByTagName("id_serie").item(0).getTextContent();
                String columna2 = element.getElementsByTagName("nombre_serie").item(0).getTextContent();
                String columna3 = element.getElementsByTagName("numero_episodios").item(0).getTextContent();
                String columna4 = element.getElementsByTagName("id_estudio").item(0).getTextContent();
                pStatement = connection.prepareStatement("INSERT INTO ImporteSerie (id_serie, "
                		+ "nombre_serie, numero_episodios, id_estudio) VALUES(?,?,?,?)");
                pStatement.setString(1, columna1);
                pStatement.setString(2, columna2);
                pStatement.setString(3, columna3);
                pStatement.setString(4, columna4);

                importado = pStatement.executeUpdate()>0;
                pStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.conexion.disconnect();
        }
		return importado;
    }
	
	public boolean importarPeliculas(String filePath) {
		boolean importado = false;
		Connection connection=null;
		PreparedStatement pStatement;
        try {
			connection = DriverManager.getConnection(this.conexion.getConnectionString());
			this.conexion.conectar(connection);
			if(!this.conexion.estaConectado());
			connection = this.conexion.getConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            NodeList nodeList = document.getElementsByTagName("fila");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String columna1 = element.getElementsByTagName("id_pelicula").item(0).getTextContent();
                String columna2 = element.getElementsByTagName("nombre_pelicula").item(0).getTextContent();
                String columna3 = element.getElementsByTagName("duracion_minutos").item(0).getTextContent();
                String columna4 = element.getElementsByTagName("id_estudio").item(0).getTextContent();
                pStatement = connection.prepareStatement("INSERT INTO ImportePelicula (id_pelicula, "
                		+ "nombre_pelicula, duracion_minutos, id_estudio) VALUES(?,?,?,?)");
                pStatement.setString(1, columna1);
                pStatement.setString(2, columna2);
                pStatement.setString(3, columna3);
                pStatement.setString(4, columna4);
                importado = pStatement.executeUpdate()>0;
                pStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.conexion.disconnect();
        }
		return importado;
    }
}