package io.github.arkobat.softwarebot.casino;

import com.google.common.primitives.Chars;
import io.github.arkobat.softwarebot.Settings;
import io.github.arkobat.softwarebot.utils.TimeConverter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

public class ScrambleSystem extends ListenerAdapter {

    private final SecureRandom random = new SecureRandom();

    public ScrambleSystem(Guild guild) {
        TextChannel channel = guild.getTextChannelById(Settings.BOT_SPAM_CHANNEL_ID);
        if (channel == null) {
            return;
        }
        word = getRandomWord();
        newWord(channel);
        streak = 0;
    }

    private ScramblePack word;
    private String scrambledWord;
    private Message message;
    private long lastGuesser;
    private int streak;
    private int clues;
    private OffsetDateTime offsetDateTime;

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getChannel().getIdLong() != Settings.BOT_SPAM_CHANNEL_ID) {
            return;
        }
        if (e.getAuthor().getIdLong() == 307541747620118538L) {
            e.getMessage().delete().queue();
            return;
        }
        if (e.getMessage().getContentRaw().equalsIgnoreCase("!hint")) {
            getClue(e.getTextChannel());
        }
        if (word == null || !e.getMessage().getContentRaw().equalsIgnoreCase(word.toString())) {
            return;
        }
        message.delete().queue();
        if (e.getAuthor().getIdLong() != lastGuesser) {
            streak = 0;
        }
        lastGuesser = e.getAuthor().getIdLong();
        streak++;
        e.getChannel().sendMessage(new EmbedBuilder().setTitle("**Korrekt gæt!**").setDescription("Ordet var `" + word + "`").setFooter("Gættet af " + e.getMember().getEffectiveName() + " på " + TimeConverter.timeToString((int) (e.getMessage().getCreationTime().toInstant().toEpochMilli() - message.getCreationTime().toInstant().toEpochMilli()) / 1000) + (streak < 2 ? "" : " • \uD83D\uDD25 x" + streak + " streak"), e.getAuthor().getAvatarUrl()).setColor(Color.GREEN).build()).queue();
        newWord(e.getTextChannel());
    }

    private void newWord(TextChannel channel) {
        word = getRandomWord();
        scrambledWord = toEmote(scrambleWord(word.toString()));
        channel.sendMessage(new EmbedBuilder().setTitle("__**Gæt ordet**__").setDescription(scrambledWord).setColor(new Color(50, 125, 242)).build()).queue(m -> {
            message = m;
            message.addReaction("⏭").queue();
            offsetDateTime = message.getCreationTime();
            clues = 0;
        });
        setTopic(channel, 0);
    }

    private void setTopic(TextChannel channel, int skips) {
        channel.getManager().setTopic("Gæt: " + scrambledWord + (skips == 0 ? "" : " (Skips " + skips + "/5)")).queue();
    }

    private ScramblePack getRandomWord() {
        Class clazz = ScramblePack.class;
        int x = random.nextInt(clazz.getEnumConstants().length);
        return ScramblePack.valueOf(clazz.getEnumConstants()[x].toString());
    }

    //private String getRandomWord() {
    //return ScramblePack.words.get(Utilities.getRandomNumber(0, ScramblePack.words.size() - 1));
    //}

    private String scrambleWord(String word) {
        java.util.List<Character> chars = Chars.asList(word.toCharArray());
        while (new String(Chars.toArray(chars)).equalsIgnoreCase(word)) {
            Collections.shuffle(chars);
        }
        return new String(Chars.toArray(chars));
    }

    private String toEmote(String word) {
        List<Character> chars = Chars.asList(word.toLowerCase().toCharArray());
        StringBuilder sb = new StringBuilder();
        chars.forEach(c -> sb.append(":regional_indicator_").append(c).append(": ").append("\u200B"));
        return sb.toString();
    }

    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.getUser().isBot()) {
            return;
        }
        if (e.getChannel().getIdLong() != Settings.BOT_SPAM_CHANNEL_ID) {
            return;
        }
        if (e.getMessageIdLong() != message.getIdLong()) {
            return;
        }
        if (!e.getReactionEmote().getName().equalsIgnoreCase("⏭")) {
            return;
        }
        e.getReaction().getUsers().queue(u -> {
            if (u.size() > 5) {
                newWord(e.getTextChannel());
                streak = 0;
                e.getTextChannel().getMessageById(e.getMessageIdLong()).queue(m -> m.clearReactions().queue());
            } else {
                setTopic(e.getTextChannel(), u.size() - 1);
            }
        });
    }

    private void getClue(TextChannel channel) {
        String header = "__**Hint**__";
        if (OffsetDateTime.now().isBefore(offsetDateTime.plusSeconds(150))) {
            channel.sendMessage(new EmbedBuilder().setTitle(header).setDescription("Du kan først få et hint om " + TimeConverter.timeToString((int) (offsetDateTime.plusSeconds(150).toEpochSecond() - OffsetDateTime.now().toEpochSecond()))).setColor(new Color(50, 125, 242)).build()).queue();
            return;
        }
        offsetDateTime = OffsetDateTime.now();
        if (clues == 0) {
            channel.sendMessage(new EmbedBuilder().setTitle(header).setDescription("Kategori = __**" + word.getCategory() + "**__").setColor(new Color(50, 125, 242)).build()).queue();
        } else {
            StringBuilder sb = new StringBuilder();
            char[] chars = word.toString().toCharArray();
            int i = 0;
            for (char c : chars) {
                if (i < clues) {
                    sb.append(":regional_indicator_").append(String.valueOf(c).toLowerCase()).append(": ");
                } else {
                    sb.append(":asterisk: ");
                }
                i++;
            }
            channel.sendMessage(new EmbedBuilder().setTitle(header).setDescription(sb.toString()).setColor(new Color(50, 125, 242)).build()).queue();
        }
        clues++;
    }
}