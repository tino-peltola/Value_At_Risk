package main;
/**
* @author Tino Peltola
* This class performce Value at risk Gaussian calculations 
*/
public class ValueAtRiskGaussian extends ValueAtRisk {
	
	/**
	*Constructor method for object
	*@param confidenceLevel confidence level of calulation
	*@param port portfolio 
	*/
	public ValueAtRiskGaussian(int confidenceLevel,Portfolio port){
		super(confidenceLevel,port);
		}
	/**
	*calculate value at risk with new confidence level
	*@param confidence new confidence leve;
	*@return value at risk estimate
	*/
	public double calculate(double confidence){
		super.setConfidence(confidence);
		return calculate();
	}
	/**
	*calculate value at risk
	*@return value at risk estimate
	*/
	public double calculate(){
		if(super.numAssets==0){
			//calculate using single asset method
			 return singleVaR();
		}else{
			//calculate using portfolio method
			return portfolioVaR();
		}
	}	
	
	/**
	*calculate value at risk for single asset
	*@return value at risk estimate for single asset
	*/
	public double singleVaR(){
		return super.getAssetValue(0)*(Math.abs((super.average(0)-(super.zScore()*volitality(0,super.average(0))))));
	}
	/**
	*create covariance matrix and calculate VaR
	*@return value at risk estimate for portfolio asset
	*/
	public double portfolioVaR(){
		//create covariance matrix
		double[][] covarianceMatrix = new double[super.getNumAsset()][super.getNumAsset()];
		//fill in covariance matrix
		for(int i = 0;i<super.getNumAsset();i++){
			for(int j = 0;j<super.getNumAsset();j++){
				if(i==j){
					covarianceMatrix [i][j] = super.variance(i, super.average(i));
				}else{
					covarianceMatrix [i][j] = (super.CalculateCorelationCo(i, j)
							*super.volitality(j, super.average(j))*super.volitality(i, super.average(i)));
				}
			}
		}

		//call on portfolio Variance to calculate variance from metrix
		return (super.zScore()*Math.sqrt(portfolioVariance(covarianceMatrix)));
	}
	/**
	*calculate variance of porfolio from covariance matrix
	*@param covarianceMatrix the porfolioss covarianceMatrix of assets
	*@return variance of matrix
	*/
	private double portfolioVariance(double[][] covarianceMatrix){
		double rowTotal = 0;
		double variance = 0;
		for(int i =0;i<covarianceMatrix.length;i++ ){
			rowTotal = 0;
			for(int j =0;j<covarianceMatrix.length;j++ ){
				rowTotal += (covarianceMatrix[i][j]*super.getAssetValue(j)) ;	
			}
			//instead of transposing the value array the mutiplication of the matrix can be done here
			variance += rowTotal*super.getAssetValue(i);
		}
		return variance;
	}
}
