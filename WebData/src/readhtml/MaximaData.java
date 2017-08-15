package readhtml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaximaData {

	private List<String> products = new ArrayList<>();
	private List<Double> prices = new ArrayList<>();

	// main method for testing
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			MaximaData data = new MaximaData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MaximaData() throws Exception {
		// Test URL
		URL url = new URL(
				"https://www.e-maxima.lv/Produkti/partika_dzerieni/augli_darzeni/salati_garsaugi/svaigi_salati.aspx");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));

		String inputLine;
		Pattern namePattern = Pattern.compile("(?<=>)(.+?)(?=</a>)");
		Pattern pricePattern = Pattern
				.compile("(?<=<strong>)(\\d{1,3})(,)(\\d{2})(?= €</strong>)");
		Matcher matcher;

		while ((inputLine = in.readLine()) != null) {
			boolean isTable = false;
			boolean isContent = false;
			if ((inputLine.replaceAll("\\s", ""))
					.equals("<divid=\"main-content\">")) {
				while ((inputLine = in.readLine()) != null) {
					if (isContent) {
						if ((inputLine.replaceAll("\\s", ""))
								.equals("</tbody></table>")) {
							break;
						}
						if ((inputLine.replaceAll("\\s", "")).equals("<h3>")) {
							inputLine = in.readLine();
							matcher = namePattern.matcher(inputLine);
							matcher.find();
							products.add(matcher.group());
							// output for testing
							System.out.println(matcher.group());
						}
						if ((inputLine.replaceAll("\\s", ""))
								.equals("<pclass=\"guide\">")) {
							inputLine = in.readLine();
							matcher = pricePattern.matcher(inputLine);
							matcher.find();
							prices.add(Double.parseDouble((matcher.group())
									.replaceAll(",", ".")));
							// output for testing
							System.out.println(matcher.group());
						}
					}
					if (isTable) {
						if ((inputLine.replaceAll("\\s", "")).equals("<tr>")) {
							isContent = true;
						}
					}
					if ((inputLine.replaceAll("\\s", ""))
							.equals("<tableclass='datatype1'>")) {
						isTable = true;
					}
				}
				break;
			}
		}
		in.close();
	}
}
