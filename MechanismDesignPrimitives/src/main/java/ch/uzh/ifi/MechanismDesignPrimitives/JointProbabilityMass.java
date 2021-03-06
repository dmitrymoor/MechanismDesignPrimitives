package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openjdk.jmh.annotations.Benchmark;

import ch.uzh.ifi.GraphAlgorithms.Graph;

/**
 * The class implements functionality for modeling joint probability mass functions using the "bombing" approach.
 * @author Dmitry Moor
 */
public class JointProbabilityMass
{
	
	private static final Logger _logger = LogManager.getLogger(JointProbabilityMass.class);
	
	/**
	 * @param dependencyGraph - the graph representing dependencies between random variables
	 */
	public JointProbabilityMass(Graph dependencyGraph)
	{
		_logger.debug("JointProbabilityMass::JointProbabilityMass( dependencyGraph )");
		if( dependencyGraph.getVertices().size() == 0)	throw new RuntimeException("No RVs specified");
			
		_numberOfRandomVars = dependencyGraph.getVertices().size();
		_dependencyGraph = dependencyGraph;
		
		_samples = new double[_nSamples][_numberOfRandomVars];
		_marginalAvailabilities = new HashMap<List<Integer>, Double>();
	}
	
	/**
	 * The method regenerates jpmf
	 */
	public void update()
	{
		_samples = new double[_nSamples][_numberOfRandomVars];
		generateSamples();
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str="jpmf";
		return str;
	}
	
	/**
	 * The method generates a deep copy of this object
	 * @return a deep copy of the object
	 */
	public JointProbabilityMass copyIt()
	{
		JointProbabilityMass jpmf = new JointProbabilityMass(_dependencyGraph);
		jpmf.setNumberOfSamples(_nSamples);
		for(int i = 0; i < _nSamples; ++i)
			jpmf.setSample(i, getSample(i));
		return jpmf;
	}
	
	/**
	 * The method sets the number of "bombs" to be used when sampling. Not equivalent to the number of types
	 * of bombs as several bombs of the same type might be thrown.
	 * @param numberOfBombs - number of "bombs"
	 */
	public void setNumberOfBombsToThrow(int numberOfBombs)
	{
		_nBombsToThrow = numberOfBombs;
	}
	
	/**
	 * The method sets the number of samples to be used for estimating marginal probabilities.
	 * @param numberOfSamples - the number of samples
	 */
	public void setNumberOfSamples(int numberOfSamples)
	{
		_nSamples = numberOfSamples;
		_samples = new double[_nSamples][_numberOfRandomVars];
	}
	
	/**
	 * The method sets up the list of bombing strategies and the corresponding probability distribution
	 * @param bombingStrategies - a list of bombing strategies
	 * @param probDistribution - probability distribution
	 */
	public void setBombs(List<IBombingStrategy> bombingStrategies, List<Double> probDistribution)
	{
		if( bombingStrategies.size() != probDistribution.size() ) throw new RuntimeException("Dimension mismatch");
		
		_bombingStrategies = bombingStrategies;
		_bombsProbDistribution = probDistribution;
	}
	
	/**
	 * The method returns a marginal probability of a bundle of nodes to be available.
	 * @param bundle a bundle
	 * @param conditioningRVs a list of realized random variables
	 * @param realizationsOfRVs realized values of conditioningRVs
	 * @return a marginal probability
	 */
	public double getMarginalProbability(List<Integer> bundle, List<Integer> conditioningRVs, List<Double> realizationsOfRVs)
	{
		//if(_marginalAvailabilities.containsKey(bundle))
		//{
			//_logger.debug("The marginal prob. for the bundle: " + bundle.toString() + " already exists: " + _marginalAvailabilities.get(bundle));
		//	return _marginalAvailabilities.get(bundle);
		//}

		double marginalProbability = 0.;
		int numberOfSamples = 0;
		for(int i = 0; i < _nSamples; ++i)
		{
			final int sampleIdx = i;
			
			List<Double> availabilitiesofGoodsInBundle = new ArrayList<Double>();
			for(int j = 0; j < _numberOfRandomVars; ++j)
			{
				int goodId = j+1;
				if(bundle.contains(goodId))
					availabilitiesofGoodsInBundle.add( _samples[sampleIdx][goodId-1] );
			}
			
			double minAvailability = Double.MAX_VALUE;
			for(Double a : availabilitiesofGoodsInBundle )
				if( a < minAvailability)
					minAvailability = a;
				
			//double minAvailability = IntStream.range(0, _numberOfRandomVars).boxed()		//Equivalent to the lines above but the performance is lower
			//		.map(j -> j+1).filter( gId ->  bundle.contains( gId ))
			//		.map( gId -> _samples[sampleIdx][gId-1] )
			//		.min( (x1, x2) -> x1.compareTo(x2) ).get();

			boolean isConditioningSatisfied = true;
			if(conditioningRVs != null && realizationsOfRVs != null)
			{
				for(int j = 0; j < conditioningRVs.size(); ++j)
					if( _samples[sampleIdx][conditioningRVs.get(j) - 1] != realizationsOfRVs.get(j) )
					{
						isConditioningSatisfied = false;
						break;
					}
				//isConditioningSatisfied = IntStream.range(0, conditioningRVs.size()).boxed()
				//		.filter( j -> _samples[sampleIdx][conditioningRVs.get(j) - 1] != realizationsOfRVs.get(j))
				//		.count() > 0 ? false : true;
			}
			
			if(isConditioningSatisfied)
			{
				marginalProbability += minAvailability;
				numberOfSamples += 1;
			}
		}
		//_marginalAvailabilities.put(bundle, marginalProbability / _nSamples);
		return marginalProbability / numberOfSamples;
	}
	
