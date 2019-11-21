package io.github.arkobat.softwarebot.listeners;

import io.github.arkobat.softwarebot.Settings;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class FreeVoiceChannels extends ListenerAdapter {

    private void onJoin(VoiceChannel channelEntered) {
        Category cat = channelEntered.getParent();
        if (cat == null || cat.getIdLong() != Settings.SNAK_CATEGORY_ID) {
            return;
        }
        if (cat.getVoiceChannels().stream().noneMatch(t -> t.getMembers().size() == 0)) {
            final int[] highest = {1};
            cat.getVoiceChannels().forEach(c -> {
                try {
                    int i = Integer.parseInt(c.getName().substring(c.getName().length() - 1));
                    highest[0] = Math.max(i, highest[0]);
                } catch (IllegalArgumentException ignored) {
                }
            });
            cat.createVoiceChannel("Hygge #" + (highest[0] + 1)).queue();
        }
    }

    private void onLeave(VoiceChannel channelLeft) {
        if (channelLeft.getIdLong() == Settings.HYGGE1_CHANNEL_ID) {
            return;
        }
        Category cat = channelLeft.getParent();
        if (cat == null || cat.getIdLong() != Settings.SNAK_CATEGORY_ID) {
            return;
        }
        if (cat.getVoiceChannels().stream().filter(v -> v.getMembers().size() == 0).count() < 2) {
            return;
        }
        if (channelLeft.getMembers().size() == 0) {
            channelLeft.delete().queue();
        }
    }

    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        try {
            onJoin(e.getChannelJoined());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        try {
            onLeave(e.getChannelLeft());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        try {
            onLeave(e.getChannelLeft());
            onJoin(e.getChannelJoined());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}