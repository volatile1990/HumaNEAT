package de.humaneat.examples.innovationdays;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.global.normalization.BagOfWordsNormalizer;
import de.humaneat.core.neat.ArtificialIntelligence;

/**
 * @author muellermak
 *
 */
public class TicketClassificationAI extends ArtificialIntelligence {

	public List<String> lines;

	private int currentInput;

	/**
	 * 
	 */
	@Override
	public void doAiLogic() {

		// Think as many times as there are lines
		currentInput = 0;
		for (int i = 0; i < lines.size(); ++i) {
			think();
		}
	}

	/**
	 * @return
	 */
	@Override
	public List<Double> getInputs() {

		// Iteratively give inputs for every line
		List<Double> input = new ArrayList<>();
		double[] normalizedLine = normalizer.normalize(lines.get(currentInput));
		for (int i = 0; i < normalizedLine.length; ++i) {
			input.add(normalizedLine[i]);
		}

		++currentInput;

		return input;
	}

	/**
	 * @param output
	 */
	@Override
	public void takeAction(List<Double> output) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return
	 */
	@Override
	public double calculateFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 */
	@Override
	public void initializeNormalizer() {
		normalizer = new BagOfWordsNormalizer("CONTENT");
		// TODO: Read input
	}

}
