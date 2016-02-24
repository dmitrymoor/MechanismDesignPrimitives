package ch.uzh.ifi.MechanismDesignPrimitives;

import static org.junit.Assert.*;

import ch.uzh.ifi.MechanismDesignPrimitives.AtomicBid;
import ch.uzh.ifi.MechanismDesignPrimitives.CombinatorialType;
import ch.uzh.ifi.MechanismDesignPrimitives.MultiUnitAtom;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;


public class testAtomicBid {

	@Test
	public void testAtomicBidSimple() {
		List<Integer> items = new LinkedList<Integer>();
		items.add(1);
		items.add(3);
		double value = 20;
		AtomicBid bid = new AtomicBid(1, items, value);
		assertTrue(bid.getValue() == value);
		assertTrue( bid.getInterestingSet().contains(1) );
		assertTrue( bid.getInterestingSet().contains(3) );
		assertTrue( bid.getInterestingSet().size() == 2 );
	}

	@Test
	public void testToString() {
		List<Integer> items = new LinkedList<Integer>();
		items.add(1);
		items.add(3);
		AtomicBid bid = new AtomicBid(1, items, 20);
		
		assertTrue( bid.toString().equals("( AgentId=1,[1, 3],Value=20.0; )") );
		
		bid.setTypeComponent("Distribution", 1.0);
		assertTrue( bid.toString().equals("( AgentId=1,[1, 3],Distribution=1.0; Value=20.0; )") );
		
	}

	@Test
	public void testSetValue() {
		List<Integer> items = new LinkedList<Integer>();
		items.add(1);
		items.add(3);
		AtomicBid bid = new AtomicBid(1, items, 20);
		assertTrue(bid.getValue() == 20);
		bid.setValue(30);
		assertTrue(bid.getValue() == 30);
	}
	
