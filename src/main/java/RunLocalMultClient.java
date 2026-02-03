import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.*;
import com.github.ocraft.s2client.bot.S2Agent;

public class RunLocalMultClient {
    public static void main(String[] args){
        TheCatSC2Bot.Bot bot = TheCatSC2Bot.getBot();
        MultiplayerOptions m = MultiplayerOptions.multiplayerSetup()
                .sharedPort(5000)
                .serverPort(PortSet.of(5001, 5002)).clientPorts(
                        PortSet.of(5003, 5004),
                        PortSet.of(5005, 5006))
                .build();

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setTimeoutMS(120000)
                .setMultiplayerOptions(m)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.PROTOSS, bot),
                        S2Coordinator.createParticipant(Race.PROTOSS))
                .launchStarcraft(4001)
                .joinGame();

        while (s2Coordinator.update()) {
        }

        s2Coordinator.quit();
    }
    }

