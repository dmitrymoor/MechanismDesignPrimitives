package ch.uzh.ifi.MechanismDesignPrimitives;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class testAllocation {

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

}
