package dam.tema8.proyecto;

import java.util.Objects;

public class Pelicula {
	public static final String CAMPOS = "id_pelicula, nombre_pelicula, duracion_minutos";
	private int id;
	private String nombre;
	private int duracionMinutos;
	private Estudio idEstudio;
	
	public Pelicula(int id, String nombre, int duracionMinutos, Estudio idEstudio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.duracionMinutos = duracionMinutos;
		this.idEstudio = idEstudio;
	}

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

	public int getDuracionMinutos() {
		return duracionMinutos;
	}

	public void setDuracionMinutos(int duracionMinutos) {
		this.duracionMinutos = duracionMinutos;
	}

	public Estudio getIdEstudio() {
		return idEstudio;
	}

	public void setIdEstudio(Estudio idEstudio) {
		this.idEstudio = idEstudio;
	}

	@Override
	public String toString() {
		return "Pelicula [id=" + id + ", nombre=" + nombre + ", duracionMinutos=" + duracionMinutos + ", idEstudio="
				+ idEstudio + "]";
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
		Pelicula other = (Pelicula) obj;
		return id == other.id && Objects.equals(nombre, other.nombre);
	}

	
	
	
	
	
}
