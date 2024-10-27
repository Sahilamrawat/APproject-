package com.angrybird.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


/** Launches the desktop (LWJGL3) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle(AngryBird.NAME + " v" + AngryBird.VERSION);
        cfg.setWindowedMode(1920, 1080);  // Set window size
        cfg.useVsync(true);  // Enable VSync

        new Lwjgl3Application(new AngryBird(), cfg);  // Launch with LWJGL3
    }
}