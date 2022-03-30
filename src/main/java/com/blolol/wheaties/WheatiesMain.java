package com.blolol.wheaties;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.SASLCapHandler;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.Map;

public class WheatiesMain {
    public static final String LOGGER_NAME = "wheaties-brain-java";
    public static final String VERSION = "1.0.0";

    public static void main(String[] args) throws IOException, IrcException {
        Map<String, String> env = System.getenv();
        Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

        WheatiesConfiguration wheatiesConfig = new WheatiesConfiguration(env, logger);
        Configuration botConfig = buildBotConfiguration(wheatiesConfig);
        PircBotX bot = new PircBotX(botConfig);

        logger.info("Starting wheaties-brain-java {}", VERSION);
        bot.startBot();
    }

    private static Configuration buildBotConfiguration(WheatiesConfiguration wheatiesConfig) {
        Configuration.Builder botConfig = new Configuration.Builder();

        configureIdentity(botConfig, wheatiesConfig);
        configureConnection(botConfig, wheatiesConfig);
        configureChannels(botConfig, wheatiesConfig);
        configureListeners(botConfig, wheatiesConfig);

        return botConfig.buildConfiguration();
    }

    private static void configureChannels(Configuration.Builder botConfig, WheatiesConfiguration wheatiesConfig) {
        botConfig.addAutoJoinChannels(wheatiesConfig.channels());
    }

    private static void configureConnection(Configuration.Builder botConfig, WheatiesConfiguration wheatiesConfig) {
        botConfig.addServer(wheatiesConfig.server(), wheatiesConfig.port());

        if (wheatiesConfig.useSasl()) {
            botConfig.addCapHandler(new SASLCapHandler(wheatiesConfig.user(), wheatiesConfig.pass()));
        } else if (wheatiesConfig.hasPass()) {
            botConfig.setServerPassword(wheatiesConfig.pass());
        }

        if (wheatiesConfig.useTls())
            botConfig.setSocketFactory(SSLSocketFactory.getDefault());
    }

    private static void configureIdentity(Configuration.Builder botConfig, WheatiesConfiguration wheatiesConfig) {
        botConfig
            .setName(wheatiesConfig.nick())
            .setLogin(wheatiesConfig.user())
            .setRealName(wheatiesConfig.realName())
            .setVersion("wheaties-brain-java " + VERSION);
    }

    private static void configureListeners(Configuration.Builder botConfig, WheatiesConfiguration wheatiesConfig) {
        ConversationListener conversationListener = new ConversationListener(wheatiesConfig);
        botConfig.addListener(conversationListener);
    }
}
