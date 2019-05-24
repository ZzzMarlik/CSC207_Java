package stormPath;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StormRecorder {
	public static void main(String[] args) throws IOException, FileNotFoundException {
		ArrayList<City> city_lst = new ArrayList<>();
		File file = new File("StormList.txt");
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(file));
		String text;
		ArrayList<String> city_name_lst = new ArrayList<>();
		text = reader.readLine();
		while (text != null) {
			String[] current_line = text.split("\\|");
			// Get storm name
			String storm_name = current_line[0].split("\\s")[0].trim();
			// Get storm year
			int storm_year = Integer.parseInt(current_line[0].split("\\s")[1]);
			// Create new storm
			Storm new_storm = new Storm(storm_name, storm_year);
			for (int i = 1; i < current_line.length ; i++) {
				// Totally new city
				if (!(city_name_lst.contains(current_line[i].trim()))) {
					city_name_lst.add(current_line[i].trim());
					City new_city = new City(current_line[i].trim());
					new_city.addStorm(new_storm);
					city_lst.add(new_city);
				}
				// Already exist, update city (add new_storm)
				else {
					city_lst.get(city_name_lst.indexOf(current_line[i].trim())).addStorm(new_storm);
				}
			}
			text = reader.readLine();
		}
		BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter City name: ");
		String input = kbd.readLine();
		while (!input.equals("exit")) {
				// Check existence of the given city input
				if (city_name_lst.contains(input)) {
					System.out.println(city_lst.get(city_name_lst.indexOf(input)).toString());
					System.out.println("Enter City name: ");
					input = kbd.readLine();
				}
				// Can't find the input
				else {
					System.out.println("This is not a valid city.");
					System.out.println("Enter City name: ");
					input = kbd.readLine();
				}
			}
		}
	}
