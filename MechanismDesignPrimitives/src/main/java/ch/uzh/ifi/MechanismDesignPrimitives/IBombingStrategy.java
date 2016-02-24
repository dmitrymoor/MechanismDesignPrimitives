package ch.uzh.ifi.MechanismDesignPrimitives;

/**
 * An interface for different "bombing" strategies used to generate a joint probability mass function.
 * @author Dmitry Moor
 *
 */
public interface IBombingStrategy 
{

	/**
	 * The method triggers the bomb application to the specified sample.
	 * @param sample - a sample to which the bomb should be applied
	 * @param nodeToBomb - specifies the epicenter of the bomb explosion.
	 * @return the sample affected by the bomb
	 */
	public double[] applyBomb(double[] sample, int nodeToBomb);
	
}
