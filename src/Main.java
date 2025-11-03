import ui.CLIInterface;

/**
 * Main Entry Point - Fake Resume Detector Application
 * Initializes and starts the CLI interface
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("[STARTUP] Initializing Fake Resume Detector...");
        
        try {
            CLIInterface cli = new CLIInterface();
            cli.start();
        } catch (Exception e) {
            System.out.println("[ERROR] Application failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
