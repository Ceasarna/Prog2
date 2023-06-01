// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 078
// Tommy Ekberg toek3476
public abstract class Item implements Vat{
	
	protected final String name;
	
	protected Item(String name) {
		this.name = name;
	}
	
	public abstract double getPrice();
	
	public final double getPricePlusVAT() {
		return getPrice() * (1 + getVAT());
	}
	
}
