package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class implements a model of an agent with quasi-linear utility parameterized by the "Allocation" object (see AAMAS paper).
 * @author Dmitry Moor
 *
 */
public class ParametrizedQuasiLinearAgent 
{

	/**
	 * A simple constructor.
	 * @param endowment initial endowment of the agent
	 * @param allocations an array of binary encoded deterministic allocations of DBs. An element i=0xabc of the array encodes the binary
	 *        allocation of the 1st DB, a={0,1}, of the second, b={0,1} and of the 3rd one, i.e., c={0,1}
	 */
	public ParametrizedQuasiLinearAgent(double endowment, int[] allocations, IParametrizedValueFunction[] valueFunctions)
	{
		if(allocations.length != valueFunctions.length) throw new RuntimeException("Dimensionality mismatch.");
		
		_valueFunction = new HashMap<Integer, IParametrizedValueFunction>();
		for(int i = 0; i < allocations.length; ++i)
			_valueFunction.put(allocations[i], valueFunctions[i]);
		
		_endowment = endowment;
	}
	
	/**
	 * The method computes (expected) utility of the bundle given current allocation.
	 * @param allocation probabilistic allocation of different DBs
	 * @param bundle bundle of goods (i.e., {x0, x1,...} - quantities)
	 * @return 
	 */
	public double computeUtility( ProbabilisticAllocation allocation, List<Double> bundle )
	{
		double utility = 0.;
		double expectedValue = 0.;
		int numberOfPossibleAllocations = (int)Math.pow(2, allocation.getNumberOfAllocatedBundles());
		double money = bundle.get(0);
		double[] goods = {bundle.get(1)};
		
		for(int i = 0; i < numberOfPossibleAllocations; i++)
		{
			double p = computeProbabilityOfAllocation(i, allocation);
			expectedValue += p * this._valueFunction.get(i).computeValue(goods);
		}
				
		return utility;
	}
	
	/**
	 * The method computes probability of deterministic allocation i to happen given the probabilistic allocation.
	 * @param detAllocation deterministic allocation encoded as a binary integer
	 * @param probAllocation probabilistic allocation of DBs
	 * @return probability of detAllocation to happen
	 */
	private double computeProbabilityOfAllocation(int detAllocation, ProbabilisticAllocation probAllocation)
	{
		return 0;
	}
	
	private double _endowment;											//Initial endowment of the consumer with money
	private Map<Integer, IParametrizedValueFunction> _valueFunction;	//Parameterized value function of the consumer. The Integer represents a binary encoding of an allocation of the DBs
	private double _arrowPrattIdx;										//Risk-aversion measure
}
