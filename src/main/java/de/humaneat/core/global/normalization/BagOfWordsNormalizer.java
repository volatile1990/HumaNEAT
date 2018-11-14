package de.humaneat.core.global.normalization;

import java.util.HashMap;
import java.util.Map;

/**
 * @author muellermak
 *
 */
public class BagOfWordsNormalizer implements Normalizer {

	public Map<String, Integer> wordbag;

	/**
	 * 
	 */
	public BagOfWordsNormalizer(String content) {
		wordbag = new HashMap<>();
		buildWordbag();
	}

	/**
	 * Builds a bag of words out of the whole data
	 * 
	 */
	private void buildWordbag() {

	}

	/**
	 * Normalizes a given line depending on the previous built wordbag
	 * 
	 * @param line
	 * @return
	 */
	@Override
	public double[] normalize(String line) {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public int getInputCount() {
		return wordbag.size();
	}
}
