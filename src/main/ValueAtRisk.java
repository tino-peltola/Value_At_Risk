package main;

import java.util.ArrayList;
import java.util.Collections;
/**
* @author Tino Peltola
*This class contains methods required for value at risk calculation
*/
public class ValueAtRisk {
	
	
	private double confidenceLevel;
	protected int numAssets;
	private double[] value;
	private double portfolioValue;
	private static ArrayList<ArrayList<Double>> performance = new ArrayList<>();
	private int volMethod =1;
	
	//constructor
	public ValueAtRisk(double c, Portfolio p){
		confidenceLevel = c;
		numAssets = p.getNumAsset();
		value = new double[numAssets];
		for(int j = 0;j<numAssets;j++){
			value[j] = p.getAsset(j).getValue();
			performance.add(p.getAsset(j).getperformances());
			portfolioValue += p.getAsset(j).getValue();
		}
	}
	
	/**
	*Getter method for asset value
	*@oaram i index of asset
	*@return value of asset at index i
	*/
	public double getAssetValue(int i){return value[i];}
	
	/**
	*Getter method for portfolio value
	*@return value of portfolio
	*/
	public double getPortfolioValue(){
		return portfolioValue;
	}
	/**
	*Getter method for asset weight
	*@param i index of asset
	*@return weight of asset in portfolio
	*/
	public double getAssetWeight(int i){return value[i]/portfolioValue;}
	
	/**
	*Getter confidence Level
	*@return confidence Level
	*/
	public double getConfidenceLevel(){return confidenceLevel;}
	
	/**
	*Getter length of performance data
	*@return length of performance data 
	*/
	public int getdataLength(){
		return performance.get(0).size();}
	
	/**
	*Getter number of data 
	*@return number of data 
	*/
	public int getNumAsset(){return numAssets;}
	
	/**
	*Getter for performance of asset on spefici day
	*@param i index of asset
	*@param j index of day
	*@return performance of asset i on day j
	*/
	public Double getperformancedata(int i, int j){return performance.get(i).get(j);}
	
	/**
	*Getter for total of all performance
	*@param i index of asset
	*@return total of all performance for asset i
	*/
	private static double total(int i){
		double total =0;
		for(int j =0;j<performance.get(i).size();j++){
			//Check for errors that are present in files downloaded from yahoo finance
			if( performance.get(i).get(j) != Double.POSITIVE_INFINITY ){
				total = total + performance.get(i).get(j);
			}	
		}
		return total;
	}
	/**
	*Moving Average Variance method for asset i
	*@param i index of asset
	*@param avg average of asset i
	*@return Moivng average variance of asset i
	*/
	public static double mAVariance(int i , double avg){
			double total = 0;
			for(int j =0;j<performance.get(i).size();j++){
				//Check for errors that are present in files downloaded from yahoo finance
				if( Math.pow(performance.get(i).get(j)-avg,2) != Double.POSITIVE_INFINITY ){
					total = total +Math.pow(performance.get(i).get(j)-avg,2);
				}
			}
			return (total/(performance.get(i).size()-1));
	}
	
	/**
	*Calculate Correlation of two assets
	*@param a index of first asset
	*@param b index of second asset
	*@return Corellation Coeffiency of the two assets
	*/
	public double CalculateCorelationCo(int a, int b) {
		double aTotal = 0, bTotal =0 ;
		
		for(int j =0;j<performance.get(a).size();j++){
			aTotal  += Math.pow(performance.get(a).get(j)-average(a),2);
			bTotal  += Math.pow(performance.get(b).get(j)-average(b),2);
		}
		return (cov(a,b))/Math.sqrt(aTotal * bTotal);
	}
	
	/**
	*Calculate covariance of two assets
	*@param a index of first asset
	*@param b index of second asset
	*@return covariance of the two assets
	*/
	public double cov(int a, int b) {
		double aAvg = average(a), bAvg = average(b),result = 0;
		for(int i=0;i<performance.get(0).size();i++){
			result  += ((performance.get(a).get(i)-aAvg))*((performance.get(b).get(i)-bAvg));
		}
		return result;
	}
	/**
	*Calculate z score using Gaussian class
	*@return confidence level as z score 
	*/
	public double zScore(){return Gaussian.inverseCDF(confidenceLevel/100);}
	public double zScore(double i) {return Gaussian.inverseCDF(i/100);}
	
