// PROG2 VT2021 Övning 3: I/O
// Grupp 375
// Tommy Ekberg toek3476


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.*;


public class Exercise3 {

	private static final int HUNDRED = 100;
	private final List<Recording> recordings = new ArrayList<>();

	
	public void exportRecordings(String fileName)  {
		try {
		FileWriter utfil = new FileWriter(fileName);
		PrintWriter out = new PrintWriter(utfil);
		
		for(Recording rec : recordings) {
			out.println("<recording>");
			out.println("\t<artist>" + rec.getArtist() + "</artist>");
			out.println("\t<title>" + rec.getTitle() + "</title>");
			out.println("\t<year>" + rec.getYear() + "</year>");
			out.println("\t<genres>");
			for(String s : rec.getGenre()) {
				out.println("\t\t<genre>" + s + "</genre>");
			}
			out.println("\t</genres>");
			out.println("</recording>");
		}
		out.close();
		
		}catch(FileNotFoundException e) {
			System.err.println("Kan ej öppna igelkott.txt!");
		}catch (IOException e){
			System.err.println(e.getMessage());
		}
	}

	public void importRecordings(String fileName) throws FileNotFoundException {
		
		File f = new File(fileName);
		if(!f.exists()) {
		    throw new FileNotFoundException("Filen fanns inte");    
		}
		
		try {
		    
		FileReader infil = new FileReader(fileName);
		BufferedReader in = new BufferedReader(infil);
		String line;
		String posts = in.readLine();
		int timer = 0;
		while (timer < Integer.parseInt(posts)) {
			line = in.readLine();
			String[] tokens = line.split(";");
			
			String artist = tokens[0];

			String title = tokens[1];

			int year = Integer.parseInt(tokens[2]);

			String amountGenres = in.readLine();

			Set<String> genres = new HashSet<String>();
			for(int i = 0; Integer.parseInt(amountGenres) > i; i++) {
				String genre = in.readLine();
				genres.add(genre);
			}
			recordings.add(new Recording(title, artist, year, genres));
			timer++;
		}
		
		}catch(IOException e) {
			System.err.println(e.getMessage());
		}
		

	}
    public Map<Integer, Double> importSales(String fileName) throws FileNotFoundException {
	    File f = new File(fileName);
		if(!f.exists()) {
		    throw new FileNotFoundException("Filen fanns inte");    
		}
		    Map<Integer, Double> salesMap = new HashMap<Integer, Double>();
		try{
       
			FileInputStream infil = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(infil);
			int antal = in.readInt();
			int year = 0;
			int month = 0;
			int day = 0;
			double value = 0.0;
			for(int i = 0; antal > i; i++) {
				year = in.readInt();
				month = in.readInt();
				day = in.readInt();
				value = in.readDouble();
				int temp = (year * HUNDRED) + month;
				
				salesMap.put(temp, salesMap.getOrDefault(temp, (double) 0) + value);

			}
			in.close();
			
			
		}catch(IOException e) {
			System.err.println("Error message: " + e);
		}
		
		
		return salesMap;
	}

	public List<Recording> getRecordings() {
		return Collections.unmodifiableList(recordings);
	}

	public void setRecordings(List<Recording> recordings) {
		this.recordings.clear();
		this.recordings.addAll(recordings);
	}
}