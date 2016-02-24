package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Dmitry Moor
 *
 */
public class AllocationWoD extends Allocation
{

	/**
	 * The method adds all allocated plans into an internal data structure.
	 * Those plans might store realizations of some random variables rather than 
	 * expected values.
	 * @param  allocatedPlans - a list of allocated plans
	 */
	public void addAllocatedPlans(List<SemanticWebType> allocatedPlans)
	{
		_allocatedPlans = new LinkedList<AtomicBid>();
		_allocatedBiddersValues = new LinkedList<List<Double> >();
		_allocatedAuctioneersValues = new LinkedList<Double>();
		_allocatedSocialWelfare= new LinkedList<Double>();
		
		int j = 0;
		for(SemanticWebType plan : allocatedPlans)
		{
			_allocatedPlans.add(plan);
			List<Double> allocatedCostsForPlan = new LinkedList<Double>();
			
			for(int i = 0; i < plan.getInterestingSet().size(); ++i)
				allocatedCostsForPlan.add( plan.getActualCost(i) );

			_allocatedBiddersValues.add(allocatedCostsForPlan);
			_allocatedAuctioneersValues.add( plan.getAllocatedValue() );
			double sw = _allocatedAuctioneersValues.get(j);
			for(Double cost : _allocatedBiddersValues.get(j))
				sw -= cost;
			_allocatedSocialWelfare.add(sw);
			j += 1;
		}
	}
	
	
	/**
	 * The method adds all allocated plans into an internal data structure.
	 * Those plans might store realizations of some random variables rather than 
	 * expected values.
	 * @param allocatedPlans a list of allocated plans
	 * @param isComplex  not used
	 */
	public void addAllocatedPlans(List<ComplexSemanticWebType> allocatedPlans, boolean isComplex)
	{
		_allocatedBiddersValues = new LinkedList<List<Double> >();
		_allocatedAuctioneersValues = new LinkedList<Double>();
		_allocatedSocialWelfare= new LinkedList<Double>();
		
		int j = 0;
		for(ComplexSemanticWebType plan : allocatedPlans)
		{
			_allocatedPlans.add(plan);
			List<Double> allocatedCostsForPlan = new LinkedList<Double>();
			
			for(int i = 0; i < plan.getAllocatedSellers().size(); ++i)
				allocatedCostsForPlan.add( plan.getActualCost( plan.getAllocatedSellers().get(i) ) );

			_allocatedBiddersValues.add(allocatedCostsForPlan);
			_allocatedAuctioneersValues.add( plan.getAllocatedValue() );
			double sw = _allocatedAuctioneersValues.get(j);
			for(Double cost : _allocatedBiddersValues.get(j))
				sw -= cost;
			_allocatedSocialWelfare.add(sw);
			j += 1;
		}
	}
	

	/*
	 * 
	 */
	public List<Integer> getActuallyAllocatedItems(int tradeIdx)
	{
		List<Integer> actuallyAllocatedItems = new LinkedList<Integer>();
		
		System.out.println(">> " + ((SemanticWebType)_allocatedPlans.get(tradeIdx)).getNumberOfSellers());
		
		for(int i = 0; i < ((SemanticWebType)_allocatedPlans.get(tradeIdx)).getNumberOfSellers(); ++i)
			actuallyAllocatedItems.add(((SemanticWebType)_allocatedPlans.get(tradeIdx)).getActuallyAllocatedTuples(i));
		
		return actuallyAllocatedItems;
	}
	
	protected List<AtomicBid> _allocatedPlans;					//A list of allocated plans
	protected List<Double> _allocatedSocialWelfare;
}
