import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

// Encapsulates open addressing to perform experiments.
public class OpenAddressing {

	// Constant initial seed value.
	private final long init_seed = 98760053;

	// Constant m value.
	private final int m = 1048576;

	// Instance of LinearCongruential to access standard deviation, pseudo random.
	private LinearCongruential lc;

	// Instantiate OpenAddressing and initialize it with the LinearCongruential.
	public OpenAddressing(LinearCongruential lc) {
		this.lc = lc;
	}

	// Performs experiment using linear probing method
	// and h(k, i) = (h'(k) + i) mod m, h'(k) = f(k, m).
	public String linearProbing(int n) {

		// Create hash table as array of double.
		double[] hashTbl = new double[m];

		// Set initial seed.
		long seed = init_seed;

		// To count number of probes.
		int numProbes = 0;

		// Loop through the table.
		for (int j = 0; j < n; j++) {

			// Obtain k value.
			long k = lc.pseudoRandom(seed);

			// Calculate hash using formula.
			long hashVal = (int) (long) Math.floor(m * ((k * ((Math.sqrt(5) - 1) / 2)) % 1));

			// Loop through the slots.
			for (int i = 0; i < n; i++) {

				// Calculate position using hash value.
				int position = (int) ((hashVal + i) % m);

				// Increment number of probes.
				numProbes++;

				// If position is empty.
				if (hashTbl[position] == 0) {

					// Insert value.
					hashTbl[position]++;

					// Out of loop (job done).
					break;
				}
			}

			// Update seed!
			seed = k;
		}

		// Call helper method and return string representation of experiment.
		return experiment(hashTbl, n, numProbes);
	}

	// Performs experiment using quadratic probing and
	// h(k, i) = (h'(k) + i(i+1)/2) mod m, h'(k) = f(k, m).
	public String quadraticProbing(int n) {

		// To count number of probes until inserted.
		int numProbes = 0;

		// Set initial seed value.
		long seed = init_seed;

		// Create hash table as an array of double.
		double[] hashTbl = new double[m];

		// Loop through the values in the array.
		for (int j = 0; j < n; j++) {

			// Obtain k value.
			long k = lc.pseudoRandom(seed);

			// Calculate hash value using formula.
			long hashVal = (int) (long) Math.floor(m * ((k * ((Math.sqrt(5) - 1) / 2)) % 1));

			// Loop through the probes.
			for (int i = 0; i < n; i++) {

				// Calculate position using formula.
				int position = (int) ((hashVal + (((long) i * (i + 1)) / 2)) % m);

				// Count probes.
				numProbes++;

				// If slot is empty.
				if (hashTbl[position] == 0) {

					// Insert value.
					hashTbl[position]++;

					// Out of loop.
					break;
				}
			}
			
			// Update seed!
			seed = k;
		}
		
		// Return string representation of the result of experiment.
		return experiment(hashTbl, n, numProbes);
	}

	// Performs experiment using double hashing technique.
	// Formulas:
	// h(k, i) = (h1(k) + i h2(k)) mod m, 
	// h1(k) = f(k, m), 
	// h2(k) = k mod m if k mod m is odd, (k mod m)+1 if k mod m is even 
	public String doubleHashing(int n) {
		
		// To count probes until inserted.
		int numProbes = 0;
		
		// Set initial seed value.
		long seed = init_seed;
		
		// Create hash table as array of double.
		double[] hashTbl = new double[m];
		
		// Loop through the elements in the array.
		for (int j = 0; j < n; j ++) {
			
			// Obtain k value.
			long k = lc.pseudoRandom(seed);
			
			// Apply formulas.
			long h = (long) Math.floor(m * ((k * ((Math.sqrt(5) - 1) / 2)) % 1));
			long h1 = k % m;
			long h2;
			if (h1 % 2 != 0) { // If odd.
				h2 = h1;
			} else { // If even.
				h1 ++;
				h2 = h1;
			}
			
			// Loop through the probes.
			for (int i = 0; i < n; i ++) {
				
				// Count probes.
				numProbes ++;
				
				// Obtain position.
				int position = (int) ((h + i * h2) % m);
				
				// If found empty slot.
				if (hashTbl[position] == 0) {
					
					// Insert value.
					hashTbl[position]++;
					
					// Out of loop.
					break;
				}
			}
			
			// Update seed.
			seed = k;
		}
		
		// Return string representation of the result of experiment.		
		return experiment(hashTbl, n, numProbes);
	}

