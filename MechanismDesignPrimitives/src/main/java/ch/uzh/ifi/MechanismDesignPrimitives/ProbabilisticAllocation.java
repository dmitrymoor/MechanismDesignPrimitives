package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;

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
	 * @param itsAllocatedBundles a list of bundles allocated for this auctioneer
	 * @param auctioneerValue an actual (not expected) value of a buyer for this allocation
	 * @param biddersValues actual (not expected) costs of sellers allocated for the buyer
	 * @param allocationProbabilities allocation probabilities of the bundles
	 * @throws Exception if the allocated bundle set is empty
	 */
	public void addAllocatedAgent(int auctioneerId, List<Integer> bidders, List<Integer> itsAllocatedBundles, 
			                      double auctioneerValue, List<Double> biddersValues, List<Double> allocationProbabilities) throws Exception
	{
		if( itsAllocatedBundles.size() == 0 ) throw new Exception("The agent has to be allocated at least one bundle.");
		if( _allocatedAuctioneersIds.size() != 0 ) throw new Exception("Probabilistic Allocation currently supports only one auctioneer.");
		
		_allocatedAuctioneersIds.add( auctioneerId );
		_allocatedBiddersIds.add( bidders );
		_allocatedBundles.add(itsAllocatedBundles);
		_allocatedAuctioneersValues.add( auctioneerValue);
		_allocatedBiddersValues.add(biddersValues);
		_allocationProbabilities = allocationProbabilities;
	}
	
	/**
	 * The method returns the list of values of bidders.
	 * @return list of values of bidders
	 */
	public List<Double> getBiddersValues()
	{
		return _allocatedBiddersValues.get(0);
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
	 * The method returns the number of different bundles
	 * @return the number of bundles
	 */
	public int getNumberOfBundles()
	{
		return _allocationProbabilities.size();
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
	
	private List<Double> _allocationProbabilities;  //Allocation probabilities of bidders (each bidder allocated to a single bundle).
}
