/*
 * This file is licensed to You under the "Simplified BSD License".
 * You may not use this software except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/bsd-license.php
 * 
 * See the COPYRIGHT file distributed with this work for information
 * regarding copyright ownership.
 */
package zuna.clustering.HAC.dendrogram;


/**
 * An ObservationNode represents a leaf node in a Dendrogram.
 * It corresponds to a singleton cluster of one observation.
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class ObservationNode implements DendrogramNode {

	private final int observation;
	private String id;
	public ObservationNode(final int observation, String id) {
		this.observation = observation;
		this.id = id;
	}

	
	public String getId() {
		return id;
	}



	public final DendrogramNode getLeft() {
		return null;
	}
	
	public final DendrogramNode getRight() {
		return null;
	}
	
	public int getObservationCount() {
		return 1;
	}
	
	public final int getObservation() {
		return observation;
	}

}