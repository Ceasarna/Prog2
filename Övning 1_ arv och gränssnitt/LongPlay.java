// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 078
// Tommy Ekberg toek3476
public class LongPlay extends Recording{
	public LongPlay(String name, String artist, int year, int condition, double price) {
		super(name, artist, year, condition, price, "LP");
	}
	
	public double getPrice() {
		double yearDifference = (2021 - year) * 5;
		final int lowestAllowedValue = 10;
		double percentCondition = (double)condition / 10;
		
		double newValue = price * percentCondition;
		newValue += yearDifference;
		
		if(newValue < lowestAllowedValue) {
			return 10;
		}
		return newValue;
	}
}
	
