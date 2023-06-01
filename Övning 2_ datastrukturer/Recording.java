// PROG2 VT2021, Övningsuppgift 2
// Grupp 337
// Tommy Ekberg toek3476

import java.util.Collection;
import java.util.Set;

public class Recording implements Comparable<Recording>{
	private final int year;
	private final String artist;
	private final String title;
	private final String type;
	private final Set<String> genre;

	public Recording(String title, String artist, int year, String type, Set<String> genre) {
		this.title = title;
		this.year = year;
		this.artist = artist;
		this.type = type;
		this.genre = genre;
	}

	public String getArtist() {
		return artist;
	}

	public Collection<String> getGenre() {
		return genre;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public int getYear() {
		return year;
	}
	
	@Override
	public int hashCode() {
		int result = year;
		result = 31 * result + artist.hashCode();
		result = 31 * result + title.hashCode();
		return result;

	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Recording) {
			Recording r = (Recording)other;
			return (r.artist == artist) && (r.title == title) && (r.year == year);
		}else {
			return false;
		}
	}
	@Override
	public String toString() {
		return String.format("{ %s | %s | %s | %d | %s }", artist, title, genre, year, type);
	}

	@Override
	public int compareTo(Recording other) {
			if(year < other.year) {
				return -1;
			}else if(year > other.year) {
				return 1;
			}else {
				return 0;
			}
	}
}
