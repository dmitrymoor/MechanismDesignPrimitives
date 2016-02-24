package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComplexSemanticWebType extends AtomicBid
{

	/**
	 * A simple constructor
	 * @param agentId - an id of a buyer
	 * @param sellersIDs - a list of IDs of sellers involved into the plan
	 * @param value - a value of a buyer for the plan
	 * @param queryFragments query fragments
	 */
	public ComplexSemanticWebType(int agentId, List<Integer> sellersIDs, double value, List<QueryFragment> queryFragments) 
	{
		super(agentId, sellersIDs, value);
		
		_queryFragments = queryFragments;
		_planPayment = 0.;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str =  "( AgentId=" + _agentId + "," + _items + ", Costs: " +/* _costs.toString() +*/ "," ;//+ _type.get("Value") + ")";
		//for(Map.Entry<String, Double> component : _type.entrySet())
		//	str += component.getKey() + "=" + component.getValue() + "; ";
		//str += " maxRecords: " + _maxNumberOfTuples.toString() + ")";
		for(QueryFragment qf : _queryFragments)
			str += qf.toString();
		
		return str;
	}
	
	/*
	 * 
	 */
	public void resolveWithSecondPrice()
	{
		_allocatedSellersIds = new LinkedList<Integer>();
		
		for(int i = 0; i < _queryFragments.size(); ++i)
		{
			double minExpectedCost = 1e+6;
			double expectedSecondPrice = 1e+6;
			int bestSellerIdx = 0;
			for(int j = 0; j < _queryFragments.get(i).getNumberOfSellers(); ++j)
			{
				double itsExpectedCost = _queryFragments.get(i).getExpectedCost(j);
				if( itsExpectedCost < minExpectedCost)
				{
					expectedSecondPrice = minExpectedCost;
					minExpectedCost = itsExpectedCost;
					bestSellerIdx = j;
				}
				else if( itsExpectedCost < expectedSecondPrice )
				{
					expectedSecondPrice = itsExpectedCost;
				}
			}
			
			_queryFragments.get(i).allocateSeller(bestSellerIdx);
			_queryFragments.get(i).setPayment(expectedSecondPrice);
			
			_allocatedSellersIds.add( _queryFragments.get(i).getAllocatedSellerId() );
			_planPayment += _queryFragments.get(i).getPayment();
		}
	}
	
	/*
	 * 
	 */
	public void resolveWithFirstPrice()
	{
		_allocatedSellersIds = new LinkedList<Integer>();
		
		for(int i = 0; i < _queryFragments.size(); ++i)
		{
			double minExpectedCost = 1e+6;
			double expectedFirstPrice = 1e+6;
			int bestSellerIdx = 0;
			for(int j = 0; j < _queryFragments.get(i).getNumberOfSellers(); ++j)
			{
				double itsExpectedCost = _queryFragments.get(i).getExpectedCost(j);
				if( itsExpectedCost < minExpectedCost)
				{
					expectedFirstPrice = itsExpectedCost;
					minExpectedCost = itsExpectedCost;
					bestSellerIdx = j;
				}
			}
			
			_queryFragments.get(i).allocateSeller(bestSellerIdx);
			_queryFragments.get(i).setPayment(expectedFirstPrice);
			
			_allocatedSellersIds.add( _queryFragments.get(i).getAllocatedSellerId() );
			_planPayment += _queryFragments.get(i).getPayment();
		}
	}
	
	/*
	 * The method computes an expected value of a buyer for this plan assuming that the plan is resolved.
	 * @return an expected value of a buyer
	 */
	public double computeExpectedValue()
	{
		double expectedNumberOfRecords = 0.;
		for(int i = 0; i < _queryFragments.size(); ++i)
		{
			int sellerIdx = _queryFragments.get(i).getAllocatedSellerIdx();
			expectedNumberOfRecords += _queryFragments.get(i).getExpectedNumberOfRecords(sellerIdx);
		}
		return expectedNumberOfRecords * this.getValue();
	}
	
	/*
	 * The method computes a total expected cost of all sellers for this plan assuming that the plan is resolved.
	 * @return an expected cost of all sellers
	 */
	public List<Double> computeExpectedCosts()
	{
		List<Double> expectedCosts = new LinkedList<Double>();
		for(int i = 0; i < _queryFragments.size(); ++i)
		{
			int sellerIdx = _queryFragments.get(i).getAllocatedSellerIdx();
			expectedCosts.add( _queryFragments.get(i).getExpectedCost(sellerIdx) );
		}
		
		return expectedCosts;
	}
	
	/*
	 * The method computes a total expected cost of all sellers for this plan assuming that the plan is resolved.
	 * @return an expected cost of all sellers
	 */
	public double computeExpectedTotalCost()
	{
		double expectedTotalCost = 0.;
		for(int i = 0; i < _queryFragments.size(); ++i)
		{
			int sellerIdx = _queryFragments.get(i).getAllocatedSellerIdx();
			expectedTotalCost += _queryFragments.get(i).getExpectedCost(sellerIdx);
		}
		
		return expectedTotalCost;
	}
	
	/*
	 * The method returns a list of sellers actually allocated for the plan.
	 */
	public List<Integer> getAllocatedSellers()
	{
		//1. Remove duplicates
		for(int i = _allocatedSellersIds.size() - 1; i >= 0; --i)
			for(int j = 0; j < i; ++j)
				if( _allocatedSellersIds.get(j) == _allocatedSellersIds.get(i) )
					_allocatedSellersIds.remove(i);
		
		return _allocatedSellersIds;
	}
	
	public double getPlanExpectedPayment()
	{
		return _planPayment;
	}
	
	public int getNumberOfFragments()
	{
		return _queryFragments.size();
	}
	
	public int getMinNumberOfRecords(int fragmentIdx)
	{
		int allocatedSellerIdx = _queryFragments.get(fragmentIdx).getAllocatedSellerIdx();
		return _queryFragments.get(fragmentIdx).getMinNumberOfRecords( allocatedSellerIdx );
	}
	
	public int getMaxNumberOfRecords(int fragmentIdx)
	{
		int allocatedSellerIdx = _queryFragments.get(fragmentIdx).getAllocatedSellerIdx();
		return _queryFragments.get(fragmentIdx).getMaxNumberOfRecords( allocatedSellerIdx );
	}
	
	public double getPayment(int sellerId)
	{
		double totalPayment = 0.;
		for(int i = 0; i < _queryFragments.size(); ++i)
			if( _queryFragments.get(i).getAllocatedSellerId() == sellerId )
				totalPayment += _queryFragments.get(i).getPayment();
		
		return totalPayment;
	}
	
	public double getPayment(int fragmentIdx, boolean isFragment)
	{
		return _queryFragments.get(fragmentIdx).getPayment();
	}
	
	public QueryFragment getFragment(int fragmentIdx)
	{
		return _queryFragments.get(fragmentIdx);
	}
	
	public double getActualCost(int sellerId)
	{
		double totalAllocatedCost = 0.;
		for(int i = 0; i < _queryFragments.size(); ++i)
		{
			if( _queryFragments.get(i).getAllocatedSellerId() == sellerId )
			{
				int sellerIdx = _queryFragments.get(i).getAllocatedSellerIdx();
				totalAllocatedCost += _queryFragments.get(i).getCost( sellerIdx ) * _queryFragments.get(i).getAllocatedNumberOfRecords();
			}
		}
		
		return totalAllocatedCost;
	}
	
	public double getAllocatedValue()
	{
		int numberOfAllocatedRecords = 0;
		for(int i = 0; i < _queryFragments.size(); ++i)
		{
			numberOfAllocatedRecords += _queryFragments.get(i).getAllocatedNumberOfRecords();
		}
		
		return numberOfAllocatedRecords * this.getValue();
	}
	
	public void setActuallyAllocatedRecords(List<Integer> actuallyAllocatedRecords)
	{
		for(int i = 0; i < _queryFragments.size(); ++i)
			_queryFragments.get(i).setAllocatedNumberOfRecords( actuallyAllocatedRecords.get(i));
	}

	private List<QueryFragment> _queryFragments;
	private List<Integer> _allocatedSellersIds;
	private double _planPayment;
}
