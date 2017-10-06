/**
 * 
 */
package ch.uzh.ifi.MechanismDesignPrimitives;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Dmitry Moor
 * The class implements a one-dimensional linear value function with a threshold. 
 */
public class LinearThresholdValueFunction implements IParametrizedValueFunction
{

	private static final Logger _logger = LogManager.getLogger(LinearThresholdValueFunction.class);
	
	/**
	 * A simple constructor.
	 */
	public LinearThresholdValueFunction()
	{
		this._marginalValue = 0;
		this._threshold = 0;
		this._params = null;
	}
	
	/**
	 * Constructor.
	 * @param marginalValue the slope of the linear value function.
	 * @param threshold the threshold value
	 * @param params parameters of the parametrized value function.
	 */
	public LinearThresholdValueFunction(double marginalValue, double threshold, double[] params)
	{
		_logger.debug("LinearThresholdValueFunction::LinearThresholdValueFunction(...)");
		this.setMarginalValue(marginalValue);
		this.setThreshold(threshold);
		this.setParams(params);
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.IParametrizedValueFunction#computeValue(double[])
	 */
	@Override
	public double computeValue(double[] Quantities) 
	{
		if( Quantities[0] < 0 ) throw new RuntimeException("Negative quantity: " + Quantities[0]);
		
		if( Quantities[0] < this._threshold )
			return this._marginalValue * Quantities[0];
		else
			return this._marginalValue * this._threshold;
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.IParametrizedValueFunction#setParams(double[])
	 */
	@Override
	public void setParams(double[] params) 
	{
		_params = new double[params.length];
		for(int i = 0; i < params.length; ++i)
			this._params[i] = params[i];
	}
	
	/**
	 * The method specifies the marginal value, i.e., the slope.
	 * @param value the marginal value of the value function.
	 */
	public void setMarginalValue(double value)
	{
		if( value < 0 ) throw new RuntimeException("Negative marginal value: " + value);
		_marginalValue = value;
	}
	
	/**
	 * The method specifies the threshold of the value function.
	 * @param threshold the threshold of the value function.
	 */
	public void setThreshold(double threshold)
	{
		if( threshold < 0 ) throw new RuntimeException("Negative threshold: " + threshold);
		_threshold = threshold;
	}

	private double _marginalValue;				//Marginal value of the single good
	private double _threshold;					//The maximum amount of the good for which the agent has positive marginal value
	private double[] _params;					//Parameters of the value function 
}
