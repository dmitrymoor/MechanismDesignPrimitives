package ch.uzh.ifi.MechanismDesignPrimitives;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Dmitry Moor
 *
 */
public class MarketPlatform 
{

	private static final Logger _logger = LogManager.getLogger(MarketPlatform.class);
	/**
	 * Constructor.
	 * @param buyers list of buyers
	 * @param sellers list of sellers
	 */
	public MarketPlatform(List<ParametrizedQuasiLinearAgent> buyers, List<AtomicBid> sellers)
	{
		_buyers  = buyers;
		_sellers = sellers;
	}
	
	/**
	 * The method computes the market demand at the given price.
	 * @param price the price of the good
	 * @param allocation probabilistic allocation of sellers (DBs)
	 * @return the market demand
	 */
	public List<Double> computeMarketDemand(double price, ProbabilisticAllocation allocation)
	{
		List<Double> marketDemand = Arrays.asList(0., 0.);
		List<Double> prices = Arrays.asList(1., price);
		
		for(ParametrizedQuasiLinearAgent buyer: _buyers)
		{
			List<Double> consumptionBundle = buyer.solveConsumptionProblem(prices, allocation);
			marketDemand.set(0, marketDemand.get(0) + consumptionBundle.get(0));
			marketDemand.set(1, marketDemand.get(1) + consumptionBundle.get(1));
			_logger.debug("Demand of i=" + buyer.getAgentId() + " x0: " + consumptionBundle.get(0) + "; x1: " + consumptionBundle.get(1));
		}
		
		return marketDemand;
	}
	
	private List<ParametrizedQuasiLinearAgent> _buyers;				//Buyers
	private List<AtomicBid> _sellers;								//Sellers
}
