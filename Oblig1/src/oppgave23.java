/**
 * oppgave23.java
 * <p>
 * Programmet finner ut om et tilfeldig tall er primtall eller ikke
 */

import static javax.swing.JOptionPane.*;

public class oppgave23 {
	
	public static void main(String[] args) {
		
		while (true) {
			int tallFraBruker;
			String tallLest = showInputDialog(null, "Skriv inn ønsket tall som skal analyseres");
			tallFraBruker = Integer.parseInt(tallLest);
			int antallDelig = 0; //Dette tallet vil vise antall ganger man kan dele på et tall og få et heltall tilbake
			int nedteller = tallFraBruker; //Dette tallet vil trekkes fra 1 hver runde helt til taller er 1
			
			do {
				if (tallFraBruker % nedteller == 0) { //nedteller er fortsatt 7
					antallDelig++;
				}
				nedteller--; //nedteller er nå 6
				
			} while (nedteller >= 1); //er nedteller større eller lik 1?
			
			if (antallDelig == 2) {
				
				showMessageDialog(null, Integer.toString(tallFraBruker) + " er et primtall!");
			} else {
				showMessageDialog(null, Integer.toString(tallFraBruker) + " er ikke et primtall!");
			}
			
		}
	}
}