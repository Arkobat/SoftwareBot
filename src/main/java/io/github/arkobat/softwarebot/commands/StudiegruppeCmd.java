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
        TextChannel teamChannel = cat.getTextChannels().stream().filter(c -> c.getName().equalsIgnoreCase("hold-" + education + "-" + getTeam(group))).findFirst().orElse(null);
        if (teamChannel == null) {
            cat.createTextChannel("hold-" + education + "-" + getTeam(group)).queue(c -> c.putPermissionOverride(e.getMember()).setAllow(1024L).queue());
        } else {
            teamChannel.putPermissionOverride(e.getMember()).setAllow(1024L).queue();
        }
        if (groupChannel == null) {
            cat.createTextChannel("gruppe-" + education + "-" + group).queue(c -> c.putPermissionOverride(e.getMember()).setAllow(1024L).queue());
        } else {
            groupChannel.putPermissionOverride(e.getMember()).setAllow(1024L).queue();
        }
        e.getChannel().sendMessage("Du er nu blevet tilføjet til team og gruppe kanalen. Find dem i bunden af Discorden").queue();

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
