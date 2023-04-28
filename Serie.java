package dam.tema8.proyecto;

import java.util.Objects;

public class Serie {
	private int id;
	private String nombre;
	private int numEpisodios;
	private Estudio idEstudio;
	
	public Serie(int id, String nombre, int numEpisodios, Estudio idEstudio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.numEpisodios = numEpisodios;
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

	public int getNumEpisodios() {
		return numEpisodios;
	}

	public void setNumEpisodios(int numEpisodios) {
		this.numEpisodios = numEpisodios;
	}
	
	public Estudio getId_estudio() {
		return idEstudio;
	}

	public void setId_estudio(Estudio id_estudio) {
		this.idEstudio = id_estudio;
	}

	@Override
	public String toString() {
		return "Serie [id=" + id + ", nombre=" + nombre + ", numEpisodios=" + numEpisodios + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return id == other.id;
	}
	
}
