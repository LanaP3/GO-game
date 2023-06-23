package inteligenca;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import logika.Igra;
import logika.Igralec;
import logika.Koordinati;
import logika.Polje;
import logika.SkupinaZetonov;
import logika.Zeton;
import splosno.Poteza;

public class Alphabeta {
	
	private int globina;
	private Random random;
	
	public Alphabeta(int globina) {
		this.globina = globina;
		random = new Random();
	}
	
	// crni igralec maksimizira, beli minimalizira
	public Poteza izberiPotezo(Igra igra) {
		if (random.nextDouble() < 0.02) return new Poteza(-1, -1); // če ni tega nemara preskočit poteze
		return alphabeta(igra, this.globina, Integer.MIN_VALUE, Integer.MAX_VALUE, igra.naPotezi() == Igralec.CRNI).poteza;
	}
	
	public OcenjenaPoteza alphabeta(Igra igra, int globina, int alpha, int beta, boolean max) {
		List<Poteza> poteze = igra.poteze();
		//poteze.remove(new Poteza(-1, -1));
		//Collections.sort(poteze, new NewSort(igra));
		//poteze.add(new Poteza(-1, -1));
		switch (igra.stanje()) {
		case ZMAGA_BELI:
			return new OcenjenaPoteza(null, Integer.MIN_VALUE);
		case ZMAGA_CRNI:
			return new OcenjenaPoteza(null, Integer.MAX_VALUE);
		default:
			if (globina == 0) return new OcenjenaPoteza(null, OceniPozicijo.oceniPozicijo(igra));
			if (max) {
				int maxEval = Integer.MIN_VALUE;
				Poteza o = poteze.get(0);
				for (Poteza p : poteze) {
					Igra kopijaIgre = new Igra(igra);
					kopijaIgre.odigraj(p);
					int eval = alphabeta(kopijaIgre, globina - 1, alpha, beta, false).ocena;
					if (eval > maxEval) {
						o = p;
						maxEval = eval;
					}
					alpha = Integer.max(alpha, eval);
					if (beta <= alpha) break;
				}
				return new OcenjenaPoteza(o, maxEval);
			} else {
				int minEval = Integer.MAX_VALUE;
				Poteza o = poteze.get(0);
				for (Poteza p : poteze) {
					Igra kopijaIgre = new Igra(igra);
					kopijaIgre.odigraj(p);
					int eval = alphabeta(kopijaIgre, globina - 1, alpha, beta, true).ocena;
					if (eval < minEval) {
						o = p;
						minEval = eval;
					}
					beta = Integer.min(beta, eval);
					if (beta <= alpha) break;
				}
				return new OcenjenaPoteza(o, minEval);
			}
		}
	}
}


