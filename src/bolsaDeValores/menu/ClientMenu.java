package bolsaDeValores.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import bolsaDeValores.Bid;
import bolsaDeValores.Client;
import bolsaDeValores.menu.Menu;
import bolsaDeValores.Stock;
import bolsaDeValores.exception.CannotConnectToServerException;
import bolsaDeValores.exception.CannotPlaceOrder;

/**
 * Classe estática usada para mostrar e tratar o menu de ações do usuário.
 * 
 * @author fabio
 *
 */
public class ClientMenu extends Menu {
	/**
	 * Mostra menu.
	 */
	public static void show() {
		clearScreen();
		System.out.println("");
		System.out.println("**************************************************");
		System.out.println("*            Bolsa de Valores - Cliente          *");
		System.out.println("**************************************************");
		System.out.println("*                                                *");
		System.out.println("*  Digite a opção desejada:                      *");
		System.out.println("*                                                *");
		System.out.println("*  1. Lançar ordem de compra                     *");
		System.out.println("*  2. Lançar ordem de venda                      *");
		System.out.println("*  3. Pesquisar o preço de uma ação              *");
		System.out.println("*  4. Atualizar o preço de todas as ações        *");
		System.out.println("*  5. Listar minhas ações                        *");
		System.out.println("*                                                *");
		System.out.println("*  9. Exibir este menu                           *");
		System.out.println("*  0. Encerrar                                   *");
		System.out.println("*                                                *");
		System.out.println("**************************************************");
		System.out.println("");
	}

	/**
	 * Captura ações do usuário.
	 * 
	 * @param client
	 *            referência para o cliente.
	 */
	public static void start(Client client) {
		ClientMenu.show();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			while (client.isRunning()) {
				while (!br.ready() && client.isRunning()) {
					Thread.sleep(200);
				}

				if (!client.isRunning()) {
					clearScreen();
					return;
				}

				String input = br.readLine();

				switch (input.toLowerCase()) {

				case "1": {
					System.out.println("");
					System.out.print("Digite o código da ação que deseja comprar: ");
					String name = br.readLine();
					System.out.print("Digite o valor máximo para compra: ");
					try {
						double price = Double.parseDouble(br.readLine());
						System.out.print("Digite a quantidade: ");
						double quantity = Double.parseDouble(br.readLine());
						client.placeOrder(new Bid(name, price, quantity, Bid.BidType.BUY, client.getId()));
						System.out.println("Ordem de compra enviada ao servidor");
					} catch (NumberFormatException e) {
						System.out.println("Ordem de compra não enviada. Digite preço e quantidade válidos.");
					} catch (CannotPlaceOrder e) {
						System.out.println("Ordem de compra não enviada: " + e.getMessage());
					}
				}
					break;

				case "2": {
					System.out.println("");
					System.out.print("Digite o código da ação que deseja vender: ");
					String name = br.readLine();
					System.out.print("Digite o valor mínimo para venda: ");
					try {
						double price = Double.parseDouble(br.readLine());
						System.out.print("Digite a quantidade: ");
						double quantity = Double.parseDouble(br.readLine());
						if (quantity > client.getQuantityOfStock(name)) {
							System.out.println(
									"Ordem de venda não enviada: O cliente não possui a quantidade suficiente.");
						} else {
							client.placeOrder(new Bid(name, price, quantity, Bid.BidType.SELL, client.getId()));
							System.out.println("Ordem de venda enviada ao servidor");
						}
					} catch (NumberFormatException e) {
						System.out.println("Ordem de venda não enviada. Digite preço e quantidade válidos.");
					} catch (CannotPlaceOrder e) {
						System.out.println("Ordem de venda não enviada: " + e.getMessage());
					}
				}
					break;

				case "3":
					try {
						System.out.println("");
						System.out.print("Digite o código da ação para pesquisar o preço: ");
						String name = br.readLine();
						Stock stock = client.requestStock(name);
						if (stock.getName().equals("")) {
							System.out.println("A ação " + name + " não existe no servidor.");
						} else {
							System.out.println("A ação " + stock.getName() + " foi cotada em R$" + stock.getPrice());
							client.updateStock(stock.getName(), stock.getPrice());
						}
					} catch (CannotConnectToServerException e) {
						System.out.println("Ordem não enviada: " + e.getMessage());
					}
					System.out.println("");
					break;

				case "4":
					try {
						List<Stock> list = client.requestListOfAllStocks();
						client.updateAll(list);
						System.out.println("Ordem de listagem de ações enviada ao servidor.");
						System.out.println("Digite 5 para ver a lista atualizada.");
					} catch (CannotConnectToServerException e) {
						System.out.println("Ordem não enviada: " + e.getMessage());
					}
					System.out.println("");
					break;

				case "5":
					client.printListOfStocks();
					break;

				case "help":
				case "h":
				case "?":
				case "9":
					System.out.println("");
					ClientMenu.show();
					break;

				case "0":
				case "q":
				case "quit":
					client.stop();
					break;

				default:
					System.out.println("Opção inválida. Digite ? para ver o menu de ações possíveis.");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
