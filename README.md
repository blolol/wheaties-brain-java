# wheaties-brain-java

wheaties-brain-java combines [PircBotX](https://github.com/pircbotx/pircbotx) and [JMegaHal](http://www.jibble.org/jmegahal/) into a simple conversational IRC bot.

The bot will respond when someone mentions its nick, and "learns" from all others. It attempts to identify "interesting" words in order to generate a relevant response, skipping common English words like articles and prepositions.

Each IRC channel gets its own "brain", so that conversations from one channel don't inadvertently leak into another.

wheaties-brain-java ignores messages that begin with any of the characters in `IGNORE_PREFIXES` (useful if you have other IRC bots whose interface involves a command prefix). It also ignores messages in the form "nick: x is y" to avoid conflicts with the [Wheaties programmable chat bot](https://github.com/blolol/wheaties).

If you're running [Matterbridge](https://github.com/42wim/matterbridge), you might have an IRC bot in your channel that sends messages of the form "[source] &lt;nick&gt; message" or "&lt;nick&gt; message". wheaties-brain-java recognizes those, and only learns from the chat message itself.

## Usage

The easiest way to run wheaties-brain-java is using the [blolol/wheaties-brain-java](https://hub.docker.com/repository/docker/blolol/wheaties-brain-java) Docker image:

```sh
docker run -e IRC_NICK=Wheaties -e IRC_SERVER=irc.example.com blolol/wheaties-brain-java:latest
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
| `IRC_SASL` | Optional | Set to `true` to authenticate using SASL using `IRC_USER` and `IRC_PASS`. |
| `IRC_SERVER` | **Required** | IRC server address. |
| `IRC_TLS` | Optional | Set to `true` to connect using TLS. |
| `IRC_USER` | **Required** | IRC server username. |
| `MATTERBRIDGE_USER` | Optional | [Matterbridge](https://github.com/42wim/matterbridge) bot username. If this environment variable is set, wheaties-brain-java will strip any `[source] <nick>` prefix from the message before "learning" it. |

### Running in development

You can use Docker Compose to run wheaties-brain-java in your development environment. First, copy `.env.example` to `.env`, and customize it to suit your needs.

```sh
cp .env.example .env
```

The `BUILD_VERSION` environment variable is used in `Dockerfile` to copy the correct wheaties-brain-java jar from `build/libs` into the resulting Docker image. If `BUILD_VERSION` is set to "1.0.0", for example, then Docker will try to copy `build/libs/wheaties-brain-java-1.0.0.jar`. See ["Building"](#building) for instructions on how to build the requisite jar.

Finally, use `docker-compose up` to build the Docker image and start the container.

```sh
docker-compose up
```

## Building

To build the project, run `./gradlew build`. To build a standalone "fat" JAR that includes all dependencies, use `./gradlew shadowJar`.

### Docker images

Gradle tasks are included for easy Docker builds.

```sh
# Build a blolol/wheaties-brain-java:VERSION image
./gradlew docker

# Tag the blolol/wheaties-brain-java:VERSION image as blolol/wheaties-brain-java:latest
./gradlew dockerTagLatest

# Push both images to Docker Hub
./gradlew dockerPush
./gradlew dockerPushLatest
```

## Releasing

To release a new version of wheaties-brain-java, change the version number in:

* The `WheatiesMain.VERSION` constant in [WheatiesMain.java](src/main/java/com/blolol/wheaties/WheatiesMain.java)
* The `version` project attribute in [build.gradle](build.gradle)

Create a Git commit containing those two changes and a message like "wheaties-brain-java 1.0.0". Then, tag the commit with a name like `v1.0.0`.

Pushing a Git tag named `v*` (such as `v1.0.0`) triggers [a GitHub Action](.github/workflows/release.yml) that builds the Docker image, tags it as `blolol/wheaties-brain-java:v1.0.0` and `blolol/wheaties-brain-java:latest`, and pushes it to [blolol/wheaties-brain-java](https://hub.docker.com/r/blolol/wheaties-brain-java) on Docker Hub.

## License

In order to be compatible with [PircBotX](https://github.com/pircbotx/pircbotx), wheaties-brain-java is licensed under the GNU General Public License v3.
