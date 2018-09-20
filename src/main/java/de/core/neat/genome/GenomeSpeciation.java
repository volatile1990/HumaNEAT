package de.core.neat.genome;

import java.util.Collections;

import de.core.neat.genes.ConnectionGene;

/**
 * @author muellermak
 *
 */
public class GenomeSpeciation {

	public NeatGenome first;
	public NeatGenome second;

	public int matchingGenes;
	public int disjointGenes;
	public int excessGenes;

	public double averageWeightDifference;

	/**
	 * @param first
	 * @param second
	 */
	public GenomeSpeciation(NeatGenome first, NeatGenome second) {
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

//		int amountFirstGenes = this.first.getNodeGenes().size() + this.first.getConnectionGenes().size();
//		int amountSecondGenes = this.second.getNodeGenes().size() + this.second.getConnectionGenes().size();
//		double n = amountFirstGenes > amountSecondGenes ? amountFirstGenes : amountSecondGenes;
//		n = n < 20 ? 1 : n;

		return c1 * this.excessGenes + c2 * this.disjointGenes + this.averageWeightDifference * c3;
	}

	/**
	 *
	 */
	private void calculateGenomeSpeciations() {

		int secondHighestNodeNumber = Collections.max(this.second.getNodeGenes().keySet());
		int firstHighestNodeNumber = Collections.max(this.first.getNodeGenes().keySet());
		int secondHighestInnovationNumber = Collections.max(this.second.getConnectionGenes().keySet());
		int firstHighestInnovationNumber = Collections.max(this.first.getConnectionGenes().keySet());

		for (Integer nodeGeneId : this.first.getNodeGenes().keySet()) {

			if (this.second.getNodeGenes().containsKey(nodeGeneId)) {

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

		for (Integer nodeGeneId : this.second.getNodeGenes().keySet()) {

			if (this.first.getNodeGenes().containsKey(nodeGeneId)) {
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
		for (Integer innovationNumber : this.first.getConnectionGenes().keySet()) {

			if (this.second.getConnectionGenes().containsKey(innovationNumber)) {

				// Calculate connection weight difference
				ConnectionGene firstConnectionGene = this.first.getConnectionGenes().get(innovationNumber);
				ConnectionGene secondConnectionGene = this.second.getConnectionGenes().get(innovationNumber);
				++matchingConnectionGenes;
				this.averageWeightDifference += Math.abs(firstConnectionGene.getWeight() - secondConnectionGene.getWeight());

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

		for (Integer innovationNumber : this.second.getConnectionGenes().keySet()) {

			if (this.first.getConnectionGenes().containsKey(innovationNumber)) {
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
