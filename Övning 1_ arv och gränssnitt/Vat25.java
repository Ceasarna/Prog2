// PROG2 VT2021, Inlämningsuppgift, del 1
// Grupp 078
// Tommy Ekberg toek3476
public interface Vat25 extends Vat{
	default double getVAT() {
		return 0.25;
	}
}
