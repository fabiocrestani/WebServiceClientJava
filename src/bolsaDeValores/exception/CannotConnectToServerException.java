package bolsaDeValores.exception;

/**
 * Exceção usada para sinalizar que não foi possível conectar ao servidor.
 * 
 * @author fabio
 *
 */
public class CannotConnectToServerException extends Exception {
	private static final long serialVersionUID = -235091359864644028L;
	private String message;

	public CannotConnectToServerException(String string) {
		this.message = string;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		return message;
	}
}
