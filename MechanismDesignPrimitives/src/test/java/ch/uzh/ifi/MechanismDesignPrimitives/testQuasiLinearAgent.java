package ch.uzh.ifi.MechanismDesignPrimitives;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class testQuasiLinearAgent {

	@Test
	public void testSimple() 
	{
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		List<Double> alloc1 = Arrays.asList(0., 0.);
		LinearThresholdValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		List<Double> alloc2 = Arrays.asList(0., 1.);
		LinearThresholdValueFunction v2 = new LinearThresholdValueFunction(4, 1, alloc2);
		
		List<Double> alloc3 = Arrays.asList(1., 0.);
		LinearThresholdValueFunction v3 = new LinearThresholdValueFunction(4, 1, alloc3);
		
		List<Double> alloc4 = Arrays.asList(1., 1.);
		LinearThresholdValueFunction v4 = new LinearThresholdValueFunction(6, 0, alloc4);
				
		Map<Integer, LinearThresholdValueFunction> valueFunctions = new HashMap<Integer, LinearThresholdValueFunction>();
		valueFunctions.put(0, v1);
		valueFunctions.put(1, v2);
		valueFunctions.put(2, v3);
		valueFunctions.put(3, v4);
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, valueFunctions);
	}

	@Test
	public void testUtility() throws Exception
	{
		//Describe probabilistic allocation of sellers
		ProbabilisticAllocation allocation1 = new ProbabilisticAllocation();
		ProbabilisticAllocation allocation2 = new ProbabilisticAllocation();
		ProbabilisticAllocation allocation3 = new ProbabilisticAllocation();
		ProbabilisticAllocation allocation4 = new ProbabilisticAllocation();
		ProbabilisticAllocation allocation5 = new ProbabilisticAllocation();
		ProbabilisticAllocation allocation6 = new ProbabilisticAllocation();
		
		int auctioneerId = 0; 					//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID1 = 0;
		int dbID2 = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID1);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID2);						//Id of the bundle allocated to the 2nd bidder
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(1.0);
		
		List<Double> allocationProbabilities2 = new LinkedList<Double>();
		allocationProbabilities2.add(0.75);
		allocationProbabilities2.add(1.0);
		
		List<Double> allocationProbabilities3 = new LinkedList<Double>();
		allocationProbabilities3.add(1.0);
		allocationProbabilities3.add(0.5);
		
		List<Double> allocationProbabilities4 = new LinkedList<Double>();
		allocationProbabilities4.add(1.0);
		allocationProbabilities4.add(1.0);
		
		List<Double> allocationProbabilities5 = new LinkedList<Double>();
		allocationProbabilities5.add(0.5);
		allocationProbabilities5.add(0.5);
		
		List<Double> allocationProbabilities6 = new LinkedList<Double>();
		allocationProbabilities6.add(0.);
		allocationProbabilities6.add(0.);
		
		allocation1.addAllocatedAuctioneer(auctioneerId, bidders, bundles,  allocationProbabilities1);
		allocation2.addAllocatedAuctioneer(auctioneerId, bidders, bundles,  allocationProbabilities2);
		allocation3.addAllocatedAuctioneer(auctioneerId, bidders, bundles,  allocationProbabilities3);
		allocation4.addAllocatedAuctioneer(auctioneerId, bidders, bundles,  allocationProbabilities4);
		allocation5.addAllocatedAuctioneer(auctioneerId, bidders, bundles,  allocationProbabilities5);
		allocation6.addAllocatedAuctioneer(auctioneerId, bidders, bundles,  allocationProbabilities6);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		List<Double> alloc1 = Arrays.asList(0., 0.);
		LinearThresholdValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		List<Double> alloc2 = Arrays.asList(0., 1.);
		LinearThresholdValueFunction v2 = new LinearThresholdValueFunction(4, 1, alloc2);
		
		List<Double> alloc3 = Arrays.asList(1., 0.);
		LinearThresholdValueFunction v3 = new LinearThresholdValueFunction(4, 1, alloc3);
		
		List<Double> alloc4 = Arrays.asList(1., 1.);
		LinearThresholdValueFunction v4 = new LinearThresholdValueFunction(6, 1, alloc4);
		
		Map<Integer, LinearThresholdValueFunction> valueFunctions = new HashMap<Integer, LinearThresholdValueFunction>();
		valueFunctions.put(0, v1);
		valueFunctions.put(1, v2);
		valueFunctions.put(2, v3);
		valueFunctions.put(3, v4);
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.);
		
		assertTrue(Math.abs(agent.computeUtility(allocation1, bundle)-5.0)<1e-6);
		assertTrue(Math.abs(agent.computeUtility(allocation2, bundle)-5.5)<1e-6);
		assertTrue(Math.abs(agent.computeUtility(allocation3, bundle)-5.0)<1e-6);
		assertTrue(Math.abs(agent.computeUtility(allocation4, bundle)-6.0)<1e-6);
		assertTrue(Math.abs(agent.computeUtility(allocation5, bundle)-2.625)<1e-6);
		assertTrue(Math.abs(agent.computeUtility(allocation6, bundle)-0.0)<1e-6);
	}
	
	
	@Test
	public void testUtility1() throws Exception
	{
		//Describe probabilistic allocation of sellers
		ProbabilisticAllocation allocation1 = new ProbabilisticAllocation();
		
		int auctioneerId = 0; 				//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID1 = 0;
		int dbID2 = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID1);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID2);						//Id of the bundle allocated to the 2nd bidder
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAuctioneer(auctioneerId, bidders, bundles, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		List<Double> alloc1 = Arrays.asList(0., 0.);
		LinearThresholdValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		List<Double> alloc2 = Arrays.asList(0., 1.);
		LinearThresholdValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		List<Double> alloc3 = Arrays.asList(1., 0.);
		LinearThresholdValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		List<Double> alloc4 = Arrays.asList(1., 1.);
		LinearThresholdValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		Map<Integer, LinearThresholdValueFunction> valueFunctions = new HashMap<Integer, LinearThresholdValueFunction>();
		valueFunctions.put(0, v1);
		valueFunctions.put(1, v2);
		valueFunctions.put(2, v3);
		valueFunctions.put(3, v4);
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
		agent.updateAllocProbabilityDistribution(allocation1);
		assertTrue(Math.abs(agent.computeExpectedMarginalValue(allocation1)-2.5 ) < 1e-6);
		assertTrue(Math.abs(agent.computeExpectedThreshold(allocation1)-1.5) < 1e-6);
		assertTrue(Math.abs(agent.computeUtility(allocation1, bundle)-3.75)<1e-6);
	}
	
	@Test
	public void testUtility2() throws Exception
	{
		//Describe probabilistic allocation of sellers
		ProbabilisticAllocation allocation1 = new ProbabilisticAllocation();
		
		int auctioneerId = 0; 				//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID1 = 0;
		int dbID2 = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID1);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID2);						//Id of the bundle allocated to the 2nd bidder
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(0.5);
		
		
		allocation1.addAllocatedAuctioneer(auctioneerId, bidders, bundles, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		List<Double> alloc1 = Arrays.asList(0., 0.);
		LinearThresholdValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		List<Double> alloc2 = Arrays.asList(0., 1.);
		LinearThresholdValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		List<Double> alloc3 = Arrays.asList(1., 0.);
		LinearThresholdValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		List<Double> alloc4 = Arrays.asList(1., 1.);
		LinearThresholdValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		Map<Integer, LinearThresholdValueFunction> valueFunctions = new HashMap<Integer, LinearThresholdValueFunction>();
		valueFunctions.put(0, v1);
		valueFunctions.put(1, v2);
		valueFunctions.put(2, v3);
		valueFunctions.put(3, v4);
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
		agent.updateAllocProbabilityDistribution(allocation1);
		assertTrue(Math.abs(agent.computeExpectedMarginalValue(allocation1)-1.5 ) < 1e-6);
		assertTrue(Math.abs(agent.computeExpectedThreshold(allocation1)-1.25) < 1e-6);
		assertTrue(Math.abs(agent.computeUtility(allocation1, bundle)-1.5*1.25)<1e-6);
	}
	
	/**
	 * Non-binding endowment constraint
	 * @throws Exception
	 */
	@Test
	public void testConsumptionProblemNonBindingEndowment() throws Exception
	{
		//Describe probabilistic allocation of sellers
		ProbabilisticAllocation allocation1 = new ProbabilisticAllocation();
		
		int auctioneerId = 0; 				//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID1 = 0;
		int dbID2 = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID1);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID2);						//Id of the bundle allocated to the 2nd bidder
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAuctioneer(auctioneerId, bidders, bundles, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 100;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		List<Double> alloc1 = Arrays.asList(0., 0.);
		LinearThresholdValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		List<Double> alloc2 = Arrays.asList(0., 1.);
		LinearThresholdValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		List<Double> alloc3 = Arrays.asList(1., 0.);
		LinearThresholdValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		List<Double> alloc4 = Arrays.asList(1., 1.);
		LinearThresholdValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		Map<Integer, LinearThresholdValueFunction> valueFunctions = new HashMap<Integer, LinearThresholdValueFunction>();
		valueFunctions.put(0, v1);
		valueFunctions.put(1, v2);
		valueFunctions.put(2, v3);
		valueFunctions.put(3, v4);
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
		List<Double> prices = new LinkedList<Double>();
		prices.add(1.0);
		agent.updateAllocProbabilityDistribution(allocation1);
		prices.add( agent.computeExpectedMarginalValue(allocation1)-1e-6);
		
		List<Double> consumptionBundle = agent.solveConsumptionProblem(prices.get(1)/*, allocation1*/);
		assertTrue( Math.abs(consumptionBundle.get(1) - agent.computeExpectedThreshold(allocation1)) < 1e-6);
		
		prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)+1e-6);
		
		consumptionBundle = agent.solveConsumptionProblem(prices.get(1)/*, allocation1*/);
		assertTrue( Math.abs(consumptionBundle.get(1) - 0) < 1e-6);
	}
	
	/**
	 * Binding endowment constraint
	 * @throws Exception
	 */
	@Test
	public void testConsumptionProblemBindingEndowment() throws Exception
	{
		//Describe probabilistic allocation of sellers
		ProbabilisticAllocation allocation1 = new ProbabilisticAllocation();
		
		int auctioneerId = 0; 				//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID1 = 0;
		int dbID2 = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID1);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID2);						//Id of the bundle allocated to the 2nd bidder
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAuctioneer(auctioneerId, bidders, bundles, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		List<Double> alloc1 = Arrays.asList(0., 0.);
		LinearThresholdValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		List<Double> alloc2 = Arrays.asList(0., 1.);
		LinearThresholdValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		List<Double> alloc3 = Arrays.asList(1., 0.);
		LinearThresholdValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		List<Double> alloc4 = Arrays.asList(1., 1.);
		LinearThresholdValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		Map<Integer, LinearThresholdValueFunction> valueFunctions = new HashMap<Integer, LinearThresholdValueFunction>();
		valueFunctions.put(0, v1);
		valueFunctions.put(1, v2);
		valueFunctions.put(2, v3);
		valueFunctions.put(3, v4);
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
		List<Double> prices = new LinkedList<Double>();
		prices.add(1.0);
		agent.updateAllocProbabilityDistribution(allocation1);
		prices.add( agent.computeExpectedMarginalValue(allocation1)-1e-6);
		
		List<Double> consumptionBundle = agent.solveConsumptionProblem(prices.get(1)/*, allocation1*/);
		assertTrue( Math.abs(consumptionBundle.get(1) - agent.getEndowment() / prices.get(1)) < 1e-6);
		
		prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)+1e-6);
		
		consumptionBundle = agent.solveConsumptionProblem(prices.get(1)/*, allocation1*/);
		assertTrue( Math.abs(consumptionBundle.get(1) - 0) < 1e-6);
	}
	
	@Test
	public void testConsumptionProblem() throws Exception
	{
		//Describe probabilistic allocation of sellers
		ProbabilisticAllocation allocation1 = new ProbabilisticAllocation();
		
		int auctioneerId = 0; 				//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID1 = 0;
		int dbID2 = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID1);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID2);						//Id of the bundle allocated to the 2nd bidder
				
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAuctioneer(auctioneerId, bidders, bundles, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 100;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		List<Double> alloc1 = Arrays.asList(0., 0.);
		LinearThresholdValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		List<Double> alloc2 = Arrays.asList(0., 1.);
		LinearThresholdValueFunction v2 = new LinearThresholdValueFunction(4, 1, alloc2);
		
		List<Double> alloc3 = Arrays.asList(1., 0.);
		LinearThresholdValueFunction v3 = new LinearThresholdValueFunction(4, 1, alloc3);
		
		List<Double> alloc4 = Arrays.asList(1., 1.);
		LinearThresholdValueFunction v4 = new LinearThresholdValueFunction(6, 1, alloc4);
		
		Map<Integer, LinearThresholdValueFunction> valueFunctions = new HashMap<Integer, LinearThresholdValueFunction>();
		valueFunctions.put(0, v1);
		valueFunctions.put(1, v2);
		valueFunctions.put(2, v3);
		valueFunctions.put(3, v4);
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		agent.updateAllocProbabilityDistribution(allocation1);
		assertTrue(Math.abs( agent.computeExpectedMarginalValue(allocation1) - 4.) < 1e-6);
		
		List<Double> prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)-1e-6);
		
		List<Double> consumptionBundle = agent.solveConsumptionProblem(prices.get(1)/*, allocation1*/);
		assertTrue( Math.abs(consumptionBundle.get(1) - agent.computeExpectedThreshold(allocation1)) < 1e-6);
		
		prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)+1e-6);
		
		consumptionBundle = agent.solveConsumptionProblem(prices.get(1)/*, allocation1*/);
		assertTrue( Math.abs(consumptionBundle.get(1) - 0) < 1e-6);
	}
}
