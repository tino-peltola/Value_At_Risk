package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
/**
 * @author  Tino Peltola
 * This Class is used to create an asset with name, monetary value and performance(decimal) list.
 */
public class Asset {
	private ArrayList<Double> performance = new ArrayList<Double>();
	private String name;
	private Double value;
	
	public Asset(String n, Double v){
		name = n;
		value = v;
		calculatePerformance();
	}
	/**
	 * Constructor used in back testing when array list of performance is pre made
	 * @param  n name of asset
	 * @param v monetary value of asset 
	 * @param a performance of the asset in deimal format
	 */
	public Asset(String n, Double v, ArrayList<Double> a){
		name = n;
		value = v;
		performance = a;
	}
	/**
	 * In case performance list is not given this method will get data from data folder and calculate it.
	 */
	private void calculatePerformance() {
		int count =0,lines = lineCounter("data\\"+ getName() +".L.csv");
		String line;
		double yesturdayClose =0, close;
		BufferedReader reader = null;
		//try open & read file, catch file not found and IO errors
		try {
			//open the reader with the file
            reader = new BufferedReader(new FileReader("data\\"+ getName() +".L.csv"));
 
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if(count == lines-101){yesturdayClose = Double.parseDouble(row[4]);}
                //gets last hundred data pounts
                if(count > lines-101){
                	close = Double.parseDouble(row[4]);
                	performance.add((close-yesturdayClose)/yesturdayClose); 
                	yesturdayClose = close;
                }
            	count++; 
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
            	//close reader if reader is not null
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	/**
	 * Getter method for Value 
	 * @return monetary value of asset 
	 */
		public double getValue(){
		return value;
	}
		
	/**
	* Getter method for Name 
	* @return name of assets
	*/
	public String getName(){return name;}
	
	/**
	 * Getter method perfomance array list
	 * @return name assets name
	 */
	public ArrayList<Double> getperformances(){return performance;}
	
	/**
	 * Calculates the line number in file
	 * @param fp the file path
	 * @return number of lines in file
	 */
	private static int lineCounter(String fp){
		 BufferedReader br = null; 
		 int rowCount = 0;
		//try reading file and count line count, catch file not found error or IO error
		try {
          br = new BufferedReader(new FileReader(fp));
          //counts the lines of the file 
          while ((br.readLine()) != null) {
              rowCount++;
          }
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          if (br != null) {
          	//try close the reader if it isn't null yet
              try {
                  br.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
		 return rowCount;
	}

	@Override
	public String toString() {
		return "Asset [performance=" + performance + ", name=" + name + ", value=" + value + "]";
	}


	
}
