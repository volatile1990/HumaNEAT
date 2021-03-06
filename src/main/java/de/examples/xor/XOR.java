package de.examples.xor;

import java.util.ArrayList;
import java.util.List;

import de.GenomePrinter;
import de.core.ArtificialIntelligence;
import de.core.genes.NodeGeneType;
import de.core.population.Population;

/**
 * @author MannoR
 *
 */
public class XOR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		float XOR_INPUT[][] = { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } };

		int populationSize = 1000;
		int generations = 400;

		List<ArtificialIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < populationSize; ++i) {
			ais.add(new XorAI(2, 1));
		}

		Population population = new Population(ais);

		ArtificialIntelligence winner = null;

		boolean foundSolution = false;
		for (int i = 0; i < generations; ++i) {

			foundSolution = false;
			for (ArtificialIntelligence ai : population.artificialIntelligences) {

				for (int j = 0; j < 4; ++j) {
					List<Float> input = new ArrayList<>();
					input.add(XOR_INPUT[j][0]);
					input.add(XOR_INPUT[j][1]);

					ai.setInputs(input);
					ai.think();
				}

//				foundSolution = testAiSuccess(ai);
				if (ai.brain.unadjustedFitness == 16f) {
					winner = ai;
					foundSolution = true;
					break;
				}

			}

			if (foundSolution) {
				break;
			}

			population.evolve();

			System.out.println("Generation: " + (i + 1) + " ## FITNESS: " + population.getFittestAI().brain.unadjustedFitness + " ## HIDDEN NODES: "
					+ population.getFittestAI().brain.getNodesByType(NodeGeneType.HIDDEN).size());

//			System.out.println("Generation: " + (i + 1) + " ## Species: " + population.getSpecies().size() + " ## Highest fitness: " + population.getFittestAI().brain.fitness
//					+ " ## HIDDENS: " + population.getFittestAI().brain.getNodesByType(NodeGeneType.HIDDEN).size());
//			System.out.println(population.getFittestAI().brain.feedForward(XOR_INPUT[0])[0] + " ## " + population.getFittestAI().brain.feedForward(XOR_INPUT[1])[0] + " ## "
//					+ population.getFittestAI().brain.feedForward(XOR_INPUT[2])[0] + " ## " + population.getFittestAI().brain.feedForward(XOR_INPUT[3])[0]);

		}

		if (foundSolution) {
			System.out.println("######### SOLUTION FOUND #########");
			GenomePrinter.printGenome(winner.brain, "D:/output/xor/winner.png");

			float[] data = new float[2];

			data[0] = 0;
			data[1] = 0;
			System.out.println("Result: " + winner.brain.feedForward(data)[0]);

			data[0] = 1;
			data[1] = 0;
			System.out.println("Result: " + winner.brain.feedForward(data)[0]);

			data[0] = 1;
			data[1] = 0;
			System.out.println("Result: " + winner.brain.feedForward(data)[0]);

			data[0] = 1;
			data[1] = 1;
			System.out.println("Result: " + winner.brain.feedForward(data)[0]);
		}

//		GenomePrinter.printGenome(population.getFittestAI().brain, "D:/output/xor/result.png");

	}

	private static boolean testAiSuccess(ArtificialIntelligence ai) {

		float[] input = new float[2];

		input[0] = 0f;
		input[1] = 0f;
		if (ai.brain.feedForward(input)[0] > 0.2) {
			return false;
		}

		input[0] = 1f;
		input[1] = 0f;
		if (ai.brain.feedForward(input)[0] < 0.8) {
			return false;
		}

		input[0] = 0f;
		input[1] = 1f;
		if (ai.brain.feedForward(input)[0] < 0.8) {
			return false;
		}

		input[0] = 1f;
		input[1] = 1f;
		if (ai.brain.feedForward(input)[0] > 0.2) {
			return false;
		}

//		GenomePrinter.printGenome(ai.brain, "D:/output/Winner.png");

		return true;
	}

}
