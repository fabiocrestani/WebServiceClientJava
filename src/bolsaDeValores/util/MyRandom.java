package bolsaDeValores.util;

import java.util.Random;

/**
 * Classe est�tica usada para gera��o de n�meros aleat�rios.
 * 
 * @author fabio
 *
 */
public class MyRandom {

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The difference
	 * between min and max can be at most <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimum value.
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	/**
	 * Gera um double aleat�rio dentro dos limites min e max, com at� duas casas
	 * decimais.
	 * 
	 * @param min
	 *            valor m�nimo.
	 * @param max
	 *            valor m�ximo.
	 * @return um double entre o valor m�nimo e o valor m�ximo.
	 */
	public static double randDouble(int min, int max) {
		int i = randInt(min * 100, max * 100);
		return i / (double) 100;
	}
}
