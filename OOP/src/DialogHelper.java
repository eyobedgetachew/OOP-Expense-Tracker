
import java.awt.Component;
import javax.swing.JOptionPane; 


class DialogHelper {

    /**
     * Shows an informational message dialog.
     * @param parentComponent The component to parent the dialog to (can be null for default).
     * @param message The message to display.
     * @param title The title of the dialog.
     */
    public static void showInfo(Component parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error message dialog.
     * @param parentComponent The component to parent the dialog to.
     * @param message The error message to display.
     * @param title The title of the dialog.
     */
    public static void showError(Component parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a warning message dialog.
     * @param parentComponent The component to parent the dialog to.
     * @param message The warning message to display.
     * @param title The title of the dialog.
     */
    public static void showWarning(Component parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a confirmation dialog.
     * @param parentComponent The component to parent the dialog to.
     * @param message The confirmation message.
     * @param title The title of the dialog.
     * @return JOptionPane.YES_OPTION if Yes is selected, otherwise JOptionPane.NO_OPTION or JOptionPane.CANCEL_OPTION.
     */
    public static int showConfirm(Component parentComponent, String message, String title) {
        return JOptionPane.showConfirmDialog(parentComponent, message, title, JOptionPane.YES_NO_OPTION);
    }
}
