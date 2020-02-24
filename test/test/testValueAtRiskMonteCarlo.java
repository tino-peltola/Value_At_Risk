package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.Portfolio;
import main.ValueAtRisk;
import main.ValueAtRiskMonteCarlo;

public class testValueAtRiskMonteCarlo {

	@Before
	public void setUp() throws Exception {
			Portfolio port =  new Portfolio();
			
			ValueAtRiskMonteCarlo mc = new ValueAtRiskMonteCarlo(95, port,1000);
			//port.addAsset("x", 50.0, p1);
			//port.addAsset("y", 50.0, p2);
			//var = new ValueAtRisk(0,port );
	
	}
	/*@Test
	public void TestingAverage() {
		assertEquals("TEST1: Calculating Mean",vmc.average(0), 15.6, 0.001);
	}
	@Test
	public void test() {
		fail("Not yet implemented");
	}*/

}
