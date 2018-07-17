package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class SellerType implements Type
{

	/**
	 * Simple constructor.
	 * @param atom an atomic bid of the seller
	 */
	public SellerType(AtomicBid atom)
	{
		_atom = atom;
	}
	
	/**
	 * Constructor.
	 * @param atom
	 * @param distribution
	 * @param mean
	 * @param variance
	 */
	public SellerType(AtomicBid atom, Distribution distribution, double mean, double variance)
	{
		_atom = atom;
		_distribution = distribution;
		_mean = mean;
		_var = variance;
	}
	
	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getAgentId()
	 */
	@Override
	public int getAgentId() 
	{
		return _atom.getAgentId();
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getTypeComponent(int, java.lang.String)
	 */
	@Override
	public Object getTypeComponent(int atomIdx, String key) 
	{
		if( atomIdx > 0) throw new RuntimeException("SellerType supports only one atom per seller: " + atomIdx);
		return getTypeComponent(key);
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getTypeComponent(java.lang.String)
	 */
	@Override
	public Object getTypeComponent(String key) 
	{
		return _atom.getTypeComponent(key);
	}

	@Override
	public void setTypeComponent(int atomIdx, String key, Object componentValue) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTypeComponent(String key, Object componentValue) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * (non-Javadoc)
	 * @see ch.uzh.ifi.MechanismDesignPrimitives.Type#getInterestingSet(int)
	 */
	@Override
	public List<Integer> getInterestingSet(int atomIdx) 
	{
		if(atomIdx > 0) throw new RuntimeException("Currently only 1 atom is supported: " + atomIdx);
		return _atom.getInterestingSet();
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
		if(atomIdx > 0) throw new RuntimeException("Currently only 1 atom is supported: " + atomIdx);
		return _atom;
	}

	@Override
	public void removeAtom(int atomIdx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAtomicBid(AtomicBid atom) {
		// TODO Auto-generated method stub
		
	}

	public Distribution getDistribution()
	{
		return _distribution;
	}
	
	public double getMean()
	{
		return _mean;
	}
	
	public double getVariance()
	{
		return _var;
	}
	
	public void setDistribution(Distribution distribution)
	{
		_distribution = distribution;
	}

	public void setMean(double mean)
	{
		_mean = mean;
	}
	
	public void setVariance(double var)
	{
		_var = var;
	}
	
	public double computeCumulativeDistribution(double cost)
	{
		double cdf = -1.;
		
		if( _distribution == Distribution.UNIFORM )
		{
			UniformRealDistribution dist = new UniformRealDistribution(_mean - Math.sqrt(3.*_var), _mean + Math.sqrt(3.*_var));
			return dist.cumulativeProbability(cost);
		}
		else if( _distribution == Distribution.NORMAL)
		{
			NormalDistribution dist = new NormalDistribution(_mean, Math.sqrt(_var));
			return dist.cumulativeProbability(cost);
		}
		
		return cdf;
	}
	
	public double computeProbabilityDensity(double cost)
	{
		double pdf = -1.;
		
		if( _distribution == Distribution.UNIFORM )
		{
			UniformRealDistribution dist = new UniformRealDistribution(_mean - Math.sqrt(3.*_var), _mean + Math.sqrt(3.*_var));
			return dist.density(cost);
		}
		else if( _distribution == Distribution.NORMAL)
		{
			NormalDistribution dist = new NormalDistribution(_mean, Math.sqrt(_var));
			return dist.density(cost);
		}

		return pdf;
	}
	
	/**
	 * The method returns the virtual cost of the seller.
	 * @return the virtual cost of the agent
	 */
	public double getItsVirtualCost()
	{
		return computeVirtualCost(_atom.getValue());
	}
	
	/**
	 * The method implements a virtual cost function of the agent.
	 * @param cost cost of the agent
	 * @return virtual cost of the agent at the given point
	 */
	public double computeVirtualCost(double cost)
	{
		double virtualCost = 0.;
		virtualCost = cost + computeCumulativeDistribution(cost)/computeProbabilityDensity(cost);

		return virtualCost;
	}
	
	/**
	 * The method computes an inverse virtual cost function at a given point
	 * @param virtualCost virtual cost
	 * @return cost that is an inverse virtual cost
	 */
	public double computeInverseVirtualCost(double virtualCost)
	{
		double cost = 0.;
		double lowerBound = _mean - Math.sqrt(3.*_var);
		
		if(_distribution == Distribution.UNIFORM)
			cost = (virtualCost + lowerBound) / 2.;
		else throw new RuntimeException("Not defined.");
		
		return cost;
	}
	
	private AtomicBid _atom;							// Atomic bid of the seller
	private Distribution _distribution;					// Distribution of the fixed cost of the seller
	private double _mean;								// Mean of the distribution
	private double _var;								// Variance of the distribution
}
