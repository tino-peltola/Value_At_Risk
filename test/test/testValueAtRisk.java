package test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.Portfolio;
import main.ValueAtRisk;

public class testValueAtRisk {
	private ValueAtRisk var;
	@Before
	public void setUp() throws Exception {
		Portfolio port =  new Portfolio();
		ArrayList<Double> p1 = new ArrayList<Double>();
		ArrayList<Double> p2 = new ArrayList<Double>();
		//List 1
        List<Double> per1 = Arrays.asList( 17.0,13.0,12.0,15.0,16.0,14.0,16.0,16.0,18.0,19.0);
        List<Double> per2 = Arrays.asList( 94.0,73.0,59.0,80.0,93.0,85.0,66.0,79.0,77.0,91.0);
        
        p1.addAll(per1);   
        p2.addAll(per2);
        
		port.addAsset("x", 50.0, p1);
		port.addAsset("y", 50.0, p2);
		var = new ValueAtRisk(0,port );
	}

	@Test
	public void TestingAverage() {
		assertEquals("TEST1: Calculating Mean",var.average(0), 15.6, 0.001);
	}
	@Test
	public void TestingStandardDeviation() {
		
		assertEquals("TEST2: Standard Deviation",var.standardDiviation(0,var.average(0)), 2.1705094128, 0.001);
		
	}
	@Test
	public void TestingVariance() {
		
		assertEquals("TEST3: Variance",var.variance(0 ,var.average(0)), 4.711111111, 0.001);
		
	}
	
	@Test
	public void TestingCoVariance() {
		System.out.println(var.cov(0,1));
		assertEquals("TEST4: CoVariance",var.cov(0,1), 134.8, 0.0001);
		
	}
	
	
	@Test
	public void TestingCorrelationCo() {
		assertEquals("TEST5: Correlation",var.CalculateCorelationCo(0,1), .5960935, 0.0001);
	}
	@Test
	public void TestingZScore() {

		assertEquals("TEST6: CNDF",var.zScore(98), 2.053749, 0.0001);
		
	}
	
}
