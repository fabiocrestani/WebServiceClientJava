package bolsaDeValores;

import bolsaDeValores.menu.ClientMenu;
import bolsaDeValores.webServiceConsumer.WebServiceConsumer;

/**
 * Classe principal.
 * 
 * @author Fabio
 *
 */
public class BolsaDeValoresClient {
	private final static String SERVER_ADDRESS = "localhost";
	private final static int SERVER_PORT = 2222;
	private final static String SERVICE_NAME = "servidorBolsaDeValores";

	/**
	 * main. Inicia o serviço do cliente.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		WebServiceConsumer.setServer(SERVER_ADDRESS, SERVER_PORT, SERVICE_NAME);
		Client client = null;
		try {
			client = new Client();
			client.start();
			ClientMenu.start(client);
		} catch (Exception e) {
			System.out.println("Erro ao executar cliente: " + e.getMessage());
		}
	}
}
