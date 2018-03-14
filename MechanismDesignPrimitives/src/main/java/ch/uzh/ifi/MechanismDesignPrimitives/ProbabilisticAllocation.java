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
		_numberOfGoods = -1;
	}
	
	/**
	 * The method adds a new auctioneer in the list of allocated auctioneers (buyers in a reverse auction, sellers in a forward auction).
	 * @param auctioneerId an ID of an allocated auctioneer (buyer)
	 * @param bidders a list of bidders corresponding to the given auctioneer
	 * @param itsBundle a list of goods associated with these bidders
	 * @param allocationProbabilities allocation probabilities of the goods
	 * @throws Exception if the allocated bundle set is empty
	 */
	public void addAllocatedAgent(int auctioneerId, List<Integer> bidders, List<Integer> itsBundle, 
            					  List<Double> allocationProbabilities) throws Exception
	{
		if( itsBundle.size() == 0 ) throw new Exception("The agent has to be interested in at least one bundle.");
		if( _allocatedAuctioneersIds.size() != 0 ) throw new Exception("Probabilistic Allocation currently supports only one auctioneer.");

		_allocatedAuctioneersIds.add( auctioneerId );
		_allocatedBiddersIds.add( bidders );
		_allocatedBundles.add( itsBundle );
		_allocationProbabilities = allocationProbabilities;
		_numberOfGoods = computeNumberOfGoods();
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
		int numberOfGoods = getNumberOfGoods();
		
		for(int good = 0; good < numberOfGoods; ++good  )
		{
			double total = 0.;
			for(int j=0; j < _allocatedBiddersIds.get(0).size(); ++j)
				if( _allocatedBundles.get(0).get(j) == good )
					total += _allocationProbabilities.get(j);
			
			for(int j=0; j < _allocatedBiddersIds.get(0).size(); ++j)
				if( _allocatedBundles.get(0).get(j) == good )
					_allocationProbabilities.set(j, _allocationProbabilities.get(j)/total);
		}
	}
	
	/**
	 * The method returns the number of different goods.
	 * @return the number of different goods
	 */
	public int getNumberOfGoods()
	{
		if( _numberOfGoods < 0) throw new RuntimeException("The number of goods was not initialized: ");
		return _numberOfGoods;
	}
	
	/**
	 * The method calculates the number of different goods.
	 * @return the number of different goods
	 */
	private int computeNumberOfGoods()
	{
		if(_numberOfGoods >= 0) throw new RuntimeException("The number of goods was already computed before.");
		
		Map<Integer, Integer> goodsCounter = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < _allocatedBundles.get(0).size(); ++i)
		{
			int good = _allocatedBundles.get(0).get(i);
			if( !goodsCounter.containsKey( good ) )
				goodsCounter.put(good, 1);
			else
				goodsCounter.put(good, goodsCounter.get(good) + 1);
		}
		
		_numberOfGoods = goodsCounter.size();
		return _numberOfGoods;
	}
	
	private List<Double> _allocationProbabilities;  	// Allocation probabilities of bidders (each bidder allocated to a single bundle).
	private int _numberOfGoods;							// Number of different goods
}
