package dam.tema8.proyecto;

import java.util.Objects;
/**
 * Clase que hace referencia a la tabla Serie en la base de datos.
 * @author David
 *
 */
public class Serie {
	public static final String CAMPOS = "id_serie, nombre_serie, numero_episodios"; //Hacer split para recoger los campos existentes en la tabla
	private int id;
	private String nombre;
	private int numEpisodios;
	private Estudio estudio;
	
	/**
	 * Constructor para la creación de objetos de tipo serie.
	 */
	public Serie(int id, String nombre, int numEpisodios, Estudio idEstudio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.numEpisodios = numEpisodios;
		this.estudio = idEstudio;
	}

	//Métodos setter y getter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNumEpisodios() {
		return numEpisodios;
	}

	public void setNumEpisodios(int numEpisodios) {
		this.numEpisodios = numEpisodios;
	}
	
	public int getId_estudio() {
		return this.estudio.getId();
	}

	public void setId_estudio(Estudio id_estudio) {
		this.estudio = id_estudio;
	}

	@Override
	public String toString() {
		return "Serie [id=" + id + ", nombre=" + nombre + ", numEpisodios=" + numEpisodios + ", estudio=" + estudio
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Serie other = (Serie) obj;
		return id == other.id && Objects.equals(nombre, other.nombre);
	}

	
	
	
}
