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

		BagOfWordsNormalizer normalizer = new BagOfWordsNormalizer(content);
		if (normalizer.wordbag.size() != 13) {
			System.out.println("Wordbag size: " + normalizer.wordbag.size() + " ## Expected size: 13");
		}

		String line1 = "Das ist ein das Test";
		String line2 = "Wululou bla Test ist hier am Start";
		String line3 = "LOOL das ist ein Test";
		String line4 = "Testing and stuff";

		double[] line1Normalized = normalizer.normalize(line1);
		for (int i = 0; i < line1Normalized.length; ++i) {
			if (line1Normalized[i] < 0 || line1Normalized[i] > 1) {
				System.out.println("Line one normalization incorrect");
			}
		}

		double[] line2Normalized = normalizer.normalize(line2);
		for (int i = 0; i < line2Normalized.length; ++i) {
			if (line2Normalized[i] < 0 || line2Normalized[i] > 1) {
				System.out.println("Line two normalization incorrect");
			}
		}
		double[] line3Normalized = normalizer.normalize(line3);
		for (int i = 0; i < line1Normalized.length; ++i) {
			if (line3Normalized[i] < 0 || line3Normalized[i] > 1) {
				System.out.println("Line three normalization incorrect");
			}
		}

		double[] line4Normalized = normalizer.normalize(line4);
		for (int i = 0; i < line4Normalized.length; ++i) {
			if (line4Normalized[i] < 0 || line4Normalized[i] > 1) {
				System.out.println("Line four normalization incorrect");
			}
		}
	}
}
