package main;
import java.util.Random;
import java.util.Arrays;

/**
* @author Tino Peltola
* This class calculates Value at risk monte carlo calculations 
*/
public class ValueAtRiskMonteCarlo extends ValueAtRisk {
	private int numOfTrials;
	private Random rand = new Random();
	private int time = 390;//trading minutes in trading day of London Stock Exchange
	private double portValue[]; //contains the value of the portfolio at the end of the simulations
	
	/**
	* constructor method
	* @param c confidence level
	* @param p portfolio
	* @param n number of trial in the simulation
	*/
	public ValueAtRiskMonteCarlo(double c, Portfolio p, int n) {
		super(c, p);
		numOfTrials = n;
		portValue = new double[numOfTrials];
	}
	/**
	* calculate value at risk using monte carlo method
	* @return estiamtee value at risk
	*/
	public double evaluate() {
			//simulate diffrent psts 
			for(int i = 0; i<super.getNumAsset(); i++){simulate(i);}
			
		Arrays.sort(portValue);
		for(int k = 0;k<portValue.length;k++){System.out.println(portValue[k]);}
		return(portValue[positionfinder()]);
	}
	
	/**
	*Finds the position on var estimate based off the confidence and data size
	*@param index of var
	*/
	private int positionfinder(){
		return (int) Math.round(portValue.length*(super.getConfidenceLevel()/100));
	}
	/**
	*Simulate a path for the stock
	*@param index asset
	*/
	private void simulate(int i) {
		double timer =0;
		
		double stockPrice = super.getAssetValue(i);
		
		double stockVol = super.volitality(i, super.average(i)/Math.sqrt(time));
		
		double stockDrift = (super.average(i)/time)-.5*stockVol*stockVol;
		for(int j = 0 ; j<numOfTrials; j++){
			stockPrice = super.getAssetValue(i);
			for (int t = 0; t<time;t++){
				timer = rand.nextGaussian();
				//adapted algorithm to incease risk 
				stockPrice += (-stockVol*timer)+stockDrift;
				
				}
			portValue[j]+= stockPrice;
		}	
	}
	
}
