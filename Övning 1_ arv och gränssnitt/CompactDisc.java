// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 078
// Tommy Ekberg toek3476
public class CompactDisc extends Recording{
	
	public CompactDisc(String name, String artist, int year, int condition, double price) {
		super(name, artist, year, condition, price, "CD");
	}
	
	public double getPrice() {
		double newValue = 0;
		final int lowestAllowedValue = 10;
		double percentCondition = (double)condition / 10.0;
		newValue = price * percentCondition;
		
		if(newValue < lowestAllowedValue) {
			return 10;
		}
		return newValue;
	}
}
