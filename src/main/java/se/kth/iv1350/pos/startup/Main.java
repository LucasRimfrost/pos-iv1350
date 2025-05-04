package se.kth.iv1350.pos.startup;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.RegistryCreator;
import se.kth.iv1350.pos.view.View;

/**
 * Contains the main method. Performs all startup of the application.
 */
public class Main {
    /**
     * The main method used to start the application.
     *
     * @param args The application does not take any command line parameters.
     */
    public static void main(String[] args) {
        RegistryCreator creator = new RegistryCreator();
        Controller controller = new Controller(creator);
        View view = new View(controller);

        view.runFakeExecution();
    }
}
