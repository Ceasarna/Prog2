// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 078
// Tommy Ekberg toek3476
public abstract class Recording extends Item implements Vat25{
	
	protected int condition;
	protected int year;
	protected double price;
	protected String artist; 
	private String type;
	
	public Recording(String name, String artist, int year, int condition, double price, String type) {		
		super(name);
		this.artist = artist;
		this.year = year;
		this.price = price;
		this.type = type;
		if(condition > 10 || condition < 0) {
			throw new IllegalArgumentException();
		}
		this.condition = condition;
	}

	public String toString() {
		return "name=" + name + ", "+ "artist=" + artist + ", "+ "year=" + year + ", " + "type=" + type + ", " + "condition=" + condition + ", " + "original price=" + price + ", " + "price=" + getPrice() + ", " + "price+vat=" + getPricePlusVAT() ;
	}
}
