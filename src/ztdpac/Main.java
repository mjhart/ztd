package ztdpac;

import gui.SpriteImp;

import java.io.File;

import javax.swing.JFrame;

import mapbuilder.DistConverter;
import mapbuilder.MapNode;
import mapbuilder.Retriever;
import mapbuilder.XmlParser;


public class Main {
	public static void main(String[] argv) {
		new GuiFrame();
	}
	
	
}




class GuiFrame extends JFrame {
	public GuiFrame() {
		super("Test");

		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		ImageRet ir = new ImageRet();
		ir.getImage(-71.40404, 41.82657, -71.40154, 41.82832);
		SpriteImp s = new SpriteImp("map.png");
		this.add(s);
		setVisible(true);
	}
}




















//package ztdpac;
//
//import java.awt.BorderLayout;
//import java.io.File;
//import java.util.List;
//
//import javax.swing.JFrame;
//
//import mapbuilder.Const;
//import mapbuilder.DistConverter;
//import mapbuilder.MapNode;
//import mapbuilder.Retriever;
//import mapbuilder.XmlParser;
//
//import cs195n.Vec2f;
//import gui.MaxGuiFrame;
//import gui.SpriteImp;
//
///**
// * The Main class is the wrapper for the whole application. It takes in the input file, instantiates
// * the App, and hands it the input file
// * @author mmkaplan
// *
// */
//public class Main {
//	public static void main(String[] argv) {
////		File sta = Retriever.getFromAddress("228 East Meade Street, Philadelphia, PA");
////		File sta = Retriever.getFromAddress("69 Brown Street, Providence, RI");
//		File sta = Retriever.getFromAddress("Av. Andres Bello 2800, Las Condes, Santiago, Chile");
//			
//
//		XmlParser x = new XmlParser();
//		MapNode cent = x.parseAddress(sta);
//		Const c = new Const(cent.lat, cent.lon);
//		DistConverter dc = new DistConverter(c);
//		System.out.println(dc.getLeft(cent.lon));
//		System.out.println(dc.getBott(cent.lat));
//		System.out.println(dc.getRight(cent.lon));
//		System.out.println(dc.getTop(cent.lat));
//		
//		File box = Retriever.getBox(dc.getLeft(cent.lon), dc.getBott(cent.lat), dc.getRight(cent.lon), dc.getTop(cent.lat));
//		MaxGuiFrame j = new MaxGuiFrame();
//		j.drawWays(x.parseBox(box));
//		System.out.println("Done");
//		return;
//
//	}
//	
//}
//
//
//
//
//class GuiFrame extends JFrame {
//	public GuiFrame() {
//		super("Test");
//
//		setSize(600, 600);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setResizable(true);
//		SpriteImp s = new SpriteImp("castle.jpg");
//		this.add(s);
//		setVisible(true);
//	}
//	
//}



//TODO
//Scaling factor for DLAT in constants
//Are vectors always resized?