package kalkulator;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class WzorRamki extends JFrame {

private static final long serialVersionUID = 1L;

public WzorRamki()
{
	setTitle("Ramka domyœlna");
}

public WzorRamki(String s)
{
	setTitle(s);
}

public void centrujOkno()
{
	Dimension polozenie = Toolkit.getDefaultToolkit().getScreenSize();
	int w = getSize().width;
	int h = getSize().width;
	int x = (polozenie.width - w) /2;
	int y = (polozenie.height - h) /2;
	setLocation(x,y);
}

public void centrujOkno(int wymX, int wymY)
{
	Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
    setBounds(center.x - wymX / 2, center.y - wymY / 2, wymX, wymY);
}
}