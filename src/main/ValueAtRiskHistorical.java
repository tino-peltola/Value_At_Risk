package main;

import java.util.ArrayList;
import java.util.Collections;
/**
* @author Tino Peltola
* This class calculates Value at risk historical calculations 
*/
public class ValueAtRiskHistorical extends ValueAtRisk {
	
	/**
	*Constructor method for object
	*@param confidenceLevel confidence level of calulation
	*@param port portfolio 
	*/
	public ValueAtRiskHistorical(int confidenceLevel,Portfolio port){
	super(confidenceLevel,port);
	}
	
	/**
	*Calculate method for historical with new confidence leve 
	*@param confidence new confidence level
	*@param port var estimate
	*/
	public double calculate(double confidence){
		super.setConfidence(confidence);
		return calculate();
	}
	/**
	*Calculate method for historical
	*@param confidence
	*@param port var estimate
	*/
	public double calculate(){
		if(super.numAssets==1){
			super.sort(0);
			return super.getAssetValue(0)*Math.abs(getperformancedata(0,Positionfinder()));
		}else{
			ArrayList<Double> porfolioPerformance = new ArrayList<Double>();
			double dayTotalP = 0;
			//add the risk of each asset
			for(int i =0; i< super.getdataLength();i++){
				dayTotalP = 0;
				for (int j= 0; j<super.numAssets;j++){
					dayTotalP += (super.getperformancedata(j, i)*super.getAssetWeight(j));
				}
				porfolioPerformance.add(dayTotalP);
			}
			sort(porfolioPerformance);
			return super.getPortfolioValue()*Math.abs(porfolioPerformance.get(Positionfinder()));
		}	
	}
	/**
	*Finds the position on var estimate based off the confidence and data size
	*@param index of var
	*/
	public int Positionfinder (){return (int) Math.round(super.getdataLength()*(super.getConfidenceLevel()/100));}
	
	/**
	*Sorts the arraylist in reverse over 
	*@param i the array rist that gets sorted
	*/
	public void sort (ArrayList<Double> i){Collections.sort(i,Collections.reverseOrder());}
	
}
