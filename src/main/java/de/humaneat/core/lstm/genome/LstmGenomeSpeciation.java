package de.humaneat.core.lstm.genome;

import java.util.Collections;

import de.humaneat.core.lstm.genes.node.LstmNodeGene;
import de.humaneat.core.lstm.genes.node.Weights;

/**
 * @author muellermak
 *
 */
public class LstmGenomeSpeciation {

	public LstmGenome first;
	public LstmGenome second;

	public int matchingGenes;
	public int disjointGenes;
	public int excessGenes;

	public double averageWeightDifference;

	/**
	 * @param first
	 * @param second
	 */
	public LstmGenomeSpeciation(LstmGenome first, LstmGenome second) {
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

		int matchingNodeGenes = 0;
		for (Integer nodeGeneId : first.nodes.keySet()) {

			if (second.nodes.containsKey(nodeGeneId)) {

				// Calculate node weight difference
				LstmNodeGene firstNode = first.nodes.get(nodeGeneId);
				LstmNodeGene secondNode = second.nodes.get(nodeGeneId);
				++matchingNodeGenes;
				averageWeightDifference += getAverageWeightdifference(firstNode, secondNode);

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
		averageWeightDifference /= matchingNodeGenes;

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
		for (Integer innovationNumber : first.connections.keySet()) {

			if (second.connections.containsKey(innovationNumber)) {

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

	/**
	 * @param secondNode
	 * @param firstNode
	 * @return
	 */
	private double getAverageWeightdifference(LstmNodeGene firstNode, LstmNodeGene secondNode) {

		double averageWeightDifference = 0;

		// Sum up input weights average
		Weights firstInputWeights = firstNode.weight.inputWeights;
		Weights secondInputWeights = secondNode.weight.inputWeights;
		averageWeightDifference += Math.abs(firstInputWeights.inputGateWeight - secondInputWeights.inputGateWeight);
		averageWeightDifference += Math.abs(firstInputWeights.forgetGateWeight - secondInputWeights.forgetGateWeight);
		averageWeightDifference += Math.abs(firstInputWeights.outputGateWeight - secondInputWeights.outputGateWeight);
		averageWeightDifference += Math.abs(firstInputWeights.selectGateWeight - secondInputWeights.selectGateWeight);

		// Sum up recurrent weights average
		Weights firstRecurrentWeights = firstNode.weight.recurrentWeights;
		Weights secondRecurrentWeights = secondNode.weight.recurrentWeights;
		averageWeightDifference += Math.abs(firstRecurrentWeights.inputGateWeight - secondRecurrentWeights.inputGateWeight);
		averageWeightDifference += Math.abs(firstRecurrentWeights.forgetGateWeight - secondRecurrentWeights.forgetGateWeight);
		averageWeightDifference += Math.abs(firstRecurrentWeights.outputGateWeight - secondRecurrentWeights.outputGateWeight);
		averageWeightDifference += Math.abs(firstRecurrentWeights.selectGateWeight - secondRecurrentWeights.selectGateWeight);

		// Divide by 8 for average
		return averageWeightDifference / 8;
	}

}
