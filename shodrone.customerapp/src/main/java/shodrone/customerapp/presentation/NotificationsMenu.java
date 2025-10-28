package shodrone.customerapp.presentation;

import java.util.LinkedList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eapli.framework.actions.menu.Menu;
import shodrone.core.presentation.console.Colorize;
import shodrone.customerapp.CustomerAppCli;

public class NotificationsMenu extends Menu {

	private static final String MENU_TITLE = "Notifications (";

	private static final int EXIT_OPTION = 0;

	public NotificationsMenu(int notificationCount) {
		super(MENU_TITLE + Colorize.toGreen(String.valueOf(notificationCount)) + ") >");
		buildNotificationsMenu();
	}

	private void buildNotificationsMenu() {
		LinkedList<String> notifications = CustomerAppCli.getProposals();
		ObjectMapper objectMapper = new ObjectMapper();

		for (int i = 0; i < notifications.size(); i++) {
			String notificationJson = notifications.get(i);
			String notifMessage = "New show proposal to accept/reject with ID: ";
			String proposalId = "";
			try {
				JsonNode node = objectMapper.readTree(notificationJson);
				if (node.has("showProposalId")) {
					proposalId = node.get("showProposalId").asText();
					notifMessage += proposalId;
				}else{
					notifMessage += "No ID found in notification";
				}
			} catch (Exception e) {
				notifMessage += "Error parsing notification";
			}
			final Menu VIEW_SHOW_PROPOSAL_MENU = new ViewShowProposal(Integer.parseInt(proposalId));
			addSubMenu(i+1, VIEW_SHOW_PROPOSAL_MENU);
		}
		addItem(EXIT_OPTION, "Return ", () -> true);
	}
}
