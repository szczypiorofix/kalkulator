package kalkulator;

import java.awt.EventQueue;

public class Kalkulator {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				new MainUI();
			}
		});
	}
}
