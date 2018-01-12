package bolsaDeValores;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MultivaluedMap;

import bolsaDeValores.Bid.BidType;
import bolsaDeValores.exception.CannotConnectToServerException;
import bolsaDeValores.exception.CannotPlaceOrder;
import bolsaDeValores.util.MyRandom;
import bolsaDeValores.webServiceConsumer.ControlMessage;
import bolsaDeValores.webServiceConsumer.WebServiceConsumer;

/**
 * Classe do cliente do mercado de ações.
 * 
 * @author Fabio
 *
 */
public class Client {
	private int id;
	private List<Stock> listOfStocks = new ArrayList<Stock>();
	private List<Bid> listOfPendingBids = new LinkedList<Bid>();
	private boolean isRunning;

	/**
	 * Construtor. Inicializa cliente com id igual ao PID do processo. Carrega a
	 * lista de ações de um arquivo e pede ao servidor o valor atualizado de todas
	 * as ações.
	 * 
	 * @throws Exception
	 */
	public Client() throws Exception {
		String processName = ManagementFactory.getRuntimeMXBean().getName();
		try {
			this.id = Integer.parseInt(processName.split("@")[0]);
		} catch (Exception e) {
			this.id = (int) (Math.random() * 10001);
		}
		System.out.println("");
		System.out.println("Inicializando cliente com id: " + id);
		isRunning = true;
		loadStocksFromFile();
		updateAll(requestListOfAllStocks());
	}

