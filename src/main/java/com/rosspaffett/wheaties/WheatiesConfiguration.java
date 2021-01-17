package com.rosspaffett.wheaties;

import org.slf4j.Logger;

import java.util.*;

public class WheatiesConfiguration {
    private final Map<String, String> env;
    private final Set<String> ignorePrefixes = new HashSet<>();
    private final Logger logger;

    public WheatiesConfiguration(Map<String, String> env, Logger logger) {
        this.env = env;
        this.logger = logger;

        configureIgnorePrefixes();
    }

    public List<String> channels() {
        return Arrays.asList(env.get("IRC_CHANNELS").split(",\\s*"));
    }

    private void configureIgnorePrefixes() {
        if (hasIgnorePrefixes()) {
            char[] prefixes = env.get("IGNORE_PREFIXES").toCharArray();
            for (char prefix : prefixes) ignorePrefixes.add(String.valueOf(prefix));
        }
    }

    public boolean hasIgnorePrefixes() {
        return env.containsKey("IGNORE_PREFIXES");
    }

    public boolean hasMatterbridgeBotUser() {
        return env.containsKey("MATTERBRIDGE_USER");
    }

    public boolean hasPass() {
        return env.containsKey("IRC_PASS");
    }

    public Set<String> ignorePrefixes() {
        return ignorePrefixes;
    }

    public Logger logger() {
        return logger;
    }

    public String matterbridgeBotUser() {
        return env.get("MATTERBRIDGE_USER");
    }

    public String nick() {
        return env.get("IRC_NICK");
    }

    public String pass() {
        return env.get("IRC_PASS");
    }

    public int port() {
        return Integer.parseInt(env.get("IRC_PORT"));
    }

    public String realName() {
        return env.get("IRC_REALNAME");
    }

    public String server() {
        return env.get("IRC_SERVER");
    }

    public String user() {
        return env.get("IRC_USER");
    }

    public boolean useTls() {
        return env.containsKey("IRC_TLS");
    }
}
