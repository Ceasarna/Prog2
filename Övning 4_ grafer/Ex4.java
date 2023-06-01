// PROG2 VT2021, Övningsuppgift 4: Grafer
// Grupp 375
// Tommy Ekberg toek3476

import java.util.*;

public interface Ex4 {

	void loadLocationGraph(String filename);

	default SortedMap<Integer, SortedSet<Nodes.RecordNode>> optionalGetAlsoLiked(Nodes.RecordNode item) {
		return null;
	}

	default long optionalGetPopularity(Nodes.RecordNode item) {
		return -1;
	}

	default TreeMap<Integer, Set<Nodes.RecordNode>> optionalGetTop5() {
		return null;
	}

	default void optionalLoadRecoGraph(String filename) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
