package main;
import java.util.ArrayList; 
/**
* @author Tino Peltola
*This class creates a portfolio that contain assets.
*/
public class Portfolio {
	ArrayList<Asset> assets = new ArrayList<Asset>();
	int numAssets;
	public Portfolio(){
	}
	
	/**
	* adds asset into the porfolio
	*@param n name of the asset
	*@param v monetary value of asset
	*/
	public void addAsset( String n, double v) {
		assets.add(new Asset(n,v));
		numAssets++;
	}
	/**
	* adds asset into the porfolio
	*@param n name of the asset
	*@param v monetary value of asset
	*@param p array list of assets performance
	*/
	public void addAsset( String n, double v, ArrayList<Double> p) {
		assets.add(new Asset(n,v,p));
		numAssets++;
	}
	/**
	* Getter method for number of assets 
	*@return number of assets 
	*/
	public int getNumAsset(){return numAssets;}
	
	/**
	* Getter method for performance array list 
	*@return array list of performance 
	*/
	public  ArrayList<Asset> getAssets(){return assets;}
	
	/**
	* Getter method particular asset
	* @param i index of asset
	*@return asset at index i
	*/
	public Asset getAsset(int i) {return assets.get(i);}
	
	/**
	* Removes everything from the portfolio
	*/
	public void removeAllAssets() {
		assets.clear();
		numAssets = 0;}
}
