package de.humaneat.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

import de.humaneat.core.global.Random;
import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.neat.genes.Counter;
import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genes.node.NodeGene;
import de.humaneat.core.neat.genome.Genome;

/**
 * @author MannoR
 *
 */
/**
 * @author MannoR
 *
 */
public class GenomeViewer extends SimpleApplication {

	public Map<Integer, Geometry> nodeGeomMap;

	public Genome currentGenome;
	public Genome genome;

	public List<Geometry> items;

	public boolean initialized;

	public GenomeViewer(Genome genome) {
		this.genome = genome;
		items = new ArrayList<>();
		initialized = false;
	}

	@Override
	public void simpleInitApp() {
		display(genome);
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		if (genome != currentGenome) {
			display(genome);
		}
	}

	/**
	 * @param genome
	 */
	public void display(Genome genome) {

		for (Geometry item : items) {
			rootNode.detachChild(item);
		}

		currentGenome = genome;

		nodeGeomMap = new HashMap<>();

		List<NodeGene> inputNodes = genome.getNodesByType(NodeGeneType.INPUT);
		inputNodes.add(genome.biasNode);
		List<NodeGene> hiddenNodes = genome.getNodesByType(NodeGeneType.HIDDEN);
		List<NodeGene> outputNodes = genome.getNodesByType(NodeGeneType.OUTPUT);

		int[] nodePosition = new int[3];
		for (int i = 0; i < inputNodes.size(); ++i) {

			nodePosition[0] = i;
			nodePosition[1] = 1;
			nodePosition[2] = i / 2;

			NodeGene inputNode = inputNodes.get(i);
			nodeGeomMap.put(inputNode.innovationNumber, drawInputNodes(inputNode, nodePosition));
		}

		for (int i = 0; i < hiddenNodes.size(); ++i) {

			nodePosition[0] = i;
			nodePosition[1] = (int) Random.random(2d, 4d);
			nodePosition[2] = i / 2;

			NodeGene hiddenNode = hiddenNodes.get(i);
			nodeGeomMap.put(hiddenNode.innovationNumber, drawHiddenNodes(hiddenNode, nodePosition));
		}

		for (int i = 0; i < outputNodes.size(); ++i) {

			nodePosition[0] = i;
			nodePosition[1] = 5;
			nodePosition[2] = i / 2;

			NodeGene outNode = outputNodes.get(i);
			nodeGeomMap.put(outNode.innovationNumber, drawOutputNodes(outNode, nodePosition));
		}

		for (ConnectionGene connection : genome.connections.values()) {
			drawConnection(nodeGeomMap.get(connection.from.innovationNumber), nodeGeomMap.get(connection.to.innovationNumber));
		}

		flyCam.setMoveSpeed(50);

		addLight();

		initialized = true;
	}

	/**
	 * @param innovationNumber
	 * @param geometry
	 * @param nodeShapes2
	 */
	private void drawConnection(Geometry geometryFrom, Geometry geometryTo) {

		float distance = geometryFrom.getLocalTranslation().distance(geometryTo.getLocalTranslation());

		Cylinder cyliner = new Cylinder(50, 50, 0.01f, distance);

		Geometry geom = new Geometry("Connection_" + Random.random(1, 1000), cyliner);
		Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);
		material.setColor("Ambient", ColorRGBA.Gray);
		material.setColor("Diffuse", ColorRGBA.Gray);
		geom.setMaterial(material);

		geom.setLocalTranslation(FastMath.interpolateLinear(.5f, geometryFrom.getLocalTranslation(), geometryTo.getLocalTranslation()));
		geom.lookAt(geometryTo.getLocalTranslation(), Vector3f.UNIT_Y);

		rootNode.attachChild(geom);
		items.add(geom);

	}

	/**
	 *
	 */
	private void addLight() {

		DirectionalLight sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White);
		sun.setDirection(new Vector3f(-.9f, -.9f, -.9f).normalizeLocal());
		rootNode.addLight(sun);

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.Gray.mult(1.3f));
		rootNode.addLight(al);
	}

	/**
	 * @param node
	 */
	private Geometry drawInputNodes(NodeGene node, int[] position) {

		Sphere nodeSphere = new Sphere(50, 50, 0.1f);

		Geometry geom = new Geometry("InputNode_" + Random.random(1, 1000), nodeSphere);
		Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);

		if (node == genome.biasNode) {
			material.setColor("Ambient", ColorRGBA.Magenta);
			material.setColor("Diffuse", ColorRGBA.Magenta);
		} else {
			material.setColor("Ambient", ColorRGBA.Green);
			material.setColor("Diffuse", ColorRGBA.Green);
		}
		geom.setMaterial(material);

		geom.setLocalTranslation(position[0], position[1], position[2]);

		rootNode.attachChild(geom);
		items.add(geom);

		return geom;
	}

	/**
	 * @param node
	 */
	private Geometry drawHiddenNodes(NodeGene node, int[] position) {

		Sphere nodeSphere = new Sphere(50, 50, 0.1f);

		Geometry geom = new Geometry("HiddenNode_" + Random.random(1, 1000), nodeSphere);
		Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);
		material.setColor("Ambient", ColorRGBA.Blue);
		material.setColor("Diffuse", ColorRGBA.Blue);
		geom.setMaterial(material);

		geom.setLocalTranslation(position[0], position[1], position[2]);

		rootNode.attachChild(geom);
		items.add(geom);

		return geom;
	}

	/**
	 * @param node
	 */
	private Geometry drawOutputNodes(NodeGene node, int[] position) {

		Sphere nodeSphere = new Sphere(50, 50, 0.1f);

		Geometry geom = new Geometry("OutputNode_" + Random.random(1, 1000), nodeSphere);
		Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);
		material.setColor("Ambient", ColorRGBA.Red);
		material.setColor("Diffuse", ColorRGBA.Red);
		geom.setMaterial(material);

		geom.setLocalTranslation(position[0], position[1], position[2]);

		rootNode.attachChild(geom);
		items.add(geom);

		return geom;
	}

	/**
	 *
	 */
	private Genome createGenome() {

		Genome genome = new Genome();

		Counter nodeInnovation = new Counter();
		Counter connectionInnovation = new Counter();

		NodeGene input1 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene input2 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene hidden1 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene hidden2 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, nodeInnovation.getNext());

		ConnectionGene in1ToHidden1 = new ConnectionGene(input1, hidden1, 0.75f, true, connectionInnovation.getNext());
		ConnectionGene int2ToHidden1 = new ConnectionGene(input2, hidden1, 0.25f, true, connectionInnovation.getNext());
		ConnectionGene int2ToHidden2 = new ConnectionGene(input2, hidden2, 0.25f, true, connectionInnovation.getNext());
		ConnectionGene hiddenToOut = new ConnectionGene(hidden1, output, 2.5f, true, connectionInnovation.getNext());
		ConnectionGene hidden2ToOut = new ConnectionGene(hidden2, output, 2.5f, true, connectionInnovation.getNext());

		genome.addNodeGene(input1);
		genome.addNodeGene(input2);
		genome.addNodeGene(hidden1);
		genome.addNodeGene(hidden2);
		genome.addNodeGene(output);

		genome.addConnectionGene(in1ToHidden1);
		genome.addConnectionGene(int2ToHidden1);
		genome.addConnectionGene(int2ToHidden2);
		genome.addConnectionGene(hiddenToOut);
		genome.addConnectionGene(hidden2ToOut);

		genome.getLinker().generateNetwork();

		return genome;
	}

}
