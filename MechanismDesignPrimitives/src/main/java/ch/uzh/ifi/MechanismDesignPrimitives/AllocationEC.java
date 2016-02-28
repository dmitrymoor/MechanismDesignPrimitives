package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

/**
 * The class defines an allocation data structure for domains with uncertainty.
 * Can be used by all EC- an Exp- mechanisms in the Mechanisms toolbox.
 * @author Dmitry Moor
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
		_allocatedGoodIds = null;
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
		
		double sw = auctioneerExpectedValue - biddersExpectedValues.stream().reduce( (x1, x2) -> x1 + x2).get();
		_expectedSocialWelfare.add( isReverse ? sw : -1*sw );
	}
	
	/**
	 * @param tradeIdx - an index of the trade (auctioneer)
	 * @return return realized values per good
	 */
	public List<Double> getRealizedRVsPerGood(int tradeIdx)
	{
		return _realizedRVsPerGood.get(tradeIdx);
	}
	
	/**
	 * The method returns a subset of goods for which availabilities are known.
	 * @param bids bids of agents
	 * @param numberOfItems number of goods
	 * @return a list of allocated goods
	 */
	public List<Integer> getAllocatedAvailabilitiesPerGood( List<Type> bids, boolean useOnlyAllocatedGoods)
	{
		if( _allocatedGoodIds != null )
			return _allocatedGoodIds;
		
		computeAvailabilitiesPerGood(bids, useOnlyAllocatedGoods);
		return _allocatedGoodIds;
	}
	
	/**
	 * The method returns a list of realized availabilities of different goods.
	 * @param bids bids of agents
	 * @param useOnlyAllocatedGoods true if realized availabilities of only allocated goods should be returned and false if 
	 *                              realized availabilities of ALL goods should be returned (even of those not allocated).
	 */
	private void computeAvailabilitiesPerGood(List<Type> bids, boolean useOnlyAllocatedGoods)
	{
		_allocatedGoodIds = new LinkedList<Integer>();
		_realizationsOfAvailabilitiesPerGood = new LinkedList<Double>();
		int numberOfItems = getRealizedRVsPerGood(0).size();
		
		if( useOnlyAllocatedGoods )	//use availabilities of only allocated goods
		{
			for(int j = 0; j < getBiddersInvolved(0).size(); ++j)
			{
				int bidderId = getBiddersInvolved(0).get(j);				
				int itsAllocatedAtom = getAllocatedBundlesByIndex(0).get(j);
				AtomicBid allocatedBundle = bids.get( bidderId-1 ).getAtom( itsAllocatedAtom );
			
				_allocatedGoodIds.addAll( IntStream.range(0, allocatedBundle.getInterestingSet().size()).boxed().map( i -> allocatedBundle.getInterestingSet().get(i)).collect(Collectors.toList()));
			}
		}
		else						//use availabilities of ALL goods (even those which were never allocated)
			_allocatedGoodIds = IntStream.range(0, numberOfItems).boxed().map( i -> i+1 ).collect(Collectors.toList());

		_realizationsOfAvailabilitiesPerGood.addAll( _allocatedGoodIds.stream().map( gId -> getRealizedRVsPerGood(0).get(gId - 1)).collect(Collectors.toList()) );
	}
	
	/**
	 * The method returns realized availabilities of goods.
	 * @param bids bids of agents
	 * @param useOnlyAllocatedGoods true if only allocated goods 
	 * @return a list of realized availabilities per good
	 */
	public List<Double> getRealizationsOfAvailabilitiesPerGood( List<Type> bids, boolean useOnlyAllocatedGoods)
	{
		if( _realizationsOfAvailabilitiesPerGood != null )	return _realizationsOfAvailabilitiesPerGood;
		
		computeAvailabilitiesPerGood(bids, useOnlyAllocatedGoods);
		return _realizationsOfAvailabilitiesPerGood;
	}
	
	/**
	 * The method returns realization of a random variable which corresponds to a buyer in a particular trade.
	 * @param auctioneerIdx - an index of a seller (a trade)
	 * @param bidderIdx - an index of an allocated buyer
	 * @return a realization of the random variable 
	 */
	public double getRealizedRV(int auctioneerIdx, int bidderIdx)
	{
		return _realizedRVs.get(auctioneerIdx).get(bidderIdx); 
	}
	
	/**
	 * The method returns realization of all random variables which corresponds to buyers in a particular trade.
	 * @param auctioneerIdx - an index of a seller (a trade)
	 * @return realizations of random variables 
	 */
	public List<Double> getRealizedRV(int auctioneerIdx)
	{
		return _realizedRVs.get(auctioneerIdx); 
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
		if( _expectedSocialWelfare.stream().reduce( (x1, x2) -> x1 + x2).isPresent() )
			return _expectedSocialWelfare.stream().reduce( (x1, x2) -> x1 + x2).get();
		else throw new RuntimeException("No data to compute expected SW: " + _expectedSocialWelfare.toString() + " " + _expectedAuctioneersValues.toString() + " " + _expectedBiddersValues.toString());
	}
	
	protected List<List<Double> > _realizedRVs;					//A list containing realized availabilities of all allocated bundles in a trade
	protected List<List<Double> > _realizedRVsPerGood; 			//A list containing realized availabilities of all allocated goods in a trade
	protected List<Integer> _allocatedGoodIds;
	protected List<Double>  _realizationsOfAvailabilitiesPerGood;//TODO: remove either this or _realizedRVsPerGood
	protected List<Double>  _expectedAuctioneersValues;			//A list of expected values of allocated auctioneers
	protected List<List<Double> > _expectedBiddersValues;		//A list of expected costs of allocated bidders
	protected List<Double>  _expectedSocialWelfare;				//A list of expected SW contribution for every buyer
}
