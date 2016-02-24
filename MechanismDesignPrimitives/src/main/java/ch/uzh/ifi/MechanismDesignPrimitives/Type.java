package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.List;

/**
 * The interface for an agent's type. This is a multi-valued multidimensional type,
 * i.e., it may consist of several components (like the value, arrival and departure time etc.)
 * The type also has to contain an interesting set (or sets) for which these characteristics are 
 * applied.
 * 
 * @author Dmitry Moor
 */
public interface Type 
{
	/**
	 * The method returns an id of the corresponding agent.
	 * @return agent's id
	 */
	public int getAgentId();
	
	/**
	 * The method returns the component of the type vector, for example, a value, arrival and departure time etc.
	 * @param atomIdx - an index of an atom for which the component should be returned
	 * @param key - a key identifying the component of interest (E.g. "Value", "ArrivalTime", "DepartureTime" etc).
	 * @return the value of the corresponding component.
	 */
	public Object getTypeComponent(int atomIdx, String key);
	
	/**
	 * The method returns the component of the type vector, for example, a value, arrival and departure time etc.
	 * @param key - a key identifying the component of interest (E.g. "Value", "ArrivalTime", "DepartureTime" etc).
	 * @return the value of the corresponding component.
	 */
	public Object getTypeComponent(String key);
	
	/**
	 * The method sets up a specified component of the type to a specified value.
	 * @param atomIdx - an index of an atom for which the component should be setup
	 * @param key - the key identifying the component (E.g.  "Value")
	 * @param componentValue - a new value for the component
	 */
	public void setTypeComponent(int atomIdx, String key, Object componentValue);
	
	/**
	 * The method sets up a specified component of the type to a specified value.
	 * @param key - the key identifying the component (E.g.  "Value")
	 * @param componentValue - a new value for the component
	 */
	public void setTypeComponent(String key, Object componentValue);
	
	/**
	 * The method returns an interesting set of the type corresponding to the specified index.
	 * @param atomIdx - an atom for which an interesting set should be returned (0 in a single-valued domain).
	 * @return an interesting set of the type.
	 */
	public List<Integer> getInterestingSet(int atomIdx);
	
	/**
	 * The method returns the number of atoms of the type.
	 * @return the number of atoms
	 */
	public int getNumberOfAtoms();
	
	/**
	 * The method returns an atom with the given index.
	 * @param atomIdx - an index of the atom
	 * @return the corresponding atomic bid
	 */
	public AtomicBid getAtom(int atomIdx);
	
	/**
	 * The method removes an atom with the specified index.
	 * @param atomIdx - an index of an atom to be removed
	 */
	public void removeAtom(int atomIdx);
	
	/**
	 * The method adds another atom to the current bid.
	 * @param atom - a new atom to be added
	 */
	public void addAtomicBid(AtomicBid atom);
}