	/**
	 * Retorna o id do cliente.
	 * 
	 * @return id do cliente.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retorna o estado do cliente.
	 * 
	 * @return true se está rodando, ou seja, não foi parado pelo usuário.
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Carrega uma lista de a��es de um arquivo de texto.
	 * 
	 * @throws Exception
	 */
	private void loadStocksFromFile() throws Exception {
		System.out.println("Carregando lista de ações...");
		File file = new File("listaDeAcoes.txt");
		Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String name = sc.nextLine();
				double quantity = MyRandom.randDouble(0, 100);
				listOfStocks.add(new Stock(name, 0, quantity));
			}
		} catch (FileNotFoundException e) {
			throw e;
		}
		sc.close();
	}

	/**
	 * Usada para pegar a quantidade de uma determinada ação do cliente.
	 * 
	 * @param name
	 *            Ação procurada.
	 * @return Quantidade da ação disponível.
	 */
	public double getQuantityOfStock(String name) {
		for (Stock s : listOfStocks) {
			if (s.getName().equalsIgnoreCase(name)) {
				return s.getQuantity();
			}
		}
		return 0;
	}

	/**
	 * Usada para enviar um lance de compra ou venda ao servidor. Além disso, insere
	 * o lance em uma lista de lances pendentes.
	 * 
	 * @param bid
	 *            Lance a ser enviado.
	 * @throws CannotPlaceOrder
	 */
	public void placeOrder(Bid bid) throws CannotPlaceOrder {
		for (Bid b : listOfPendingBids) {
			if (b.getClientId() == bid.getClientId() && b.getStockName().equals(bid.getStockName())
					&& b.getType() == bid.getType()) {
				throw new CannotPlaceOrder("Você já registrou uma ordem desse tipo para esta ação.");
			}
		}
		listOfPendingBids.add(bid);

		String responseString = null;
		try {
			responseString = WebServiceConsumer.post(new Gson().toJson(bid, Bid.class));
			ControlMessage response = new Gson().fromJson(responseString, ControlMessage.class);
			if (response.getCode() != ControlMessage.ControlMessageCode.ACK) {
				throw new CannotPlaceOrder("Servidor recusou a ordem.");
			}
		} catch (CannotConnectToServerException e) {
			throw new CannotPlaceOrder("Erro ao conectar ao servidor.");
		}
	}

	/**
	 * Usada para pedir o valor de uma ação ao servidor.
	 * 
	 * @param name
	 *            Nome da ação procurada.
	 * @return Um objeto Stock o valor atualizado da ação.
	 * @throws CannotConnectToServerException
	 */
	public Stock requestStock(String name) throws CannotConnectToServerException {
		try {
			String string = WebServiceConsumer.get("GetPrice", "stockName", name);
			return new Gson().fromJson(string, Stock.class);
		} catch (Exception e) {
			throw new CannotConnectToServerException("Erro ao conectar ao servidor.");
		}
	}

	/**
	 * Envia uma mensagem ao servidor pedindo a lista de todas as ações.
	 * 
	 * @return A lista de todas as ações do servidor.
	 * @throws CannotConnectToServerException
	 */
	public List<Stock> requestListOfAllStocks() throws CannotConnectToServerException {
		String line = WebServiceConsumer.get("ListAll");
		Type listType = new TypeToken<ArrayList<Stock>>() {
		}.getType();
		List<Stock> list = new Gson().fromJson(line, listType);
		return list;
	}

	/**
	 * Usada para atualizar o valor de uma ação do cliente.
	 * 
	 * @param name
	 *            Nome da ação a ser atualizada.
	 * @param price
	 *            Novo preço da ação.
	 */
	public void updateStock(String name, double price) {
		for (Stock s : listOfStocks) {
			if (s.getName().equalsIgnoreCase(name)) {
				s.setPrice(price);
				return;
			}
		}
	}

	/**
	 * Usada para atualizar o valor de todas as ações do cliente.
	 * 
	 * @param list
	 *            Lista com as ações atualizadas.
	 */
	public void updateAll(List<Stock> list) {
		for (Stock remoteStock : list) {
			for (Stock localStock : listOfStocks) {
				if (remoteStock.getName().equalsIgnoreCase(localStock.getName())) {
					localStock.setPrice(remoteStock.getPrice());
					break;
				}
			}
		}
	}

	/**
	 * Imprime uma lista com todas as ações do cliente.
	 */
	public void printListOfStocks() {
		System.out.println("===========================");
		System.out.println("Ação    Preço\tQuantidade");
		System.out.println("===========================");
		for (Stock s : listOfStocks) {
			System.out.println(s.toString());
		}
	}

	/**
	 * Encerra o cliente
	 */
	public void stop() {
		isRunning = false;
		System.out.println("Finalizando cliente...");
	}

	/**
	 * Vai até o servidor e pergunta se teve update no lance passado em bid. Ao
	 * receber um update, remove o lance da lista de lances pendentes.
	 * 
	 * @param bid
	 *            Lance de interesse a ser pesquisado no servidor.
	 * @throws CannotConnectToServerException
	 */
	private void checkForUpdate(Bid bid) throws CannotConnectToServerException {
		try {
			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			params.add("stockName", bid.getStockName());
			params.add("clientId", bid.getClientId() + "");
			String string = WebServiceConsumer.get("Poll", params);
			Bid remoteBid = new Gson().fromJson(string, Bid.class);

			if (remoteBid == null) {
				return;
			}

			if (remoteBid.getStockName().equalsIgnoreCase(bid.getStockName()) && bid.getClientId() == getId()) {
				String typeAsString = "";
				if (remoteBid.getType() == BidType.BUY) {
					typeAsString = "compradas";
				} else if (remoteBid.getType() == BidType.SELL) {
					typeAsString = "vendidas";
				}
				System.out.print("Lance " + remoteBid.getStockName() + " acaba de ser transacionado no servidor. Foram "
						+ typeAsString + " " + remoteBid.getQuantity() + " ações pelo preço de R$"
						+ remoteBid.getNegotiatedPrice());
				bid.setQuantity(bid.getQuantity() - remoteBid.getQuantity());
				updateLocalStockQuantity(remoteBid);
				if (bid.getQuantity() <= 0) {
					removeBidFromList(remoteBid);
				}
			}
		} catch (Exception e) {
			throw new CannotConnectToServerException("Erro ao conectar ao servidor.");
		}
	}

	/**
	 * Atualiza a quantidade local de uma ação baseada no lance recebido como
	 * resposta do servidor.
	 * 
	 * @param bid
	 *            Lance recebido contendo a quantidade transacionada.
	 */
	private void updateLocalStockQuantity(Bid bid) {
		for (Stock s : listOfStocks) {
			if (s.getName().equalsIgnoreCase(bid.getStockName())) {
				s.setPrice(bid.getNegotiatedPrice());
				if (bid.getType() == Bid.BidType.BUY) {
					s.setQuantity(s.getQuantity() + bid.getQuantity());
				} else if (bid.getType() == Bid.BidType.SELL) {
					s.setQuantity(s.getQuantity() - bid.getQuantity());
				} else {
					System.out.println("updateLocalStockQuantity: Tipo inválido: " + bid.getType());
				}
			}
		}
	}

	/**
	 * Remove um lance da lista de lances pendentes.
	 * 
	 * @param bid
	 *            Lance a ser removido
	 */
	private void removeBidFromList(Bid bidToRemove) {
		Iterator<Bid> it = listOfPendingBids.iterator();
		while (it.hasNext()) {
			Bid bid = it.next();
			if (bid.getStockName().equalsIgnoreCase(bidToRemove.getStockName())) {
				it.remove();
			}
		}
	}

	/**
	 * Inicia thread que faz polling no servidor perguntando por uma atualização nos
	 * seus lances.
	 */
	public void start() {
		new Thread() {
			@Override
			public void run() {
				while (isRunning()) {
					Iterator<Bid> it = listOfPendingBids.iterator();
					while (it.hasNext()) {
						Bid bid = it.next();
						try {
							checkForUpdate(bid);
						} catch (CannotConnectToServerException e) {
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

}
