package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.List;

public class QueryFragment 
{

	/*
	 * @param sellersUsed - a list of sellers willing to provide records for this query fragment
	 * @param costs - reported marginal costs of sellers
	 * @param minNumberOfTuples - min number of records each seller can provide
	 * @param maxNumberOfTuples - max number of records each seller can provide
	 */
	public QueryFragment(List<Integer> sellersUsed, List<Double> costs, List<Integer> minNumberOfRecords, List<Integer> maxNumberOfRecords)
	{
		_sellersUsed = sellersUsed;
		_costs = costs;
		_minNumberOfRecords = minNumberOfRecords;
		_maxNumberOfRecords = maxNumberOfRecords;
		
		_allocatedNumberOfRecords = 0;
		_allocatedSellerIdx = -1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str = "[QF"+_queryFragmentId + " Sellers: " + _sellersUsed.toString() + "; Costs: " + _costs.toString() +
				     "; minRec="+_minNumberOfRecords.toString() + "; maxRec="+_maxNumberOfRecords.toString() + 
				     "; s*=" + _allocatedSellerIdx + " (idx); p=" + _payment + " ] ";
		return str;
	}
	
	/*
	 * @return the number of sellers willing to answer the fragment
	 */
	public int getNumberOfSellers()
	{
		return _sellersUsed.size();
	}
	
	public double getCost(int sellerIdx)
	{
		return _costs.get(sellerIdx);
	}
	
	/*
	 * @param sellerIdx - an index of a seller
	 * @return expected cost of the specified seller
	 */
	public double getExpectedCost(int sellerIdx)
	{
		return _costs.get(sellerIdx) * (_maxNumberOfRecords.get(sellerIdx) - _minNumberOfRecords.get(sellerIdx)) / 2;
	}
	
	public double getExpectedNumberOfRecords(int sellerIdx)
	{
		return (double)(_maxNumberOfRecords.get(sellerIdx) - _minNumberOfRecords.get(sellerIdx)) / 2.;
	}
	
	public void allocateSeller(int allocatedSellerIdx)
	{
		_allocatedSellerIdx = allocatedSellerIdx;
	}
	
	public void setPayment(double payment)
	{
		_payment = payment;
	}
	
	public void setAllocatedNumberOfRecords(int allocatedNumberOfRecords)
	{
		_allocatedNumberOfRecords = allocatedNumberOfRecords;
	}
	
	public int getAllocatedSellerIdx()
	{
		return _allocatedSellerIdx;
	}
	
	public int getAllocatedSellerId()
	{
		return getSellerId(_allocatedSellerIdx);
	}
	
	public double getPayment()
	{
		return _payment;
	}
	
	public int getSellerId(int selerIdx)
	{
		return _sellersUsed.get(selerIdx);
	}
	
	public int getMinNumberOfRecords(int sellerIdx)
	{
		return _minNumberOfRecords.get(sellerIdx);
	}
	
	public int getMaxNumberOfRecords(int sellerIdx)
	{
		return _maxNumberOfRecords.get(sellerIdx);
	}
	
	public int getAllocatedNumberOfRecords()
	{
		return _allocatedNumberOfRecords;
	}
	
	private int _queryFragmentId;
	
	private List<Integer> _sellersUsed;					//Sellers involved in answering the query fragment
	private List<Double> _costs;						//(marginal) Costs of sellers involved
	private List<Integer> _minNumberOfRecords;			//The minimum numbers of triples provided by each seller
	private List<Integer> _maxNumberOfRecords;			//The maximum number of triples reported by each seller
	
	private int _allocatedSellerIdx;
	private double _payment;
	private int _allocatedNumberOfRecords;
}
