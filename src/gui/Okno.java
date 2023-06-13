package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igralec;
import splosno.Poteza;
import vodja.Vodja;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	public Platno platno;
	protected int dimenzija = 9;
	private JMenuItem menuIgralecIgralec;
	private JMenuItem menuRacIgralec;
	private JMenuItem menuIgralecRac;
	private JMenuItem menuRacRac;
	private JMenuItem menu9;
	private JMenuItem menu19;
	private JMenuItem menu13;
	private JMenuItem preskociPotezo;
	private JLabel status;
	
	public Okno() {
		super();
		setTitle("GO");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//this.setLayout(new BorderLayout());
		this.setLayout(new GridBagLayout());
		
		platno = new Platno(800, 800);
		GridBagConstraints platno_layout = new GridBagConstraints();
		platno_layout.gridx = 0;
		platno_layout.gridy = 0;
		platno_layout.fill = GridBagConstraints.BOTH;
		platno_layout.weightx = 1.0;
		platno_layout.weighty = 1.0;
		getContentPane().add(platno, platno_layout);
		// this.add(platno, BorderLayout.NORTH);
		
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
			    status.getFont().getStyle(),
			    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("Izberite igro!");
		
	
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuNovaIgra = dodajMenu(menubar, "Nova igra...");
		JMenu izberiVelikost = dodajMenu(menubar, "Velikost plošče...");
		JMenu trenutnaIgra = dodajMenu(menubar, "Trenutna igra...");
		
		menuIgralecIgralec = dodajMenuItem(menuNovaIgra, "Igralec : igralec");
		menuRacIgralec = dodajMenuItem(menuNovaIgra, "Računalnik : igralec");
		menuIgralecRac = dodajMenuItem(menuNovaIgra, "Igralec : računalnik");
		menuRacRac = dodajMenuItem(menuNovaIgra, "Računalnik : računalnik");
		
		menu19 = dodajMenuItem(izberiVelikost, "19x19");
		menu13 = dodajMenuItem(izberiVelikost, "13x13");
		menu9 = dodajMenuItem(izberiVelikost, "9x9");
		
		preskociPotezo = dodajMenuItem(trenutnaIgra, "Preskoči potezo");
		//this.pack();
	}
	
	private JMenu dodajMenu(JMenuBar menubar, String naslov) {
		JMenu menu = new JMenu(naslov);
		menubar.add(menu);
		return menu;
	}
	
	private JMenuItem dodajMenuItem(JMenu menu, String naslov) {
		JMenuItem menuitem = new JMenuItem(naslov);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		return menuitem;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object objekt = e.getSource();
		if (objekt == menuIgralecIgralec) {
			Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.C);
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.igrajNovoIgro(dimenzija);
			}
		else if (objekt == menuRacIgralec) {
			Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.R);
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.igrajNovoIgro(dimenzija);
			}
		else if (objekt == menuIgralecRac) {
			Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.C);
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.igrajNovoIgro(dimenzija);
			}
		else if (objekt == menuRacRac) {
			Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.R);
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.igrajNovoIgro(dimenzija);
			}
		else if (objekt == menu9) {
			platno.spremeniDimenzijo(9);
			dimenzija = 9;
			Vodja.igra = null;
			osveziGUI();
			}
		else if (objekt == menu13) {
			platno.spremeniDimenzijo(13);
			dimenzija = 13;
			Vodja.igra = null;
			osveziGUI();
			}
		else if (objekt == menu19) {
			platno.spremeniDimenzijo(19);
			dimenzija = 19;
			Vodja.igra = null;
			osveziGUI();
			}
		else if (objekt == preskociPotezo) {
			if (Vodja.igra != null) {
				Vodja.igrajClovekovoPotezo(new Poteza(-1, -1));
			}
		}
		
	}
	
	public void osveziGUI() {
		if (Vodja.igra == null) status.setText("Izberi igro");
		else {
			switch(Vodja.igra.stanje()) {
			case ZMAGA_BELI:
				status.setText("Zmagal je beli");
				break;
			case ZMAGA_CRNI:
				status.setText("Zmagal je črni");
				break;
			case V_TEKU:
				status.setText("Na potezi je " + Vodja.igra.naPotezi() + " - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi()) + 
						" ... Preskoki: " + Vodja.igra.preskoki);
			}
		}
		platno.repaint();
	}


}

