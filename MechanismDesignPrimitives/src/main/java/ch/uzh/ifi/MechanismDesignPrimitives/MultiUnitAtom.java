package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiUnitAtom extends AtomicBid
{

	/*
	 * Constructor
	 * @param agentId - agent's id
	 * @param items - the list of items
	 * @param units - list of units of each kind of items
	 * @param value - the value of the bundle
	 */
	public MultiUnitAtom(int agentId, List<Integer> items, List<Integer> units, double value) throws Exception 
	{
		super(agentId, items, value);
		if(items.size() != units.size()) throw new Exception("The number of units should match the number of items");

		_units = units;
	}

	/*
	 * Constructor
	 * @param agentId - agent's id
	 * @param items - the set of items
	 * @param units - list of units of each kind of items
	 * @param value - the value of the bundle
	 */
	public MultiUnitAtom(int agentId, Set<Integer> items, List<Integer> units, double value) throws Exception 
	{
		super(agentId, items, value);
		if(items.size() != units.size()) throw new Exception("The number of units should match the number of items");

		_units = units;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Mechanisms.AtomicBid#copyIt()
	 */
	@Override
	public AtomicBid copyIt()
	{
		AtomicBid bid;
		try 
		{
			bid = new MultiUnitAtom(_agentId, _items, _units, _type.get("Value") );
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new RuntimeException("Can't copy the bid " + this.toString());
		}
		
		Iterator it = _type.entrySet().iterator();
		while( it.hasNext() )
		{
			Map.Entry<String, Double> pair = (Map.Entry<String, Double>)it.next();
			bid.setTypeComponent(pair.getKey(), pair.getValue());
		}
		return bid;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str =  "( AgentId=" + _agentId + ", Items=" + _items + ", units=" + _units + ", " ;
		for(Map.Entry<String, Double> component : _type.entrySet())
			str += component.getKey() + "=" + component.getValue() + "; ";
		str += ")";
		return str;
	}
	
	/*
	 * 
	 */
	public int getNumberOfUnits(int index)
	{
		return _units.get(index);
	}
	
	/*
	 * 
	 */
	public List<Integer> getNumbersOfUnits()
	{
		return _units;
	}
	
	/*
	 * 
	 */
	@Override
	public int getNumberOfUnitsByItemId(int itemId)
	{
		int idx = 0;
		for(Integer item : _items)
		{
			if(itemId == item)
				return _units.get(idx);
			idx += 1;
		}
		throw new RuntimeException("no such itemId " + itemId);
	}
	
	private List<Integer> _units;				//The list of numbers of units for every item
}
