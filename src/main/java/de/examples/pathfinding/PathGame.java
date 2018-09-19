package de.examples.pathfinding;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.core.ArtificialIntelligence;
import de.core.population.Population;

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

		int populationSize = 150;
		int generations = 200;

		// Initialize field
		int[][] playfield = getRandomPlayfield(50, 50);

		// Create AIS
		List<ArtificialIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < populationSize; ++i) {
			ais.add(new PathfindingAI(2, 4));
		}
		Population population = new Population(ais);

//		iniUi(ais);

		Random random = new SecureRandom();
		List<Integer> currentX = new ArrayList<>();
		List<Integer> currentY = new ArrayList<>();
		List<Integer> targetX = new ArrayList<>();
		List<Integer> targetY = new ArrayList<>();
		for (int i = 0; i < 50; ++i) {
			currentX.add(random.nextInt(50));
			currentY.add(random.nextInt(50));
			targetX.add(random.nextInt(50));
			targetY.add(random.nextInt(50));
		}

		// Run NN
		PathfindingAI winner = null;
		for (int i = 0; i < generations; ++i) {

			// Draw all players
//			frame.canvas.drawPlayers(population.artificialIntelligences);
//			Thread.sleep(1000);

			for (ArtificialIntelligence ai : population.artificialIntelligences) {

				int targetsReached = 0;
				for (int testCases = 0; testCases < 50; ++testCases) {

					// Set random position & target for current testCase
					PathfindingAI pAi = (PathfindingAI) ai;
					pAi.currentX = currentX.get(i);
					pAi.currentY = currentY.get(i);
					pAi.targetX = targetX.get(i);
					pAi.targetY = targetY.get(i);

					// Let each one do 150 moves in every generation
					for (int j = 0; j < 100; ++j) {

						PathfindingAI pathfindingAI = (PathfindingAI) ai;

						ai.setInputs(pathfindingAI.getCurrentPosition());
						ai.think();
						pathfindingAI.move();

						// Continue with next testcase if the target was reached
						if (pathfindingAI.currentX == pathfindingAI.targetX && pathfindingAI.currentY == pathfindingAI.targetY) {
//							System.out.println(
//									"REACHED A TARGET: " + pathfindingAI.currentX + "#" + pathfindingAI.targetX + " ::: " + pathfindingAI.currentY + "#" + pathfindingAI.targetY);
							++targetsReached;
							break;
						}

					}

					pAi.saveDatasets();

					// Update players on UI
//				for (ArtificialIntelligence ai : population.artificialIntelligences) {
//					frame.canvas.update((PathfindingAI) ai);
//				}
//
//				Thread.sleep(50);
				}

				if (targetsReached >= 49) {
					winner = (PathfindingAI) ai;
					break;
				}
			}

			if (winner != null) {
				break;
			}

			population.evolve();

			System.out.println("GENERATION: " + (i + 1) + " ## FITNESS: " + population.getFittestAI().brain.unadjustedFitness);
		}

		if (winner != null) {

			System.out.println("#################");
			System.out.println("WE HAVE WINNER!!!");

//			List<ArtificialIntelligence> ai = new ArrayList<>();
//			ai.add(winner);
//
//			frame.canvas.players.clear();
//			frame.canvas.drawPlayers(population.artificialIntelligences);
//
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			winner.currentX = 3;
//			winner.currentY = 7;
//			for (int i = 0; i < 100; ++i) {
//
//				winner.setInputs(winner.getCurrentPosition());
//				winner.think();
//				winner.move();
//
//				frame.canvas.update((PathfindingAI) ai.get(0));
//				try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				if (winner.targetX == winner.currentX && winner.targetY == winner.currentY) {
//					break;
//				}
//			}
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
