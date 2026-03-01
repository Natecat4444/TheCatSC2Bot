import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.*;
import com.github.ocraft.s2client.bot.S2Agent;

public class runCatLadder {
    public static void main(String[] args) {
        TheCatSC2Bot.Bot bot = new TheCatSC2Bot().getBot();

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .setTimeoutMS(300000) //5min
                .setRawAffectsSelection(true)
                .loadLadderSettings(args)
                .setStepSize(2)
                .setParticipants(S2Coordinator.createParticipant(Race.PROTOSS, bot))
                .connectToLadder()
                .joinGame();

        while (s2Coordinator.update()) {
        }

        s2Coordinator.quit();
    }
}
