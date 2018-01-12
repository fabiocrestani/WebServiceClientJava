package bolsaDeValores.exception;

/**
 * Exceção utilizada para sinalizar que não foi possível enviar ordem ao
 * servidor.
 * 
 * @author fabio
 *
 */
public class CannotPlaceOrder extends Exception {
	private static final long serialVersionUID = -8976215450695559651L;

	protected String message;

	public CannotPlaceOrder(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
