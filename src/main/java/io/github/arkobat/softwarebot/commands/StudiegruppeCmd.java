package io.github.arkobat.softwarebot.commands;

import io.github.arkobat.softwarebot.Command;
import io.github.arkobat.softwarebot.Settings;
import io.github.arkobat.softwarebot.utils.NumberUtils;
import io.github.arkobat.softwarebot.utils.Utils;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Set;

public class StudiegruppeCmd extends Command {
    public StudiegruppeCmd(String command, Set<String> aliases) {
        super(command, aliases);
    }

    @Override
    public void execute(MessageReceivedEvent e) {
        if (e.getChannel().getIdLong() != Settings.BOT_SPAM_CHANNEL_ID) {
            return;
        }
        String[] args = Utils.getArgs(e.getMessage());
        if (args.length != 3) {
            e.getChannel().sendMessage("Forkert syntax. Brug `!gruppe <linje> <nr>`").queue();
            return;
        }

        String education = args[1];

        if (!education.equalsIgnoreCase("SE") && !education.equalsIgnoreCase("ST")) {
            e.getChannel().sendMessage("throw new IllegalArgumentException(\"Ukendt linje. Vælg mellem SE og ST!\");").queue();
            return;
        }

        Integer group = NumberUtils.stringToInt(args[2]);
        if (group == null || group < 1 || group > 30) {
            e.getChannel().sendMessage("throw new IllegalArgumentException(\"Dit gruppenummer skal være et tal mellem `1` og `30`!\");").queue();
            return;
        }

        Category cat = e.getGuild().getCategoryById(Settings.STUDIEGRUPPER_CATEGORY_ID);
        if (cat == null) {
            e.getChannel().sendMessage("Kunne ikke finde studierummene :(").queue();
            return;
        }

        cat.getTextChannels().forEach(c -> {
            if (c.getPermissionOverride(e.getMember()) != null) {
                c.getPermissionOverride(e.getMember()).delete().queue();
            }
        });

        TextChannel groupChannel = cat.getTextChannels().stream().filter(c -> c.getName().equalsIgnoreCase("gruppe-" + education + "-" + group)).findFirst().orElse(null);
        if (groupChannel == null) {
            cat.createTextChannel("gruppe-" + education + "-" + group).queue(c -> {
                c.putPermissionOverride(e.getMember()).setAllow(1024L).queue();
                inform(e.getMember(), (TextChannel) c, e.getChannel());
            });
        } else {
            groupChannel.putPermissionOverride(e.getMember()).setAllow(1024L).queue();
            inform(e.getMember(), groupChannel, e.getChannel());
        }

    }

    private void inform(Member member, TextChannel group, MessageChannel messageChannel) {
        messageChannel.sendMessage(member.getAsMention() + ": Du er nu blevet tilføjet til " + group.getAsMention()).queue();
    }
}
