package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * The class implements a generic atomic bid of an agent.
 * @author Dmitry Moor
 */
public class AtomicBid implements Type
{
	//Properties of an atom
	public static final String IsBidder = "isSeller";					//1.0 if this is a type of a bidder, 0.0 if this is a type of an auctioneer
	public static final String Distribution = "Distribution";			//Probability distribution for uncertain domains
	public static final String LowerBound = "LowerBound";				//Lower bound for the uniform probability distribution
	public static final String UpperBound = "UpperBound";				//Upper bound for the uniform probability distribution
	public static final String MinValue = "minValue";					//Minimum possible value of an agent for this atom
	public static final String MaxValue = "maxValue";					//Maximum possible value of an agent for this atom
	public static final String Value = "Value";							//A value of a bidder for this atom
	
	private static final Logger _logger = LogManager.getLogger(AtomicBid.class);
	
	/**
	 * The method constructs a simple atomic bid {bundle, value}
	 * @param agentId  an id of the agent
	 * @param items  a set of items of the bid
	 * @param value  evaluation of the set of items
	 */
	public AtomicBid(int agentId, List<Integer> items, double value)
	{
		_logger.debug("AtomicBid::AtomicBid ( agentId=" + agentId + ", items="+items.toString() + ", value="+value +")" );

		_items = items;
		_type = new HashMap<String, Double>();
		_type.put(Value, value);
		_agentId = agentId;
	}
	
	/**
	 * The method constructs a simple atomic bid {items, value}
	 * @param agentId  an id of an agent
	 * @param items  a set of items of the bid
	 * @param value  evaluation of the set of items
	 */
	public AtomicBid(int agentId, Set<Integer> items, double value)
	{
		_logger.debug("AtomicBid::AtomicBid ( agentId=" + agentId + ", items="+items.toString() + ", value="+value +")" );
		_items = new ArrayList<Integer>();
		for(Integer item : items)
			_items.add(item);
		
		_type = new HashMap<String, Double>();
		_type.put(Value, value);
		_agentId = agentId;
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object rhsObj)
	{
		_logger.debug("-> equals(...)");
		AtomicBid rhs = (AtomicBid)rhsObj;
		
		if( _agentId == rhs.getAgentId() )
		{
			if( ! (rhs.getInterestingSet().contains(_items) && _items.contains(rhs.getInterestingSet()) ) )
				return false;
			
			Iterator<Entry<String, Double> > it = _type.entrySet().iterator();
			while( it.hasNext() )
			{
				Map.Entry<String, Double> pair = it.next();
				if( !rhs.getTypeComponent(pair.getKey()).equals(pair.getValue()))
					return false;
			}
		}
		
		_logger.debug("<- equals(...)");
		return true;
	}
	
	/**
	 * The method creates a deep copy of the atom.
	 * @return a new atomic bid which is a deep copy of this object
	 */
	public AtomicBid copyIt()
	{
		_logger.debug("-> copyIt()");
		AtomicBid bid = new AtomicBid(_agentId, _items, _type.get(Value) );
		
		Iterator<Entry<String, Double> > it = _type.entrySet().iterator();
		while( it.hasNext() )
		{
			Map.Entry<String, Double> pair = it.next();
			bid.setTypeComponent(pair.getKey(), pair.getValue());
		}
		
		_logger.debug("<- copyIt()");
		return bid;
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str =  "( AgentId=" + _agentId + "," + _items + "," ;
		for(Map.Entry<String, Double> component : _type.entrySet())
			str += component.getKey() + "=" + component.getValue() + "; ";
		return str +  ")";
	}
	
	/**
	 * The method sets up a value for the atomic bid
	 * @param value a new value
	 */
	public void setValue(double value)
	{
		_type.put(Value, value);
	}
	
