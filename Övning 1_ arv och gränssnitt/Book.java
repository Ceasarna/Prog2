// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 078
// Tommy Ekberg toek3476
public class Book extends Item implements Vat6{
	
	private boolean bound;
	private double price;
	private String author;
	
	
	public Book(String name, String author, double price, boolean bound) {
		super(name);
		this.author = author;
		this.bound = bound; //Lägg in villkor, ifall bound så ökar VAT till 25. Price kommer ändras.
		this.price = price;
	}
	
	@Override
	public double getPrice() {
		final double staticIncrease = 1.25;
		if(bound) {
			return price * staticIncrease;
		}
		return price;
	}
	
	public String toString() {
		return "name=" + name + ", "+ "author=" + author + ", " + "bound=" + bound + ", " + "price=" + getPrice() + ", " + "price+vat=" + getPricePlusVAT() ;
	}
	
}
