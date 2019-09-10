package io.github.arkobat.softwarebot.listeners;

import io.github.arkobat.softwarebot.Command;
import io.github.arkobat.softwarebot.commands.StudiegruppeCmd;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashSet;
import java.util.Set;

public class OnMessage extends ListenerAdapter {
    private Set<Command> commands = new HashSet<>();


    public OnMessage() {

        addCommand(new StudiegruppeCmd("gruppe", new HashSet<>()));
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            if (!(e.getChannel() instanceof TextChannel)) {
                return;
            }
            if (e.getAuthor().isBot()) {
                return;
            }
            executeCommand(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public Command getCommand(String gameTitle) {
        return this.commands.stream().filter(command -> command.getCommand().equalsIgnoreCase(gameTitle)).findFirst().get();
    }

    private void addCommand(Command cmd) {
        commands.add(cmd);
    }

    private boolean executeCommand(MessageReceivedEvent e) {
        if (commands.size() == 0) {
            return false;
        }
        for (Command cmd : commands) {
            if (cmd.isCommand(e.getMessage())) {
                cmd.execute(e);
                return true;
            }
        }
        return false;
    }
}