package mapbuilder;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;


import gui.SpriteImp;

/**
 * The Main class is the wrapper for the whole application. It takes in the input file, instantiates
 * the App, and hands it the input file
 * @author mmkaplan
 *
 */
public class Main {
	public static void main(String[] argv) {
		XmlParser x = new XmlParser();
//		x.parseBox("/gpfs/main/home/mmkaplan/course/cs032/ztd/69bs.xml");
		Retriever r = new Retriever();
//		r.getBox(-71.40794, 41.82544, -71.40086, 41.82944);
		File sta = r.getFromAddress("228 East Meade Street, Philadelphia PA");
		x.parseAddress(sta);
//		JFrame j = new GuiFrame();
		System.out.println("Done");
		return;
	}
	
}




class GuiFrame extends JFrame {
	public GuiFrame() {
		super("Test");

		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		SpriteImp s = new SpriteImp("/gpfs/main/home/mmkaplan/course/cs032/ztd/castle.jpg");
		this.add(s);
		setVisible(true);
	}
	
}