	/**
	 * The method returns a random sample.
	 * @return a random sample
	 */
	public double[] getSample()
	{
		int sampleIdx = (int)(Math.random() * _nSamples);
		return _samples[sampleIdx];
	}
	
	/**
	 * The method returns a sample from the set of generated samples
	 * @param sIdx an index of the sample
	 * @return the sample corresponding to the specified index
	 */
	public double[] getSample(int sIdx)
	{
		return _samples[sIdx];
	}
	
	/**
	 * The method set the sample
	 * @param sIdx
	 * @param sample
	 */
	public void setSample(int sIdx, double[] sample)
	{
		for(int i = 0; i < _numberOfRandomVars; ++i)
			_samples[sIdx][i] = sample[i];
	}
	
	/**
	 * The method generates samples from the joint pmf using "bombing" algorithm (described in comments).
	 * A sample is a state of every node in the graph after the specified number of bombs was thrown.
	 */
	private void generateSamples()
	{
		_logger.debug("->generateSamples()");
		
		for(int i = 0; i < _nSamples; ++i)							//TODO: parallelize this
		{
			double[] sample = new double[_numberOfRandomVars];		//Initialize states of every node in the dependency graph
			for(int j = 0; j < _numberOfRandomVars; ++j)
				sample[j] = 1;
			
			for(int j = 0; j < _nBombsToThrow; ++j)					//Throw the specified number of bombs into the dependency graph
				sample = throwDeterministicBomb(sample);
			
			for(int j = 0; j < _numberOfRandomVars; ++j)			//Add the sample to the set of generated samples
				_samples[i][j] = sample[j];
		}
		_logger.debug("<-generateSamples()");
	}
	
	/**
	 * The method throws a "bomb" into the grid
	 * @param sample - a sample to be bombed
	 * @return the sample after being bombed
	 */
	private double[] throwDeterministicBomb(double[] sample)
	{
		int nodeToBomb = (int)Math.floor(Math.random() * _numberOfRandomVars);				//Pick a random node uniformly

		do
		{
			int bombToThrow = (int)Math.floor(Math.random() * _bombingStrategies.size());	//Choose a bomb to be thrown according to the prob. of falling
			double prob = Math.random();
			if( prob <= _bombsProbDistribution.get(bombToThrow) )
				return _bombingStrategies.get(bombToThrow).applyBomb(sample, nodeToBomb);
		}
		while(true);
	}
	
	/**
	 * The method returns a list of bombing strategies used to generate the jpmf
	 * @return a list of bombing strategies
	 */
	public List<IBombingStrategy> getBombs()
	{
		return _bombingStrategies;
	}
	
	private int _numberOfRandomVars;						//The number of random variables (= #nodes in the dependency graph)
	private Graph _dependencyGraph;							//Dependency graph of the spatial domain
	private double[][] _samples;							//An array of _nSamples per every RV
	private Map<List<Integer>, Double> _marginalAvailabilities;//Expected marginal availabilities of bundles
	
	private int _nSamples;									//Number of samples
	private List<IBombingStrategy> _bombingStrategies;		//Possible bombing strategies
	private List<Double>		   _bombsProbDistribution;	//Probability distribution over possible bombing strategies
	private int _nBombsToThrow;								//Number of bombs to be used by the bombing algorithm
}
