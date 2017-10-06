package ch.uzh.ifi.MechanismDesignPrimitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class testValueFunction {

	@Test
	public void testSimple() 
	{
		double marginalValue = 1.5;
		double threshold = 4;
		double params[] = {0,1,0};
		IParametrizedValueFunction valueFunction = new LinearThresholdValueFunction(marginalValue, threshold, params);
		
		double quantities[] = {0};
		assertTrue(valueFunction.computeValue( quantities ) == 0);
		
		quantities[0] = 2;
		assertTrue(valueFunction.computeValue( quantities ) == 3);
		
		quantities[0] = 4;
		assertTrue(Math.abs(valueFunction.computeValue( quantities ) - 6) < 1e-6);
		
		quantities[0] = 10;
		assertTrue(Math.abs(valueFunction.computeValue( quantities ) - 6) < 1e-6);
	}

}
