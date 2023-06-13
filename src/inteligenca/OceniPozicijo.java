package inteligenca;

import java.util.HashSet;
import java.util.Set;

import logika.Igra;
import logika.Igralec;
import logika.Koordinati;
import logika.Polje;
import logika.SkupinaZetonov;
import logika.Zeton;

public class OceniPozicijo {
	
	public static int oceniPozicijo(Igra igra) {
		int n = 0;
		int tocke = igra.prestejTocke();
		int crniZetoni = 0; // število vseh črnih žetonov na plošči
		int beliZetoni = 0; // število vseh belih žetonov na plošči
		if (igra.preskoki == 1) {
			if (tocke > 0 && igra.na_potezi == Igralec.CRNI) return Integer.MAX_VALUE;
			else if (tocke <= 0 && igra.na_potezi == Igralec.BELI) return Integer.MIN_VALUE;
		}
		for (SkupinaZetonov skupinaZet : igra.skupine_zetonov) {
			Polje barva = skupinaZet.barva;
			//int svobode = tockeSkupine(skupinaZet.skupina, barva, igra)[0];
			//nt velikost = tockeSkupine(skupinaZet.skupina, barva, igra)[1];
			int m = tockeSkupine (skupinaZet.skupina, barva, igra);
			if (barva == Polje.CRNO) {
				crniZetoni += skupinaZet.skupina.size();
				n += m;
			}
			else {
				beliZetoni += skupinaZet.skupina.size();
				n -= m;
			}
		}
		
		return  n + 
				(igra.n) * (beliZetoni + crniZetoni) * tocke + 
				(igra.n * 2) * (crniZetoni - beliZetoni); // neke številke, mogoče drugače boljše delal
	}
	
	public static int tockeSkupine(Set<Zeton> skupina, Polje barva, Igra igra) {
		Set<Zeton> svobode = new HashSet<Zeton>();
		for (Zeton zeton : skupina) {
			for (Koordinati koordSosed : zeton.sosedi) {
				Zeton sosed = igra.mreza.get(koordSosed);
				if (sosed.polje == Polje.PRAZNO) {
					svobode.add(sosed);
				}
			}
		}
		//return new int[] {svobode.size(),skupina.size()};
		if (svobode.size() < 2) {
			return 0;
		}
		return (svobode.size()-1)*skupina.size();
		//return Math.min((svobode.size()-1), 7)*(Math.min(skupina.size(), 5));
	}
	
}
