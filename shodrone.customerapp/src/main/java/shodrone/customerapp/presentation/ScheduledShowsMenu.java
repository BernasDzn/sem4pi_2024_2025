package shodrone.customerapp.presentation;

import java.util.LinkedList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eapli.framework.actions.menu.Menu;
import shodrone.customerapp.CustomerAppCli;

import static shodrone.customerapp.presentation.AllShowsMenu.viewShowDetails;

public class ScheduledShowsMenu extends Menu {

    private static final String MENU_TITLE = "Scheduled Shows >";

    private static final int EXIT_OPTION = 0;

    public ScheduledShowsMenu() {
        super(MENU_TITLE);
        buildScheduledShowsMenu();
    }

    private void buildScheduledShowsMenu() {
        LinkedList<String> scheduledShows = CustomerAppCli.getScheduledShows();
        ObjectMapper objectMapper = new ObjectMapper();

		for (int i = 0; i < scheduledShows.size(); i++) {
			String scheduledShowsJson = scheduledShows.get(i);
			String scheduledShowsMessage = "";
			try {
				JsonNode node = objectMapper.readTree(scheduledShowsJson);
				if (node.has("showId") && node.has("date")) {
					scheduledShowsMessage += "ID: " + node.get("showId").asText();
					String[] parts = node.get("date").asText().split(" ");
					scheduledShowsMessage += " | Date: " + parts[0] + " Time: " + parts[1];
				}
				else{
					scheduledShowsMessage += "No ID found in Show";
				}
			} catch (Exception e) {
				scheduledShowsMessage += "Error parsing sheduled show";
			}
			addItem(i + 1, scheduledShowsMessage, () -> {
				viewShowDetails(scheduledShowsJson);
				return true;
			});
		}
        addItem(EXIT_OPTION, "Return ", () -> true);
    }
    
}
