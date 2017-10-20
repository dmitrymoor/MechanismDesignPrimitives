package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleType implements Type
{
	
	public SimpleType(int agentId, double val)
	{
		_agentId = agentId;
		_interestingSet = new LinkedList<Integer>();
		_interestingSet.add(0);
		
		_type = new HashMap<String, Double>();
		_type.put("Value", val);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Agent id=" + _agentId + " value=" + _type.get("Value");
	}
	
	/*
	 * 
	 */
	public double getValue()
	{
		return (double)_type.get("Value");
	}
	
	/*
	 * 
	 */
	public void setValue(double newValue)
	{
		_type.put("Value", newValue);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#getAgentId()
	 */
	@Override
	public int getAgentId()
	{
		return _agentId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#getTypeComponent(java.lang.String)
	 */
	@Override
	public Object getTypeComponent(int atomIdx, String key)
	{
		return _type.get(key);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#getInterestingSet()
	 */
	@Override
	public List<Integer> getInterestingSet(int atomIdx)
	{
		return _interestingSet;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#getNumberOfAtoms()
	 */
	@Override
	public int getNumberOfAtoms() 
	{
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#getAtom(int)
	 */
	@Override
	public AtomicBid getAtom(int atomIdx) 
	{
		List<Integer> items = new LinkedList<Integer>();
		items.add(1);
		AtomicBid atom = new AtomicBid(_agentId, items, (double)_type.get("Value"));
		return atom;
	}

	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#removeAtom(int)
	 */
	@Override
	public void removeAtom(int atomIdx) 
	{
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.Type#addAtomicBid(Mechanisms.AtomicBid)
	 */
	@Override
	public void addAtomicBid(AtomicBid bid) 
	{
		//TODO: add throwing exception as it is not possible to add a new atom to a SimpleType
	}
	
	/**
	 * 
	 * @param atomIdx
	 * @param key
	 * @param componentValue
	 */
	@Override
	public void setTypeComponent(int atomIdx, String key, Object componentValue) 
	{
		if( atomIdx > 1)	throw new RuntimeException("Simple type can contain at most one atom: " + atomIdx);
		else setTypeComponent(key, componentValue);
	}
	
	/**
	 * The method sets up components of the generic type.
	 * @param key
	 * @param componentValue
	 */
	@Override
	public void setTypeComponent(String key, Object componentValue) 
	{
		if(_type.containsKey(key)) throw new RuntimeException("The type already has such a key.");
		_type.put(key, (double)componentValue);
	}
	
	/**
	 * 
	 */
	@Override
	public Object getTypeComponent(String key) 
	{
		return _type.get(key);
	}
	
	private List<Integer> _interestingSet;
	private Map<String, Double> _type;
	private int _agentId;
}
