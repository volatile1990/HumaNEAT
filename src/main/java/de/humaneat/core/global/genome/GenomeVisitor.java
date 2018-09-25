package de.humaneat.core.global.genome;

import de.humaneat.core.lstm.genome.LstmGenome;
import de.humaneat.core.neat.genome.Genome;

/**
 * @author muellermak
 *
 */
public interface GenomeVisitor {

	void visit(Genome genome);

	void visit(LstmGenome genome);

}
