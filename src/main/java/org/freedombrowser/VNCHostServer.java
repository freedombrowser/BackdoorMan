package org.freedombrowser;

import java.io.File;
import java.net.URISyntaxException;
import java.io.FileWriter;
import java.io.IOException;

public class VNCHostServer {

    public static void main(String[] args) {
        Process process = null;
        try {
            // Get the directory of the JAR file
            String jarDirectory = getJarDirectory();

            // Specify the command to start the VNC server with the password and port
            String command = jarDirectory + "\\winvnc.exe";
            String command_args = "-run";

            System.out.println("[VNCHostServer] [Debug] VNC Command: " + command);
            // Use ProcessBuilder to start the VNC server
            ProcessBuilder processBuilder = new ProcessBuilder(command, command_args);

            // Set the working directory to the JAR file's directory
            processBuilder.directory(new File(jarDirectory));

            // Start the process
            process = processBuilder.start();

            // Wait for the process to complete (optional)
            int exitCode = process.waitFor();
            System.out.println("VNC Server process exited with code " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure that the external program is terminated before exiting
            if (process != null) {
                process.destroy();
            }
        }
    }

    private static String getJarDirectory() throws URISyntaxException {
        // Get the location of the JAR file
        String jarPath = VNCHostServer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        // Extract the directory from the JAR file path
        return new File(jarPath).getParent();
    }
}

