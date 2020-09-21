/*
ta inn et tall fra bruker
sjekke om det er et primtall
vi sjekker ved å se antall ganger den kan deles på tallene under seg
hvis antall ganger er 2, er er tallet et primtall
 */

import static javax.swing.JOptionPane.*;



public class Primtall {
	public static void main(String[] args) {
		
		while (true) { //for å få den til å gå flere ganger
			int tallFraBruker;
			String tallLest = showInputDialog(null, "Skriv inn ønsket tall som skal analyseres");
			tallFraBruker = Integer.parseInt(tallLest);
			int antallGanger = 0;
			int kopi = tallFraBruker;
			
			do {
				if( tallFraBruker % kopi == 0) {
					antallGanger++;
				}
				kopi--;
			} while (kopi >= 1);
			
			if(antallGanger == 2) {
				showMessageDialog(null, Integer.toString(tallFraBruker) + " er et primtall!");
			} else {
				showMessageDialog(null, Integer.toString(tallFraBruker) + " er ikke et primtall!");
			}
		}
	}
}