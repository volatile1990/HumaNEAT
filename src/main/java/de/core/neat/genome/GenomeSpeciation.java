package de.core.neat.genome;

import java.util.Collections;

import de.core.neat.genes.connection.ConnectionGene;

/**
 * @author muellermak
 *
 */
public class GenomeSpeciation {

	public Genome first;
	public Genome second;

	public int matchingGenes;
	public int disjointGenes;
	public int excessGenes;

	public double averageWeightDifference;

	/**
	 * @param first
	 * @param second
	 */
	public GenomeSpeciation(Genome first, Genome second) {
		this.first = first;
		this.second = second;

		calculateGenomeSpeciations();
	}

	/**
	 * Refer for 3.3, (1) in paper;
	 *
	 * Formula: Compatibility distance = (c1 * excess genes / n) + (c2 * disjoint
	 * genes / n) + c3 * matching genes
	 *
	 * N = Number of genes in the larger genome; can be Set to 1 for <= genes
	 *
	 * If the distance is < 3 two genomes belong to the same species
	 *
	 * @param first
	 * @param second
	 * @param c1
	 * @param c2
	 * @param c3
	 * @return the compatibilityDistance
	 */
	public double compatibilityDistance(double c1, double c2, double c3) {

//		int amountFirstGenes = this.first.nodes.size() + this.first.connections.size();
//		int amountSecondGenes = this.second.nodes.size() + this.second.connections.size();
//		double n = amountFirstGenes > amountSecondGenes ? amountFirstGenes : amountSecondGenes;
//		n = n < 20 ? 1 : n;

		return c1 * excessGenes + c2 * disjointGenes + averageWeightDifference * c3;
	}

	/**
	 *
	 */
	private void calculateGenomeSpeciations() {

		int secondHighestNodeNumber = Collections.max(second.nodes.keySet());
		int firstHighestNodeNumber = Collections.max(first.nodes.keySet());
		int secondHighestInnovationNumber = Collections.max(second.connections.keySet());
		int firstHighestInnovationNumber = Collections.max(first.connections.keySet());

		for (Integer nodeGeneId : first.nodes.keySet()) {

			if (second.nodes.containsKey(nodeGeneId)) {

				// Matching gene
				++matchingGenes;
			} else if (secondHighestNodeNumber >= nodeGeneId) {

				// Disjoint gene
				++disjointGenes;
			} else {

				// Excess gene
				++excessGenes;
			}

		}

		for (Integer nodeGeneId : second.nodes.keySet()) {

			if (first.nodes.containsKey(nodeGeneId)) {
				continue;
			}

			// Sum up disjoint and excess genes
			if (firstHighestNodeNumber >= nodeGeneId) {
				++disjointGenes;
			} else {
				++excessGenes;
			}
		}

		// Count disjoint connections by innovation number
		int matchingConnectionGenes = 0;
		for (Integer innovationNumber : first.connections.keySet()) {

			if (second.connections.containsKey(innovationNumber)) {

				// Calculate connection weight difference
				ConnectionGene firstConnectionGene = first.connections.get(innovationNumber);
				ConnectionGene secondConnectionGene = second.connections.get(innovationNumber);
				++matchingConnectionGenes;
				averageWeightDifference += Math.abs(firstConnectionGene.weight - secondConnectionGene.weight);

				// Matching gene
				++matchingGenes;
			} else if (secondHighestInnovationNumber >= innovationNumber) {

				// Disjoint gene
				++disjointGenes;
			} else {

				// Excess gene
				++excessGenes;
			}
		}
		averageWeightDifference /= matchingConnectionGenes;

		for (Integer innovationNumber : second.connections.keySet()) {

			if (first.connections.containsKey(innovationNumber)) {
				continue;
			}

			// Sum up disjoint & excess genes
			if (firstHighestInnovationNumber >= innovationNumber) {
				++disjointGenes;
			} else {
				++excessGenes;
			}

		}

	}
}
