package ch.uzh.ifi.MechanismDesignPrimitives;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class testQuasiLinearAgent {

	@Test
	public void testSimple() 
	{
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		double[] alloc1 = {0,0};
		IParametrizedValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		double[] alloc2 = {0,1};
		IParametrizedValueFunction v2 = new LinearThresholdValueFunction(4, 1, alloc2);
		
		double[] alloc3 = {1,0};
		IParametrizedValueFunction v3 = new LinearThresholdValueFunction(4, 1, alloc3);
		
		double[] alloc4 = {1,1};
		IParametrizedValueFunction v4 = new LinearThresholdValueFunction(6, 0, alloc4);
		
		IParametrizedValueFunction[] valueFunctions = {v1, v2, v3, v4};
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, allocations, valueFunctions);
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
		
		int auctioneerId = 0; 				//The market platform, M
		
		List<Integer> bidders = new LinkedList<Integer>();
		bidders.add(1);
		bidders.add(2);
		
		int dbID1 = 0;
		int dbID2 = 1;
		List<Integer> bundles = new LinkedList<Integer>();
		bundles.add(dbID1);						//Id of the bundle allocated to the 1st bidder
		bundles.add(dbID2);						//Id of the bundle allocated to the 2nd bidder
		
		double auctioneerValue = 0;
		List<Double> biddersValues = new LinkedList<Double>();
		biddersValues.add(10.);
		biddersValues.add(12.);
		
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
		
		allocation1.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities1);
		allocation2.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities2);
		allocation3.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities3);
		allocation4.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities4);
		allocation5.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities5);
		allocation6.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities6);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		double[] alloc1 = {0,0};
		IParametrizedValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		double[] alloc2 = {0,1};
		IParametrizedValueFunction v2 = new LinearThresholdValueFunction(4, 1, alloc2);
		
		double[] alloc3 = {1,0};
		IParametrizedValueFunction v3 = new LinearThresholdValueFunction(4, 1, alloc3);
		
		double[] alloc4 = {1,1};
		IParametrizedValueFunction v4 = new LinearThresholdValueFunction(6, 1, alloc4);
		
		IParametrizedValueFunction[] valueFunctions = {v1, v2, v3, v4};
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, allocations, valueFunctions);
		
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
		
		double auctioneerValue = 0;
		List<Double> biddersValues = new LinkedList<Double>();
		biddersValues.add(10.);
		biddersValues.add(12.);
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		double[] alloc1 = {0,0};
		IParametrizedValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		double[] alloc2 = {0,1};
		IParametrizedValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		double[] alloc3 = {1,0};
		IParametrizedValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		double[] alloc4 = {1,1};
		IParametrizedValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		IParametrizedValueFunction[] valueFunctions = {v1, v2, v3, v4};
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, allocations, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
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
		
		double auctioneerValue = 0;
		List<Double> biddersValues = new LinkedList<Double>();
		biddersValues.add(10.);
		biddersValues.add(12.);
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(0.5);
		
		
		allocation1.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		double[] alloc1 = {0,0};
		IParametrizedValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		double[] alloc2 = {0,1};
		IParametrizedValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		double[] alloc3 = {1,0};
		IParametrizedValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		double[] alloc4 = {1,1};
		IParametrizedValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		IParametrizedValueFunction[] valueFunctions = {v1, v2, v3, v4};
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, allocations, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
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
		
		double auctioneerValue = 0;
		List<Double> biddersValues = new LinkedList<Double>();
		biddersValues.add(10.);
		biddersValues.add(12.);
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 100;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		double[] alloc1 = {0,0};
		IParametrizedValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		double[] alloc2 = {0,1};
		IParametrizedValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		double[] alloc3 = {1,0};
		IParametrizedValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		double[] alloc4 = {1,1};
		IParametrizedValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		IParametrizedValueFunction[] valueFunctions = {v1, v2, v3, v4};
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, allocations, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
		List<Double> prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)-1e-6);
		
		List<Double> consumptionBundle = agent.solveConsumptionProblem(prices, allocation1);
		assertTrue( Math.abs(consumptionBundle.get(1) - agent.computeExpectedThreshold(allocation1)) < 1e-6);
		
		prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)+1e-6);
		
		consumptionBundle = agent.solveConsumptionProblem(prices, allocation1);
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
		
		double auctioneerValue = 0;
		List<Double> biddersValues = new LinkedList<Double>();
		biddersValues.add(10.);
		biddersValues.add(12.);
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.5);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 1;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		double[] alloc1 = {0,0};
		IParametrizedValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		double[] alloc2 = {0,1};
		IParametrizedValueFunction v2 = new LinearThresholdValueFunction(1, 2, alloc2);
		
		double[] alloc3 = {1,0};
		IParametrizedValueFunction v3 = new LinearThresholdValueFunction(1, 2, alloc3);
		
		double[] alloc4 = {1,1};
		IParametrizedValueFunction v4 = new LinearThresholdValueFunction(4, 1, alloc4);
		
		IParametrizedValueFunction[] valueFunctions = {v1, v2, v3, v4};
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, allocations, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		List<Double> bundle = new LinkedList<Double>();
		bundle.add(0.);
		bundle.add(1.5);
		
		List<Double> prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)-1e-6);
		
		List<Double> consumptionBundle = agent.solveConsumptionProblem(prices, allocation1);
		assertTrue( Math.abs(consumptionBundle.get(1) - agent.getEndowment() / prices.get(1)) < 1e-6);
		
		prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)+1e-6);
		
		consumptionBundle = agent.solveConsumptionProblem(prices, allocation1);
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
		
		double auctioneerValue = 0;
		List<Double> biddersValues = new LinkedList<Double>();
		biddersValues.add(10.);
		biddersValues.add(12.);
		
		List<Double> allocationProbabilities1 = new LinkedList<Double>();
		allocationProbabilities1.add(0.);
		allocationProbabilities1.add(1.0);
		
		
		allocation1.addAllocatedAgent(auctioneerId, bidders, bundles, auctioneerValue, biddersValues, allocationProbabilities1);
		
		//Describe buyer
		double endowment = 100;
		int allocations[] = {0b00, 0b01, 0b10, 0b11};	// 4 possible allocations
		
		double[] alloc1 = {0,0};
		IParametrizedValueFunction v1 = new LinearThresholdValueFunction(0, 0, alloc1);
		
		double[] alloc2 = {0,1};
		IParametrizedValueFunction v2 = new LinearThresholdValueFunction(4, 1, alloc2);
		
		double[] alloc3 = {1,0};
		IParametrizedValueFunction v3 = new LinearThresholdValueFunction(4, 1, alloc3);
		
		double[] alloc4 = {1,1};
		IParametrizedValueFunction v4 = new LinearThresholdValueFunction(6, 1, alloc4);
		
		IParametrizedValueFunction[] valueFunctions = {v1, v2, v3, v4};
		ParametrizedQuasiLinearAgent agent = new ParametrizedQuasiLinearAgent(1, endowment, allocations, valueFunctions);
		
		//Compute utility of the buyer for the given probabilistic allocation and bundle
		//List<Double> bundle = new LinkedList<Double>();
		//bundle.add(0.);
		//bundle.add(1.5);
		
		assertTrue(Math.abs( agent.computeExpectedMarginalValue(allocation1) - 4.) < 1e-6);
		
		List<Double> prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)-1e-6);
		
		List<Double> consumptionBundle = agent.solveConsumptionProblem(prices, allocation1);
		assertTrue( Math.abs(consumptionBundle.get(1) - agent.computeExpectedThreshold(allocation1)) < 1e-6);
		
		prices = new LinkedList<Double>();
		prices.add(1.0);
		prices.add( agent.computeExpectedMarginalValue(allocation1)+1e-6);
		
		consumptionBundle = agent.solveConsumptionProblem(prices, allocation1);
		assertTrue( Math.abs(consumptionBundle.get(1) - 0) < 1e-6);
	}
}
