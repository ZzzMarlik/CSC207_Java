package stormPath;

import java.util.ArrayList;

public class City {
	private String name;
	private ArrayList storms;

	public City(String name) {
		this.name = name;
		this.storms = new ArrayList();
	}

	public boolean wasHit(Storm s) {
		return s.getCities().contains(this);
	}

    public ArrayList getStorms() {
        return storms;
    }

    public String getName() {
		return name;
	}

	public boolean onSamePath(City c2) {
        for (int i = 0; i < this.storms.size(); i++) {
            if (this.storms.contains(c2.storms.get(i))) {
                return true;
            }
        }
        return false;
    }

	public void addStorm(Storm s) {
		if (!(this.storms.contains(s))) {
			this.storms.add(s);
		}
		if (!(s.getCities().contains(this))) {
			s.getCities().add(this);
		}
	}

	@Override
	public String toString() {
		StringBuilder acc = new StringBuilder();
		if (this.storms.size() == 0) {
			return this.name + " ()";
		}
		if (this.storms.size() == 1) {
			return this.name + " (" + ((Storm) (this.storms.get(0))).getName() + ")";
		}
		if (this.storms.size() > 1) {
			for (int i = 0; i < this.storms.size() - 1; i++) {
				acc.append(((Storm) (this.storms.get(i))).getName()).append(", ");
			}
			return this.name + " (" + acc + ((Storm) (this.storms.get(this.storms.size() - 1))).getName() + ")";
		}
		return "";
	}

	@Override
	public boolean equals(Object c2) {
		if (!(c2 instanceof City)) {
			return false;
		}
		if (!(this.name.equals(((City) c2).getName()))) {
			return false;
		}
		if (this.storms.size() != ((City) c2).storms.size()) {
			return false;
		}
		for (int i = 0; i < this.storms.size(); i++) {
			if (!(this.storms.contains(((City) c2).storms.get(i)))) {
				return false;
			}
		}
		return true;
	}
}