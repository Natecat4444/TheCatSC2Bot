import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.Race;

public class TheCatSC2Bot {
    private static class Bot extends S2Agent {
        @Override
        public void onGameStart(){
            System.out.println("THE CATS OF THE VOID WILL CLAIM ALL");
        }
    }
    public static void main(String[] args) {
        Bot bot = new Bot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.PROTOSS, bot),
                        S2Coordinator.createComputer(Race.ZERG, Difficulty.VERY_EASY))
                .launchStarcraft()
                .startGame(BattlenetMap.of("Cloud Kingdom LE"));
        s2Coordinator.quit();
    }
}
