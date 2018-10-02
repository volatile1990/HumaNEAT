package de.humaneat.examples.lstm;

import de.humaneat.core.lstm.population.LstmPopulation;

/**
 * @author muellermak
 *
 */
public class SimpleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int[] input = { 3, 6, 9, 12, 15, 18, 21, 24, 27 };
		int expectedPrediction = 30;

		LstmPopulation population = new LstmPopulation(SimpleLstmAi.class);

	}
}
