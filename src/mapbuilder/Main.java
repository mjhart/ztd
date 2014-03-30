package mapbuilder;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import cs195n.Vec2f;
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
		x.parseBox("69bs.xml");
		//System.out.println(PathFinder.findSrcs(x.getNodes(), new Vec2f(-71.4003000f, 41.8292000f), new Vec2f(-71.4052900f, 41.8256900f)));
		//Retriever r = new Retriever();
		//r.getBox(-71.40794, 41.82544, -71.40086, 41.82944);
		//r.getFromAddress("228 East Meade Street, Philadelphia PA");
		//x.parseAddress("address2.xml");
		JFrame j = new GuiFrame();
	}
	
}




class GuiFrame extends JFrame {
	public GuiFrame() {
		super("Test");

		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		SpriteImp s = new SpriteImp("castle.jpg");
		this.add(s);
		setVisible(true);
	}
	
}