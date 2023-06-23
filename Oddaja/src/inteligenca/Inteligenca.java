package inteligenca;

import java.util.Random;

import logika.Igra;
import logika.Igralec;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends KdoIgra { 
	public Random random;
	public Alphabeta ab3;
	public Alphabeta ab2;
	public Alphabeta ab1;
	
	public Inteligenca() {
		super("Tadej Jeršin & Lana Prijon");
		random = new Random();
		ab3 = new Alphabeta(3);
		ab2 = new Alphabeta(2);
		ab1 = new Alphabeta(1);
	}
	
	public Poteza izberiPotezo(Igra igra) {
		int tocke = igra.prestejTocke();
		// zmaga če lahko
		if (igra.preskoki == 1) {
			if (tocke > 0 && igra.na_potezi == Igralec.CRNI) return new Poteza(-1, -1);
			else if (tocke <= 0 && igra.na_potezi == Igralec.BELI) return new Poteza(-1, -1);
		}
		switch (igra.n) {
		case 9:
			if (igra.poteze == 0) return nakljucnaPoteza(igra);
			return ab3.izberiPotezo(igra);
		case 13:
			if (igra.poteze == 0) return nakljucnaPoteza(igra);
			return ab2.izberiPotezo(igra);
		case 19: 
			if (igra.poteze == 0) return nakljucnaPoteza(igra);
			return ab1.izberiPotezo(igra);
		}
		return null;
	}
	
	public Poteza nakljucnaPoteza(Igra igra) {
		int randomIndex = random.nextInt(igra.poteze().size());
		return igra.poteze().get(randomIndex);
	}
} 
