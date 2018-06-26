package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

/**
 * The allocation class can be used for any general combinatorial exchange setting.
 * @author Dmitry Moor
 */
public class Allocation 
{

	/**
	 * A simple constructor 
	 */
	public Allocation()
	{
		_allocatedAuctioneersIds = new ArrayList<Integer>();
		_allocatedBiddersIds =new ArrayList<List<Integer> >();
		_allocatedBundles = new CopyOnWriteArrayList<List<Integer> >();
		_allocatedAuctioneersValues = new ArrayList<Double>();
		_allocatedBiddersValues = new ArrayList< List<Double> >();
	}
	
	/**
	 * The method adds new agents in the list of allocated agents (buyers in a reverse auction).
	 * @param auctioneerId an ID of an allocated agent (buyer)
	 * @param bidders a list of bidders allocated for the given auctioneer
	 * @param itsAllocatedBundles a list of bundles allocated for this auctioneer
	 * @param autioneerValue an actual (not expected) value of a buyer for this allocation
	 * @param biddersValues actual (not expected) costs of sellers allocated for the buyer
	 * @throws Exception if the allocated bundle set is empty
	 */
	public void addAllocatedAgent(int auctioneerId, List<Integer> bidders, List<Integer> itsAllocatedBundles, 
			                      double autioneerValue, List<Double> biddersValues) throws Exception
	{		
		_allocatedAuctioneersIds.add( auctioneerId );
		_allocatedBiddersIds.add( bidders );
		_allocatedBundles.add(itsAllocatedBundles);
		_allocatedAuctioneersValues.add( autioneerValue);
		_allocatedBiddersValues.add(biddersValues);
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str = "Allocation: \n";
		for(int i = 0; i < _allocatedAuctioneersIds.size(); ++i)
			str += "Auctioneer id: " + _allocatedAuctioneersIds.get(i) + ". Bidders IDs: " + _allocatedBiddersIds.get(i).toString() 
			     + " Allocated Bundles: " + _allocatedBundles.toString() + ".\n Allocated Auctioneer Values: " + _allocatedAuctioneersValues.toString()
			     + " Allocated Bidders Values: " + _allocatedBiddersValues.toString() + ".\n";

		return str;
	}
	
	/**
	 * The method returns the total allocated welfare of allocated plans in the forward auction.
	 * @return the total welfare
	 */
	public double getAllocatedWelfare()
	{
		return IntStream.range(0, _allocatedAuctioneersValues.size()).boxed().map( i -> getTotalAllocatedBiddersValue(i) - _allocatedAuctioneersValues.get(i)).reduce((x1, x2)-> x1+x2).get();
	}
	
	/**
	 * The method returns a Id of a buyer with a given index.
	 * @param index - an index of an auctioneer
	 * @return a ID of the auctioneer corresponding to the index
	 */
	public int getAuctioneerId(int index)
	{
		if( index > _allocatedAuctioneersIds.size() - 1)   throw new RuntimeException("The index " + index + " exceeds the number of allocated buyers: " + _allocatedAuctioneersIds.size());
		return _allocatedAuctioneersIds.get(index);
	}
	
	/**
	 * The method returns the index of an auctioneer with the given id
	 * @param auctioneerId - an id of an auctioneer
	 * @return an index of the auctioneer
	 */
	public int getAuctioneerIndexById(int auctioneerId)
	{
		if(_allocatedAuctioneersIds.indexOf(auctioneerId) > 0)
			return _allocatedAuctioneersIds.indexOf(auctioneerId);
		else throw new RuntimeException("No such an ID=" + auctioneerId + " among allocated agents: " + _allocatedAuctioneersIds.toString() );
	}
	
	/**
	 * The method returns the number of allocated auctioneers
	 * @return the number of allocated auctioneers
	 */
	public int getNumberOfAllocatedAuctioneers()
	{
		return _allocatedAuctioneersIds.size();
	}
	
	/**
	 * The method returns indices of plans allocated for the given agent
	 * @param tradeIndex an index of an agent for which the list of bundles should be returned
	 * @return an index of a bundle (a plan) of this agent which is allocated for the agent
	 */
	public List<Integer> getAllocatedBundlesOfTrade(int tradeIndex)
	{
		if(tradeIndex > _allocatedBundles.size() - 1)  throw new RuntimeException("No bundles allocated for agent with index=" + tradeIndex);
		return _allocatedBundles.get(tradeIndex);
	}
	
	/**
	 * The method checks if an agent was allocated in the CA.
	 * @param agentId - an ID of an agent to be checked
	 * @return true if the agent was allocated and false otherwise
	 */
	public boolean isAllocated(int agentId)
	{
		if(_allocatedAuctioneersIds.contains(agentId))
			return true;
		
		for(List<Integer> biddersInTrade : _allocatedBiddersIds )
			if( biddersInTrade.contains(agentId))
				return true;
			
		return false;
	}
	
	/**
	 * The method returns the allocated value of the auctioneer for his trades
	 * @param auctioneerIdx - an index of the auctioneer 
	 * @return the total value of the auctioneer for his/her trade
	 */
	public double getAuctioneersAllocatedValue(int auctioneerIdx)
	{
		return _allocatedAuctioneersValues.get(auctioneerIdx);
	}
	
	/**
	 * The method returns the allocated value of a buyer for his trades.
	 * @param tradeIdx - an index of a buyer 
	 * @return the total value of a buyer for his trade
	 */
	public double getTotalAllocatedBiddersValue(int tradeIdx)
	{
		return _allocatedBiddersValues.get(tradeIdx).stream().reduce((x1, x2) -> x1 + x2).get();
	}
	
	/**
	 * The method returns a list of bidders (buyers in the forward auction and sellers in the reverse auction)
	 * involved in the trade.
	 * @param tradeIdx - an index of a trade
	 * @return a list of bidders involved in the trade
	 */
	public List<Integer> getBiddersInvolved(int tradeIdx)
	{
		if(_allocatedBiddersIds.size() == 0) throw new RuntimeException("No bidders involved.");
		return _allocatedBiddersIds.get(tradeIdx);
	}
	
	/**
	 * The method returns an allocated value of a buyer in a forward auction or 
	 * an allocated cost of a seller in a reverse auction.
	 * @param tradeIdx - an index of an allocated trade
	 * @param bidderIdx- an index of an allocated bidder
	 * @return an actually allocated value of a bidder
	 */
	public double getBiddersAllocatedValue(int tradeIdx, int bidderIdx)
	{
		return _allocatedBiddersValues.get(tradeIdx).get(bidderIdx);
	}
	
	/**
	 * The methods sets up the values of allocated bidders after actual allocation happened.
	 * @param allocatedBiddersValues - values of bidders after allocation
	 */
	public void setAllocatedBiddersValues(List<Double> allocatedBiddersValues)
	{
		_allocatedBiddersValues.add(allocatedBiddersValues);
	}
	
	protected List<Integer> _allocatedAuctioneersIds;           //A list of IDs of allocated auctioneers (sellers in a forward auction and buyers in a reverse auction)
	protected List<List<Integer> > _allocatedBiddersIds;        //A list of IDs of allocated bidders (buyers in a forward auction and sellers in a reverse auction)
	protected List<Double>  _allocatedAuctioneersValues;        //A list of allocated values of allocated auctioneers
	protected List<List<Double> > _allocatedBiddersValues;      //A list of allocated costs of allocated bidders
	protected List<List<Integer> > _allocatedBundles;           //A list of bundle indexes allocated to every winner (an index of the bundle in the bid of the corresponding agent)
}
