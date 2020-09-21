import java.util.Date;

public class Task3 {
	
	public static double xPowerOfN(double x, int n) {
		return Math.pow(x, n);
	}
	
	public static void main(String[] args) {
		double x = 1.001;
		int n = 10500;
		Date start = new Date();
		int rounds = 0;
		double time;
		Date end;
		do {
			xPowerOfN(x, n);
			end = new Date();
			rounds++;
		} while (end.getTime() - start.getTime() < 1000);
		time = (double) (end.getTime() - start.getTime()) / rounds;
		System.out.println("Time per round " + time);
		System.out.println(xPowerOfN(x,n));
		System.out.println("Testdata, x = 2, n = 10, Ans should be 1024. Ans is " + xPowerOfN(2, 10));
		
	}
}
