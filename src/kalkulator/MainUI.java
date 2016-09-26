package kalkulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;


public class MainUI {

private final DateFormat timeFormat = new SimpleDateFormat("EEEEEEEEEEEE  dd MMM yyyy  HH:mm:ss");
private ActionListener akcja;
private String wynikS = "0", p="", czas;
private String[] historia = {"", "", "", "", "", "", "" ,"", "", "", "", ""};
private int linia = 0;

private String czcionka = "Arial";
private int styl = Font.PLAIN, rozmiar = 14;

private BigDecimal wP = new BigDecimal("0"), wJP = new BigDecimal("0"), wA = new BigDecimal("0");

private boolean przecinek = false, poprzecinku = false, rownanie = false, pierw = false, blad=false, odNowa = true;

private int dzialanie = 0;
private MathContext mc;
private boolean pokazCzas = true;

private WzorRamki ramkaGlowna;
private JMenuBar menuBar1;
private JMenu menuOpcje, menuWidok, menuPomoc;
private JMenuItem menuOpcjeZakoncz, menuPomocWersja, menuWidokTabela;
private JCheckBoxMenuItem menuWidokData;
private Przycisk button;
private JButton bWyczysc, bPokazTabele;
private JPanel panelGlowny, panelWyniku, panelCzasu, panelPrzyciskow, panelHistorii;
private JTextField poleWyniku, poleCzasu;
private JTextArea poleHistorii;
private JScrollPane panelPrzewijania;
private Object kolumny[] = { "L.p.","Pierwsza liczba", "Operator", "Druga liczba", "Wynik"};
private JTable tabela;
private Date data;
private Timer timer;
private WindowListener sluchaczOkna;
private KeyListener sluchaczKlawiszy;
private Font font1 = new Font("Verdana", 1, 14);
private Font font2 = new Font("Verdana", 0, 12);
private String h="";
Font aktualnaCzcionka = new Font(czcionka, styl, rozmiar);

public MainUI()
{
	akcja = new SluchaczAkcji();
	
	ramkaGlowna = new WzorRamki("Kalkulator");
	ramkaGlowna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	ramkaGlowna.setLayout(new BorderLayout());
	
	// Pasek menu
	menuBar1 = new JMenuBar();
	
	// Menu opcje
	menuOpcje = new JMenu("Opcje");
	menuOpcje.setMnemonic('O');
	menuOpcjeZakoncz = new JMenuItem("Zakoñcz", 'z');
	menuOpcjeZakoncz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
	menuOpcjeZakoncz.addActionListener(new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			System.exit(0);
		}
	});
	
	menuOpcje.add(menuOpcjeZakoncz);
	
	// Menu widok
	menuWidok = new JMenu("Widok");
	menuWidok.setMnemonic('W');
	menuWidokTabela = new JMenuItem("Tabela wyników");
	menuWidokTabela.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			WzorRamki ramkaTabeli = new WzorRamki("Tabela wyników");
			ramkaTabeli.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			ramkaTabeli.centrujOkno(500, 500);
			ramkaTabeli.setLayout(new BorderLayout());
			String[][] daneTabeli = new String[30][5];
			tabela = new JTable(daneTabeli, kolumny);
			panelPrzewijania = new JScrollPane(tabela);
			ramkaTabeli.add(panelPrzewijania, BorderLayout.CENTER);
			JPanel panelDolnyTabeli = new JPanel(new GridLayout(1,2, 10,1));
			JButton bWyslij = new JButton("Wyœlij");
			panelDolnyTabeli.add(bWyslij);
			JButton bWyczysc = new JButton("Wyczyœæ");
			panelDolnyTabeli.add(bWyczysc);
			JButton bPodglad = new JButton("Podgl¹d wydruku");
			panelDolnyTabeli.add(bPodglad);
			JButton bWydrukuj = new JButton("Wydrukuj");
			panelDolnyTabeli.add(bWydrukuj);
			ramkaTabeli.add(panelDolnyTabeli, BorderLayout.SOUTH);
			ramkaTabeli.pack();
			ramkaTabeli.setVisible(true);
		}
	});		
	
	menuWidokTabela.setMnemonic('t');
	menuWidokTabela.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
	menuWidok.add(menuWidokTabela);
	menuWidokData = new JCheckBoxMenuItem("Data i czas");
	menuWidokData.setSelected(true);
	menuWidokData.setMnemonic('m');
	menuWidokData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
	menuWidokData.addActionListener(akcja);
	menuWidok.add(menuWidokData);
	
	//Menu pomoc
	menuPomoc = new JMenu("Pomoc");
	menuPomoc.setMnemonic('P');
	menuPomoc.setToolTipText("Pomoc");
	menuPomocWersja = new JMenuItem("Kalkulator - informacje");
	menuPomocWersja.setMnemonic('i');
	menuPomocWersja.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
	menuPomocWersja.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Nie pytaj, która to wersja kalkulatora ...", "Kalkulator - informacje", JOptionPane.PLAIN_MESSAGE);
				}
			});
	menuPomoc.add(menuPomocWersja);
	menuBar1.add(menuOpcje);
	menuBar1.add(menuWidok);
	menuBar1.add(menuPomoc);
	
	// panel g³ówny ramki
	panelGlowny = new JPanel(new BorderLayout());
	
	//panel wyniku
	panelWyniku = new JPanel();
	poleWyniku = new JTextField(wynikS, 20);
	poleWyniku.setHorizontalAlignment(JTextField.RIGHT);
	poleWyniku.setEditable(false);
	sluchaczKlawiszy = new Klawisze();
	poleWyniku.addKeyListener(sluchaczKlawiszy);
	poleWyniku.setFont(font1);
	panelWyniku.add(poleWyniku);
	
	//panel przycisków
	panelPrzyciskow = new JPanel(new GridLayout(5,4, 5, 5));
	//panelPrzyciskow.setFont(aktualnaCzcionka);
	dodajPrzycisk("C", akcja);
	dodajPrzycisk("<=", akcja);
	dodajPrzycisk("\u221A", akcja);
	dodajPrzycisk("+", akcja);
	dodajPrzycisk("7", akcja);
	dodajPrzycisk("8", akcja);
	dodajPrzycisk("9", akcja);
	dodajPrzycisk("-", akcja);
	dodajPrzycisk("4", akcja);
	dodajPrzycisk("5", akcja);
	dodajPrzycisk("6", akcja);
	dodajPrzycisk("\u00D7", akcja);
	dodajPrzycisk("1", akcja);
	dodajPrzycisk("2", akcja);
	dodajPrzycisk("3", akcja);
	dodajPrzycisk("\u00F7", akcja);
	dodajPrzycisk(".", akcja);
	dodajPrzycisk("0", akcja);
	dodajPrzycisk("+/-", akcja);
	dodajPrzycisk("=", akcja);
	
	JPanel panelPrzyciskow1 = new JPanel();	
	panelPrzyciskow1.add(panelPrzyciskow, BorderLayout.CENTER);
	
	// panel wyœwietlania czasu
	panelCzasu = new JPanel();
	poleCzasu = new JTextField("CZAS");
	poleCzasu.setMinimumSize(new Dimension(100, 50));
	poleCzasu.setFont(font2);
	poleCzasu.setEditable(false);
	poleCzasu.setBorder(null);
	poleCzasu.setFocusable(false);
	poleCzasu.setHorizontalAlignment(JTextField.LEFT);
    
    ActionListener timerListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {  	
        	if (pokazCzas) {
        	data = new Date();
            czas = timeFormat.format(data);
            poleCzasu.setText(czas);
        	}
            else poleCzasu.setText("");
         }
    };
    
    timer = new Timer(250, timerListener);
    timer.setInitialDelay(1);
    timer.start();
	panelCzasu.add(poleCzasu);
	
	//panel histoii wyników
	panelHistorii = new JPanel(new BorderLayout());
	
	poleHistorii = new JTextArea(15,20);
	
	poleHistorii.setLineWrap(true);
	poleHistorii.setWrapStyleWord(true);
	poleHistorii.setFont(font2);
	poleHistorii.setEditable(false);
	poleHistorii.setText("");

	
	poleHistorii.setEditable(false);
	bWyczysc = new JButton("Wyczyœæ");
	bPokazTabele = new JButton("Poka¿ tabelê");
	
	JPanel panelPrzyciskowHistorii = new JPanel(new FlowLayout());
	panelHistorii.add(poleHistorii, BorderLayout.CENTER);
	panelPrzyciskowHistorii.add(bPokazTabele);
	panelPrzyciskowHistorii.add(bWyczysc);
	panelHistorii.add(panelPrzyciskowHistorii, BorderLayout.SOUTH);
	
	bWyczysc.addActionListener(new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for (int i=0; i <12; ++i) historia[i]="";
			h="";
			linia=0;
			poleHistorii.setText(h);
		}
	});
	
	//dodawanie pomniejszych paneli do panelu g³ównego
	panelGlowny.add(panelCzasu, BorderLayout.SOUTH);
	panelGlowny.add(panelPrzyciskow, BorderLayout.CENTER);
	panelGlowny.add(panelWyniku, BorderLayout.NORTH);
	panelGlowny.add(panelHistorii, BorderLayout.EAST);
	
	//dodawanie g³ównych elementów do ramki g³ównej
	ramkaGlowna.add(panelGlowny, BorderLayout.CENTER);
	ramkaGlowna.add(menuBar1, BorderLayout.NORTH);
	
	ramkaGlowna.pack();
	ramkaGlowna.centrujOkno(520,360);
	sluchaczOkna = new Terminator();
	ramkaGlowna.addWindowListener(sluchaczOkna);
	
	//ramkaGlowna.setResizable(false);
	
	ramkaGlowna.setVisible(true);
}

