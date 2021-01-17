package com.rosspaffett.wheaties;

import org.jibble.jmegahal.JMegaHal;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversationListener extends ListenerAdapter {
    private static final Pattern MATTERBRIDGE_PREFIX_PATTERN = Pattern.compile("\\A\\s*(\\[.+?]\\s+)?<.+?>\\s*");
    private static final Pattern URL_PATTERN = Pattern.compile("\\b\\S+://\\S+\\b");
    public static final String URL_PLACEHOLDER = "%wheaties-brain-java:url%";
    private static final String URL_PLACEHOLDER_PATTERN = Pattern.quote(URL_PLACEHOLDER);

    private final JMegaHal brain;
    private final Pattern commandAssignmentPattern;
    private final Pattern nickPrefixPattern;
    private final Random random = new Random();
    private final List<String> urls = Collections.synchronizedList(new ArrayList<>());
    private final WheatiesConfiguration wheatiesConfig;
    private final WordChooser wordChooser;

    public ConversationListener(WheatiesConfiguration wheatiesConfig) {
        this.wheatiesConfig = wheatiesConfig;
        this.brain = new JMegaHal();
        this.wordChooser = new WordChooser(wheatiesConfig.nick());

        this.commandAssignmentPattern = Pattern.compile("\\A" + wheatiesConfig.nick() + ":\\s*\\S+\\s+is\\s+",
            Pattern.CASE_INSENSITIVE);
        this.nickPrefixPattern = Pattern.compile("\\A\\s*" + wheatiesConfig.nick() + ":",
            Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void onMessage(MessageEvent event) {
        if (shouldIgnoreMessage(event)) return;

        if (shouldRespondToMessage(event))
            respond(event);
        else
            learn(event);
    }

    private synchronized JMegaHal brain() {
        return brain;
    }

    private String chooseSeedWord(String sentence) {
        return wordChooser.interestingWordFrom(sentence);
    }

    private void learn(MessageEvent event) {
        String strippedMessage = stripMessage(event);
        brain().add(strippedMessage);
    }

    private Logger logger() {
        return wheatiesConfig.logger();
    }

    private boolean messageIsCommandAssignment(MessageEvent event) {
        return commandAssignmentPattern.matcher(event.getMessage()).find();
    }

    private boolean messageSentBySelf(MessageEvent event) {
        return event.getUser().getNick().equalsIgnoreCase(event.getBot().getNick());
    }

    private boolean messageStartsWithIgnoredPrefix(MessageEvent event) {
        return wheatiesConfig.ignorePrefixes().stream().anyMatch(event.getMessage()::startsWith);
    }

    private String replaceUrls(String message) {
        while (message.contains(URL_PLACEHOLDER))
            message = message.replaceFirst(URL_PLACEHOLDER_PATTERN, urls.get(random.nextInt(urls.size())));
        return message;
    }

    private void respond(MessageEvent event) {
        String seedWord = chooseSeedWord(stripMessage(event));
        String response;

        if (seedWord != null)
            response = brain().getSentence(seedWord);
        else
            response = brain().getSentence();

        if (response != null && response.length() > 0) {
            response = replaceUrls(response);
            event.respondWith(response);
        }
    }

    private boolean shouldIgnoreMessage(MessageEvent event) {
        return messageStartsWithIgnoredPrefix(event)
            || messageSentBySelf(event)
            || messageIsCommandAssignment(event);
    }

    private boolean shouldRespondToMessage(MessageEvent event) {
        return event.getMessage().toLowerCase().contains(event.getBot().getNick().toLowerCase());
    }

    private String stripMatterbridgePrefix(String message) {
        if (wheatiesConfig.hasMatterbridgeBotUser())
            return MATTERBRIDGE_PREFIX_PATTERN.matcher(message).replaceFirst("");
        return message;
    }

    private String stripMessage(MessageEvent event) {
        String strippedMessage = Colors.removeFormattingAndColors(event.getMessage());
        strippedMessage = stripMatterbridgePrefix(strippedMessage);
        strippedMessage = stripNickPrefix(strippedMessage);
        strippedMessage = stripUrls(strippedMessage);

        return strippedMessage;
    }

    private String stripNickPrefix(String message) {
        return nickPrefixPattern.matcher(message).replaceFirst("");
    }

    private String stripUrls(String message) {
        Matcher matcher = URL_PATTERN.matcher(message);
        matcher.results().forEach(match -> urls.add(message.substring(match.start(),  match.end())));
        return matcher.replaceAll(URL_PLACEHOLDER);
    }
}
