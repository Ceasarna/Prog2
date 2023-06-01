// PROG2 VT2021, Övningsuppgift 2
// Grupp 337
// Tommy Ekberg toek3476

import java.util.*;


public class Searcher implements SearchOperations, Stats{
		
	private TreeMap<Integer, HashSet<Recording>> allTracksByYear = new TreeMap<>();
	private HashSet<String> allUniqueGenres = new HashSet<>();
	private HashMap<String, Recording> allUniqueTitles = new HashMap<>();
	private HashMap<String, Set<Recording>> orderGenre = new HashMap<>();
	private TreeMap<String, HashSet<Recording>> artistTracks = new TreeMap<>();
	private HashSet<Recording> allRecordings = new HashSet<>();
	
	
	public Searcher(Collection<Recording> data) {
		allRecordings.addAll(data);
		for(Recording recording : data) {
			allUniqueTitles.put(recording.getTitle(), recording);
			
			HashSet<Recording> setYear = allTracksByYear.getOrDefault(recording.getYear(), new HashSet<>());
			allTracksByYear.put(recording.getYear(), setYear);
			setYear.add(recording);
			
			HashSet<Recording> setArtistTrack = artistTracks.getOrDefault(recording.getArtist(), new HashSet<>());
			artistTracks.put(recording.getArtist(), setArtistTrack);
			setArtistTrack.add(recording);
	
			for(String genre : recording.getGenre()) {
				Set<Recording> setGenre = orderGenre.getOrDefault(genre, new HashSet<>());
				orderGenre.put(genre, setGenre);
				setGenre.add(recording);
				allUniqueGenres.add(genre);	
			}
		}
		
	}
	
	@Override
	public long numberOfArtists() {
		return artistTracks.size();
	}

	@Override
	public long numberOfGenres() {
		return allUniqueGenres.size();
	}

	@Override
	public long numberOfTitles() {
		return allUniqueTitles.size();
	}
	
	@Override
	public boolean doesArtistExist(String name) {
		return artistTracks.containsKey(name);
	}
	
	@Override
	public Collection<String> getGenres() {
		return Collections.unmodifiableSet(allUniqueGenres);
	}
	
	@Override
    public Collection<Recording> getRecordingsByGenre(String genre) {
		return Collections.unmodifiableSet(orderGenre.getOrDefault(genre, Collections.emptySet()));
        //return Collections.unmodifiableCollection(orderGenre.getOrDefault(genre, Collections.emptySet()));
    }

	@Override
	public Optional<Recording> getRecordingByName(String title) {
		Optional<Recording> opt = Optional.ofNullable(allUniqueTitles.get(title));
		return opt;
	}
	
	@Override
	public Collection<Recording> getRecordingsAfter(int year) {
		SortedMap<Integer, HashSet<Recording>> biggerThanYear = allTracksByYear.tailMap(year);
		Set<Recording> temp = new HashSet<Recording>();
		for(Set<Recording> rec : biggerThanYear.values()){
			temp.addAll(rec);
		}
		return Collections.unmodifiableSet(temp);
	}
	
	
	@Override
	public SortedSet<Recording> getRecordingsByArtistOrderedByYearAsc(String artist) {
		
		Set<Recording> temp = artistTracks.get(artist);
		SortedSet<Recording> firstSort = new TreeSet<Recording>(temp);
		
		return Collections.unmodifiableSortedSet(firstSort);
	}

	@Override
	public Collection<Recording> getRecordingsByGenreAndYear(String genre, int yearFrom, int yearTo) {
		TreeMap<Integer, Set<Recording>> tracksFromGenre =  new TreeMap<Integer, Set<Recording>>();
		for(Recording rec : orderGenre.get(genre)) {	
			Set<Recording> setYearTrack = tracksFromGenre.getOrDefault(rec.getYear(), new HashSet<>());
			tracksFromGenre.put(rec.getYear(), setYearTrack);
			setYearTrack.add(rec);
		}
		
		Set<Recording> recByGenre = new HashSet<Recording>(getRecordingsByGenre(genre));
		Set<Recording> recByYear = new HashSet<Recording>();
		SortedMap<Integer, Set<Recording>> tracksBetween = tracksFromGenre.subMap(yearFrom, yearTo+1);
		
		for(Set<Recording> rec : tracksBetween.values()){
			recByYear.addAll(rec);
		}
		
		recByGenre.retainAll(recByYear);
		
		return Collections.unmodifiableSet(recByGenre);
	}

	
    @Override
	public Collection<Recording> offerHasNewRecordings(Collection<Recording> offered) {
		HashSet<Recording> returnSet = new HashSet<Recording>(offered);
		returnSet.removeAll(allRecordings);
		return Collections.unmodifiableCollection(returnSet);
	}
}

