package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * 
 */
public class SemanticWebType extends AtomicBid
{
	/*
	 * A simple constructor
	 * @param agentId - an id of a buyer
	 * @param items - a list of IDs of sellers involved into the plan
	 * @param value - a marginal value of a buyer for the plan
	 * @param costs - marginal costs of sellers for the plan
	 * @param minNumberOfRecordsFromSellers - the min number of tuples every seller can provide
	 * @param maxNumberOfRecordsFromSellers - the reported number of tuples of sellers for the plan
	 * @param minNumberOfRecordsForBuyer - the minimum number of records a buyer can receive
	 * @param maxNumberOfRecordsForBuyer - the maximum number of records a buyer can receive
	 */
	public SemanticWebType(int agentId, List<Integer> sellersIDd, double value, List<Double> costs, 
			               List<Integer> minNumberOfRecordsFromSellers, List<Integer> maxNumberOfRecordsFromSellers, 
			               int minNumberOfRecordsForBuyer, int maxNumberOfRecordsForBuyer) 
	{
		super(agentId, sellersIDd, value);
		
		if( (minNumberOfRecordsFromSellers.size() != maxNumberOfRecordsFromSellers.size()) || (minNumberOfRecordsFromSellers.size() != costs.size()))
			throw new RuntimeException("The numbers of tuples provided by sellers should coinside with the number of marginal costs of sellers");
		
		setCosts(costs);
		_minNumberOfRecordsFromSellers = minNumberOfRecordsFromSellers;
		_maxNumberOfRecordsFromSellers = maxNumberOfRecordsFromSellers;
		_minNumberOfRecordsForBuyer = minNumberOfRecordsForBuyer;
		_maxNumberOfRecordsForBuyer = maxNumberOfRecordsForBuyer;
		_actuallyAllocatedTuples = new LinkedList<Integer>();
		_expectedCosts = new LinkedList<Double>();
	}
	
	public SemanticWebType(int agentId, List<Integer> items, double value, List<Double> costs) 
	{
		super(agentId, items, value);
		setCosts(costs);
	}

