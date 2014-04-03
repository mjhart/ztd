package mapbuilder;

import java.awt.geom.Point2D;

public class DistConverter {
	
	private final Const c;
	private double left;
	private double bott;
	private double right;
	private double top;
	private double latrat;
	private double lonrat;

	public DistConverter(Const c) {
		this.c = c;
	}
	

	
	
	public double latRat(double toplat, double bottlat, double mapheight) {
		latrat = (mapheight)/(toplat - bottlat);
		return latrat;
	}
	
	public double lonRat(double leftlon, double rightlon, double mapwidth) {
		lonrat = (mapwidth)/(rightlon - leftlon);
		return lonrat;
	}
	
	
	
	
	public Point2D convertLLToAB(MapNode ll) {
		double lat = ll.lat - bott;
		lat = (top-bott) - lat;
		double b = lat*latrat;
		
		double lon = ll.lon - left;
		double a = lon*lonrat;
		
		Point2D res = new Point2D.Double(a, b);
		return res;
	}
	
	
	
	public double getBott(double centlat) {
		bott = centlat - c.DLAT;
		return bott;
	}
	
	public double getTop(double centlat) {
		top = centlat + c.DLAT;
		return top;
	}
	
	public double getLeft(double centlon) {
		left = centlon - c.DLON;
		return left;
	}
	
	public double getRight(double centlon) {
		right = centlon + c.DLON;
		return right;
	}

}
