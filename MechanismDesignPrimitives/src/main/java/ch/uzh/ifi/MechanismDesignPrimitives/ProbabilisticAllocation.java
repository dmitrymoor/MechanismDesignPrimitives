package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The class implements a probabilistic allocation, i.e., a data structure that stores allocated bundles (goods)
 * and their probabilities to be allocated to a particular bidder.
 * @author Dmitry Moor
 *
 */
public class ProbabilisticAllocation extends Allocation
{

	/**
	 * A simple constructor.
	 */
	public ProbabilisticAllocation()
	{
		
	}
	
	/**
	 * The method adds a new agents in the list of allocated agents (buyers in a reverse auction).
	 * @param auctioneerId an ID of an allocated agent (buyer)
	 * @param bidders a list of bidders allocated for the given auctioneer
	 * @param itsBundle a list of goods associated with this auctioneer
	 * @param allocationProbabilities allocation probabilities of the bundles
	 * @throws Exception if the allocated bundle set is empty
	 */
	public void addAllocatedAgent(int auctioneerId, List<Integer> bidders, List<Integer> itsBundle, 
            					  List<Double> allocationProbabilities) throws Exception
	{
		if( itsBundle.size() == 0 ) throw new Exception("The agent has to be allocated at least one bundle.");
		if( _allocatedAuctioneersIds.size() != 0 ) throw new Exception("Probabilistic Allocation currently supports only one auctioneer.");

		_allocatedAuctioneersIds.add( auctioneerId );
		_allocatedBiddersIds.add( bidders );
		_allocatedBundles.add(itsBundle);
		_allocationProbabilities = allocationProbabilities;
	}
	
	/**
	 * The method returns the list of allocation probabilities
	 * @return the list of allocation probabilities
	 */
	public List<Double> getAllocationProbabilities()
	{
		List<Double> copy = new LinkedList<Double>();
		for( Double p : _allocationProbabilities )
			copy.add(p);
		
		return copy;
	}
	
	/**
	 * The method returns the allocation probability of a bidder with the specified ID
	 * @param bidderId an Id of the bidder
	 * @return allocation probability of the bidder with the specified Idx
	 */
	public double getAllocationProbabilityOfBidderById(int bidderId)
	{
		int bidderIdx = -1;
		for(int i = 0; i < _allocatedBiddersIds.get(0).size(); ++i)
			if( _allocatedBiddersIds.get(0).get(i) == bidderId )
			{
				bidderIdx = i;
				break;
			}
		
		return _allocationProbabilities.get(bidderIdx);
	}
	
	/**
	 * The method returns an allocation probability of the bundle with the specified Id.
	 * @param bundleId an Id of the bundle.
	 * @return total allocation probability of the bundle with the given ID
	 */
	public double getAllocationProbabilityOfBundle(int bundleId)
	{
		double allocationProbabilityOfBundle = 0.;
		for(int i = 0; i < _allocatedBundles.get(0).size(); ++i)
			if( _allocatedBundles.get(0).get(i) == bundleId )
				allocationProbabilityOfBundle += _allocationProbabilities.get(i); 
		
		if(allocationProbabilityOfBundle > 1. + 1e-6)
			throw new RuntimeException("Probability is > 1 : " + allocationProbabilityOfBundle);
		
		return allocationProbabilityOfBundle;
	}
	
	/**
	 * The method returns the number of agents who got a non-zero allocation probability.
	 * @return the number of agents who are allocated with probability > 0
	 */
	public int getNumberOfAllocatedBundles()
	{
		int numberOfAllocatedBidders = 0;
		for(Double a : _allocationProbabilities)
			if( a > 0)
				numberOfAllocatedBidders += 1;
		return numberOfAllocatedBidders;
	}
	
	/**
	 * The method returns the number of  bidders
	 * @return the number of bidders
	 */
	public int getNumberOfBidders()
	{
		return _allocationProbabilities.size();
	}
	
	/**
	 * The method resets allocation probabilities of sellers.
	 * @param allocationProbabilities new allocation probabilities of sellers
	 */
	public void resetAllocationProbabilities(List<Double> allocationProbabilities)
	{
		if( allocationProbabilities.size() != _allocationProbabilities.size() ) throw new RuntimeException("Dimensionality mismatch.");
		_allocationProbabilities = allocationProbabilities;
	}
	
	/**
	 * The method sets the allocation probability of the specified bundle to 0.
	 * @param bundleId an Id of the bundle to be deallocated
	 */
	public void deallocateBundle(int bundleId)
	{
		for(int i = 0; i < _allocatedBundles.get(0).size(); ++i)
			if( _allocatedBundles.get(0).get(i) == bundleId )
				_allocationProbabilities.set(i, 0.);
	}
	
	/**
	 * The method normalizes probabilities of allocation of bidders that bid for the same good.
	 */
	public void normalize()
	{
		int numberOfDBs = getNumberOfGoods();
		
		for(int i = 0; i < numberOfDBs; ++i  )
		{
			double total = 0.;
			for(int j=0; j < _allocatedBiddersIds.get(0).size(); ++j)
				if( _allocatedBundles.get(0).get(j) == i )
					total += _allocationProbabilities.get(j);
			
			for(int j=0; j < _allocatedBiddersIds.get(0).size(); ++j)
				if( _allocatedBundles.get(0).get(j) == i )
					_allocationProbabilities.set(j, _allocationProbabilities.get(j)/total);
		}
	}
	
	/**
	 * The method returns the number of different goods among all bidders.
	 * @return the number of different goods
	 */
	public int getNumberOfGoods()
	{
		Map<Integer, Integer> goodsCounter = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < _allocatedBundles.get(0).size(); ++i)
		{
			int good = _allocatedBundles.get(0).get(i);
			if( !goodsCounter.containsKey( good ) )
				goodsCounter.put(good, 1);
			else
				goodsCounter.put(good, goodsCounter.get(good) + 1);
		}
		
		return goodsCounter.size();
	}
	
	private List<Double> _allocationProbabilities;  	//Allocation probabilities of bidders (each bidder allocated to a single bundle).
}