// Dzia³ania przycisków
private class SluchaczAkcji implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent event)
	{
      //blad = false;
	  p = event.getActionCommand();
	  poleWyniku.requestFocus();
	  
	  if (p == "Data i czas") pokazCzas = !pokazCzas;
	  else
      {  
    	  policz(p);
      }
	}
}

//Dzia³ania klawiszy
public class Klawisze implements KeyListener
{
@Override
public void keyPressed(KeyEvent e) {
		char k = e.getKeyChar();
		if ((k == '0') || (k == '1') || (k == '2') || (k == '3') || (k == '4') || (k == '5') || (k == '6') || (k == '7') || (k == '8')
			|| (k == '9') || (k == '+') || (k == '-') || (k == '=') )
	    {
			policz(String.valueOf(k));
	    }
		if (k == '*') policz("\u00D7");
		if (k == '/') policz("\u00F7");
		if (e.getKeyCode() == KeyEvent.VK_ENTER) policz("=");
		if ((k == ',') || (k == '.')) policz(".");
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) policz("<=");
		if (e.getKeyCode() == KeyEvent.VK_DELETE) policz("C");
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
}

@Override
public void keyReleased(KeyEvent e) {}

@Override
public void keyTyped(KeyEvent e) {}
}

public class Terminator implements WindowListener
{
	public void windowClosing(WindowEvent e)
	{
		/**	Object[] opcje = {"TAK", "NIE"};
		int wyborKoniec = JOptionPane.showOptionDialog(null, "Wyjœæ z programu?", "Kalkulator", 0, JOptionPane.QUESTION_MESSAGE, null, opcje, opcje[0]);
		if (wyborKoniec == 0) System.exit(0);
	**/
	}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e){}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}

