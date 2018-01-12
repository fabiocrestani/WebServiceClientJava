package bolsaDeValores.webServiceConsumer;

/**
 * Classe usada para passar mensagens de controle entre cliente e servidor.
 * 
 * @author Fabio
 *
 */
public class ControlMessage {
	/**
	 * Mensagens de controle disponíveis.
	 * 
	 * @author Fabio
	 *
	 */
	public enum ControlMessageCode {
		NACK, ACK
	}

	private ControlMessageCode code;

	public ControlMessage() {
	}

	public ControlMessage(int code) {
		if (code == 0) {
			this.code = ControlMessageCode.ACK;
		} else if (code == 1) {
			this.code = ControlMessageCode.NACK;
		}
	}

	public ControlMessageCode getCode() {
		return code;
	}

	public void setCode(ControlMessageCode code) {
		this.code = code;
	}
}