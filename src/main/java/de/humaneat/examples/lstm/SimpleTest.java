package de.humaneat.examples.lstm;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.lstm.ArtificialLstmIntelligence;
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

		int populationSize = 1000;
		int generations = 500;

		List<ArtificialLstmIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < populationSize; ++i) {
			ais.add(new SimpleLstmAi(9, 1));
		}

		LstmPopulation population = new LstmPopulation(SimpleLstmAi.class);

		for (int i = 0; i < generations; ++i) {

		}
	}
}
