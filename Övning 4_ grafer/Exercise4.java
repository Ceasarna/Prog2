// PROG2 VT2021, Övningsuppgift 4: Grafer
// Grupp 375
// Tommy Ekberg toek3476

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Exercise4 implements Ex4 {
	
	private final Graph<Nodes.GraphNode> graph;
	
	private Map<String, Nodes.LocationNode> connectMap = new HashMap<>();
	
	public Exercise4(Graph<Nodes.GraphNode> graph) {
		this.graph = graph;
	}

	@Override
	public void loadLocationGraph(String filename) {
		try{
			FileReader reader = new FileReader(filename);

			BufferedReader in = new BufferedReader(reader);
			String line;
			boolean flag = true;
			while ((line = in.readLine()) != null){
				int counter=0;
				String[] tokens = line.split(";");
				if(flag) {
					for(int i=0; counter < tokens.length; i++) {
						String plats = tokens[counter];
						counter++;
					
						double x = Double.parseDouble(tokens[counter]);
						counter++;

						double y = Double.parseDouble(tokens[counter]);
						counter++;	
						
						Nodes.LocationNode node = new Nodes.LocationNode(plats, x, y);
						
						connectMap.put(plats, node);	
						
						graph.add(node);
					}
				}
				flag = false;
				if(counter == 0) {
					String plats1 = tokens[0];
					String plats2 = tokens[1];
					String forbindelse = tokens[2];
					int vikt = Integer.parseInt(tokens[3]);
										
					graph.connect(connectMap.get(plats1), connectMap.get(plats2), forbindelse, vikt);
				}
				counter= 0;
			}
			
			in.close();
			reader.close();
			}catch(IOException e){
				System.err.println(e);
			}
	}
}