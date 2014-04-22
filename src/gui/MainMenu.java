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
	
	public MainMenu(int w, int h) {
		_w = w;
		_h = h;
		_cbs = new ArrayList<ControlButton>();
		_toggle = 1;
		this.g = null;
		_first = true;
	}

	public void draw(Graphics2D g) {
		this.g = g;
		if (_first) {
			_go = new ControlButton("GO", _w/2, 3*_h/5);
			int c = 30;
			_addline1 = new EditableTextBox(_etbwidth, _w/2, _h/5 + c);
			_addline2 = new EditableTextBox(_etbwidth, _w/2, _h/5 + 2*c);
			_cbs.add(new ControlButton("Brown University", 3*_w/2, _h/5 + 2*c));
			_cbs.add(new ControlButton("Wall Street", 3*_w/2, _h/5 + 3*c));
			_cbs.add(new ControlButton("The White House", 3*_w/2, _h/5 + 4*c));
			_cbs.add(new ControlButton("Eiffel Tower", 3*_w/2, _h/5 + 5*c));
			_cbs.add(new ControlButton("Statue of Liberty", 3*_w/2, _h/5 + 6*c));
			_first = false;
		}
		
		java.awt.Color colorholder = g.getColor();
		g.setColor(Color.GRAY);
		g.fill(new Rectangle2D.Float(0,0,_w,_h));
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		new Text("Zombie Tower Defense", centerX("Zombie Tower Defense", _w), _h/10);

		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		new Text("Enter an address", centerX("Enter an address", _w/2), _h/5);
		
		FontMetrics fm = g.getFontMetrics();
		int c = fm.getHeight() + 10;
		new Text("Or choose from one", centerX("Or choose from one", 3*_w/2), _h/5);
		new Text("of our locations", centerX("of our locations", 3*_w/2), _h/5 + c - 10);
		
		g.drawLine((int) (_w/2), (int) (_h/5), (int) (_w/2), (int) (4*_h/5));
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		_addline1.draw();
		_addline2.draw();
		_go.draw();
		
		for (ControlButton cb: _cbs) {
			cb.draw();
		}
		
		g.setColor(colorholder);
	}
	
	private float centerX(String name, float rightline) {
		FontMetrics fm = g.getFontMetrics();
		int c = fm.stringWidth(name);
		int x = (int) (.5*rightline - .5*c);
		return x;
	}
	
	private class ControlButton {
		private String _name;
		private RoundRectangle2D _r;
		private Rectangle2D _bb;
		private float x;
		private float y;
		public ControlButton(String name, float rightline, float y) {
			_name = name;
			g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
			float x = centerX(name, rightline);
			this.x = x;
			this.y = y;
		}
		public void draw() {
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
			FontMetrics fm = g.getFontMetrics();
			_bb = fm.getStringBounds(_name, g);
			_r = new RoundRectangle2D.Float(x,y,(float) (_bb.getWidth()+10), (float) (_bb.getHeight()+5), 5, 5);
			g.draw(_r);
			g.drawString(_name, x+5,(int) (y+_bb.getHeight()+1));
		}
		public RoundRectangle2D getRoundRect() {
			return _r;
		}
		public String getName() {
			return _name;
		}
	}
	
	
	
	private class EditableTextBox {
		private String _text;
		private String _widthholder;
		private RoundRectangle2D _r;
		private Rectangle2D _bb;
		private float x;
		private float y;
		public EditableTextBox(float textwidth, float rightline, float y) {
			_widthholder = "";
			for (int i = 0; i < textwidth; i++) {
				_widthholder = _widthholder + "d";
			}
			g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
			float x = centerX(_widthholder, rightline);
			this.x = x;
			System.out.println(x);
			System.out.println(_w);
			this.y = y;
			_text = "";
			_bb = null;
			_r = null;
		}
		public void draw() {
			g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
			FontMetrics fm = g.getFontMetrics();
			_bb = fm.getStringBounds(_widthholder, g);
			_r = new RoundRectangle2D.Float(x,y,(float) (_bb.getWidth()+10), (float) (_bb.getHeight()+5), 5 , 5);
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
			g.draw(_r);
			g.drawString(_text, x+5,(int) (y+_bb.getHeight()+1));
		}
		public void addLetter(String letter) {
			if (_text.length() < _etbwidth) {
				_text = _text + letter;
			}
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
	}
	
	
	
	private class Text {
		public Text(String name, float x, float y) {
			g.drawString(name, x, y);
		}
	}
	
	
	
	
	//Work in terms of vectors or x y?
	public String contains(int x, int y) {
		for (ControlButton cb: _cbs) {
			if (cb.getRoundRect().contains(x, y)) {
				return cb.getName();
			}
		}
		if (_go.getRoundRect().contains(x, y)) {
			return (_addline1.getText() + " " + _addline2.getText());
		}
		this.chooseAddline(x,y);
		return null;
	}
	
	
	
	public void chooseAddline(int x, int y) {
		if (_addline1.contains(x, y)) {
			_toggle = 1;
		}
		else if (_addline2.contains(x, y)) {
			_toggle = 2;
		}
	}
	
	
	
	public void keyTyped(String letter) {
		EditableTextBox holder = null;
		EditableTextBox notholder = null;
		if ((_toggle == 1) && (_addline1.getText().length() < _etbwidth)) {
			holder = _addline1;
			notholder = _addline2;
		}
		else {
			_toggle = 2;
			holder = _addline2;
			notholder = _addline1;
		}
		
		if (letter.length() > 1) {
			if (letter.equals("backspace")) {
				if ((_addline2.getText().length() == 0) && (_toggle == 2)) {
					_toggle = 1;
					notholder.backspace();
				}
				else {
					holder.backspace();
				}
			}
			else if (letter.equals("enter")) {
				if (_toggle == 1) {
					_toggle = 2;
				}
				else {
					_toggle = 1;
				}

			}
		}
		else {
			holder.addLetter(letter);
		}
	}
	
	
	public void clear() {
		_addline1.clear();
		_addline2.clear();
	}
	
}
