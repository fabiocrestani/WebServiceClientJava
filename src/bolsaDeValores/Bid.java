package bolsaDeValores;

/**
 * Classe para tratamento de lances de compra e venda.
 * 
 * @author Fabio
 *
 */
public class Bid {
	/**
	 * Tipo de lance.
	 * 
	 * @author Fabio
	 *
	 */
	public enum BidType {
		SELL, BUY;
	}

	/**
	 * Status do lance no servidor.
	 * 
	 * @author Fabio
	 *
	 */
	public enum BidStatus {
		PENDING, DONE, SENT_TO_CLIENT;
	}

	private String stockName;
	private double quantity;
	private double negotiatedPrice;
	private BidType type;
	private int clientId;
	private BidStatus status;

	/**
	 * Construtor
	 * 
	 * @param name
	 *            Nome da ação.
	 * @param price
	 *            Preço negociado.
	 * @param quantity
	 *            Quantidade negociada.
	 * @param type
	 *            Tipo (compra ou venda).
	 * @param clientId
	 *            id do cliente.
	 */
	public Bid(String name, double price, double quantity, BidType type, int clientId) {
		this.stockName = name;
		this.negotiatedPrice = price;
		this.quantity = quantity;
		this.type = type;
		this.clientId = clientId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getNegotiatedPrice() {
		return negotiatedPrice;
	}

	public void setNegotiatedPrice(double negotiatedPrice) {
		this.negotiatedPrice = negotiatedPrice;
	}

	public BidType getType() {
		return type;
	}

	public void setType(BidType type) {
		this.type = type;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public BidStatus getStatus() {
		return status;
	}

	public void setStatus(BidStatus status) {
		this.status = status;
	}
}
