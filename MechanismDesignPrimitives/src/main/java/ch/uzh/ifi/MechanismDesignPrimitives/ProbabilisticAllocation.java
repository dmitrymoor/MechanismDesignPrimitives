package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.List;

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
	 * @param autioneerValue an actual (not expected) value of a buyer for this allocation
	 * @param biddersValues actual (not expected) costs of sellers allocated for the buyer
	 * @param allocationProbabilities allocation probabilities of the bundles
	 * @throws Exception if the allocated bundle set is empty
	 */
	public void addAllocatedAgent(int auctioneerId, List<Integer> bidders, List<Integer> itsAllocatedBundles, 
			                      double autioneerValue, List<Double> biddersValues, List<Double> allocationProbabilities) throws Exception
	{
		if( itsAllocatedBundles.size() == 0 ) throw new Exception("The agent has to be allocated at least one bundle.");
		if( _allocatedAuctioneersIds.size() != 0 ) throw new Exception("Probabilistic Allocation currently supports only one auctioneer.");
		
		_allocatedAuctioneersIds.add( auctioneerId );
		_allocatedBiddersIds.add( bidders );
		_allocatedBundles.add(itsAllocatedBundles);
		_allocatedAuctioneersValues.add( autioneerValue);
		_allocatedBiddersValues.add(biddersValues);
		_allocationProbabilities = allocationProbabilities;
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
	 * @return total allocation probability of the bundle with thte given ID
	 */
	public double getAllocationProbabilityOfBundle(int bundleId)
	{
		double allocationProbabilityOfBundle = 0.;
		for(int i = 0; i < _allocatedBundles.get(0).size(); ++i)
			if( _allocatedBundles.get(0).get(i) == bundleId )
				allocationProbabilityOfBundle += _allocationProbabilities.get(i); 
		
		return allocationProbabilityOfBundle;
	}
	
	private List<Double> _allocationProbabilities;  			//Allocation probabilities of bidders.
}