package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Dmitry Moor
 *
 */
public class CombinatorialType implements Type
{

	private static final Logger _logger = LogManager.getLogger(CombinatorialType.class);
	
	/**
	 * The method constructs an empty bid
	 */
	public CombinatorialType()
	{
		_logger.debug("CombinatorialType::CombinatorialType()");
		//_type = new HashMap<String, Double>();
		_atomicBids = new LinkedList<AtomicBid>();
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str = "Combinatorial Bid: ";
		for(AtomicBid atom : _atomicBids)
			str += atom.toString() + "\n";
		
		return str;
	}
	
	/**
	 * The method returns the number of atoms within the combinatorial type
	 * @return the number of atoms
	 */
	public int getNumberOfAtoms()
	{
		return _atomicBids.size();
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getAtom(int)
	 */
	@Override
	public AtomicBid getAtom(int atomIdx)
	{
		return _atomicBids.get(atomIdx);
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#removeAtom(int)
	 */
	@Override
	public void removeAtom(int atomIdx)
	{
		_atomicBids.remove(atomIdx);
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#addAtomicBid(ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid)
	 */
	@Override
	public void addAtomicBid(AtomicBid bid)
	{
		_atomicBids.add(bid);
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getAgentId()
	 */
	@Override
	public int getAgentId() 
	{
		if( _atomicBids.size() > 0)
			return _atomicBids.get(0).getAgentId();
		else
			return 0;
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getTypeComponent(int, java.lang.String)
	 */
	@Override
	public Object getTypeComponent(int atomIdx, String key) 
	{
		return _atomicBids.get(atomIdx).getTypeComponent(key);
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getTypeComponent(java.lang.String)
	 */
	@Override
	public Object getTypeComponent(String key) 
	{
		throw new RuntimeException("An atom must be specified.");
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getInterestingSet(int)
	 */
	@Override
	public List<Integer> getInterestingSet(int atomIdx) 
	{
		return _atomicBids.get(atomIdx).getInterestingSet();
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#setTypeComponent(int, java.lang.String, java.lang.Object)
	 */
	@Override
	public void setTypeComponent(int atomIdx, String key, Object componentValue) 
	{
		_atomicBids.get(atomIdx).setTypeComponent(key, componentValue);
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#setTypeComponent(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setTypeComponent(String key, Object componentValue) 
	{
		throw new RuntimeException("Not possible to set a type component of a combinatorial type. ");
	}
	
	/**
	 * The method returns a list of atoms of the combinatorial type.
	 * @return a list of atoms of the type
	 */
	public List<AtomicBid> getAtoms()
	{
		return _atomicBids;
	}
	
	protected List<AtomicBid> _atomicBids;						//Atomic bids within the type
}