	/**
	*calculate volitality according to specified method
	*@param i index of asset
	*@param avg average of asset i
	*@return volitality of asset 
	*/
	public double volitality(int i, double avg){
		switch(volMethod) {
		   case 0 :
			   //Moving Average
			   System.out.println("Variance: "+variance(i ,avg));
			  return Math.sqrt(variance(i ,avg));
		   case 1 :
			  //EWMA
		      return Math.sqrt(variance(i,avg)); 
		   default : 
			   //GARCH(1,1)
			  return Math.sqrt(variance(i,avg));
		}
	}
	/**
	*calculate variance according to specified method
	*@param i index of asset
	*@param avg average of asset i
	*@return variance of asset 
	*/
	public double variance(int i , double avg){
		switch(volMethod) {
		   case 0 :
			   //Moving Average
		      return mAVariance(i, avg);
		   case 1 :
			   //EWMA
		      return eMWA(i,0); 
		   default : 
			   //GARCH(1,1)
			  return gARCH(i,0,mAVariance(i, avg));
		}
	}
	
	/**
	*GARCH(1,1) Variance method for asset i
	*@param i index of asset
	*@param depth the depth of the recursive call
	*@param longV long term volitility
	*@return GARCH(1,1) variance
	*/
	private double gARCH(int i, int depth, double longV) {
		//industry standard weights
		double gamma = .1, alpha =.1,beta =.8;
		//basecase
		if(performance.get(i).size()-depth==0){
			double var = 0;
			for(int c = 0;c<6;c++){
				var =+ Math.pow(performance.get(i).get(c),2);
			}
			var=var/6;
			return (Math.pow(performance.get(i).get(0), 2)*(alpha))+(var*beta)+(longV*gamma);
		}else{
			depth++;
			//recursive call
			return (longV*gamma)+(Math.pow(performance.get(i).get(performance.get(i).size()-depth), 2)*alpha)+(gARCH(i,depth,longV)*beta);
		}
	}
	/**
	*EWMA Variance method for asset i
	*@param i index of asset
	*@param depth the depth of the recursive call
	*@return EWMA variance
	*/
	private double eMWA(int i, int depth) {
		double lambda = .94; //industry standard 
		if(performance.get(i).size()-depth==0){
			double var = 0;
			int div = 6;
			for(int c = 0;c<div;c++){
				//Check for errors that are present in files downloaded from yahoo finance
				if( Math.pow(performance.get(i).get(c),2) != Double.POSITIVE_INFINITY ){
					var =+ Math.pow(performance.get(i).get(c),2) ; 
				}else{div++ ; 
				}
			}
			var=var/6;
			return var;
		}else{
			depth++;
			//Check for errors that are present in files downloaded from yahoo finance
			if(Math.pow(performance.get(i).get(performance.get(i).size()-depth), 2) != Double.POSITIVE_INFINITY){
				return (Math.pow(performance.get(i).get(performance.get(i).size()-depth), 2)*(1-lambda))+(eMWA(i,depth)*lambda);
			}else{
				//in cases of missing data that days result will not be added to the volitality inorderd to elimate chance of error
				return (eMWA(i,depth)*lambda);
			}
		}
	}
	
	/**
	*Calculates average of assets performance
	*@param i index of asset
	*@return average perforamnce of stock
	*/
	public static double average(int a){return total(a)/performance.get(a).size();}
	
	/**
	*sorts performance list in reverse order
	*@param i index of asset
	*/
	public void sort(int i){Collections.sort(performance.get(i),Collections.reverseOrder());}

	
	/**
	*resets confidence 
	*@param c new confidence Level
	*/
	public void setConfidence(double c) {confidenceLevel = c;}
}