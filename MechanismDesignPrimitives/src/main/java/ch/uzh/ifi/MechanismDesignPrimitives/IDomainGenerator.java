package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.List;

/**
 * Interfaces for different domain generation strategies.
 * @author Dmitry
 *
 */
public interface IDomainGenerator 
{
	/**
	 * The method generates a new bid for the specified agent from the implemented domain.
	 * @param seed - a random seed
	 * @param type - the type of the agent
	 * @return a combinatorial type generated for the implemented domain
	 */
	public Type generateBid(long seed, Type type);
}
