package io.github.arkobat.softwarebot.listeners;

import io.github.arkobat.softwarebot.Settings;
import io.github.arkobat.softwarebot.casino.ScrambleSystem;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class OnReady extends ListenerAdapter {

    public void onGuildReady(GuildReadyEvent e) {
        e.getJDA().addEventListener(new ScrambleSystem(e.getJDA().getGuildById(Settings.SOFTWARE_GUILD_ID)));
        e.getJDA().getPresence().setGame(Game.playing("Java.jar"));
    }
}