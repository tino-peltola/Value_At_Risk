/**
* Reference: George Marsaglia 
* Source: https://introcs.cs.princeton.edu/java/22library/Gaussian.java
* This code has been changed to work with the current program but is sourced from the above source
*/
package main;
// This code has been sourced from:
//The content of this class is intelectual property of princten, minor changes have been implemented to make this program appropriate for the project 
public class Gaussian {
	
	public Gaussian(){}
    // return pdf(x) = standard Gaussian pdf
    public static double pdf(double x) {
        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
    }
    // return cdf(z) = standard Gaussian cdf using Taylor approximation
    public static double cdf(double z) {
        if (z < -8.0) return 0.0;
        if (z >  8.0) return 1.0;
        double sum = 0.0, term = z;
        for (int i = 3;sum + term != sum; i += 2) {
            sum  = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * pdf(z);
    }
    // return cdf(z, mu, sigma) = Gaussian cdf with mean mu and stddev sigma
    public static double cdf(double z, double mu, double sigma) {return cdf((z - mu) / sigma);} 

    // Compute z such that cdf(z) = y via bisection search
    public static double inverseCDF(double y) { return inverseCDF(y, 0.00000001, -8, 8);} 

    // bisection search
    private static double inverseCDF(double y, double delta, double lo, double hi) {
        double mid = lo + (hi - lo) / 2;
        if (hi - lo < delta) return mid;
        if (cdf(mid) > y) return inverseCDF(y, delta, lo, mid);
        else              return inverseCDF(y, delta, mid, hi);
    }
    
}