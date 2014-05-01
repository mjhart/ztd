package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;



public class MainMenu {

	
	private int _w; //Width of the whole frame
	private int _h; //Height of the whole frame
	private Graphics2D g;
	
	private List<ControlButton> _cbs; //A list of control buttons. Needed to check for mouse clicks
	private EditableTextBox _addline1;
	private EditableTextBox _addline2;
	private int _toggle;
	private ControlButton _go;
	private int _etbwidth = 25;
	private boolean _first;
	private final Color _background = Color.GRAY;
	
	public MainMenu(int w, int h) {
		_w = w;
		_h = h;
		_cbs = new ArrayList<ControlButton>();
		_toggle = 0;
		this.g = null;
		_first = true;
	}

	public void draw(Graphics2D g) {
		this.g = g;
		if (_first) {
			_go = new ControlButton("GO", _w/2, 3*_h/5, g);
			int c = 30;
			_addline1 = new EditableTextBox(_etbwidth, _w/2, _h/5 + c);
			_addline2 = new EditableTextBox(_etbwidth, _w/2, _h/5 + 2*c);
			_cbs.add(new ControlButton("Brown University", 3*_w/2, _h/5 + 2*c, g));
			_cbs.add(new ControlButton("Wall Street", 3*_w/2, _h/5 + 3*c, g));
			_cbs.add(new ControlButton("The White House", 3*_w/2, _h/5 + 4*c, g));
			_cbs.add(new ControlButton("Eiffel Tower", 3*_w/2, _h/5 + 5*c, g));
			_cbs.add(new ControlButton("Statue of Liberty", 3*_w/2, _h/5 + 6*c, g));
			_first = false;
		}
		
		java.awt.Color colorholder = g.getColor();
		g.setColor(_background);
		g.fill(new Rectangle2D.Float(0,0,_w,_h));
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Helvetica", Font.BOLD, 30));
		new Text("Zombie Tower Defense", centerX("Zombie Tower Defense", _w), _h/10);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		new Text("Enter an address", centerX("Enter an address", _w/2), _h/5);
		
		FontMetrics fm = g.getFontMetrics();
		int c = fm.getHeight() + 10;
		new Text("Or choose from one", centerX("Or choose from one", 3*_w/2), _h/5);
		new Text("of our locations", centerX("of our locations", 3*_w/2), _h/5 + c - 10);
		
		g.drawLine((int) (_w/2), (int) (_h/5), (int) (_w/2), (int) (3*_h/5));
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		_addline1.draw();
		_addline2.draw();
		
		
		_go.draw(g, _background);
		
		for (ControlButton cb: _cbs) {
			cb.draw(g, _background);
		}
		
		g.setColor(colorholder);
	}
	
	private float centerX(String name, float rightline) {
		FontMetrics fm = g.getFontMetrics();
		int c = fm.stringWidth(name);
		int x = (int) (.5*rightline - .5*c);
		return x;
	}
	
	
	private class EditableTextBox {
		private String _text;
		private String _disptext;
		private String _widthholder;
		private RoundRectangle2D _r;
		private Rectangle2D _bb;
		private float x;
		private float y;
		private int blink = 0;
		private boolean _selected = false;
		public EditableTextBox(float textwidth, float rightline, float y) {
			_widthholder = "";
			for (int i = 0; i < textwidth; i++) {
				_widthholder = _widthholder + "d";
			}
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			float x = centerX(_widthholder, rightline);
			this.x = x;
			this.y = y;
			_text = "";
			_bb = null;
			_r = null;
		}
		public void draw() {
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			FontMetrics fm = g.getFontMetrics();
			_bb = fm.getStringBounds(_widthholder, g);
			_r = new RoundRectangle2D.Float(x,y,(float) (_bb.getWidth()+10), (float) (_bb.getHeight()+5), 10, 10);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			g.draw(_r);
			_disptext = _text;
			if ((blink > 50) && (blink < 100) && (_selected)) {
				_disptext = _disptext + "|";
			}
			while (fm.getStringBounds(_disptext, g).getWidth() > _bb.getWidth()) {
				_disptext = _disptext.substring(1);
			}
			g.drawString(_disptext, x+5,(int) (y+_bb.getHeight()+1));
			blink++;
			blink = blink % 100;
		}
		public void addLetter(String letter) {
			_text = _text + letter;
		}
		public void backspace() {
			int len = _text.length();
			if (len > 0) {
				_text = _text.substring(0, len-1);
			}
		}
		public void clear() {
			_text = "";
		}
		public String getText() {
			return _text;
		}
		public boolean contains(int x, int y) {
			if (_r.contains(x, y)) {
				return true;
			}
			return false;
		}
		public void select() {
			_selected = true;
		}
		public void unselect() {
			_selected = false;
		}
	}
	
	
	
	private class Text {
		public Text(String name, float x, float y) {
			g.drawString(name, x, y);
		}
	}
	
	
	
	
	public String contains(int x, int y, boolean click) {
		for (ControlButton cb: _cbs) {
			if (cb.getRoundRect().contains(x, y)) {
				cb.highlight();
				return cb.getName();
			}
			else {
				cb.unhighlight();
			}
		}
		if (_go.getRoundRect().contains(x, y)) {
			_go.highlight();
			return (_addline1.getText() + " " + _addline2.getText());
		}
		else {
			_go.unhighlight();
		}
		if (click) {
			this.chooseAddline(x,y);
		}
		return null;
	}
	
	
	
	public void chooseAddline(int x, int y) {
		if (_addline1.contains(x, y)) {
			_toggle = 1;
			_addline1.select();
			_addline2.unselect();
		}
		else if (_addline2.contains(x, y)) {
			_toggle = 2;
			_addline2.select();
			_addline1.unselect();
		}
		System.out.println("Choose: " + _toggle);
	}
	
	
	//Right now this is set up so that if neither addline in in focus nothing happens
	public void keyTyped(String letter) {
		System.out.println(_toggle);
		EditableTextBox holder = null;
		boolean focus = true;
		if (_toggle == 1) {
			holder = _addline1;
		}
		else if (_toggle == 2) {
			_toggle = 2;
			holder = _addline2;
		}
		else {
			focus = false;
		}
		if ((letter.length() > 1) && (focus)) {
			if (letter.equals("backspace")) {
				holder.backspace();
			}
			else if ((letter.equals("enter")) || (letter.equals("tab"))) {
				if (_toggle == 1) {
					_toggle = 2;
					_addline2.select();
					_addline1.unselect();
				}
			}
		}
		else if (focus) {
			holder.addLetter(letter);
		}
	}
	

	
	
	public void clear() {
		_addline1.clear();
		_addline2.clear();
	}
	
}