	// Helper method to return result of experiment.
	private String experiment(double[] hashTbl, int n, int numProbes) {

		// To build string output.
		StringBuilder output = new StringBuilder();

		output.append("array size = " + m).append("\n");
		output.append("# of elements inserted: " + n).append("\n");
		output.append("load factor: " + (double) n / m).append("\n");

		output.append("average # of probes performed by insertion procedure = " + (numProbes / (double) n)).append("\n");
		output.append("\n");
		output.append("---------------------------------------------------------------------------------------------")
				.append("\n");
		output.append("The following is the distribution of the clusters.").append("\n");
		output.append("Display format is: cluster size, # of clusters, (# of clusters)/(total # of clusters)\n")
				.append("\n");

		TreeMap<Integer, Double> sizeFullClusterMap = new TreeMap<>(); // Stores size of full cluster as a key
		// and number of clusters with this size as a value.

		TreeMap<Integer, Double> sizeEmptyClusterMap = new TreeMap<>(); // Stores size of empty cluster as
		// a key and number of clusters with this size as a value.
		
		int numFullClusters = 0;
		int numEmptyClusters = 0;
		
		double sumClusterSize = 0; // Stores sum of sizes of all clusters (the same variable
		// for full and empty clusters).

		// Create array lists to store sizes of both of clusters to
		// properly calculate standard deviation of sizes.
		ArrayList<Double> listFullClusterSizes = new ArrayList<>();
		ArrayList<Double> listEmptyClusterSizes = new ArrayList<>();

		// Loop through the table.
		for (int j = 0; j < hashTbl.length; j ++) {

			// Store current size of cluster (initially 0).
			int currentSize = 0;

			// True if next for-loop was broken ('break' keyword was used)
			// and was not ended. It needs to update j variable to avoid
			// missing current value in the table in case loop was ended properly.
			boolean broken = false;

			if (hashTbl[j] == 0) { // Start of empty cluster.

				// Continue looping table to get all contiguous empty slots.
				for (int i = j; i < hashTbl.length; i ++) {

					// If next slot is also empty.
					if (hashTbl[i] == 0) {
						currentSize ++; // Increment current size of empty cluster.
					} else { // Otherwise (if slot is full).
						j = i - 1; // Update j to avoid missing current slot.
						broken = true; // Mark this loop as broken.
						break; // Out of loop.
					}
				}

				// If loop was ended (or not broken)
				if (!broken) {
					j = hashTbl.length - 1; // Go back to the previous slot.
				}
				
				// Store size.
				listEmptyClusterSizes.add((double) currentSize);
				
				// Count empty clusters.
				numEmptyClusters ++;
				
				// If size already exists in the map, increment it, otherwise
				// just put 1.
				sizeEmptyClusterMap.merge(currentSize, 1.0, (a, b) -> a + b);

			} else { // Start of full cluster.

				// Continue looping table to get all contiguous full slots.
				for (int i = j; i < hashTbl.length; i++) {

					// If next slot is also full.
					if (hashTbl[i] != 0) {
						currentSize ++; // Increment current size of full cluster.
					} else { // Otherwise (if slot is empty).
						j = i - 1; // Update j to avoid missing current slot.
						broken = true; // Mark this loop as broken.
						break; // Out of loop.
					}
				}

				// If loop was ended (or not broken)
				if (!broken) {
					j = hashTbl.length - 1; // Go back to the previous slot.
				}
				
				// Store size.
				listFullClusterSizes.add((double) currentSize);
				
				// Count full clusters.
				numFullClusters ++;
				
				// If size already exists in the map, increment it, otherwise
				// just put 1.
				sizeFullClusterMap.merge(currentSize, 1.0, (a, b) -> a + b);
			}
		}

		// Iterate over key-value pairs to build output with 
		// cluster size, # of clusters, (# of clusters)/(total # of clusters).
		// For full clusters.
		for (Map.Entry<Integer, Double> me : sizeFullClusterMap.entrySet()) {
			int k = me.getKey();
			double v = me.getValue();
			sumClusterSize += k * v; // Calculate sum using size and number of clusters with this size.
			output.append(k + ", " + (int) v + ", " + (v / numFullClusters)).append("\n");
		}
		
		output.append("\n");
		output.append("total # of clusters = " + numFullClusters).append("\n");
		output.append("average cluster size = " + (sumClusterSize / numFullClusters)).append("\n");

		// Convert ArrayList of Double to array of double to calculate standard
		// deviation.
		double[] arrFullClusterSizes = new double[listFullClusterSizes.size()];
		for (int j = 0; j < arrFullClusterSizes.length; j++) {
			arrFullClusterSizes[j] = listFullClusterSizes.get(j);
		}

		output.append("standard deviation of cluster sizes = " + lc.standardDeviation(arrFullClusterSizes) + "\n")
				.append("\n");

		output.append("---------------------------------------------------------------------------------------------")
				.append("\n");
		output.append("The following is the distribution of the empty clusters.").append("\n");
		output.append(
				"Display format is: empty cluster size, # of empty clusters, (# of empty clusters)/(total # of empty clusters)\n")
				.append("\n");

		// Reset sum.
		sumClusterSize = 0;
		
		// Iterate over key-value pairs to build output with 
		// cluster size, # of clusters, (# of clusters)/(total # of clusters).
		// For empty clusters.
		for (Map.Entry<Integer, Double> entry : sizeEmptyClusterMap.entrySet()) {
			int key = entry.getKey();
			double val = entry.getValue();
			sumClusterSize += key * val;
			output.append(key + ", " + (int) val + ", " + (val / numEmptyClusters)).append("\n");
		}

		output.append("\n");
		output.append("total # of empty clusters = " + numEmptyClusters).append("\n");
		output.append("average empty cluster size = " + (sumClusterSize / numEmptyClusters)).append("\n");

		// Convert ArrayList of Double to array of double to calculate standard
		// deviation.
		double[] arrEmptyClusterSizes = new double[listEmptyClusterSizes.size()];
		for (int j = 0; j < arrEmptyClusterSizes.length; j++) {
			arrEmptyClusterSizes[j] = listEmptyClusterSizes.get(j);
		}

		output.append(
				"standard deviation of empty cluster sizes = " + lc.standardDeviation(arrEmptyClusterSizes) + "\n")
				.append("\n");
		
		return output.toString();
	}

}
