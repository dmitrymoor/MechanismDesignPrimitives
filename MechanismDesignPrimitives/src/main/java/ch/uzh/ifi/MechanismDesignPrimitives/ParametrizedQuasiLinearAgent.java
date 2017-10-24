package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class implements a model of an agent with quasi-linear utility parameterized by the "Allocation" object (see AAMAS paper).
 * @author Dmitry Moor
 *
 */
public class ParametrizedQuasiLinearAgent 
{

	private static final Logger _logger = LogManager.getLogger(ParametrizedQuasiLinearAgent.class);
	/**
	 * A simple constructor.
	 * @param endowment initial endowment of the agent
	 * @param allocations an array of binary encoded deterministic allocations of DBs. An element i=0xabc of the array encodes the binary
	 *        allocation of the 1st DB, a={0,1}, of the second, b={0,1} and of the 3rd one, i.e., c={0,1}
	 */
	public ParametrizedQuasiLinearAgent(int id, double endowment, int[] allocations, IParametrizedValueFunction[] valueFunctions)
	{
		if(allocations.length != valueFunctions.length) throw new RuntimeException("Dimensionality mismatch.");
		_logger.debug("ParametrizedQuasiLinearAgent::ParametrizedQuasiLinearAgent("+id+", "+endowment+", " + Arrays.toString(allocations) + ", valueFunctions)");
		
		_id = id;
		_valueFunction = new HashMap<Integer, IParametrizedValueFunction>();
		for(int i = 0; i < allocations.length; ++i)
			_valueFunction.put(allocations[i], valueFunctions[i]);
		
		_endowment = endowment;
	}
	
	/**
	 * The method returns the endowment of the agent.
	 * @return the endowment of the agent
	 */
	public double getEndowment()
	{
		return _endowment;
	}
	
	/**
	 * The method returns agent's Id
	 * @return id of the agent
	 */
	public int getAgentId()
	{
		return _id;
	}
	
	/**
	 * The method computes (expected) utility of the bundle given current allocation.
	 * @param allocation probabilistic allocation of different DBs
	 * @param bundle bundle of goods (i.e., {x0, x1,...} - quantities)
	 * @return utility of the agent for the specified bundle under given allocation
	 */
	public double computeUtility( ProbabilisticAllocation allocation, List<Double> bundle )
	{
		_logger.debug("-> computeUtility( allocation=" + allocation.toString()+ ", bundle= " + bundle.toString() + ")");
		double money = bundle.get(0);
		double goods = bundle.get(1);							//Only one good for now
		
		double expectedMarginalValue = computeExpectedMarginalValue(allocation);
		double expectedThreshold = computeExpectedThreshold(allocation);
		
		_logger.debug("Expected marginal value: " + expectedMarginalValue + "; Expected threshold: " + expectedThreshold);
		
		if( goods <= expectedThreshold )
			return expectedMarginalValue * goods + money;
		else
			return expectedMarginalValue * expectedThreshold + money;
	}
	
	/**
	 * The method solves the consumption problem of the agent given market prices.
	 * @param prices market prices for goods
	 * @param allocation probabilistic allocation of DBs
	 * @return the optimal consumption bundle
	 * @throws IloException 
	 */
	public List<Double> solveConsumptionProblem(List<Double> prices, ProbabilisticAllocation allocation)
	{
		if( prices.get(0) != 1. ) throw new RuntimeException("Price for money must be equal to 1: " + prices.get(0));
		_logger.debug("solveConsumptionProblem("+ Arrays.toString(prices.toArray()) + ", " + Arrays.toString(allocation.getAllocationProbabilities().toArray()) +  ")");
		
		List<Double> optBundle = new LinkedList<Double>();
		double optGood0 = 0.;
		double optGood1 = 0.;
		
		List<Double> singleGoodBundle = new LinkedList<Double>();
		singleGoodBundle.add(0.);
		singleGoodBundle.add(1.);
		
		double expectedMarginalValue = computeExpectedMarginalValue(allocation); 
		double expectedThreshold = computeExpectedThreshold(allocation); 
		_logger.debug("Agent: " + _id + ". expectedMarginalValue=" + expectedMarginalValue);
		_logger.debug("Agent: " + _id + ". expectedThreshold=" + expectedThreshold);
		
		if( prices.get(1) <= expectedMarginalValue )
			optGood1 = expectedThreshold;
		else 
			optGood1 = 0.;
		
		optGood0 = _endowment - prices.get(1) * optGood1;
		if(optGood0 < 0)
		{
			optGood0 = 0;
			optGood1 = _endowment/prices.get(1);
		}
		
		optBundle.add(optGood0);
		optBundle.add(optGood1);
		return optBundle;
	}
	
