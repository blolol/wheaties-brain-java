# wheaties-brain-java

wheaties-brain-java combines [PircBotX](https://github.com/pircbotx/pircbotx) and [JMegaHal](http://www.jibble.org/jmegahal/) into a simple conversational IRC bot.

The bot will respond when someone mentions its nick, and "learns" from all others. It attempts to identify "interesting" words in order to generate a relevant response, skipping common English words like articles and prepositions.

wheaties-brain-java ignores messages that begin with any of the characters in `IGNORE_PREFIXES` (useful if you have other IRC bots whose interface involves a command prefix). It also ignores messages in the form "nick: x is y" to avoid conflicts with the [Wheaties programmable chat bot](https://github.com/blolol/wheaties).

If you're running [Matterbridge](https://github.com/42wim/matterbridge), you might have an IRC bot in your channel that sends messages of the form "[source] &lt;nick&gt; message" or "&lt;nick&gt; message". wheaties-brain-java recognizes those, and only learns from the chat message itself.

## Usage

The easiest way to run wheaties-brain-java is using the [wheaties-brain-java](https://hub.docker.com/repository/docker/blolol/wheaties-brain-java) Docker image:

```sh
docker run -e IRC_NICK=Wheaties -e IRC_SERVER=irc.example.com wheaties-brain-java:latest
```

wheaties-brain-java can be configured using the following environment variables.

| Name | Required? | Description |
|------|-----------|-------------|
| `IGNORE_PREFIXES` | Optional | Ignore messages starting with any character in this environment variable's value. |
| `IRC_CHANNELS` | **Required** | Comma-separated list of IRC channels to join. |
| `IRC_NICK` | **Required** | IRC nickname. |
| `IRC_PASS` | Optional | IRC server password. |
| `IRC_PORT` | **Required** | IRC server port. |
| `IRC_REALNAME` | **Required** | IRC real name. |
| `IRC_SERVER` | **Required** | IRC server address. |
| `IRC_TLS` | Optional | Set to `true` to connect using TLS. |
| `IRC_USER` | **Required** | IRC server username. |
| `MATTERBRIDGE_USER` | Optional | [Matterbridge](https://github.com/42wim/matterbridge) bot username. If this environment variable is set, wheaties-brain-java will strip any `[source] <nick>` prefix from the message before "learning" it. |

## Building

To build the project, run `./gradlew build`. To build a standalone "fat" JAR that includes all dependencies, use `./gradlew shadowJar`. To build a Docker image tagged with the project's current version, run `./gradlew docker`.

## License

In order to be compatible with [PircBotX](https://github.com/pircbotx/pircbotx), wheaties-brain-java is licensed under the GNU General Public License v3.
