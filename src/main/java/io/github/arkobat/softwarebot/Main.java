package io.github.arkobat.softwarebot;

import io.github.arkobat.softwarebot.casino.ScrambleSystem;
import io.github.arkobat.softwarebot.listeners.*;
import io.github.arkobat.softwarebot.utils.Random;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Main {

    private Main() {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
        //Log.out("Connecting to server...");
        try {
            new Random();
            new Config();
            new JDABuilder(Config.get(Config.Path.BOT_TOKEN))
                    .setGame(Game.playing("loading.jar"))
                    .setAutoReconnect(true)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListener(
                            new AutoRole(),
                            new OnJoin(),
                            new OnLeave(),
                            new OnMessage(),
                            new OnReady(),
                            new FreeVoiceChannels()
                    )
                    .build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
