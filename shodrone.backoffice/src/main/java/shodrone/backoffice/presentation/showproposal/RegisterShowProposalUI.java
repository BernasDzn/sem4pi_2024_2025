package shodrone.backoffice.presentation.showproposal;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.backoffice.presentation.figure.FigurePrinter;
import shodrone.backoffice.presentation.showrequest.ShowRequestPrinter;
import shodrone.core.application.figure.ListFigureController;
import shodrone.core.application.showproposal.RegisterShowProposalController;
import shodrone.core.application.showrequest.ListShowRequestController;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.drone_model.DroneModel;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.showproposal.ShowProposalDescription;
import shodrone.core.domain.showproposal.ShowProposalDuration;
import shodrone.core.domain.showproposal.ShowProposalNumberOfDrones;
import shodrone.core.domain.showproposal.ShowProposalVideo;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.presentation.console.ConsoleEvent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegisterShowProposalUI extends AbstractUI {
    private final RegisterShowProposalController controller = new RegisterShowProposalController();
    private final ListShowRequestController listShowRequestController = new ListShowRequestController();
    private final ListFigureController listFigureController = new ListFigureController();

    @Override
    protected boolean doShow() {
        // Ensure that there's figures available before proceeding
        if (!listFigureController.allComissionedFigures().iterator().hasNext()) {
            ConsoleEvent.error("Need to add a figure first.");
            return false;
        }
        // Ensure that there's drone models available before proceeding
        if (!controller.getAllDroneModels().iterator().hasNext()) {
            ConsoleEvent.error("Need to add a drone model first.");
            return false;
        }

        ShowRequest showRequest = selectShowRequest();
        if (showRequest == null) {
            ConsoleEvent.error("Exiting proposal registration.");
            return false;
        }
        ShowProposalDescription description = inputDescription();
        ShowProposalDuration duration = inputDuration();
        ShowProposalNumberOfDrones numberOfDrones = inputNumberOfDrones();

        askFiguresAndDroneModelsType();

        ShowProposalVideo video = inputVideo();
        Place place = inputPlace();
        Date date = inputDate();

        controller.registerShowProposal(showRequest, description, duration, numberOfDrones, video, place, date);
        ConsoleEvent.success("Show proposal registered successfully!");
        return true;
    }

    private DroneModel askDroneModel() {
        boolean isValid = false;
        List<DroneModel> drone_models = new ArrayList<>();
        controller.getAllDroneModels().forEach(drone_models::add);
        DroneModel selectedDroneModel = null;
        while (!isValid) {
            try {
                SelectWidget<DroneModel> dm_selector = new SelectWidget<>("Select a drone model:",
                        drone_models);
                dm_selector.show();

                if (dm_selector.selectedElement() != null) {
                    isValid = true;
                    selectedDroneModel = dm_selector.selectedElement();
                } else {
                    ConsoleEvent.error("Need to select a drone model!");
                }
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return selectedDroneModel;
    }

    private ShowRequest selectShowRequest() {
        ShowRequest showRequest = null;
        try {
            final Iterable<ShowRequest> allShowRequests = listShowRequestController.allShowRequests();

            if (allShowRequests.iterator().hasNext()) {
                final SelectWidget<ShowRequest> selector = new SelectWidget<>("Select a show request:", allShowRequests,
                        new ShowRequestPrinter());
                selector.show();
                showRequest = selector.selectedElement();
                if (showRequest == null) {
                    String answer;
                    do {
                        answer = Console.readLine("Quit proposal registration? Yes-y No-n");
                        if (answer.equalsIgnoreCase("y")) {
                            throw new Exception("Cancel");
                        }
                        if (answer.equalsIgnoreCase("n")) {
                            selectShowRequest();
                        } else {
                            answer = "";
                        }
                    } while (answer.isEmpty());
                }
            } else {
                throw new Exception("No show requests available.");
            }
        } catch (Exception e) {
            ConsoleEvent.error(e.getMessage());
        }
        return showRequest;
    }

    private ShowProposalDescription inputDescription() {
        ShowProposalDescription description = null;
        File descriptionFile = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println("Select a description file");
                FileDialog dialog = new FileDialog((Frame) null, "Select a Description File", FileDialog.LOAD);
                dialog.setFile("*.pdf");
                dialog.setVisible(true);
                String file = dialog.getFile();
                dialog.dispose();
                descriptionFile = new File(dialog.getDirectory(), file);
                description = new ShowProposalDescription(descriptionFile);
                isValid = true;
                System.out.println(descriptionFile.getName());
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return description;
    }

    private ShowProposalNumberOfDrones inputNumberOfDrones() {
        ShowProposalNumberOfDrones numberOfDrones = null;
        int n = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                n = Console.readInteger("Number of drones");
                numberOfDrones = new ShowProposalNumberOfDrones(n);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return numberOfDrones;
    }

    private ShowProposalDuration inputDuration() {
        ShowProposalDuration duration = null;
        int d = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                d = Console.readInteger("Duration (Minutes)");
                duration = new ShowProposalDuration(d);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return duration;
    }

    private ShowProposalVideo inputVideo() {
        ShowProposalVideo video = null;
        String url = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println("Insert video URL");
                url = Console.readLine("Video URL");
                video = new ShowProposalVideo(url);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return video;
    }

    private Date inputDate() {
        String d = "";
        Date date = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                d = Console.readNonEmptyLine("Date (dd/mm/yyyy hh:mm)", "Date cannot be empty");
                date = new Date(d);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return date;
    }

    private Place inputPlace() {
        Place place = null;
        String coordinates = "";
        boolean isValid = false;
        while (!isValid) {
            try {
                coordinates = Console.readNonEmptyLine("Place (latitude, longitude)", "Coordinates cannot be empty");
                place = new Place(coordinates);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return place;
    }

    private void askFiguresAndDroneModelsType() {
        Map<Figure, List<String>> figuresDroneTypes = new LinkedHashMap<>();
        boolean isValid = false;
        int count = 0;
        final Iterable<Figure> allFigures = listFigureController.allComissionedFigures();
        final SelectWidget<Figure> selector = new SelectWidget<>("Select figures for show proposal:", allFigures,
                new FigurePrinter());
        Map<Integer, Figure> figures = new LinkedHashMap<>();

        while (!isValid) {
            try {
                selector.show();
                Figure selectedFigure = selector.selectedElement();
                if (selectedFigure == null) {
                    if (!figures.isEmpty())
                        isValid = true;
                    else
                        ConsoleEvent.error("Need to select at least one figure!");
                } else {

                    count++;
                    figures.put(count, selectedFigure);
                    controller.validateFigures(figures);

                    if (!figuresDroneTypes.containsKey(selectedFigure))
                        figuresDroneTypes.put(selectedFigure, selectedFigure.getDSLCode().getDrone_types());

                    ConsoleEvent.success("Figure " + selectedFigure.getFigureName() + " added to proposal!");

                }
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
                figures.remove(count);
                count--;
            }
        }
        controller.addFigures(figures);
        askDroneModelTypes(figuresDroneTypes);
    }

    private void askDroneModelTypes(Map<Figure, List<String>> figuresDroneTypes) {
        System.out.println("-Assign drone types to drone models-");
        Map<DroneModel, List<String>> droneModelTypes = new LinkedHashMap<>();
        for (Figure figure : figuresDroneTypes.keySet()) {
            for (String type : figuresDroneTypes.get(figure)) {
                System.out.println("Figure: " + figure.getFigureName() + " - Drone type: " + type);
                DroneModel selectedDroneModel = askDroneModel();
                if (!droneModelTypes.containsKey(selectedDroneModel))
                    droneModelTypes.put(selectedDroneModel, new ArrayList<>());
                droneModelTypes.get(selectedDroneModel).add(type);
            }

            try {
                for (DroneModel droneModel : droneModelTypes.keySet()) {
                    controller.addDroneModelType(droneModel, figure, droneModelTypes.get(droneModel));
                }
                droneModelTypes.clear();
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }

        ConsoleEvent.success("Drone model types validated successfully!");
    }

    @Override
    public String headline() {
        return "Register Show Request";
    }
}
