package proyecto_fundamentos_programacion;

public class Sale {

	private Product product;
	private int quantity;

	public Sale(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getTotal() {
		return product.getPrice() * quantity;
	}

	@Override
	public String toString() {
		return product.getId() + ";" + quantity;
	}
}
