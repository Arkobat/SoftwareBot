package io.github.arkobat.softwarebot.commands;

import io.github.arkobat.softwarebot.Command;
import io.github.arkobat.softwarebot.Settings;
import io.github.arkobat.softwarebot.utils.NumberUtils;
import io.github.arkobat.softwarebot.utils.Utils;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Set;

public class StudiegruppeCmd extends Command {
    public StudiegruppeCmd(String command, Set<String> aliases) {
        super(command, aliases);
    }

    @Override
    public void execute(MessageReceivedEvent e) {
        String[] args = Utils.getArgs(e.getMessage());
        if (args.length != 2) {
            e.getChannel().sendMessage("Forkert syntax. Brug `!gruppe <nr>`").queue();
            return;
        }
        Integer group = NumberUtils.stringToInt(args[1]);
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

        TextChannel groupChannel = cat.getTextChannels().stream().filter(c -> c.getName().equalsIgnoreCase("gruppe-" + group)).findFirst().get();
        TextChannel teamChannel = cat.getTextChannels().stream().filter(c -> c.getName().equalsIgnoreCase("hold-" + getTeam(group))).findFirst().get();
        groupChannel.putPermissionOverride(e.getMember()).setAllow(1024L).queue();
        teamChannel.putPermissionOverride(e.getMember()).setAllow(1024L).queue();

        e.getChannel().sendMessage("Du er nu blevet tilføjet til " + groupChannel.getAsMention() + " og " + teamChannel.getAsMention()).queue();
    }

    private int getTeam(int group) {
        if (group >= 1 && group <= 5) {
            return 1;
        } else if (group >= 6 && group <= 10) {
            return 2;
        } else if (group >= 11 && group <= 15) {
            return 3;
        } else if (group >= 16 && group <= 20) {
            return 4;
        } else if (group >= 21 && group <= 25) {
            return 5;
        } else if (group >= 26 && group <= 30) {
            return 6;
        }
        return -1;
    }
}
