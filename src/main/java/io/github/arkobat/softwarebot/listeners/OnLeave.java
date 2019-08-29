package io.github.arkobat.softwarebot.listeners;

import io.github.arkobat.softwarebot.Settings;
import io.github.arkobat.softwarebot.utils.Random;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

public class OnLeave extends ListenerAdapter {

    public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
        if (e.getGuild().getIdLong() != Settings.SOFTWARE_GUILD_ID) {
            return;
        }
        e.getGuild().getTextChannelById(Settings.VELKOMST_CHANNEL_ID).sendMessage("System.out.println(\"" + leaveMessages.get(Random.getRandomNumber(leaveMessages.size()-1)).replaceAll("%user%", e.getMember().getAsMention()) + "\");").queue();
    }

    private List<String> leaveMessages = Arrays.asList(
            "%user% y u leave so soon :("
    );
}