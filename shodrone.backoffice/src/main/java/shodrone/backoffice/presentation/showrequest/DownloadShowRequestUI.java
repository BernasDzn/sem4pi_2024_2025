package shodrone.backoffice.presentation.showrequest;

import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.backoffice.presentation.showrequest.menu.FilterSelectionMenu;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.domain.showrequest.ShowRequestDescription;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class DownloadShowRequestUI extends AbstractUI {

    @Override
    protected boolean doShow() {
            FilterSelectionMenu selectionMenu = new FilterSelectionMenu();
            selectionMenu.withFilter(false);
            ShowRequest selectedShowRequest = selectionMenu.selectShow();
            if (selectedShowRequest == null) {
                return false;
            }

            System.out.println("Downloading show request description...");

            ShowRequestDescription description = selectedShowRequest.getDescription();
            if (description == null) {
                ConsoleEvent.error("Failed to download show request description.");
                return false;
            }

            String fileName = selectedShowRequest.getCustomer().getCustomerName()+
                    "_ShowRequest_"+selectedShowRequest.getDate();
            fileName = fileName.replaceAll(" ", "_");
            fileName = fileName.replaceAll(":", "_");
            fileName = fileName.replaceAll("/", "_");

            FileDialog dialog = new FileDialog((Frame) null, "Save File", FileDialog.SAVE);
            dialog.setFile(fileName);
            dialog.setVisible(true);
            String filePath = dialog.getDirectory().replace('\\', '/');
            fileName = dialog.getFile();

            if (fileName.endsWith(".pdf")) {
                fileName = fileName.substring(0, fileName.length()-4);
            }

           try {
               File file = description.blobToFile(fileName, filePath);
               if (file == null) {
                   return false;
               }

               ConsoleEvent.success("Show request description downloaded successfully.");
           }catch (Exception e) {
               ConsoleEvent.error("Failed to download show request description.");
               return false;
           }

           return true;
    }

    @Override
    public String headline() {
        return "Download Show Request Description";
    }
}
