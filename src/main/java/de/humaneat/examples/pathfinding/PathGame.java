package de.humaneat.examples.pathfinding;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.Property;
import de.humaneat.core.neat.population.Population;

/**
 * @author muellermak
 *
 */
public class PathGame {

	public static PlayFrame frame;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

//		// Create new population
		Population population = new Population(PathfindingAI.class);
//
//		// Assign population to a population trainer
//		PopulationTrainer trainer = new PopulationTrainer(population, false);
//
//		// Train until desired fitness is reached
//		trainer.trainToFitness(1000);

		// Run NN
		PathfindingAI winner = null;
		for (int i = 0; i < Property.GENERATIONS.getValue(); ++i) {

			PlayFrame.getInstance().canvas.drawPlayers(population.artificialIntelligences);
			Thread.sleep(1000);

			// Let each one do 150 moves in every generation
			for (int j = 0; j < 100; ++j) {

				for (ArtificialIntelligence ai : population.artificialIntelligences) {

					PathfindingAI pathfindingAI = (PathfindingAI) ai;
					ai.think();
					pathfindingAI.move();

					if (pathfindingAI.currentX == pathfindingAI.targetX && pathfindingAI.currentY == pathfindingAI.targetY) {
						System.out.println("REACHED TARGET");
						winner = pathfindingAI;
						break;
					}

				}

					PlayFrame.getInstance().canvas.update();

				Thread.sleep(50);

				if (winner != null) {
					break;
				}
			}
			if (winner != null) {
				break;
			}

			PlayFrame.getInstance().canvas.players.clear();

			population.evolve();

			System.out.println("GENERATION: " + (i + 1) + " ## FITNESS: " + population.fittestAI.brain.unadjustedFitness);
		}

		if (winner != null)

		{

			List<ArtificialIntelligence> ai = new ArrayList<>();
			ai.add(winner);

			PlayFrame.getInstance().canvas.players.clear();
			PlayFrame.getInstance().canvas.drawPlayers(population.artificialIntelligences);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			winner.currentX = 3;
			winner.currentY = 7;
			for (int i = 0; i < 100; ++i) {

				winner.think();

				PlayFrame.getInstance().canvas.update();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (winner.targetX == winner.currentX && winner.targetY == winner.currentY) {
					break;
				}
			}
		}

	}

	private static void iniUi(List<ArtificialIntelligence> ais) {

		PathGame.frame = new PlayFrame();
	}

	/**
	 * @param width
	 * @param height
	 * @return
	 */
	private static int[][] getRandomPlayfield(int width, int height) {

		int[][] playfield = new int[height][width];
		Random random = new SecureRandom();
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {

				if (i == 45 && j == 49) {
					playfield[i][j] = 1;
				} else if (i == 3 && j == 7) {
					playfield[i][j] = 1;
				} else {
					playfield[i][j] = random.nextInt(101);
				}
			}
		}

		return playfield;
	}

}
