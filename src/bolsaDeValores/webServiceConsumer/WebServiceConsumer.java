package bolsaDeValores.webServiceConsumer;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import bolsaDeValores.exception.CannotConnectToServerException;

/**
 * Classe usada para consumir um serviço web. Deve-se primeramente chamar
 * setSever() para setar o endereço, porta e nome do serviço.
 * 
 * @author Fabio
 */
public class WebServiceConsumer {
	private final static int HTTP_OK = 200;
	private final static int HTTP_CREATED = 201;
	private static String SERVER_ADDRESS;
	private static int SERVER_PORT;
	private static String SERVICE_NAME;
	private final static boolean debugMode = false;

	/**
	 * Inicializa os parâmetros do servidor.
	 * 
	 * @param serverAddress
	 * @param serverPort
	 * @param serviceName
	 */
	public static void setServer(String serverAddress, int serverPort, String serviceName) {
		WebServiceConsumer.SERVER_ADDRESS = serverAddress;
		WebServiceConsumer.SERVER_PORT = serverPort;
		WebServiceConsumer.SERVICE_NAME = serviceName;
	}

	/**
	 * Envia uma requisição ao servidor configurado via POST e retorna a resposta.
	 * 
	 * @param args
	 *            String com os argumentos a serem enviados ao servidor.
	 * @return Resposta do servidor em uma String.
	 * @throws CannotConnectToServerException
	 *             Se não foi possível enviar a mensagem ao servidor.
	 */
	public static String post(String args) throws CannotConnectToServerException {
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://" + SERVER_ADDRESS + ":" + SERVER_PORT + "/" + SERVICE_NAME + "/Post");
		ClientResponse response = null;

		try {
			response = webResource.type("application/json").post(ClientResponse.class, args);
		} catch (Exception e) {
			throw new CannotConnectToServerException("Servidor não responde.");
		}

		if (!((response.getStatus() == HTTP_CREATED) || (response.getStatus() == HTTP_OK))) {
			throw new CannotConnectToServerException(
					"Erro. não foi possível conectar ao servidor. Código do erro HTTP: " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		debug("-> " + args);
		debug("<- " + output);
		return output;
	}

	/**
	 * Envia uma requisição ao servidor configurado via GET e retorna a resposta.
	 * 
	 * @param serviceName
	 *            serviço do servidor
	 * @param params
	 *            Mapa key/value com os parâmetros a serem enviados ao servidor.
	 * @return Resposta do servidor em uma String.
	 * @throws CannotConnectToServerException
	 */
	public static String get(String serviceName, MultivaluedMap<String, String> params)
			throws CannotConnectToServerException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client
				.resource(UriBuilder.fromUri("http://" + SERVER_ADDRESS + ":" + SERVER_PORT).build());
		String result = null;
		try {
			result = service.path(SERVICE_NAME).path(serviceName).queryParams(params).accept(MediaType.APPLICATION_JSON)
					.get(String.class);
		} catch (Exception e) {
			throw new CannotConnectToServerException("Servidor não responde.");
		}
		debug("<- " + result);
		return result;
	}

	/**
	 * Igual ao caso anterior, porém com apenas uma key e um value.
	 * 
	 * @param serviceName
	 *            serviço do servidor
	 * @param key
	 *            nome do parâmetro
	 * @param value
	 *            valor do parâmetro
	 * @return Resposta do servidor em uma String.
	 * @throws CannotConnectToServerException
	 *             Se não foi possível enviar a mensagem ao servidor.
	 */
	public static String get(String serviceName, String key, String value) throws CannotConnectToServerException {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add(key, value);
		return get(serviceName, params);
	}

	/**
	 * Igual ao caso anterior, porém, sem argumentos
	 * 
	 * @param serviceName
	 *            nome do serviço
	 * @return Resposta do servidor em uma String.
	 */
	public static String get(String serviceName) throws CannotConnectToServerException {
		return get(serviceName, "", "");
	}

	/**
	 * Método privado utilizado para logar a troca de mensagens do web service.
	 * 
	 * @param message
	 *            Mensagem a ser logada no console.
	 */
	private static void debug(String message) {
		if (debugMode) {
			System.out.println(message);
		}
	}

}
