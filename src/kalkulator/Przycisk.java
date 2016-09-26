package kalkulator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Przycisk extends JButton {

private static final long serialVersionUID = 1L;
private final Font fontP = new Font("Verdana", 1, 16);
private final int sx = 50, sy = 50;
		
public Przycisk ()
{
	setText("X");
}
		
public Przycisk (String s, ActionListener listener)
{	
	setText(s);
	//setFocusable(false);
	//setContentAreaFilled(false);
	//setFocusPainted(false);
	//setBorderPainted(false);
	addActionListener(listener);
	setFont(fontP);
	setPreferredSize(new Dimension(sx,sy));
}
}
