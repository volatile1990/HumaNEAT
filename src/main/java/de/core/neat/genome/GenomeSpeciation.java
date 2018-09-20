package de.core.neat.genome;

import java.util.Collections;

import de.core.neat.genes.ConnectionGene;

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

		this.calculateGenomeSpeciations();
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

		return c1 * this.excessGenes + c2 * this.disjointGenes + this.averageWeightDifference * c3;
	}

	/**
	 *
	 */
	private void calculateGenomeSpeciations() {

		int secondHighestNodeNumber = Collections.max(this.second.nodes.keySet());
		int firstHighestNodeNumber = Collections.max(this.first.nodes.keySet());
		int secondHighestInnovationNumber = Collections.max(this.second.connections.keySet());
		int firstHighestInnovationNumber = Collections.max(this.first.connections.keySet());

		for (Integer nodeGeneId : this.first.nodes.keySet()) {

			if (this.second.nodes.containsKey(nodeGeneId)) {

				// Matching gene
				++this.matchingGenes;
			} else if (secondHighestNodeNumber >= nodeGeneId) {

				// Disjoint gene
				++this.disjointGenes;
			} else {

				// Excess gene
				++this.excessGenes;
			}

		}

		for (Integer nodeGeneId : this.second.nodes.keySet()) {

			if (this.first.nodes.containsKey(nodeGeneId)) {
				continue;
			}

			// Sum up disjoint and excess genes
			if (firstHighestNodeNumber >= nodeGeneId) {
				++this.disjointGenes;
			} else {
				++this.excessGenes;
			}
		}

		// Count disjoint connections by innovation number
		int matchingConnectionGenes = 0;
		for (Integer innovationNumber : this.first.connections.keySet()) {

			if (this.second.connections.containsKey(innovationNumber)) {

				// Calculate connection weight difference
				ConnectionGene firstConnectionGene = this.first.connections.get(innovationNumber);
				ConnectionGene secondConnectionGene = this.second.connections.get(innovationNumber);
				++matchingConnectionGenes;
				this.averageWeightDifference += Math.abs(firstConnectionGene.weight - secondConnectionGene.weight);

				// Matching gene
				++this.matchingGenes;
			} else if (secondHighestInnovationNumber >= innovationNumber) {

				// Disjoint gene
				++this.disjointGenes;
			} else {

				// Excess gene
				++this.excessGenes;
			}
		}
		this.averageWeightDifference /= matchingConnectionGenes;

		for (Integer innovationNumber : this.second.connections.keySet()) {

			if (this.first.connections.containsKey(innovationNumber)) {
				continue;
			}

			// Sum up disjoint & excess genes
			if (firstHighestInnovationNumber >= innovationNumber) {
				++this.disjointGenes;
			} else {
				++this.excessGenes;
			}

		}

	}
}
