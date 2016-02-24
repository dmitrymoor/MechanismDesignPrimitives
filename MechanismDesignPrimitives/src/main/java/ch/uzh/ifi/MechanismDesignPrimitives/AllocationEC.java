package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;

/**
 * The class defines an allocation data structure for domains with uncertainty.
 * Can be used by all EC- an Exp- mechanisms in the Mechanisms toolbox.
 * @author Dmitry Moor
 *
 */
public class AllocationEC extends Allocation 
{

	/**
	 * A simple constructor
	 */
	public AllocationEC()
	{
		super();
		_realizedRVs = new LinkedList<List<Double> >();
		_realizedRVsPerGood = new LinkedList<List<Double> > ();
		_allocatedAvailabilitiesPerGood = null;
		_realizationsOfAvailabilitiesPerGood = null;
		_expectedBiddersValues = new LinkedList<List<Double> >();
		_expectedAuctioneersValues = new LinkedList<Double>();
		_expectedSocialWelfare= new LinkedList<Double>();
	}
	
	/**
	 * The method adds a new agents in the list of allocated agents (buyers).
	 * @param agentId an ID of an allocated agent (buyer)
	 * @param bidders a list of sellers' IDs allocated for this buyer
	 * @param itsAllocatedBundles a list of bundles allocated for this agent
	 * @param auctioneerExpectedValue an expected (not actual) value of a buyer for this allocation
	 * @param biddersExpectedValues expected (not actual) costs of sellers allocated for the buyer
	 * @param isReverse true if the auction is reverse and false if the auction is forward
	 * @throws Exception if the allocated bundle set is empty
	 */
	public void addAllocatedAgents(int agentId, List<Integer> bidders, List<Integer> itsAllocatedBundles, 
			                       double auctioneerExpectedValue, List<Double> biddersExpectedValues, boolean isReverse) throws Exception
	{
		if( itsAllocatedBundles.size() == 0 ) throw new Exception("The agent have to be allocated at least one bundle.");
		
		_allocatedAuctioneersIds.add( agentId );
		_allocatedBiddersIds.add(bidders);
		_allocatedBundles.add(itsAllocatedBundles);
		_expectedAuctioneersValues.add(auctioneerExpectedValue);
		_expectedBiddersValues.add(biddersExpectedValues);
		
		double sw = auctioneerExpectedValue;
		for(Double cost : biddersExpectedValues)
			sw -= cost;
		
		if( ! isReverse)
			sw = -1*sw;
		
		_expectedSocialWelfare.add( sw );
	}
	
	/**
	 * @param tradeIdx - an index of the trade (seller)
	 * @return return realized values per good
	 */
	public List<Double> getRealizedRVsPerGood(int tradeIdx)
	{
		return _realizedRVsPerGood.get(tradeIdx);
	}
	
	/**
	 * @param bids bids of agents
	 * @param numberOfItems number of goods
	 * @return a list of allocated goods
	 */
	public List<Integer> getAllocatedAvailabilitiesPerGood( List<Type> bids, int numberOfItems)
	{
		if( _allocatedAvailabilitiesPerGood != null )
			return _allocatedAvailabilitiesPerGood;
		
		computeAvailabilitiesPerGood(bids, numberOfItems);
		return _allocatedAvailabilitiesPerGood;
	}
	
	/**
	 * @param bids bids of agents
	 * @param numberOfItems number of goods
	 */
	private void computeAvailabilitiesPerGood(List<Type> bids, int numberOfItems)
	{
		_allocatedAvailabilitiesPerGood = new LinkedList<Integer>();
		_realizationsOfAvailabilitiesPerGood = new LinkedList<Double>();
		
		for(int j = 0; j < getBiddersInvolved(0).size(); ++j)
		{
			int bidderId = getBiddersInvolved(0).get(j);				
			int itsAllocatedAtom = getAllocatedBundlesByIndex(0).get(j);
			AtomicBid allocatedBundle = bids.get( bidderId-1 ).getAtom( itsAllocatedAtom );
			
			if(numberOfItems <= 0)
				for(int k = 0; k < allocatedBundle.getInterestingSet().size(); ++k)
				{
					int goodId = allocatedBundle.getInterestingSet().get(k);
					_allocatedAvailabilitiesPerGood.add( goodId );
					_realizationsOfAvailabilitiesPerGood.add( getRealizedRVsPerGood(0).get(goodId-1));
				}
			else
				for(int k = 0; k < numberOfItems; ++k)
				{
					int goodId = k+1;
					_allocatedAvailabilitiesPerGood.add( goodId );
					_realizationsOfAvailabilitiesPerGood.add(getRealizedRVsPerGood(0).get(k));
				}
		}
	}
	
