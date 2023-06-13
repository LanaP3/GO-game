package logika;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import splosno.Poteza;

public class Igra {
	public Map<Koordinati, Zeton> mreza;
	public Igralec na_potezi;
	public Set<SkupinaZetonov> skupine_zetonov;
	public int n; // dimenzija mreze
	public int preskoki; // število zaporednih preskokov poteze (če >= 2 se igra konča)
	public int poteze; // število vseh potez v igri
	
	public Igra(int n) {
		this.n = n;
		mreza = new HashMap<Koordinati, Zeton>();
		na_potezi = Igralec.CRNI;
		skupine_zetonov = new HashSet<SkupinaZetonov>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				mreza.put(new Koordinati(i, j), new Zeton(i, j, n));
			}
		}
	}
	
	// naredi kopijo igre
	public Igra(Igra igra) {
		this.mreza = new HashMap<Koordinati, Zeton>();
		this.skupine_zetonov = new HashSet<SkupinaZetonov>();
		this.na_potezi = igra.na_potezi;
		this.n = igra.n;
		this.preskoki = igra.preskoki;
		for (int i = 0; i < igra.n; i++) {
			for (int j = 0; j < igra.n; j++) {
				this.mreza.put(new Koordinati(i, j), new Zeton(igra.mreza.get(new Koordinati(i, j))));
			}
		}
		for (SkupinaZetonov s : igra.skupine_zetonov) {
			this.skupine_zetonov.add(new SkupinaZetonov(s));
		}
	}
	
	
	public Igralec naPotezi() {
		return na_potezi;
	}
	
	public Stanje stanje() {
		int tocke;
		if (preskoki >= 2) {
			tocke = prestejTocke();
			if (tocke > 0) return Stanje.ZMAGA_CRNI;
			else return Stanje.ZMAGA_BELI; // če je 'neodločeno' zmaga beli
		}
		return Stanje.V_TEKU;
	}
	
	public int prestejTocke() {
		int tocke = 0;
		Set<Koordinati> pregledani = new HashSet<Koordinati>();
		Map<Integer, Set<Koordinati>> praznaSkupina;
		for (Zeton z : mreza.values()) {
			if (!pregledani.contains(z.koordinati)) {
				switch (z.polje()) {
				case BELO:
					tocke--;
					pregledani.add(z.koordinati);
					break;
				case CRNO:
					tocke++;
					pregledani.add(z.koordinati);
					break;
				case PRAZNO:
					praznaSkupina = praznaSkupina(z.koordinati);
					if (praznaSkupina != null) {
						if (praznaSkupina.containsKey(1)) {
							tocke += praznaSkupina.get(1).size();
							pregledani.addAll(praznaSkupina.get(1));
						} else if (praznaSkupina.containsKey(-1)){
							tocke -= praznaSkupina.get(-1).size();
							pregledani.addAll(praznaSkupina.get(-1));
						} else {
							pregledani.addAll(praznaSkupina.get(0));
						}
					}
					break;
				}
			}
		}
		return tocke;
	}
	
	private Map<Integer, Set<Koordinati>> praznaSkupina(Koordinati k) {
		Set<Koordinati> prazni = new HashSet<Koordinati>();
		prazni.add(k);
		Set<Koordinati> prazni2 = new HashSet<Koordinati>();
		prazni2.add(k);
		Polje barva = Polje.PRAZNO;
		boolean f = false;
		int l = 0;
		do {
			l = prazni.size();
			for (Koordinati i : prazni) {
				for (Koordinati j : mreza.get(i).sosedi) {
					switch (mreza.get(j).polje()) {
					case BELO:
						if (barva == Polje.CRNO) f = true;
						else if (barva == Polje.PRAZNO) barva = Polje.BELO;
						break;
					case CRNO:
						if (barva == Polje.BELO) f = true;
						else if (barva == Polje.PRAZNO) barva = Polje.CRNO;
						break;
					case PRAZNO:
						prazni2.add(j);
						break;
					}
				}
			}
			prazni.addAll(prazni2);	
		} while (prazni.size() > l);
		
		HashMap<Integer, Set<Koordinati>> map = new HashMap<Integer, Set<Koordinati>>();
		if (f) map.put(0, prazni);
		else if (barva == Polje.CRNO) map.put(1, prazni);
		else map.put(-1, prazni);
		return map;
	}
	
	public void odstraniUjete() { 
		SkupinaZetonov obkoljena = null;
		Set<SkupinaZetonov> obkoljene_druga_barva = new HashSet<SkupinaZetonov>();
		for (SkupinaZetonov sk : skupine_zetonov) {
			if (jeObkoljena(sk)) {
				switch (sk.barva) {
				case BELO:
					if (na_potezi.polje() == Polje.BELO) obkoljene_druga_barva.add(sk);
					else obkoljena = sk;
					break;
				case CRNO:
					if (na_potezi.polje() == Polje.CRNO) obkoljene_druga_barva.add(sk);
					else obkoljena = sk;
					break;
				case PRAZNO:
					assert false;
				}
			}
		}
		if (obkoljene_druga_barva.size() > 0) {
			for (SkupinaZetonov s : obkoljene_druga_barva) {
				s.odstraniSkupino();
				skupine_zetonov.remove(s);
			}
		} else if (obkoljena != null) {
			obkoljena.odstraniSkupino();
			skupine_zetonov.remove(obkoljena);
		}
	}
	
	private boolean jeObkoljena(SkupinaZetonov s) {
		for (Zeton z : s.skupina) {
			for (Koordinati k : z.sosedi) {
				if (mreza.get(k).polje == Polje.PRAZNO) return false;
			}
		}
		return true;
	}
	
	
	public boolean odigraj(Poteza poteza) {
		int x = poteza.x();
		int y = poteza.y();
		Koordinati k = new Koordinati(x, y);
		if (x == -1 && y == -1) {
			preskoki++;
			poteze++;
			na_potezi = na_potezi.nasprotnik();
			return true;
		}
		Zeton zeton = mreza.get(k);
		if (zeton.polje == Polje.PRAZNO) {
			preskoki = 0;
			poteze++;
			zeton.spremeniBarvo(na_potezi.polje());
			SkupinaZetonov s = new SkupinaZetonov(zeton);
			for (Koordinati l : zeton.sosedi) {
				Zeton nov_zeton = mreza.get(l);
				if (nov_zeton.polje == na_potezi.polje()) {
					Iterator<SkupinaZetonov> iter = skupine_zetonov.iterator();
					while (iter.hasNext()) {
						SkupinaZetonov sk = iter.next();
						if (sk.isIn(nov_zeton)) {
							for (Zeton z : sk.skupina) {
								s.skupina.add(z);
							}
							iter.remove();
						}
					}
				}
			}
			skupine_zetonov.add(s);
			
			na_potezi = na_potezi.nasprotnik();
			odstraniUjete();
			return true;
		}
		return false;
	}

	public LinkedList<Poteza> poteze() {
		LinkedList<Poteza> moznePoteze = new LinkedList<Poteza>();
		if (stanje() == Stanje.V_TEKU) {
			for (Entry<Koordinati, Zeton> entry: this.mreza.entrySet()) {
				Zeton o = entry.getValue();
				if (o.polje == Polje.PRAZNO) {
					int x = o.koordinati.getX();
					int y = o.koordinati.getY();
					moznePoteze.add(new Poteza(x,y));
				}
			}
		}
		moznePoteze.add(new Poteza(-1, -1)); // preskok
		return moznePoteze; 
	}
}
