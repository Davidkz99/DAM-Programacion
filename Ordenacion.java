package dam.tema8.proyecto;

public class Ordenacion {
	private int index;
	private String order;
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