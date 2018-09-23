package de.humaneat.examples.xor;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.genes.node.NodeGeneType;
import de.humaneat.core.neat.population.Population;
import de.humaneat.graphics.GenomeViewer;

/**
 * @author MannoR
 *
 */
public class XOR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		double XOR_INPUT[][] = { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 0 } };

		int populationSize = 250;
		int generations = 400;

		List<ArtificialIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < populationSize; ++i) {
			ais.add(new XorAI(2, 1));
		}

		Population population = new Population(ais);

		GenomeViewer genomeViewer = new GenomeViewer(ais.get(0).brain);
		genomeViewer.start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("STARING:");

		ArtificialIntelligence winner = null;

		boolean foundSolution = false;
		for (int i = 0; i < generations; ++i) {

			foundSolution = false;
			for (ArtificialIntelligence ai : population.artificialIntelligences) {

				for (int j = 0; j < 4; ++j) {
					List<Double> input = new ArrayList<>();
					input.add(XOR_INPUT[j][0]);
					input.add(XOR_INPUT[j][1]);

					ai.think(input);
				}

//				foundSolution = testAiSuccess(ai);
				if (ai.brain.unadjustedFitness >= 16) {
					winner = ai;
					foundSolution = true;
					break;
				}

			}

			if (foundSolution) {
				break;
			}

			population.evolve();

			genomeViewer.genome = population.fittestAI.brain;

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Generation: " + (i + 1) + " ## FITNESS: " + population.fittestAI.brain.unadjustedFitness + " ## HIDDEN NODES: "
					+ population.fittestAI.brain.getNodesByType(NodeGeneType.HIDDEN).size());

//			System.out.println("Generation: " + (i + 1) + " ## Species: " + population.getSpecies().size() + " ## Highest fitness: " + population.getFittestAI().brain.fitness
//					+ " ## HIDDENS: " + population.getFittestAI().brain.getNodesByType(NodeGeneType.HIDDEN).size());
//			System.out.println(population.getFittestAI().brain.feeder.feedForward(XOR_INPUT[0])[0] + " ## " + population.getFittestAI().brain.feeder.feedForward(XOR_INPUT[1])[0] + " ## "
//					+ population.getFittestAI().brain.feeder.feedForward(XOR_INPUT[2])[0] + " ## " + population.getFittestAI().brain.feeder.feedForward(XOR_INPUT[3])[0]);

		}

		if (foundSolution) {
			System.out.println("######### SOLUTION FOUND #########");
			System.out.println("WINNER HIDDEN NODES: " + winner.brain.getNodesByType(NodeGeneType.HIDDEN).size());
//			GenomePrinter.printGenome(winner.brain, "D:/output/xor/winner.png");

			double[] data = new double[2];

			data[0] = 0;
			data[1] = 0;
			System.out.println("Result: " + winner.brain.getFeeder().feedForward(data)[0]);

			data[0] = 1;
			data[1] = 0;
			System.out.println("Result: " + winner.brain.getFeeder().feedForward(data)[0]);

			data[0] = 1;
			data[1] = 0;
			System.out.println("Result: " + winner.brain.getFeeder().feedForward(data)[0]);

			data[0] = 1;
			data[1] = 1;
			System.out.println("Result: " + winner.brain.getFeeder().feedForward(data)[0]);
		}

//		GenomePrinter.printGenome(population.getFittestAI().brain, "D:/output/xor/result.png");

	}

	private static boolean testAiSuccess(ArtificialIntelligence ai) {

		double[] input = new double[2];

		input[0] = 0f;
		input[1] = 0f;
		if (ai.brain.getFeeder().feedForward(input)[0] > 0.2) {
			return false;
		}

		input[0] = 1f;
		input[1] = 0f;
		if (ai.brain.getFeeder().feedForward(input)[0] < 0.8) {
			return false;
		}

		input[0] = 0f;
		input[1] = 1f;
		if (ai.brain.getFeeder().feedForward(input)[0] < 0.8) {
			return false;
		}

		input[0] = 1f;
		input[1] = 1f;
		if (ai.brain.getFeeder().feedForward(input)[0] > 0.2) {
			return false;
		}

//		GenomePrinter.printGenome(ai.brain, "D:/output/Winner.png");

		return true;
	}

}
