package de.core.lstm.genes;

import de.core.global.components.NodeEngager;
import de.core.neat.genes.connection.ConnectionGene;

/**
 * @author MannoR
 *
 */
public class LSTMNodeEngager extends NodeEngager {

	/**
	 * Activates all lstm node gates
	 */
	@Override
	public void activate() {

		LSTMNodeGene lstmNodeGene = (LSTMNodeGene) node;

		// Engage forget gate
		lstmNodeGene.forgetGateOut[0] = lstmNodeGene.forgetGateActivation.activate(lstmNodeGene.weights[0] * lstmNodeGene.recurrentCellOutput);
		lstmNodeGene.forgetGateOut[0] = lstmNodeGene.forgetGateActivation.activate(lstmNodeGene.weights[0] * lstmNodeGene.inputSum);

		// Engage input gate
		lstmNodeGene.inputGateOut[0] = lstmNodeGene.inputGateActivation.activate(lstmNodeGene.weights[1] * lstmNodeGene.recurrentCellOutput);
		lstmNodeGene.inputGateOut[1] = lstmNodeGene.inputGateActivation.activate(lstmNodeGene.weights[1] * lstmNodeGene.inputSum);

		// Engage select gate
		lstmNodeGene.selectGateOut[0] = lstmNodeGene.selectGateActivation.activate(lstmNodeGene.weights[2] * lstmNodeGene.recurrentCellOutput);
		lstmNodeGene.selectGateOut[1] = lstmNodeGene.selectGateActivation.activate(lstmNodeGene.weights[2] * lstmNodeGene.inputSum);

		// Engage output gate
		lstmNodeGene.outputGateOut[0] = lstmNodeGene.outputGateActivation.activate(lstmNodeGene.weights[3] * lstmNodeGene.recurrentCellOutput);
		lstmNodeGene.outputGateOut[1] = lstmNodeGene.outputGateActivation.activate(lstmNodeGene.weights[3] * lstmNodeGene.inputSum);
	}

	/**
	 * Generates the recurrentCellOutput with the following operations:
	 * 1: Use tanh on the cell state vector
	 * 2: Multiply the output gate result with the cell state vector
	 */
	@Override
	public void fire() {

		LSTMNodeGene lstmNodeGene = (LSTMNodeGene) node;

		lstmNodeGene.recurrentCellOutput = lstmNodeGene.cellStateActivation.activate(lstmNodeGene.cellState[0]) * lstmNodeGene.outputGateOut[0];
		lstmNodeGene.outputValue = lstmNodeGene.cellStateActivation.activate(lstmNodeGene.cellState[1]) * lstmNodeGene.outputGateOut[1];

		// Set the output as payload on all outgoing connections
		for (ConnectionGene connection : lstmNodeGene.outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = lstmNodeGene.outputValue;
			connection.activated = true;
		}
	}

}
