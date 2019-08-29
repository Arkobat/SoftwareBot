package io.github.arkobat.softwarebot.listeners;

import io.github.arkobat.softwarebot.Settings;
import io.github.arkobat.softwarebot.utils.Random;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

public class OnJoin extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (e.getGuild().getIdLong() != Settings.SOFTWARE_GUILD_ID) {
            return;
        }
        e.getGuild().getTextChannelById(Settings.VELKOMST_CHANNEL_ID).sendMessage("System.out.println(\"" + joinMessages.get(Random.getRandomNumber(joinMessages.size()-1)).replaceAll("%user%", e.getMember().getAsMention()) + "\");").queue();
    }

    private List<String> joinMessages = Arrays.asList(
            "%user% just joined the server - glhf!",
            "%user% just joined. Everyone, look busy!",
            "%user% just joined. Can I get a heal?",
            "%user% joined your party.",
            "%user% joined. You must construct additional pylons.",
            "Welcome, %user%. We were expecting you ( ͡° ͜ʖ ͡°)",
            "Welcome, %user%. We hope you brought pizza.",
            "A wild %user% appeared.",
            "Swoooosh. %user% just landed.",
            "Brace yourselves. %user% just joined the server.",
            "%user% just joined. Hide your bananas.",
            "%user% just arrived. Seems OP - please nerf.",
            "%user% just slid into the server.",
            "A %user% has spawned in the server.",
            "Big %user% showed up!",
            "Where’s %user%? In the server!",
            "%user% hopped into the server. Kangaroo!!",
            "%user% just showed up. Hold my beer."
    );
}