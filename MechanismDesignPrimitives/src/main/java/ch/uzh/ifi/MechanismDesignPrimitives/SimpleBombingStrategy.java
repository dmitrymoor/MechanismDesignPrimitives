package ch.uzh.ifi.MechanismDesignPrimitives;

import ch.uzh.ifi.GraphAlgorithms.Graph;

/**
 * 
 * @author Dmitry Moor
 *
 */
public abstract class SimpleBombingStrategy implements IBombingStrategy
{	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.IBombingStrategy#applyBomb(double[], int)
	 */
	@Override
	public abstract double[] applyBomb(double[] sample, int nodeToBomb);
	
	protected Graph  _dependencyGraph;				//Dependency graph for RVs
	protected double _probabilityToExplode;			//Probability that the bomb actually affects the sample
}
