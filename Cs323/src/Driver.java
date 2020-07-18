// Driver class to run the program.
public class Driver {

	// Main method to run the program.
	public static void main(String args[]) {

		// Instantiate LinearCongruential to access method to
		// calculate standard deviation, pseudo random value.
		LinearCongruential lc = new LinearCongruential();

		// Instantiate Chaining and initialize it with LinearCongruential.
		Chaining c = new Chaining(lc);
		
		System.out.println("================ Chaining experiments with h(k) = k mod m, A ================\n");

		System.out.println("================ 800000 ================\n");
		System.out.println(c.experimentA(800000));
		
		System.out.println("================ 1000000 ================\n");
		System.out.println(c.experimentA(1000000));
		
		System.out.println("================ 2000000 ================\n");
		System.out.println(c.experimentA(2000000));
		
		System.out.println("================ 3000000 ================\n");
		System.out.println(c.experimentA(3000000));
		
		
		System.out.println("================ Chaining experiments with h(k) = f(k, m), B ================\n");

		System.out.println("================ 800000 ================\n");
		System.out.println(c.experimentB(800000));
		
		System.out.println("================ 1000000 ================\n");
		System.out.println(c.experimentB(1000000));
		
		System.out.println("================ 2000000 ================\n");
		System.out.println(c.experimentB(2000000));
		
		System.out.println("================ 3000000 ================\n");
		System.out.println(c.experimentB(3000000));

		// Instantiate OpenAddressing and initialize it with LinearCongruential.
		OpenAddressing oa = new OpenAddressing(lc);
		
		
		System.out.println("================ Linear probing experiments ================\n");

		System.out.println("================ 500000 ================\n");
		System.out.println(oa.linearProbing(500000));
		
		System.out.println("================ 800000 ================\n");
		System.out.println(oa.linearProbing(800000));
		
		System.out.println("================ 1000000 ================\n");
		System.out.println(oa.linearProbing(1000000));
		
		System.out.println("================ 1048575 ================\n");
		System.out.println(oa.linearProbing(1048575));
		
		
		System.out.println("================ Quadratic probing experiments ================\n");

		System.out.println("================ 500000 ================\n");
		System.out.println(oa.quadraticProbing(500000));
		
		System.out.println("================ 800000 ================\n");
		System.out.println(oa.quadraticProbing(800000));
		
		System.out.println("================ 1000000 ================\n");
		System.out.println(oa.quadraticProbing(1000000));
		
		System.out.println("================ 1048575 ================\n");
		System.out.println(oa.quadraticProbing(1048575));
		
		
		System.out.println("================ Double hashing experiments ================\n");

		System.out.println("================ 500000 ================\n");
		System.out.println(oa.doubleHashing(500000));
		
		System.out.println("================ 800000 ================\n");
		System.out.println(oa.doubleHashing(800000));
		
		System.out.println("================ 1000000 ================\n");
		System.out.println(oa.doubleHashing(1000000));
		
		System.out.println("================ 1048575 ================\n");
		System.out.println(oa.doubleHashing(1048575));
	}

}