	public SemanticWebType(int agentId, List<Integer> items, double value) 
	{
		super(agentId, items, value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str =  "( AgentId=" + _agentId + "," + _items + ", Costs: " + _costs.toString() + "," ;//+ _type.get("Value") + ")";
		for(Map.Entry<String, Double> component : _type.entrySet())
			str += component.getKey() + "=" + component.getValue() + "; ";
		str += " maxRecords: " + _maxNumberOfRecordsFromSellers.toString() + " maxReceived: " + _maxNumberOfRecordsForBuyer + ")";
		return str;
	}
	
	/*
	 * The methods sets the costs 
	 */
	public void setCosts( List<Double> costs)
	{
		if( costs.size() != _items.size() ) throw new RuntimeException("The size of the list of costs should match to the size of the list of sellers (items)");
		_costs = costs;
	}
	
	public double getCost(int idx)
	{
		return _costs.get(idx);
	}
	
	public double getCostOfItem(int itemId)
	{
		for(int i = 0; i < _items.size(); ++i)
			if(_items.get(i) == itemId)
				return _costs.get(i);
		throw new RuntimeException("No such item: " + itemId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#getTypeComponent(java.lang.String)
	 */
	@Override 
	public Object getTypeComponent(String key)
	{
		if( key.substring(0, 4).equals("Cost") )
			if( key.substring(4, key.length()).matches("[0-9]+") )
			{
				if( Integer.parseInt( key.substring(4, key.length()) )  <= _costs.size() )
					return _costs.get( Integer.parseInt( key.substring(4, key.length()) ) - 1 );
				else throw new RuntimeException("the cost index is larger than the number of cost factors: " + key.substring(4, key.length()) + " -> " + _costs.size() + " " + key);
			}
			else if( key.length() == (new String("Cost").length()) )
			{
				double cost = 0.;						//return total cost
				for( Double c : _costs )
					cost += c;
				return cost;
			}
		return _type.get(key);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#getTypeComponent(java.lang.String)
	 */
	@Override
	public void setTypeComponent(String key, Object componentValue)
	{
		if( key.substring(0, 4).equals("Cost") )
		{
			if( key.substring(4, key.length()).matches("[0-9]+") )
			{
				if( Integer.parseInt( key.substring(4, key.length()) )  <= _costs.size() )
					_costs.set( Integer.parseInt( key.substring(4, key.length()) ) - 1, (Double)componentValue  );
				else throw new RuntimeException("the cost index is larger than the number of cost factors: " + key.substring(4, key.length()) + " -> " + _costs.size() + " " + key);
			}
		}
		else
			_type.put(key, (Double)componentValue );
	}
	
	public int getMinNumberOfTuples(int index)
	{
		return _minNumberOfRecordsFromSellers.get(index);
	}
	
	public int getMaxNumberOfTuples(int index)
	{
		return _maxNumberOfRecordsFromSellers.get(index);
	}
	
	public int getNumberOfSellers()
	{
		return _costs.size();
	}
	
	/*
	 * The method recursively computes the expected social welfare for the given plan.
	 * @param numberOfSellers - defines the current depth of recursion
	 */
	public double computeExpectedSW(int numberOfSellers)
	{
		double expectedSW = 0.;
		
		if( numberOfSellers == 0 )
		{
			return 0.;
		}
		else
		{
			//for(int t = getMinNumberOfTuples(numberOfSellers-1); t <= getMaxNumberOfTuples(numberOfSellers-1); ++t)
			//	expectedSW += ((getValue()-getCost(numberOfSellers-1))*t + computeExpectedSW( numberOfSellers - 1))
			//			   *(1./(getMaxNumberOfTuples(numberOfSellers-1)-getMinNumberOfTuples(numberOfSellers-1)+1));
			List<Double> expectedCosts = computeExpectedCosts();
			double expectedTotalCost = 0.;
			for(double c : expectedCosts)
				expectedTotalCost += c;
			expectedSW = computeExpectedValue(numberOfSellers) - expectedTotalCost;
			return expectedSW;
		}
	}
	
	/*
	 * Compute the expected social welfare when the number of tuples provided by some sellers are fixed
	 * and the number of tuples provided by other sellers are random variables.
	 * @param numberOfSellers - the number of sellers in the plan (recursive)
	 * @param fixedSellerIdx - a list of indexes of sellers for which the number of tuples is fixed.
	 * @return the social welfare
	 */
	public double computeExpectedSW(int numberOfSellers, List<Integer> fixedSellerIdx)
	{
		double expectedSW = 0.;
		
		if( numberOfSellers == 0 )
		{
			return 0.;
		}
		else
		{
			for(int t = getMinNumberOfTuples(numberOfSellers-1); t <= getMaxNumberOfTuples(numberOfSellers-1); ++t)
				if( fixedSellerIdx.contains(numberOfSellers-1) )
					expectedSW += (getValue()*_actuallyAllocatedTuples.get(numberOfSellers-1) + computeExpectedSW( numberOfSellers - 1, fixedSellerIdx))
					           *(1./(getMaxNumberOfTuples(numberOfSellers-1)-getMinNumberOfTuples(numberOfSellers-1)+1));
				else
					expectedSW += ((getValue()-getCost(numberOfSellers-1))*t + computeExpectedSW( numberOfSellers - 1, fixedSellerIdx))
							   *(1./(getMaxNumberOfTuples(numberOfSellers-1)-getMinNumberOfTuples(numberOfSellers-1)+1));
			
			return expectedSW;
		}
	}
	
	public double computeExpectedValue(int numberOfSellers)
	{
		double expectedValue = 0.;
		
		if( numberOfSellers == 0 )
		{
			return 0.;
		}
		else
		{
			//1. Assuming that the number of records recieved by the buyer is equal to the number of records sent by sellers
			//for(int t = getMinNumberOfTuples(numberOfSellers-1); t <= getMaxNumberOfTuples(numberOfSellers-1); ++t)
			//	expectedValue += ( getValue()*t + computeExpectedValue( numberOfSellers - 1))
			//			      *  (1./(getMaxNumberOfTuples(numberOfSellers-1)-getMinNumberOfTuples(numberOfSellers-1)+1));
			
			expectedValue = getValue() * (_maxNumberOfRecordsForBuyer - _minNumberOfRecordsForBuyer) / 2.;
			return expectedValue;
		}
	}
	
	public List<Double> computeExpectedCosts()
	{
		_expectedCosts = new LinkedList<Double>();
		for(int i = 0; i < this.getNumberOfSellers(); ++i)
			_expectedCosts.add( _costs.get(i) * (_maxNumberOfRecordsFromSellers.get(i) - _minNumberOfRecordsFromSellers.get(i)) / 2 );
		
		return _expectedCosts;
	}
	
	public void setActuallyAllocatedTuples(List<Integer> actuallyAllocatedTuples)
	{
		_actuallyAllocatedTuples = actuallyAllocatedTuples;
	}
	
	public int getActuallyAllocatedTuples(int index)
	{
		return _actuallyAllocatedTuples.get(index);
	}
	
	public List<Double> getExpectedCosts()
	{
		return _expectedCosts;
	}
	
	public double getActualCost(int idx)
	{
		return _actuallyAllocatedTuples.get(idx) * _costs.get(idx);
	}
	
	public double getExpectedCost(int idx)
	{
		return _expectedCosts.get(idx);
	}
	
	/*
	 * The method returns the actual value of a buyer for this plan (after allocation happened).
	 */
	public double getAllocatedValue()
	{
		int numberOfAllocatedRecords = 0;
		for(int i = 0; i < _actuallyAllocatedTuples.size(); ++i)
			numberOfAllocatedRecords += _actuallyAllocatedTuples.get(i);
		
		return numberOfAllocatedRecords * this.getValue();
	}
	
	private List<Double> _costs;							//(marginal) Costs of different items (sellers)
	private List<Integer> _minNumberOfRecordsFromSellers;	//The minimum numbers of triples provided by each seller
	private List<Integer> _maxNumberOfRecordsFromSellers;	//The maximum number of triples reported by each seller
	private List<Integer> _actuallyAllocatedTuples;			//The numbers of tuples actually allocated by sellers in this plan
	private List<Double> _expectedCosts;
	private int _minNumberOfRecordsForBuyer;				//
	private int _maxNumberOfRecordsForBuyer;				//
}
