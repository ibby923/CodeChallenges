//Given an array of integers, write some code to find all the integers that appear more than once in the array, sorted by which appears most often to least often (once)
package ibby.betaville.PlugFamily;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;




public class ArrayCount {

	
	//Accept the Hashmap as a input parameter and sort the Map on values, this is done using the Custom sort Comparator to sort the objects that are values in the HashMap
	public static HashMap sortComparator(Map unsorted) {
		LinkedList list = new LinkedList(unsorted.entrySet());
		
		Collections.sort(list,new Comparator() {public int compare(Object o1,Object o2) {
			 	return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});
		
		HashMap sortedMap = new LinkedHashMap();
		
		for(Iterator it= list.iterator();it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(),entry.getValue());
	}	
		
		return sortedMap;
	}
	
	

	public static void main(String [] args) {
		
		int arrLength=0;
		//Accept the length of the Array from the user
		try {
			System.out.println("enter the length of the Array");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String strLine = br.readLine();
			arrLength = Integer.parseInt(strLine);
		} 	catch(IOException e) {}
		
		//Initialize the Array of that length
		int[] intArray = new int[arrLength];
		//Generate random numbers from 0 to the length/4 to get some repeated numbers
		Random gene = new Random();
			for(int i=0;i<intArray.length;i++) {
				intArray[i] = gene.nextInt(arrLength/4);
			}	
			
		//Initialize a Map of size of the Array
			HashMap<Integer,Integer> myMap = new HashMap<Integer,Integer>(arrLength);
			
			
			//Add the numbers to the HashMap and add their respective counts
			for(int i=0;i<intArray.length;i++) {
				Integer frequency = myMap.get(intArray[i]);
				myMap.put(intArray[i],(frequency==null)?1:frequency+1);
			}
			
			//Eliminate the numbers that occur only once
			for(int i=0;i<intArray.length;i++) {
				Integer freq = myMap.get(intArray[i]);
					if(freq==1) {
						myMap.remove(intArray[i]);
					}
			}
			
			//Call the custom Comparator function to sort the Map on Values
			myMap = sortComparator(myMap);
				for (Entry<Integer, Integer> entry : myMap.entrySet()) {
						System.out.println(entry.getKey() + " ===> " + entry.getValue());
				}	
				System.out.println();
				System.out.println("Top frequency numbers in the array, The numbers occuring once are removed from the result set");
				
	}
}
