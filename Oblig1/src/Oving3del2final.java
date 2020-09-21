import java.util.Scanner;

class Oving3del2final {
	public static void main(String[] args) {
		while (true) {
			Scanner in = new Scanner(System.in);
			System.out.println("Tast inn et tall: ");
			int a = in.nextInt();
			int b = 1;
			int c = a;
			int d = 0;
			
			if (b < a) {
				do {
					if (c % a == 0) {
						d++;
					}
					a--;
				}
				while (b <= a);
			}
			if (d == 2) {
				System.out.println("Primtall\n");
			} else {
				System.out.println("Ikke et primtal\n");
			}
		}
	}
}