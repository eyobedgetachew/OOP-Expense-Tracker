import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

/**
 * A utility class responsible for handling the saving of the summary report to a file.
 * This separates the file I/O logic from the main GUI class.
 */
public class SummaryFileHandler {

    /**
     * Shows a file chooser dialog and saves the provided report content to a file.
     *
     * @param parentComponent The parent JFrame for the dialog, allowing it to be centered.
     * @param reportContent The string content to be written to the file.
     * @return true if the file was saved successfully, false otherwise.
     */
    public boolean saveSummary(Component parentComponent, String reportContent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Expense Summary Report");
        fileChooser.setSelectedFile(new File("ExpenseSummaryReport.txt"));

        int userSelection = fileChooser.showSaveDialog(parentComponent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(reportContent);
                return true; // Return true on success
            } catch (IOException ex) {
                // If there's an error, show a dialog and return false
                DialogHelper.showError(parentComponent,
                        "Failed to save file: " + ex.getMessage(),
                        "File Save Error");
                return false;
            }
        }
        return false; // Return false if user canceled
    }
}
