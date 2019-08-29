package io.github.arkobat.softwarebot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Set;

public abstract class Command {

    public Command(String command, Set<String> aliases) {
        this.command = command;
        this.aliases = aliases;
    }

    public abstract void execute(MessageReceivedEvent e);

    private String command;
    private Set<String> aliases;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public boolean isCommand(Message message) {
        String prefix = "!";
        String messageContent = message.getContentRaw().split(" ")[0];
        if (messageContent.startsWith(prefix)) {
            if (messageContent.equalsIgnoreCase(prefix + this.command)) {
                return true;
            }
            return this.aliases.contains(messageContent.toLowerCase().replaceFirst(prefix, ""));
        }
        return false;
    }
}