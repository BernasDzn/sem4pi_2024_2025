package shodrone.customerapp.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eapli.framework.actions.menu.Menu;
import org.springframework.http.converter.json.GsonBuilderUtils;
import shodrone.core.presentation.console.Colorize;
import shodrone.customerapp.CustomerAppCli;

import java.util.List;

public class AllShowsMenu extends Menu{

    private static final String MENU_TITLE = "All Shows >";

    private static final int EXIT_OPTION = 0;

    public AllShowsMenu() {
        super(MENU_TITLE);
        buildScheduledShowsMenu();
    }

    private void buildScheduledShowsMenu() {
        List<String> shows = CustomerAppCli.getAllShows();
        String showName;
        for(int i = 0; i < shows.size(); i++) {
            final String show = shows.get(i);
            ObjectMapper objectMapper = new ObjectMapper();
            try{
                JsonNode node = objectMapper.readTree(show);
                if(node.has("showId") && node.has("date")) {
                    showName = "ID: " + node.get("showId").asText() + " | Date: " + node.get("date").asText();
                } else {
                    System.out.println(Colorize.toRed("No ID found in Show"));
                    continue;
                }
            }
            catch (Exception e) {
                System.out.println(Colorize.toRed("Error parsing show details"));
                continue;
            }
            addItem(i + 1, showName, () -> {
                viewShowDetails(show);
                return true;
            });
        }
        addItem(EXIT_OPTION, "Return ", () -> true);
    }

    public static void viewShowDetails(String show) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(show);
            if (node.has("showId")){
                String showId = node.get("showId").asText();
                System.out.println(Colorize.toGreen("Show ID: ") + showId);
            } else {
                System.out.println(Colorize.toRed("No ID found in Show"));
            }
            if (node.has("date")) {
                String date = node.get("date").asText();
                System.out.println(Colorize.toGreen("Date: ") + date);
            } else {
                System.out.println(Colorize.toRed("No Date found in Show"));
            }
            if (node.has("numberOfDrones")) {
                String numberOfDrones = node.get("numberOfDrones").asText();
                System.out.println(Colorize.toGreen("Number of Drones: ") + numberOfDrones);
            } else {
                System.out.println(Colorize.toRed("No Number of Drones found in Show"));
            }
            if (node.has("duration")) {
                String duration = node.get("duration").asText();
                System.out.println(Colorize.toGreen("Duration: ") + duration);
            } else {
                System.out.println(Colorize.toRed("No Duration found in Show"));
            }
            if (node.has("place")) {
                String place = node.get("place").asText();
                System.out.println(Colorize.toGreen("Place: ") + place);
            } else {
                System.out.println(Colorize.toRed("No Place found in Show"));
            }
            if (node.has("video")) {
                String video = node.get("video").asText();
                System.out.println(Colorize.toGreen("Video: ") + video);
            } else {
                System.out.println(Colorize.toRed("No Video found in Show"));
            }
            if (node.has("figures")) {
                String figures = node.get("figures").asText();
                System.out.println(Colorize.toGreen("Figures:\n") + figures);
            } else {
                System.out.println(Colorize.toRed("No Figures found in Show"));
            }
            if (node.has("droneModelTypes")) {
                String droneModelTypes = node.get("droneModelTypes").asText();
                System.out.println(Colorize.toGreen("Drone Model Types:\n") + droneModelTypes);
            } else {
                System.out.println(Colorize.toRed("No Drone Model Types found in Show"));
            }
        } catch (JsonProcessingException e) {
            System.out.println(Colorize.toRed("Failed to parse Show Details"));
        }
    }

}
