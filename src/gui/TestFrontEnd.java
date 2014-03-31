package gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;

import cs195n.CS195NFrontEnd;
import cs195n.SwingFrontEnd;
import cs195n.Vec2i;

public class TestFrontEnd extends SwingFrontEnd {

	private JFrame frame;
	
	public TestFrontEnd(String title, boolean fullscreen) {
		super(title, fullscreen);
		super.startup();
	}
	
	public TestFrontEnd(String title, boolean fullscreen, Vec2i size) {
		super(title, fullscreen, size);
		super.setDebugMode(true);
		//frame = new JFrame("Test");
		//frame.setPreferredSize(new Dimension(500, 500));
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDraw(Graphics2D g) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResize(Vec2i newSize) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) {
		//new TestFrontEnd("ZTD", true);
		new TestFrontEnd("ZTD", false, new Vec2i(500, 500));
	}
}
