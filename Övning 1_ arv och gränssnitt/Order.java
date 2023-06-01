// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 078
// Tommy Ekberg toek3476
    public class Order {
	private Item [] items;
	private long counter;
	private long orderNumber;
	
	public Order(Item ...items) {
		this.items = items;
		counter++;
		
	}
	
	public double getTotalValue() {
		double total = 0;
		for(Item item : items) {
			total +=  item.getPrice();
		}
		return total;	
	}
	
	public double getTotalValuePlusVAT() {
		double total = 0;
		for(Item item : items) {
			total +=  item.getPricePlusVAT();

		}
		
		return total;	
	}
	
	public String getReceipt() {
		
		String rece = "Receipt for order #" + counter + "\n";
		rece += "-----------\n";
		
		for(Item item : items) {
			if(item instanceof Recording) {
				rece += "* Recording { " + item.toString() + " }\n";	
			
			}else if(item instanceof Book) {
				rece += "* Book { " + item.toString() + " }\n";
			}
			else {
				return "Något gick fel!";
			}
		}
		rece += "Total excl. VAT: " + getTotalValue() + "\n";
		rece += "Total incl. VAT: " + getTotalValuePlusVAT();
		return rece;
	}

}


