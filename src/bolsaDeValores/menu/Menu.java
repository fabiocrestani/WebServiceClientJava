package bolsaDeValores.menu;

import java.io.IOException;

/**
 * Classe para a interface gr�fica da aplica��o.
 * 
 * @author fabio
 *
 */
public class Menu {
	/**
	 * Limpa a tela
	 */
	protected static void clearScreen() {
		try {
			Runtime.getRuntime().exec("clear");
		} catch (IOException e) {
			// System.out.print("\033[H\033[2J");
			// System.out.flush();
		}
	}
}
