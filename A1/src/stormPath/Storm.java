package stormPath;

import java.util.ArrayList;

public class Storm {
	private String name;
	private ArrayList cities;
	private int year;

	public Storm(String name, int year) {
		this.name = name;
		this.year = year;
		this.cities = new ArrayList();
	}

	public String getName() {
		return name;
	}

	public int getYear() {
		return year;
	}

	public ArrayList getCities() {
		return cities;
	}

	public void addCity(City c1) {
		if (!this.cities.contains(c1)) {
			this.cities.add(c1);
		}
		if (!c1.getStorms().contains(this)) {
		    c1.getStorms().add(this);
        }
	}

	@Override
	public String toString() {
	    if (this.cities.size() == 0) {
	        return name + ", " + year;
        }
	    if (this.cities.size() == 1) {
	        return name + ", " + year + System.lineSeparator() + ((City) (this.getCities().get(0))).getName();
        }
		String acc = "";
		for (int i = 0 ; i < this.getCities().size() - 1; i++) {
			acc += ((City) (this.getCities().get(i))).getName() + System.lineSeparator();
		}
		return name + ", " + year + System.lineSeparator() + acc +
                ((City) (this.getCities().get(this.getCities().size() - 1))).getName();
	}

	@Override
	public boolean equals(Object s2) {
		return (s2 instanceof Storm) && (this.name.equals(((Storm) s2).getName())) && (this.year == ((Storm) s2).getYear());
	}
}
