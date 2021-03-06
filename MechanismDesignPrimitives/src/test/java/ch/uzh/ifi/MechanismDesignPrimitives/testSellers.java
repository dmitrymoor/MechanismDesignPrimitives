package ch.uzh.ifi.MechanismDesignPrimitives;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class testSellers {

	@Test
	public void test() 
	{
		int dbID1 = 1;
		int sellerId = 1;
		double sellerCost = 10;
		List<Integer> bundle = Arrays.asList(dbID1);
		
		AtomicBid atom = new AtomicBid(sellerId, bundle, sellerCost);
		
		SellerType seller = new SellerType(atom, Distribution.UNIFORM, 1., 1./3.);
		
		assertTrue( seller.getDistribution() == Distribution.UNIFORM );
		assertTrue( Math.abs((double)seller.getMean() - 1.0) < 1e-6);
		assertTrue( Math.abs((double)seller.getVariance() - 1./3.) < 1e-6);
		assertTrue( Math.abs((double)seller.computeCumulativeDistribution(1.) - 0.5) < 1e-6);
		assertTrue( Math.abs((double)seller.computeCumulativeDistribution(3.) - 1.) < 1e-6);
		assertTrue( Math.abs((double)seller.computeCumulativeDistribution(2.) - 1.) < 1e-6);
		assertTrue( Math.abs((double)seller.computeCumulativeDistribution(0.) - 0.) < 1e-6);
		
		assertTrue( Math.abs((double)seller.computeProbabilityDensity(0.) - 0.5) < 1e-6);
		assertTrue( Math.abs((double)seller.computeProbabilityDensity(1.) - 0.5) < 1e-6);
		assertTrue( Math.abs((double)seller.computeProbabilityDensity(2.) - 0.5) < 1e-6);
	}

}
