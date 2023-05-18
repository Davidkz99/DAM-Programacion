package dam.tema8.proyecto;

public class Estudio {
	public static final String CAMPOS = "id_estudio, nombre_estudio";
	private int id;
	private String nombre;
	public Estudio(int id) {
		super();
		this.id = id;
	}
	public Estudio(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return "Estudio [id=" + id + ", nombre=" + nombre + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estudio other = (Estudio) obj;
		return id == other.id;
	}
	
	
}
