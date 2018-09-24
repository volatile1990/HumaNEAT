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

		int populationSize = 1000;
		int generations = 500;

		List<ArtificialLstmIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < populationSize; ++i) {
			ais.add(new SimpleLstmAi(1, 1));
		}

		LstmPopulation population = new LstmPopulation(ais);
	}
}
