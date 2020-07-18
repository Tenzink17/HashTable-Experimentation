import java.util.Map;
import java.util.TreeMap;

// Encapsulates experiments with separate chaining.
public class Chaining {

	// Constant initial seed.
	private final long init_seed = 98760053;

	// Constant M.
	private final int m = 1000003;

	// Reference to the class that contains methods
	// for pseudo-random generator.
	private LinearCongruential lc;

	// Instantiate Chaining and initialize it with
	// LinearCongruential.
	public Chaining(LinearCongruential lc) {
		this.lc = lc;
	}

	// Performs experiment using h(k) = k mod m, where k is the generated key value.
	public String experimentA(int n) {
		
		// Assign seed value.
		long seed = init_seed;
		
		// Create hash table as array of double values.
		// Here I use double (not int) to get the correct
		// result from LinearCongruential#standardDeviation() method.
		double[] hashTbl = new double[m];

		// Loop through the all values in the array.
		for (int i = 0; i < n; i ++) {
			
			// Calculate k using seed.
			long k = lc.pseudoRandom(seed);
			
			// Calculate position to insert element (here h(k) = k mod m is used).
			int position = (int) (k % m); 
			
			// Insert element (just increment current value) to
			// appropriate position.
			hashTbl[position]++;
			
			// Update seed!
			seed = k;
		}

		// Return the string representation of result of experiment A.
		return experiment(hashTbl, n);
	}

	// Performs experiment B. Here h(k) = f(k, m) hash function is used.
	public String experimentB(int n) {
		
		// Initialize seed value.
		long seed = init_seed;
		
		// Create hash table as array of double.
		double[] hashTbl = new double[m];

		// Loop through the array.
		for (int i = 0; i < n; i ++) {
			
			// Get k using seed.
			long k = lc.pseudoRandom(seed);
			
			// Use h(k) = f(k, m) hash function to obtain position.
			int position = (int) (long) Math.floor(m * ((k * ((Math.sqrt(5) - 1) / 2)) % 1));
			
			// Insert element (just increment current value) to
			// appropriate position. Here it is bucket size.
			hashTbl[position]++;
			
			// Update seed.
			seed = k;
		}

		// Return the string representation of result of experiment B.
		return experiment(hashTbl, n);
	}

	// Helper method to get the experiment results.
	private String experiment(double[] hashTbl, int n) {
		
		// First, create string builder to build output.
		StringBuilder output = new StringBuilder();
		
		// Map to store bucket size as a key and number of buckets with this size as a value.
		// Note, I used TreeMap to sort bucket sizes (keys).
		TreeMap<Double, Integer> bucketSizeBucketNumMap = new TreeMap<>(); 
		
		// Iterate over bucket sizes.
		for (double bucketSize : hashTbl) {
			
			// Here if current bucket size (key) already exists in the map, 
			// then we need increment number of buckets with this size (value, a + b).
			// Otherwise just put 1.
			bucketSizeBucketNumMap.merge(bucketSize, 1, (a, b) -> a + b);			
		}

		output.append("array size = total # of buckets: " + m).append("\n");
		output.append("# of elements inserted: " + n + "\n").append("\n");
		output.append("The following is the distribution of bucket sizes.").append("\n");
		output.append("Display format is: bucket size, # of buckets, (# of buckets)/(total # of buckets)\n").append("\n");

		// Iterate over all key-value pairs to obtain bucket size and number of buckets with
		// this size. Also calculate (# of buckets)/(total # of buckets).
		for (Map.Entry<Double, Integer> me : bucketSizeBucketNumMap.entrySet()) { 
			output.append((int) (double) me.getKey() + ", " + me.getValue() + ", " + 
			((double) (int) me.getValue() / m)).append("\n");
		}
		output.append("\n");
		output.append("load factor: " + (double) n / m).append("\n");
		output.append(
				"standard deviation of bucket sizes from load factor = " + lc.standardDeviation(hashTbl)).append("\n");
		output.append("\n");
		
		return output.toString();
	}

}
