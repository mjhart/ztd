package mapbuilder;

/**
 * The Main class is the wrapper for the whole application. It takes in the input file, instantiates
 * the App, and hands it the input file
 * @author mmkaplan
 *
 */
public class Main {
	public static void main(String[] argv) {
		XmlParser x = new XmlParser();
		x.parseBox("/gpfs/main/home/mmkaplan/course/cs032/ztd/69bs.xml");
		Retriever r = new Retriever();
		r.getBox(-71.40794, 41.82544, -71.40086, 41.82944);
		r.getFromAddress("228 East Meade Street, Philadelphia PA");
		x.parseAddress("/gpfs/main/home/mmkaplan/course/cs032/ztd/address2.xml");
	}
	
}