import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class TheCatSC2Bot {
    private static class Bot extends S2Agent {
        @Override
        public void onGameStart(){
            System.out.println("THE CATS OF THE VOID WILL CLAIM ALL");
        }

        @Override
        public void onStep(){
//            System.out.println(observation().getGameLoop());
            tryBuildPylon();
        }

        private boolean tryBuildPylon() {
            if(observation().getFoodUsed() <= observation().getFoodCap() - 2){
                return false;
            }

            if( observation().getFoodCap() == 200){
                return false;
            }

            return tryBuildStructure(Abilities.BUILD_PYLON, Units.PROTOSS_PROBE);
        }

        private boolean tryBuildStructure(Ability ability, UnitType unitType){
            if (!observation().getUnits(Alliance.SELF, doesBuildWith(ability)).isEmpty()){
                return false;
            }

            Optional<UnitInPool> unitInPool = getRandomUnit(unitType);
            if(unitInPool.isPresent()){
                Unit unit = unitInPool.get().unit();
                actions().unitCommand(unit, ability, unit.getPosition().toPoint2d().add(Point2d.of(getRandomScalar(), getRandomScalar()).mul(15)), false);
                return true;
            }
            return false;
        }

        private Predicate<UnitInPool> doesBuildWith(Ability ability){
            return unitInPool -> unitInPool.unit().getOrders().stream().anyMatch(unitOrder -> ability.equals(unitOrder.getAbility()));
        }

        private Optional<UnitInPool> getRandomUnit(UnitType unitType){
            List<UnitInPool> units = observation().getUnits(Alliance.SELF, UnitInPool.isUnit(unitType));
            return units.isEmpty() ? Optional.empty() : Optional.of(units.get((ThreadLocalRandom.current().nextInt(units.size()))));
        }

        @Override
        public void onUnitIdle(UnitInPool unitInPool){
            Unit unit = unitInPool.unit();
            switch ((Units) unit.getType()){
                case PROTOSS_NEXUS:
                    actions().unitCommand(unit, Abilities.TRAIN_PROBE, false);
                    break;
                default:
                    break;
            }
        }

        private float getRandomScalar(){
            return ThreadLocalRandom.current().nextFloat() *2 -1;
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
        while (s2Coordinator.update()){

        }
        s2Coordinator.quit();
    }
}
