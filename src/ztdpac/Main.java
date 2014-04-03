package ztdpac;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;

import mapbuilder.Const;
import mapbuilder.DistConverter;
import mapbuilder.MapNode;
import mapbuilder.Retriever;
import mapbuilder.XmlParser;

import cs195n.Vec2f;
import gui.MaxGuiFrame;
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
//		File box = Retriever.getBox(-71.40794, 41.82544, -71.40086, 41.82944);

		File sta = Retriever.getFromAddress("69 Brown Street, Providence, RI");

		MapNode cent = x.parseAddress(sta);
		Const c = new Const(cent.lat, cent.lon);
		DistConverter dc = new DistConverter(c);
		System.out.println(dc.getLeft(cent.lon));
		System.out.println(dc.getBott(cent.lat));
		System.out.println(dc.getRight(cent.lon));
		System.out.println(dc.getTop(cent.lat));
		
		File box = Retriever.getBox(dc.getLeft(cent.lon), dc.getBott(cent.lat), dc.getRight(cent.lon), dc.getTop(cent.lat));
		x.parseBox(box);
		List<MapNode> nodes = x.getNodes();
		for (MapNode mn: nodes) {
			System.out.println(dc.convertLLToAB(mn));
		}
		MaxGuiFrame j = new MaxGuiFrame();
		j.drawWays(x.parseBox(box));
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
		SpriteImp s = new SpriteImp("castle.jpg");
		this.add(s);
		setVisible(true);
	}
	
}