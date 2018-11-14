package de.humaneat.test;

import de.humaneat.core.global.normalization.BagOfWordsNormalizer;

/**
 * @author muellermak
 *
 */
public class BagOfWordsNormalizerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String content = "Das ist ein das Test\nWululou bla Test ist hier am Start\nLOOL das ist ein Test\nTesting and stuff";

		BagOfWordsNormalizer normalizer = new BagOfWordsNormalizer();
		normalizer.buildWordbag(content);
		if (normalizer.wordbag.size() != 13) {
			System.out.println("Wordbag size: " + normalizer.wordbag.size() + " ## Expected size: 13");
		}

		String line1 = "Das ist ein das Test";
		String line2 = "Wululou bla Test ist hier am Start";
		String line3 = "LOOL das ist ein Test";
		String line4 = "Testing and stuff";

		double[] line1Normalized = normalizer.normalize(line1);
		if (line1Normalized[0] != 3 || line1Normalized[1] != 3 || line1Normalized[2] != 2 || line1Normalized[3] != 3 || line1Normalized[4] != 3) {
			System.out.println("Line one normalization incorrect");
		}

		double[] line2Normalized = normalizer.normalize(line2);
		if (line2Normalized[0] != 1 || line2Normalized[1] != 1 || line2Normalized[2] != 3 || line2Normalized[3] != 3 || line2Normalized[4] != 1) {
			System.out.println("Line two normalization incorrect");
		}

		double[] line3Normalized = normalizer.normalize(line3);
		if (line3Normalized[0] != 1 || line3Normalized[1] != 3 || line3Normalized[2] != 3) {
			System.out.println("Line four normalization incorrect");
		}

		double[] line4Normalized = normalizer.normalize(line4);
		if (line4Normalized[0] != 1 || line4Normalized[1] != 1 || line4Normalized[2] != 1) {
			System.out.println("Line four normalization incorrect");
		}
	}
}
