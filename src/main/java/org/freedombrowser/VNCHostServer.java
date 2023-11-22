package org.freedombrowser;

import java.io.File;
import java.net.URISyntaxException;
import java.io.FileWriter;
import java.io.IOException;

import static org.freedombrowser.MainProcess.getJarDirectory;

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

            // Processbuilder actually has aids wtf
            ProcessBuilder processBuilder = new ProcessBuilder(command, command_args);

            processBuilder.directory(new File(jarDirectory));

            process = processBuilder.start();

            // Wait for the process to complete (optional)
            int exitCode = process.waitFor();
            System.out.println("VNC Server process exited with code " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

