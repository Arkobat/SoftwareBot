package io.github.arkobat.softwarebot.listeners;

import io.github.arkobat.softwarebot.Settings;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class AutoRole extends ListenerAdapter {
    public AutoRole() {
        softwareRoles.put(Settings.SOFTWARE_ENGINEERING_EMOTE_NAME, Settings.SOFTWARE_ENGINEERING_ROLE_ID);
        softwareRoles.put(Settings.SOFTWARETEKNOLOGI_EMOTE_NAME, Settings.SOFTWARETEKNOLOGI_ROLE_ID);

        fromRoles.put(Settings.JYLLAND_EMOTE_NAME, Settings.JYLLAND_ROLE_ID);
        fromRoles.put(Settings.FYN_ROLE_EMOTE_NAME, Settings.FYN_ROLE_ID);
        fromRoles.put(Settings.SJALLAND_EMOTE_NAME, Settings.SJALLAND_ROLE_ID);
        fromRoles.put(Settings.ANDETSTEDS_EMOTE_NAME, Settings.ANDETSTEDS_ROLE_ID);

        colorRoles.put(Settings.PINK_COLOR_EMOTE_NAME, Settings.PINK_COLOR_ROLE_ID);
        colorRoles.put(Settings.GREEN_COLOR_EMOTE_NAME, Settings.GREEN_COLOR_ROLE_ID);
        colorRoles.put(Settings.BLUE_COLOR_EMOTE_NAME, Settings.BLUE_COLOR_ROLE_ID);
        colorRoles.put(Settings.RED_COLOR_EMOTE_NAME, Settings.RED_COLOR_ROLE_ID);
        colorRoles.put(Settings.YELLOW_COLOR_EMOTE_NAME, Settings.YELLOW_COLOR_ROLE_ID);
    }

    private final Map<String, Long> softwareRoles = new HashMap<>();
    private final Map<String, Long> fromRoles = new HashMap<>();
    private final Map<String, Long> colorRoles = new HashMap<>();

    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.getChannel().getIdLong() != Settings.CHOOSE_ROLE_CHANNEL_ID) {
            return;
        }
        if (e.getUser().isBot()) {
            return;
        }
        switch (e.getMessageId()) {
            case Settings.CHOOSE_SOFTWARE_MESSAGE_ID:
                addRole(softwareRoles, e.getMember(), e.getReaction());
                break;
            case Settings.CHOOSE_FROM_MESSAGE_ID:
                addRole(fromRoles, e.getMember(), e.getReaction());
                break;
            case Settings.CHOOSE_COLOR_MESSAGE_ID:
                addRole(colorRoles, e.getMember(), e.getReaction());
                break;
        }
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        if (e.getChannel().getIdLong() != Settings.CHOOSE_ROLE_CHANNEL_ID) {
            return;
        }
        if (e.getUser().isBot()) {
            return;
        }
        switch (e.getMessageId()) {
            case Settings.CHOOSE_SOFTWARE_MESSAGE_ID:
                removeRole(softwareRoles, e.getMember(), e.getReaction());
                break;
            case Settings.CHOOSE_FROM_MESSAGE_ID:
                removeRole(fromRoles, e.getMember(), e.getReaction());
                break;
            case Settings.CHOOSE_COLOR_MESSAGE_ID:
                removeRole(colorRoles, e.getMember(), e.getReaction());
                break;
        }
    }





    private void addRole(Map<String, Long> reactionRoles, Member member, MessageReaction reaction) {
        String reactionName = reaction.getReactionEmote().getName();
        if (reactionRoles.keySet().stream().noneMatch(k -> k.equalsIgnoreCase(reactionName))) {
            reaction.removeReaction(member.getUser()).queue();
            return;
        }
        if (member.getRoles() != null && member.getRoles().stream().anyMatch(r -> reactionRoles.keySet().stream().anyMatch(l -> reactionRoles.get(l) == r.getIdLong()))) {
            reaction.removeReaction(member.getUser()).queue();
            return;
        }
        Role role = member.getGuild().getRoleById(reactionRoles.get(reactionName));
        if (role == null) {
            return;
        }
        reaction.getGuild().getController().addSingleRoleToMember(member, role).queue();
    }

    private void removeRole(Map<String, Long> reactionRoles, Member member, MessageReaction reaction) {
        String reactionName = reaction.getReactionEmote().getName();
        if (reactionRoles.keySet().stream().noneMatch(k -> k.equalsIgnoreCase(reactionName))) {
            return;
        }
        if (member.getRoles() == null) {
            return;
        }
        Role role = member.getGuild().getRoleById(reactionRoles.get(reactionName));
        if (role == null) {
            return;
        }
        reaction.getGuild().getController().removeSingleRoleFromMember(member, role).queue();
    }
}