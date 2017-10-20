package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class SellerType implements Type
{

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
	
	
	
	@Override
	public int getAgentId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getTypeComponent(int atomIdx, String key) 
	{
		return null;
	}

	@Override
	public Object getTypeComponent(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTypeComponent(int atomIdx, String key, Object componentValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTypeComponent(String key, Object componentValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Integer> getInterestingSet(int atomIdx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfAtoms() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AtomicBid getAtom(int atomIdx) {
		// TODO Auto-generated method stub
		return null;
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
	
	private AtomicBid _atom;							// Atomic bid of the seller
	private Distribution _distribution;					// Distribution of the fixed cost of the seller
	private double _mean;								// Mean of the distribution
	private double _var;								// Variance of the distribution
}