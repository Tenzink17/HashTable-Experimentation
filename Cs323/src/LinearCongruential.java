import java.util.stream.DoubleStream;

// Encapsulates pseudo-random generation methods.
public class LinearCongruential {
	
	// Constant a.
	private final long a = 16807;
	
	// Constant M.
	private final long M = 2147483647;
	
	// Method to calculate mean value.
	public double mean(double[] data) {
		int sum = 0;
		for (double d : data) {
			sum += d;
		}
		return (double)sum / data.length;
	}

	// Method to calculate standard deviation value.
	public double standardDeviation(double[] data) {

		// Obtain mean.
		double mean = mean(data);

		// Loop through the elements in the input array.
		for (int i = 0; i < data.length; i ++) {
			
			// Update current element using formula.
			data[i] = Math.pow(data[i] - mean, 2);
		}
		
		// Obtain sum of all elements in the updated array.
		double sum = DoubleStream.of(data).sum();
		
		// Return standard deviation using formula.
		return Math.sqrt(sum / (data.length - 1));

	}
	
	// Obtain pseudo random valud using given seed.
	public long pseudoRandom(long seed) {
		return (a * seed) % M;
	}
}
