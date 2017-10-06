package ch.uzh.ifi.MechanismDesignPrimitives;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class testAllocation {

	/**
	 * 
	 */
	@Test
	public void test() 
	{
		Allocation allocation = new Allocation();
		int auctioneerId = 0;
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		List<Integer> itsAllocatedBundles = new LinkedList<Integer>();
		itsAllocatedBundles.add(0);
		itsAllocatedBundles.add(0);
		double autioneerValue = 0.;
		List<Double> biddersValues = Arrays.asList(10., 20.);
		
		try 
		{
			allocation.addAllocatedAgent(auctioneerId, bidders, itsAllocatedBundles, autioneerValue, biddersValues);
			assertTrue(allocation.getAllocatedWelfare() == 30.);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Test probabilistic allocation in a domain with two bidders (data providers, or sellers) and
	 * a single auctioneer (i.e., market platform, M).
	 * 
	 */
	@Test
	public void testProbabilisticAllocation()
	{
		ProbabilisticAllocation allocation = new ProbabilisticAllocation();
		
		int auctioneerId = 0; 				//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID);						//Id of the bundle allocated to the 2nd bidder
		
		double auctioneerValue = 0;
		List<Double> biddersValues = new LinkedList<Double>();
		biddersValues.add(10.);
		biddersValues.add(12.);
		
		List<Double> allocationProbabilities = new LinkedList<Double>();
		allocationProbabilities.add(0.3);
		allocationProbabilities.add(0.7);
		
		try
		{
			allocation.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities);
			assertTrue(allocation.getAuctioneerId(0) == 0);
			assertTrue(allocation.getAllocationProbabilityOfBidderById(1) == 0.3);
			assertTrue(allocation.getAllocationProbabilityOfBidderById(2) == 0.7);
			assertTrue(allocation.getAllocationProbabilityOfBundle(dbID) == 1.);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
