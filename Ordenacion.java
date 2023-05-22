package dam.tema8.proyecto;
/**
 * Clase para hacer referencia a campos de ordenación
 * @author David
 */
public class Ordenacion {
	private int index;
	private String order;
	/**
	 * Constructor para la creación de objetos de tipo Ordenacion.
	 * @param index Número de la columna de la tabla.
	 * @param order Manera en la que vamos a ordenar, ya sea ASC o DESC.
	 */
	public Ordenacion(int index, String order) {
		this.index = index;
		this.order = order;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "Ordenacion [index=" + index + ", order=" + order + "]";
	}
	
	
}