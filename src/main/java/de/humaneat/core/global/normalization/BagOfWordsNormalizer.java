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
	public BagOfWordsNormalizer() {
		wordbag = new HashMap<>();
	}

	/**
	 * Builds a bag of words out of the whole data
	 * 
	 */
	public void buildWordbag(String content) {

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
}
