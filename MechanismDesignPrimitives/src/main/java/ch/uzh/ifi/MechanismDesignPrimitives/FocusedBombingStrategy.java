package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.List;

import ch.uzh.ifi.GraphAlgorithms.Graph;
import ch.uzh.ifi.GraphAlgorithms.VertexCell;

/**
 * 
 * @author Dmitry Moor
 *
 */
public class FocusedBombingStrategy extends SimpleBombingStrategy
{

	/**
	 * A simple constructor.
	 * @param dependencyGraph - graph of dependencies between RVs
	 * @param probabilityToExplode - probability that the bomb affects the sample
	 * @param primaryReductionCoefficient - reduction coef. for the epicenter of the bomb explosion
	 * @param secondaryReductionCoefficient - reduction coef. for neighbors of the bomb explosion
	 */
	public FocusedBombingStrategy( Graph dependencyGraph, double probabilityToExplode, double primaryReductionCoefficient, double secondaryReductionCoefficient )
	{
		_dependencyGraph = dependencyGraph;
		_probabilityToExplode = probabilityToExplode;
		_primaryReductionCoefficient = primaryReductionCoefficient;
		_secondaryReductionCoefficient = secondaryReductionCoefficient;
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.SimpleBombingStrategy#applyBomb(double[], int)
	 */
	@Override
	public double[] applyBomb(double[] sample, int nodeToBomb) 
	{
		sample[nodeToBomb] -= _primaryReductionCoefficient;					// 1 -> (1-_primaryReductionCoef)
		
		List<VertexCell> itsNeighbors = _dependencyGraph.getAdjacencyLists().get(nodeToBomb);
		for(int i = 0; i < itsNeighbors.size(); ++i)
			sample[itsNeighbors.get(i)._v.getID()-1] =  Math.max(0., (double)sample[itsNeighbors.get(i)._v.getID()-1] - _secondaryReductionCoefficient );
		
		return sample;
	}

	protected double _primaryReductionCoefficient;							//Reduction coef. for the epicenter of the bomb explosion
	protected double _secondaryReductionCoefficient;						//Reduction coef. for neighbors of the bomb explosion
}
