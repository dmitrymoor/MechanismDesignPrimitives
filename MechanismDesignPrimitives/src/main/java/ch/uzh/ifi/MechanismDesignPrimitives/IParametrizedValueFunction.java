package ch.uzh.ifi.MechanismDesignPrimitives;

/**
 * An interface for different value functions of consumers.
 * @author Dmitry Moor
 *
 */
public interface IParametrizedValueFunction 
{

	/**
	 * The method computes value for the given bundle.
	 * @param Quantities contains quantities of goods x0,...,xn the value of which should be estimated.
	 * @return the value of the bundle with the specified quantities of goods
	 */
	double computeValue(double[] Quantities);
	
	/**
	 * The method sets the parameters of the parameterized value function.
	 * @param params an array of parameters of the parameterized value function.
	 */
	void setParams(double[] params);
}