	@Test
	public void testComplexBid() {
		List<Integer> items1 = new LinkedList<Integer>();
		items1.add(1);
		items1.add(3);
		
		List<Integer> items2  = new LinkedList<Integer>();
		items2.add(2);
		items2.add(4);
		
		AtomicBid atom1 = new AtomicBid(1, items1, 20);
		AtomicBid atom2 = new AtomicBid(1, items2, 30);
		
		Vector bid1 = new Vector();
		bid1.add(atom1);
		bid1.add(atom2);
		
		Vector bid2 = new Vector();
		bid2.add(atom2);
		
		Vector bids = new Vector();
		bids.add(bid1);
		bids.add(bid2);
	
		assertTrue(bids.toString().equals("[[( AgentId=1,[1, 3],Value=20.0; ), ( AgentId=1,[2, 4],Value=30.0; )], [( AgentId=1,[2, 4],Value=30.0; )]]"));
	}
	
/*	@Test
	public void testCombinatorialBid() {
		List<Integer> items1 = new LinkedList<Integer>();
		items1.add(1);
		items1.add(3);
		AtomicBid bid1 = new AtomicBid(1, items1, 20);
		
		List<Integer> items2 = new LinkedList<Integer>();
		items2.add(1);
		items2.add(2);
		AtomicBid bid2 = new AtomicBid(1, items2, 20);
		
		CombinatorialType bid = new CombinatorialType();
		bid.addAtomicBid(bid1);
		bid.addAtomicBid(bid2);
		
		assertTrue(bid1.getValue() == 20);
		bid1.setValue(30);
		assertTrue(bid1.getValue() == 30);
		
		bid.setTypeComponent(0, "Distribution", 1.0);
		bid.setTypeComponent(1, "Distribution", 1.0);
		bid.setTypeComponent(0, "minValue", 0.0);
		bid.setTypeComponent(1, "minValue", 0.0);
		bid.setTypeComponent(0, "maxValue", 1.0);
		bid.setTypeComponent(1, "maxValue", 2.0);
		
		bid.resetType(1);
		double value11 = (Double)bid.getTypeComponent(0, "Value");
		double value12 = (Double)bid.getTypeComponent(1, "Value");
		bid.resetType(2);
		double value21 = (Double)bid.getTypeComponent(0, "Value");
		double value22 = (Double)bid.getTypeComponent(1, "Value");
		bid.resetType(1);
		double value31 = (Double)bid.getTypeComponent(0, "Value");
		double value32 = (Double)bid.getTypeComponent(1, "Value");
		assertTrue(value11 == value31);
		assertTrue(value12 == value32);
		assertTrue(value11 != value21);
		assertTrue(value12 != value22);
	}*/
	/*
	@Test
	public void testMultiUnitAtom() throws Exception {
		List<Integer> items1 = new LinkedList<Integer>();
		items1.add(1);
		items1.add(3);
		List<Integer> units1 = new LinkedList<Integer>();
		units1.add(10);
		units1.add(30);
		AtomicBid bid1 = new MultiUnitAtom(1, items1, units1, 20);
		
		List<Integer> items2 = new LinkedList<Integer>();
		items2.add(1);
		items2.add(2);
		List<Integer> units2 = new LinkedList<Integer>();
		units2.add(10);
		units2.add(20);
		AtomicBid bid2 = new MultiUnitAtom(2, items2, units2, 20);
		
		assertTrue(bid2.toString().contains("( AgentId=2, Items=[1, 2], units=[10, 20], Value=20.0; )"));
		assertTrue(bid1.toString().contains("( AgentId=1, Items=[1, 3], units=[10, 30], Value=20.0; )"));
		assertTrue( ((MultiUnitAtom)bid1).getNumberOfUnits(0) == 10 );
		assertTrue( ((MultiUnitAtom)bid1).getNumberOfUnits(1) == 30 );
		
		
		CombinatorialType bid = new CombinatorialType();
		bid.addAtomicBid(bid1);
		bid.addAtomicBid(bid2);
		
		assertTrue(bid1.getValue() == 20);
		bid1.setValue(30);
		assertTrue(bid1.getValue() == 30);
		
		bid.setTypeComponent(0, "Distribution", 1.0);
		bid.setTypeComponent(1, "Distribution", 1.0);
		bid.setTypeComponent(0, "minValue", 0.0);
		bid.setTypeComponent(1, "minValue", 0.0);
		bid.setTypeComponent(0, "maxValue", 1.0);
		bid.setTypeComponent(1, "maxValue", 2.0);
		
		bid.resetType(1);
		double value11 = (Double)bid.getTypeComponent(0, "Value");
		double value12 = (Double)bid.getTypeComponent(1, "Value");
		bid.resetType(2);
		double value21 = (Double)bid.getTypeComponent(0, "Value");
		double value22 = (Double)bid.getTypeComponent(1, "Value");
		bid.resetType(1);
		double value31 = (Double)bid.getTypeComponent(0, "Value");
		double value32 = (Double)bid.getTypeComponent(1, "Value");
		assertTrue(value11 == value31);
		assertTrue(value12 == value32);
		assertTrue(value11 != value21);
		assertTrue(value12 != value22);
	}
	*/
	@Test
	public void testAtomCopy() throws Exception {

		List<Integer> items1 = new LinkedList<Integer>();
		items1.add(1);
		items1.add(3);

		List<Integer> units1 = new LinkedList<Integer>();
		units1.add(10);
		units1.add(30);
		AtomicBid bid = new MultiUnitAtom(1, items1, units1, 20);
		bid.setTypeComponent("Distribution", 1.0);
		bid.setTypeComponent("minValue", 0.0);
		bid.setTypeComponent("maxValue", 1.0);
		
		AtomicBid bid2 = bid.copyIt();
		
		assertTrue( ! bid.equals(bid2) );
		assertTrue( bid.getAgentId() == bid2.getAgentId() );
		assertTrue( bid.getValue() == bid2.getValue() );
		assertTrue( bid.getTypeComponent("Distribution") == bid2.getTypeComponent("Distribution") );
		assertTrue( bid.getTypeComponent("minValue") == bid2.getTypeComponent("minValue") );
		assertTrue( bid.getTypeComponent("maxValue") == bid2.getTypeComponent("maxValue") );
		assertTrue( bid.getNumberOfUnitsByItemId(1) == bid2.getNumberOfUnitsByItemId(1));
		assertTrue( bid.getNumberOfUnitsByItemId(3) == bid2.getNumberOfUnitsByItemId(3));
	}
}
