package dam.tema8.proyecto;

import java.util.Objects;

public class Pelicula {
	private int id;
	private String nombre;
	private int duracionMinutos;
	private Estudio id_estudio;
	
	public Pelicula(int id, String nombre, int duracionMinutos, Estudio id_estudio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.duracionMinutos = duracionMinutos;
		this.id_estudio = id_estudio;
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

	public Estudio getId_estudio() {
		return id_estudio;
	}

	public void setId_estudio(Estudio id_estudio) {
		this.id_estudio = id_estudio;
	}

	@Override
	public String toString() {
		return "Pelicula [id=" + id + ", nombre=" + nombre + ", duracionMinutos=" + duracionMinutos + ", id_estudio="
				+ id_estudio + "]";
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
		return id == other.id;
	}
	
}
