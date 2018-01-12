package bolsaDeValores;

/**
 * Classe para armazenar os dados das ações do usuário.
 * 
 * @author Fabio
 *
 */
public class Stock {
	private String name;
	private double price;
	private double quantity;

	/**
	 * Construtor.
	 * 
	 * @param name
	 *            Nome da ação.
	 * @param price
	 *            Preço atual.
	 * @param quantity
	 *            Quantidade possuída.
	 */
	public Stock(String name, double price, double quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return name + "   R$ " + (String.format("%.2f", price)) + "\t" + (String.format("%.2f", quantity));
	}
}