	/**
	 * The method returns the value of the atom
	 * @return the value of the atom
	 */
	public double getValue()
	{
		return _type.get(Value);
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getAgentId()
	 */
	public int getAgentId()
	{
		return _agentId;
	}
	
	/**
	 * The method returns the interesting set of the atom.
	 * @return the bundle
	 */
	public List<Integer> getInterestingSet()
	{
		return _items;
	}
	
	/**
	 * The method sets the bundle corresponding to this atom.
	 * @param interestingSet a bundle
	 */
	public void setInterestingSet(List<Integer> interestingSet)
	{
		_items = interestingSet;
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getTypeComponent(java.lang.String)
	 */
	public Object getTypeComponent(String key)
	{
		return _type.get(key);
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#setTypeComponent(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setTypeComponent(String key, Object componentVal)
	{
		_type.put(key, (Double)componentVal ); 
	}
	
	/**
	 * The method specifies an integer type component
	 * @param key a type component
	 * @param componentVal value of the component
	 */
	public void setTypeComponent(String key, Integer componentVal)
	{
		setTypeComponent(key, componentVal.doubleValue());
	}
	
	/**
	 * The method specifies a type component.
	 * @param key a type component
	 * @param componentVal value of the component
	 */
	public void setTypeComponent(String key, Double componentVal)
	{
		_type.put(key, componentVal );
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getTypeComponent(int, java.lang.String)
	 */
	@Override
	public Object getTypeComponent(int atomIdx, String key) 
	{
		return getTypeComponent(key);
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#setTypeComponent(int, java.lang.String, java.lang.Object)
	 */
	@Override
	public void setTypeComponent(int atomIdx, String key, Object componentValue) 
	{
		if( atomIdx >= getNumberOfAtoms() ) throw new RuntimeException("Cannot set the " + atomIdx +"-th component of an atom.");
		setTypeComponent(key, componentValue);
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getInterestingSet(int)
	 */
	@Override
	public List<Integer> getInterestingSet(int atomIdx) 
	{
		if( atomIdx >= getNumberOfAtoms() ) throw new RuntimeException("Cannot get a bundle of the " + atomIdx +"-th component of an atom.");
		return getInterestingSet();
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getNumberOfAtoms()
	 */
	@Override
	public int getNumberOfAtoms() 
	{
		return 1;
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getAtom(int)
	 */
	@Override
	public AtomicBid getAtom(int atomIdx) 
	{
		if(atomIdx >= getNumberOfAtoms()) throw new RuntimeException("Incorrect atom index: " + atomIdx + " (trying to get an atom of an atom).");
		return this;
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#removeAtom(int)
	 */
	@Override
	public void removeAtom(int atomIdx) 
	{
		if(atomIdx >= getNumberOfAtoms() ) 	throw new RuntimeException("Incorrect atom index: " + atomIdx);
		else 								throw new RuntimeException("An atom can't remove itself");
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#addAtomicBid(ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid)
	 */
	@Override
	public void addAtomicBid(AtomicBid bid) 
	{
		throw new RuntimeException("Can't add an atom to the atom");
	}
	
	/**
	 * The method returns the number of units (1) of items for the atom
	 * @param itemId an id of an item for which the number of units should be received
	 * @return the number of units for the atom
	 */
	public int getNumberOfUnitsByItemId(int itemId)
	{
		if( _items.contains(itemId))
			return 1;
		else throw new RuntimeException("no such itemId " + itemId);
	}
	
	/**
	 * Given a list of costs of goods, the method computes the total additive cost of the bundle.
	 * @param costs  costs of individual goods
	 * @return the cost of the bundle, i.e., the sum of costs of all items in the bundle
	 */
	public double computeCost(List<Double> costs)
	{
		if(_items.size() > 0)
			return _items.stream().map( gId -> costs.get( gId-1 ) ).reduce( (x1, x2) -> x1 + x2 ).get();
		else return 0.;
	}
	
	protected int _agentId;						//An id of the agent
	protected List<Integer> _items;				//A set of items of the bid
	protected Map<String, Double> _type;		//A set of features of the type (e.g., "value" etc.)
}