	/**
	 * The method returns realized availabilities of goods.
	 * @param bids bids of agents
	 * @param numberOfItems number of goods
	 * @return a list of realized availabilities per good
	 */
	public List<Double> getRealizationsOfAvailabilitiesPerGood( List<Type> bids, int numberOfItems)
	{
		if( _realizationsOfAvailabilitiesPerGood != null )
			return _realizationsOfAvailabilitiesPerGood;
		
		computeAvailabilitiesPerGood(bids, numberOfItems);
		return _realizationsOfAvailabilitiesPerGood;
	}
	
	/**
	 * The method returns realization of a random variable which corresponds to a buyer in a particular trade.
	 * @param sellerIdx - an index of a seller (a trade)
	 * @param buyerIdx - an index of an allocated buyer
	 * @return a realization of the random variable 
	 */
	public double getRealizedRV(int sellerIdx, int buyerIdx)
	{
		return _realizedRVs.get(sellerIdx).get(buyerIdx); 
	}
	
	/**
	 * The method returns realization of all random variables which corresponds to buyers in a particular trade.
	 * @param sellerIdx - an index of a seller (a trade)
	 * @return realizations of random variables 
	 */
	public List<Double> getRealizedRV(int sellerIdx)
	{
		return _realizedRVs.get(sellerIdx); 
	}
	
	/**
	 * The method used to store a list of realized availabilities of allocated bundles (but not individual goods).
	 * @param realizedRVs - realized values of availabilities of allocated bundles
	 */
	public void addRealizedRVs(List<Double> realizedRVs)
	{
		_realizedRVs.add(realizedRVs);
	}
	
	/**
	 * The method used to store realized availabilities of ALL goods in the auction. (NOTE: the m-m can ignore some of these 
	 * realizations, e.g., if it is assumed that realized availabilities of only allocated goods are known).
	 * @param realizedRVsPerGood - realized availabilities of all goods in the auction
	 */
	public void addRealizedValuesPerGood(List<Double> realizedRVsPerGood)
	{
		_realizedRVsPerGood.add(realizedRVsPerGood);
	}
	
	/**
	 * The method returns an expected value of a buyer in a forward auction or 
	 * an expected cost of a seller in a reverse auction.
	 * @param tradeIdx - an index of an allocated trade
	 * @param bidderIdx- an index of an allocated bidder
	 * @return an expected value of a bidder
	 */
	public double getBiddersExpectedValue(int tradeIdx, int bidderIdx)
	{
		return _expectedBiddersValues.get(tradeIdx).get(bidderIdx);
	}
	
	/**
	 * The method returns the expected value of a buyer for his trades.
	 * @param buyerIdx  an index of a buyer 
	 * @return the total value of a buyer for his trade
	 */
	public double getAuctioneerExpectedValue(int buyerIdx)
	{
		return _expectedAuctioneersValues.get(buyerIdx);
	}
	
	/**
	 * The method returns the total expected welfare of allocated plans.
	 * @return the total welfare
	 */
	public double getExpectedWelfare()
	{
		double welfare = 0.;
		for( Double val : _expectedSocialWelfare )
			welfare += val;
		return welfare;
	}
	
	protected List<List<Double> > _realizedRVs;					//A list containing realized availabilities of all allocated bundles in a trade
	protected List<List<Double> > _realizedRVsPerGood; 			//A list containing realized availabilities of all allocated goods in a trade
	protected List<Integer> _allocatedAvailabilitiesPerGood;
	protected List<Double>  _realizationsOfAvailabilitiesPerGood;//TODO: remove this or _realizedRVsPerGood ???
	protected List<Double>  _expectedAuctioneersValues;			//A list of expected values of allocated auctioneers
	protected List<List<Double> > _expectedBiddersValues;		//A list of expected costs of allocated bidders
	protected List<Double>  _expectedSocialWelfare;				//A list of expected SW contribution for every buyer
}