private static final BigDecimal TWO = BigDecimal.valueOf(2L);

//METODA NA OBLICZANIE SQRT W BIGDECIMAL
public static BigDecimal sqrt(BigDecimal x, MathContext mc) {
	BigDecimal g = x.divide(TWO, mc);
	boolean done = false;
	final int maxIterations = mc.getPrecision() + 1;		
	for (int i = 0; !done && i < maxIterations; i++) {
		// r = (x/g + g) / 2
		BigDecimal r = x.divide(g, mc);
		r = r.add(g);
		r = r.divide(TWO, mc);
		done = r.equals(g);
		g = r;
	}
	return g;
}

public void dodajPrzycisk(String t, ActionListener listener)
{
	button = new Przycisk(t, listener);
    if (t == "<=") button.setToolTipText("Wstecz");
    else if (t == "\u221A") button.setToolTipText("Pierwiastek");
    else if (t == "C") button.setToolTipText("Wyczyœæ");
    else if (t == "+") button.setToolTipText("Dodawanie");
    else if (t == "-") button.setToolTipText("Odejmowanie");
    else if (t == "=") button.setToolTipText("Równanie");
    else if (t =="\u00D7") button.setToolTipText("Mno¿enie");
    else if (t =="\u00F7") button.setToolTipText("Dzielenie");
    else if (t == "+/-") button.setToolTipText("Plus / Minus");
    else button.setToolTipText(t);
    panelPrzyciskow.add(button);
}

