package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class implements a model of an agent with a quasi-linear utility function parameterized by the "Allocation" object (see AAMAS paper).
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
	public ParametrizedQuasiLinearAgent(int id, double endowment, List< LinearThresholdValueFunction> valueFunctions)
	{
		_logger.debug("ParametrizedQuasiLinearAgent::ParametrizedQuasiLinearAgent("+id+", "+endowment+", valueFunctions)");
		
		_id = id;
		_valueFunction = valueFunctions;
		_endowment = endowment;
	}
	
	/**
	 * A simple constructor.
	 * @param endowment initial endowment of the agent
	 * @param allocations an array of binary encoded deterministic allocations of DBs. An element i=0xabc of the array encodes the binary
	 *        allocation of the 1st DB, a={0,1}, of the second, b={0,1} and of the 3rd one, i.e., c={0,1}
	 */
	public ParametrizedQuasiLinearAgent(int id, double endowment, Map<Integer, LinearThresholdValueFunction> valueFunctions)
	{
		_logger.debug("ParametrizedQuasiLinearAgent::ParametrizedQuasiLinearAgent("+id+", "+endowment+", valueFunctions)");
		
		_id = id;
		_valueFunction = new CopyOnWriteArrayList<LinearThresholdValueFunction>();
		for(int i = 0; i < valueFunctions.size(); ++i)
			_valueFunction.add(valueFunctions.get(i));
		
		_endowment = endowment;
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "Agent id=" + _id + ":\n";
		//for(Map.Entry<Integer, LinearThresholdValueFunction> entry: _valueFunction.entrySet())
		//	str += entry.getKey() + " v'=" + entry.getValue().getMarginalValue() + " " + 
		//			                " xMax =" + entry.getValue().getThreshold() + "\n";
		return str;
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
	public double computeUtility( int detAlloc, int numberOfGoods, List<Double> bundle )
	{
		//updateAllocProbabilityDistribution(allocation);
		updateAllocProbabilityDistribution(detAlloc, numberOfGoods);
		return computeUtility( bundle );
	}
	
	/**
	 * The method computes (expected) utility of the bundle given current allocation.
	 * @param allocation probabilistic allocation of different DBs
	 * @param bundle bundle of goods (i.e., {x0, x1,...} - quantities)
	 * @return utility of the agent for the specified bundle under given allocation
	 */
	public double computeUtility( List<Double> bundle )
	{
		double money = bundle.get(0);
		double goods = bundle.get(1);							//Only one good for now
		
		if( goods <= _expectedThreshold )
			return _expectedMarginalValue * goods + money;
		else
			return _expectedMarginalValue * _expectedThreshold + money;
	}
	
	/**
	 * The method updates the probability distribution over deterministic allocations.
	 * @param probAllocation probabilistic allocation
	 */
	/*public void updateAllocProbabilityDistribution(ProbabilisticAllocation probAllocation)
	{
		int numberOfDeterministicAllocations = (int)Math.pow(2, probAllocation.getNumberOfGoods());
		int[] detAllocations = new int[numberOfDeterministicAllocations]; 
		double[] probabilities = new double[numberOfDeterministicAllocations];
		double[] vals = new double[numberOfDeterministicAllocations];
		double[] threshold = new double[numberOfDeterministicAllocations];
		
		try
		{
			List<Thread> threads = new LinkedList<Thread>();
			for(int i = 0; i < _numberOfThreads; ++i)
			{
				Thread thread = new Thread(new DistributionUpdateWorker("Thread", i, numberOfDeterministicAllocations, probAllocation, 
						 												probabilities, detAllocations, vals, threshold) );
				threads.add(thread);
			}
			
			for(int i = 0; i < _numberOfThreads; ++i)
				threads.get(i).start();
			
			for(int i = 0; i < _numberOfThreads; ++i)
				threads.get(i).join(0);
		}
		catch(InterruptedException e)
		{
		    e.printStackTrace();
		}

		_expectedMarginalValue = 0;
		for(double v : vals)
			_expectedMarginalValue += v;
		
		_expectedThreshold = 0;
		for(double t : threshold)
			_expectedThreshold += t;
		
		_allocProbDistribution = new EnumeratedIntegerDistribution(detAllocations, probabilities);
		_logger.debug("Updated expected marginal value of buyer " + _id + " is: " + _expectedMarginalValue + "(=" +computeExpectedMarginalValue() +"); expected threshold: " + _expectedThreshold + "(="+ computeExpectedThreshold() + ")");
	}*/
	
	/**
	 * The method updates the probability distribution over deterministic allocations.
	 * @param probAllocation probabilistic allocation
	 */
	public void updateAllocProbabilityDistribution(int detAlloc, int numberOfGoods)
	{
		//int numberOfDeterministicAllocations = (int)Math.pow(2, numberOfGoods);
		//int[] detAllocations = new int[numberOfDeterministicAllocations]; 
		//double[] probabilities = new double[numberOfDeterministicAllocations];
		
		//probabilities[detAlloc] = 1.;
		_expectedMarginalValue = /*probabilities[detAlloc] */ _valueFunction.get(detAlloc).getMarginalValue();
		_expectedThreshold = /*probabilities[detAlloc] */ _valueFunction.get(detAlloc).getThreshold();
		//_allocProbDistribution = new EnumeratedIntegerDistribution(detAllocations, probabilities);
		_detAlloc = detAlloc;
		_logger.debug("Updated expected marginal value of buyer " + _id + " is: " + _expectedMarginalValue + "(=" +computeExpectedMarginalValue() +"); expected threshold: " + _expectedThreshold + "(="+ computeExpectedThreshold() + ")");
	}
	
/*	public AbstractIntegerDistribution getAllocProbabilityDistribution()
	{
		return _allocProbDistribution;
	}*/
	
/*	public void setAllocProbabilityDistribution(AbstractIntegerDistribution allocProbDistribution)
	{
		_allocProbDistribution = allocProbDistribution;
		_expectedMarginalValue = computeExpectedMarginalValue();
		_expectedThreshold = computeExpectedThreshold();		
		_logger.debug("Updated expected marginal value of buyer " + _id + " is: " + _expectedMarginalValue + "; expected threshold: " + _expectedThreshold);
	}*/
	
	/**
	 * The method solves the consumption problem of the agent given market prices and allocation of DBs.
	 * @param price market price for rows
	 * @param allocation probabilistic allocation of DBs
	 * @return the optimal consumption bundle
	 * @throws IloException 
	 */
	public List<Double> solveConsumptionProblem(double price)
	{
		// Optimal bundle
		List<Double> optBundle = new LinkedList<Double>();
		double optGood0 = 0.;
		double optGood1 = 0.;
		
		// If the marginal value is smaller than the price, then don't consume. Otherwise, consume the maximum amount.
		if( price <= _expectedMarginalValue )
			optGood1 = _expectedThreshold;
		else 
			optGood1 = 0.;
		
		optGood0 = _endowment - price * optGood1;
		if(optGood0 < 0)
		{
			optGood0 = 0;
			optGood1 = _endowment/price;
		}
		
		optBundle.add(optGood0);
		optBundle.add(optGood1);
		_logger.debug("Agent: " + _id + ". Given price " + price + " my demand: " + optBundle.get(0) + ", " + optBundle.get(1));
		return optBundle;
	}
	
	/**
	 * The method computes expected marginal value corresponding to the specified probabilistic allocation of DBs
	 * @param probAllocation probabilistic allocation of sellers
	 * @return expected marginal value
	 */
	public double computeExpectedMarginalValue(ProbabilisticAllocation probAllocation)
	{
		_logger.debug("computeExpectedMarginalValue("+ Arrays.toString(probAllocation.getAllocationProbabilities().toArray())+")");
		_numberOfGoods = probAllocation.getNumberOfGoods();
		
		double expectedMarginalValue = computeExpectedMarginalValue();
		_logger.debug("Agent " + _id + "; Expected Marginal Value for allocation " + probAllocation.toString() + " is " + expectedMarginalValue);
		
		return expectedMarginalValue;
	}
	
	/**
	 * The method computes expected marginal value corresponding to the specified probabilistic allocation of DBs
	 * @return expected marginal value
	 */
	public double computeExpectedMarginalValue()
	{
		double expectedMarginalValue = 0.;
		//int numberOfDeterministicAllocations = (int)Math.pow(2, _numberOfGoods);
		
		expectedMarginalValue = _valueFunction.get(_detAlloc).getMarginalValue();
		/*for(int detAllocation = 0; detAllocation < numberOfDeterministicAllocations; detAllocation++)
		{
			double prob = _allocProbDistribution.probability(detAllocation);
			expectedMarginalValue += prob * _valueFunction.get(detAllocation).getMarginalValue();
			//_logger.debug("Agent " + _id + ": updating v' += " + prob + " * " + _valueFunction.get(detAllocation).getMarginalValue());
		}*/
		
		return expectedMarginalValue;
	}
	
	/**
	 * The method computes expected threshold corresponding to the specified probabilistic allocation of DBs
	 * @param probAllocation probabilistic allocation of DBs
	 * @return expected threshold
	 */
	public double computeExpectedThreshold(ProbabilisticAllocation probAllocation)
	{
		_logger.debug("computeExpectedThreshold("+ Arrays.toString(probAllocation.getAllocationProbabilities().toArray())+")");
		_numberOfGoods = probAllocation.getNumberOfGoods();
		
		double expectedThreshold = computeExpectedThreshold();
		_logger.debug("Agent " + _id + "; Expected Threshold for allocation " + probAllocation.toString() + " is " + expectedThreshold);
		
		return expectedThreshold;
	}
	
	/**
	 * The method computes expected threshold corresponding to the specified probabilistic allocation of DBs
	 * @return expected threshold
	 */
	public double computeExpectedThreshold()
	{
		double expectedThreshold = 0.;
		//int numberOfDeterministicAllocations = (int)Math.pow(2, _numberOfGoods);
		
		expectedThreshold = _valueFunction.get(_detAlloc).getThreshold();
/*		for(int detAllocation = 0; detAllocation < numberOfDeterministicAllocations; detAllocation++)
		{
			double prob = _allocProbDistribution.probability(detAllocation);
			expectedThreshold += prob * _valueFunction.get(detAllocation).getThreshold();
		}*/
		
		return expectedThreshold;
	}
	
	/**
	 * The method computes probability of a particular deterministic allocation to happen given the probabilistic allocation.
	 * @param detAllocation deterministic allocation encoded as a binary integer
	 * @param probAllocation probabilistic allocation of DBs
	 * @return probability of detAllocation to happen
	 */
	private double computeProbabilityOfAllocation(int detAllocation, ProbabilisticAllocation probAllocation)
	{
		//_logger.debug("computeProbabilityOfAllocation(" + detAllocation + ", " + Arrays.toString( probAllocation.getAllocationProbabilities().toArray() ) + ")");
		int nGoods = probAllocation.getNumberOfBidders();						// Single-minded bidders (bidders==sellers in the reverse auction)
		double prob = 1;
		
		for(int good = 0; good < nGoods; ++good)
		{
			//First, check if the good (DB) needs to be allocated under detAllocation
			long isAllocated = 1;
			isAllocated = isAllocated << good;
			
			//Then, use the probability of the bundle to be allocated if it is required by detAllocation
			if( (isAllocated & detAllocation) > 0)
				prob *= probAllocation.getAllocationProbabilityOfBundle(good);
			else
				prob *= 1-probAllocation.getAllocationProbabilityOfBundle(good);
		}
		
		if( prob < -1e-6) throw new RuntimeException("Negative probability: " + prob + " of det. allocation " + detAllocation);
		else if (prob < 0) prob = 0.;

		_logger.debug("The resulting prob of det. alloc " + detAllocation + " is " + prob);
		return prob;
	}
	
	public void setNumberOfGoods(int nGoods)
	{
		_numberOfGoods = nGoods;
	}
	
	/**
	 * The class implements a thread worker that recomputes the probability distribution over deterministic allocations.
	 * @author Dmitry Moor
	 *
	 */
	private class DistributionUpdateWorker implements Runnable
	{
		private Thread _thread;										// A thread object
		private String _threadName;									// The thread's name
		private int _threadId;										// A thread id
		private ProbabilisticAllocation _alloc;
		private int _nDeterministicAllocations;						// Number of deterministic allocations
		private int _idxLow;										// Lower bound of the thread index range
		private int _idxHigh;										// Upper bound of the thread index range
		private double[] _probabilities;							// Probabilities of deterministic allocations
		private int[] _detAllocations;								// Deterministic allocations in binary form
		private double[] _vals;
		private double[] _threshold;
		
		public DistributionUpdateWorker(String name, int threadId, int nDeterministicAllocations, ProbabilisticAllocation allocation, double[] probabilities, 
				            int[] detAlloc, double[] vals, double[] threshold)
		{
			_threadName = name + threadId;
			_threadId = threadId;
			_alloc = allocation;
			_nDeterministicAllocations = nDeterministicAllocations;
			_probabilities = probabilities;
			_detAllocations = detAlloc;
			_vals = vals;
			_threshold = threshold;
			
			_idxLow = _threadId * _nDeterministicAllocations / _numberOfThreads;
			_idxHigh = (_threadId + 1) * _nDeterministicAllocations / _numberOfThreads - 1;
			_logger.debug("Thread " + _threadId + " processes det allocations ["+_idxLow +", " + _idxHigh+"]");
		}
		
		@Override
		public void run() 
		{							
			for( int i = _idxLow; i <= _idxHigh; ++i )
			{
				_detAllocations[i] = i;
				_probabilities[i] = computeProbabilityOfAllocation(_detAllocations[i], _alloc);		// Probability of a certain deterministic allocation

				if( _probabilities[i] < 0. && _probabilities[i] > -1e-6 )
					_probabilities[i] = 0.;
				else if ( _probabilities[i] < 0. ) throw new RuntimeException("Negative prob: " + _probabilities[i]);
				
				_vals[i] = _probabilities[i] * _valueFunction.get(_detAllocations[i]).getMarginalValue();
				_threshold[i] = _probabilities[i] * _valueFunction.get(_detAllocations[i]).getThreshold();
			}			
		}
		
		public void start()
		{
			if(_thread == null)
			{
				_thread = new Thread(this, _threadName);
				_thread.start();
			}
		}
	}
	
	public void setNumberOfThreads(int numberOfThreads)
	{
		_numberOfThreads = numberOfThreads;
	}
	
	private int _id;												// Agent id
	private double _endowment;										// Initial endowment of the consumer with money
	private int _numberOfGoods;										// Number of goods
	private List<LinearThresholdValueFunction> _valueFunction;		// Parameterized value function of the consumer. The Integer represents a binary encoding of an allocation of the DBs
	private double _arrowPrattIdx;									// Risk-aversion measure	
	//private AbstractIntegerDistribution _allocProbDistribution;		// Probability distribution over deterministic allocations
	private double _expectedMarginalValue;							// Expected marginal value over deterministic allocation
	private double _expectedThreshold;								// Expected threshold over deterministic allocation
	private int _numberOfThreads = 4;								// Number of threads
	
	private int _detAlloc;
}
