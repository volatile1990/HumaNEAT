package de.humaneat.core.global;

import java.security.SecureRandom;
import java.util.List;

/**
 * @author MannoR
 *
 */
public class Random {

	public static final SecureRandom random = new SecureRandom();

	/**
	 * Returns a random object from the given array
	 */
	public static <T> T random(T[] array) {

		if (array.length == 0) {
			throw new IllegalArgumentException("Given array must not be empty");
		}

		return array[Random.random.nextInt(array.length)];
	}

	/**
	 * Returns a random object from the given list
	 */
	public static <T> T random(List<T> list) {

		if (list.size() == 0) {
			throw new IllegalArgumentException("Given list must not be empty");
		}

		return list.get(Random.random.nextInt(list.size()));
	}

	/**
	 * Returns a random double value between given min and max
	 *
	 * @param min
	 * @param max
	 */
	public static double random(double min, double max) {

		if (min > max) {
			throw new IllegalArgumentException("Minimum can't be greater than maximum for generating random");
		}

		return min + (max - min) * Random.random.nextDouble();
	}

	/**
	 * Returns true or false based on the given chance (1 => 100% true)
	 *
	 * @param chance
	 * @return
	 */
	public static boolean success(double chance) {
		return Random.random.nextDouble() <= chance;
	}
}