public void policz(String x)
{	
	blad = false;
	if (x.equals("C"))
	{
		wynikS = "0";
		wA = new BigDecimal("0");
		wP = new BigDecimal("0");
		wJP = new BigDecimal("0");
		dzialanie = 0;
		przecinek = false;
		poprzecinku = false;
		odNowa = true;
	}
	
	if (x.equals("<="))
	{
		if ((wA.compareTo(BigDecimal.valueOf(0)) != 0) && (wynikS.length() > 0))
		{
			int cut = wynikS.length();
			if ((wynikS.charAt(0) == '-') && (wynikS.length()== 2)) wynikS = "";
			if (wynikS.length() > 1) wynikS = wynikS.substring(0, cut-1);
			else wynikS = "0";
			wA = new BigDecimal(wynikS);
		}
	}
	
	if (x.equals("+/-"))
	{
		wA = wA.multiply(new BigDecimal("-1"));
		wP = wA;
		wynikS = String.valueOf(wA);
		przecinek = false;
	}
	
	if (x.equals(".") && (!poprzecinku)) {
		przecinek = true;
		historia[linia]=historia[linia]+".";
	}
	
	if (x.equals("0") || x.equals("1") || x.equals("2") || x.equals("3") || x.equals("4") || x.equals("5") || x.equals("6") || x.equals("7") || x.equals("8") || x.equals("9"))
	{		
		
		if ((przecinek) && (wynikS.length() == 0))
		{
			wynikS = "0";
		}
		if ((przecinek) && (wynikS.length() > 0))
		{
			wynikS = wynikS + ".";
			poprzecinku = true;
			przecinek = false;
		}
		
		if ((odNowa) || (pierw))
		{
			wynikS = "";
			odNowa = false;
			pierw = false;
			//dzialanie = 0;
		}
		
		if (!odNowa)
		{
			wynikS = wynikS + x;
			odNowa = false;
		}
		else {
			if (!poprzecinku) {
				odNowa = true;
				wynikS = x;
			}
			else {
				odNowa = false;
				wynikS=wynikS+x;
			}
		}
		wA = new BigDecimal(wynikS);
		wP = wA;
	}
	
	if (x.equals("\u221A"))
	{
		mc = new MathContext(20);
		wP = sqrt(wA, mc).stripTrailingZeros();
		wynikS = String.valueOf(wP.toPlainString());
		pierw = true;
		przecinek = false;
		poprzecinku=false;
		dzialanie = 0;
		historia[linia]="\u221A" +wA.toPlainString() +" = " +wP.toPlainString() +"\n";
		h="";
		for (int i=0; i <12; ++i)
	  	{
	  		h=h+historia[i];
	  	}
		
		if (linia <11) linia = linia + 1;
		else {
			// sortowanie	
			historia[0] = historia[1];
			historia[1] = historia[2];
			historia[2] = historia[3];
			historia[3] = historia[4];
			historia[4] = historia[5];
			historia[5] = historia[6];
			historia[6] = historia[7];
			historia[7] = historia[8];
			historia[8] = historia[9];
			historia[9] = historia[10];
			historia[10] = historia[11];	
		}
		poleHistorii.setText(h);
	}
	
	if ((x.equals("+")) || (x.equals("-")) || (x.equals("\u00D7")) || (x.equals("\u00F7")))
	{
		if ((dzialanie > 0) && (!rownanie))
		{
			switch(dzialanie)
			{
			case 1: {
				wA = wP.add(wJP).stripTrailingZeros();
				wynikS = String.valueOf(wA.toPlainString());
				historia[linia] = historia[linia] + " + " +wP;
				wJP = wA;
				break;
			}
			case 2: {
				wA = wJP.subtract(wP).stripTrailingZeros();
				wynikS = String.valueOf(wA.toPlainString());
				historia[linia] = historia[linia] + " - " +wP;
				wJP = wA;
				break;
			}
			case 3: {
				wA = wJP.multiply(wP).stripTrailingZeros();
				wynikS = String.valueOf(wA.toPlainString());
				historia[linia] = historia[linia] + " \u00D7 " +wP;
				wJP = wA;
				break;
			}
			case 4: {
				if (wA.compareTo(BigDecimal.valueOf(0)) != 0)
				{
					wA = wJP.divide(wP, 20, RoundingMode.HALF_UP).stripTrailingZeros();
					wynikS = String.valueOf(wA.toPlainString());
					historia[linia] = historia[linia] + " \u00F7 " +wP;
					wJP = wA;
				}
				else blad = true;
				break;
			}
			}
		}
		
		if (!rownanie) {
		wP = wA;
		wJP = wP;
		wA = new BigDecimal("0");
		}
		else {
			wP = wJP;
			wJP = wA;
			wA = new BigDecimal("0");
		}
		if (x.equals("+")) dzialanie = 1;
		else if (x.equals("-")) dzialanie = 2;
		else if (x.equals("\u00D7")) dzialanie = 3;
		else if (x.equals("\u00F7")) dzialanie = 4;
		odNowa = true;
		poprzecinku = false;
		przecinek = false;
		rownanie = false;
	}
	
	if ((x.equals("=")) && (dzialanie > 0))
	{
		odNowa = true;
		historia[linia] = wJP.toPlainString();
		switch (dzialanie) {
		case 1: {
			wA = wP.add(wJP).stripTrailingZeros();
			wynikS = String.valueOf(wA.toPlainString());
			historia[linia] = historia[linia] + " + " +wP;
			wJP = wA;
			break;
		}
		case 2: {
			wA = wJP.subtract(wP).stripTrailingZeros();
			wynikS = String.valueOf(wA.toPlainString());
			historia[linia] = historia[linia] + " - " +wP;
			wJP = wA;
			break;
		}
		case 3: {
			wA = wJP.multiply(wP).stripTrailingZeros();
			wynikS = String.valueOf(wA.toPlainString());
			historia[linia] = historia[linia] + " \u00D7 " +wP;
			wJP = wA;
			break;
		}
		case 4: {
			if (wA.compareTo(BigDecimal.valueOf(0)) != 0)
			{
				wA = wJP.divide(wP, 20, RoundingMode.HALF_UP).stripTrailingZeros();
				wynikS = String.valueOf(wA.toPlainString());
				historia[linia] = historia[linia] + " \u00F7 " +wP;
				wJP = wA;
			}
			else blad = true;
			break;
		}
		}
		historia[linia]=historia[linia]+" = " +wA.toPlainString() + "\n";

		h="";
		for (int i=0; i <12; ++i)
	  	{
	  		h=h+historia[i];
	  	}
		
		if (linia <11) linia = linia + 1;
		else {
			// sortowanie	
			historia[0] = historia[1];
			historia[1] = historia[2];
			historia[2] = historia[3];
			historia[3] = historia[4];
			historia[4] = historia[5];
			historia[5] = historia[6];
			historia[6] = historia[7];
			historia[7] = historia[8];
			historia[8] = historia[9];
			historia[9] = historia[10];
			historia[10] = historia[11];	
		}
		rownanie=true;
		przecinek = false;
		poprzecinku = false;
	}
	
	if (!blad) poleWyniku.setText(wynikS);
    else poleWyniku.setText("B³êdne dzia³anie !!!");

	if (rownanie) poleHistorii.setText(h);
}


class panTab1 extends JPanel {
private static final long serialVersionUID = 1L;

public panTab1()
{

}
}

}