	/**
	 * The method computes expected marginal value corresponding to the specified probabilistic allocation of DBs
	 * @param allocation probabilistic allocation of DBs
	 * @return expected marginal value
	 */
	public double computeExpectedMarginalValue(ProbabilisticAllocation allocation)
	{
		_logger.debug("computeExpectedMarginalValue("+ Arrays.toString(allocation.getAllocationProbabilities().toArray())+")");
		
		double expectedMarginalValue = 0.;
		int numberOfPossibleDeterministicAllocations = (int)Math.pow(2, allocation.getNumberOfGoods());
		
		for(int i = 0; i < numberOfPossibleDeterministicAllocations; i++)
		{
			double prob = computeProbabilityOfAllocation(i, allocation);
			expectedMarginalValue += prob * ((LinearThresholdValueFunction)(_valueFunction.get(i))).getMarginalValue();
		}
		
		_logger.debug("Agent " + _id + "; Expected Marginal Value for allocation " + allocation.toString() + " is " + expectedMarginalValue);
		return expectedMarginalValue;
	}
	
	/**
	 * The method computes expected threshold corresponding to the specified probabilistic allocation of DBs
	 * @param allocation probabilistic allocation of DBs
	 * @return expected threshold
	 */
	public double computeExpectedThreshold(ProbabilisticAllocation allocation)
	{
		_logger.debug("computeExpectedThreshold("+ Arrays.toString(allocation.getAllocationProbabilities().toArray())+")");
		double expectedThreshold = 0.;
		
		int numberOfPossibleDeterministicAllocations = (int)Math.pow(2, allocation.getNumberOfGoods());
		
		for(int i = 0; i < numberOfPossibleDeterministicAllocations; i++)
		{
			double prob = computeProbabilityOfAllocation(i, allocation);
			expectedThreshold += prob * ((LinearThresholdValueFunction)(_valueFunction.get(i))).getThreshold();
		}
		
		_logger.debug("Agent " + _id + "; Expected Threshold for allocation " + allocation.toString() + " is " + expectedThreshold);
		return expectedThreshold;
	}
	
	/**
	 * The method computes probability of deterministic allocation i to happen given the probabilistic allocation.
	 * @param detAllocation deterministic allocation encoded as a binary integer
	 * @param probAllocation probabilistic allocation of DBs
	 * @return probability of detAllocation to happen
	 */
	private double computeProbabilityOfAllocation(int detAllocation, ProbabilisticAllocation probAllocation)
	{
		_logger.debug("computeProbabilityOfAllocation(" + detAllocation + ", " + Arrays.toString( probAllocation.getAllocationProbabilities().toArray() ) + ")");
		int nBundles = probAllocation.getNumberOfBidders();
		double prob = 1;
		
		for(int bundle = 0; bundle < nBundles; ++bundle)
		{
			//First, check if the bundle needs to be allocated under detAllocation
			int isAllocated = 1;
			isAllocated = isAllocated << bundle;
			
			//Then, use the probability of the bundle to be allocated if it is required by detAllocation
			if( (isAllocated & detAllocation) > 0)
				prob *= probAllocation.getAllocationProbabilityOfBundle(bundle);
			else
				prob *= (1-probAllocation.getAllocationProbabilityOfBundle(bundle));
		}
		
		_logger.debug("The resulting prob is " + prob);
		return prob;
	}
	
	private int _id;													//Agent id
	private double _endowment;											//Initial endowment of the consumer with money
	private Map<Integer, IParametrizedValueFunction> _valueFunction;	//Parameterized value function of the consumer. The Integer represents a binary encoding of an allocation of the DBs
	private double _arrowPrattIdx;										//Risk-aversion measure	
}
