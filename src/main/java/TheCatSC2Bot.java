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

        private final int zealotsMax = 25;
        private final int stalkersMax = 20;
        private final int immortalsMax = 5;
        private boolean warpgate = false;

        @Override
        public void onGameStart(){
            System.out.println("THE CATS OF THE VOID WILL CLAIM ALL");
        }

        @Override
        public void onStep(){
//            System.out.println(observation().getGameLoop());
            tryBuildPylon();
            tryBuildGateway();
            tryBuildCybercore();
            tryBuildForge();
            tryBuildTwilightCouncil();
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

        private boolean tryBuildGateway(){
            if (countUnitType(Units.PROTOSS_PYLON) < 1){
                return false;
            }

            if(countUnitType(Units.PROTOSS_GATEWAY) > 3){
                return false;
            }

            return tryBuildStructure(Abilities.BUILD_GATEWAY, Units.PROTOSS_PROBE);
        }

        private boolean tryBuildCybercore(){
            if (countUnitType(Units.PROTOSS_GATEWAY) + countUnitType(Units.PROTOSS_WARP_GATE) < 1){
                return false;
            }
            if(countUnitType(Units.PROTOSS_CYBERNETICS_CORE) > 0){
                return false;
            }

            return tryBuildStructure(Abilities.BUILD_CYBERNETICS_CORE, Units.PROTOSS_PROBE);
        }

        private boolean tryBuildForge(){
            if(countUnitType(Units.PROTOSS_FORGE) >= 1){
                return false;
            }

            return tryBuildStructure(Abilities.BUILD_FORGE, Units.PROTOSS_PROBE);
        }

        private boolean tryBuildTwilightCouncil(){
            if(countUnitType(Units.PROTOSS_CYBERNETICS_CORE) > 0){
                return false;
            }

            if(countUnitType(Units.PROTOSS_TWILIGHT_COUNCIL) > 0){
                return false;
            }

            return tryBuildStructure(Abilities.BUILD_TWILIGHT_COUNCIL, Units.PROTOSS_PROBE);
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
                case PROTOSS_PROBE:
                    findNearestMineralPatch(unit.getPosition().toPoint2d()).ifPresent(mineralPath -> actions().unitCommand(unit, Abilities.SMART, mineralPath, false));
                    break;
                case PROTOSS_ASSIMILATOR:
                    break;
                case PROTOSS_CYBERNETICS_CORE:
                    if(!warpgate){
                        actions().unitCommand(unit, Abilities.RESEARCH_WARP_GATE, false);
                    }
                    break;
                case PROTOSS_WARP_GATE:
                    warpUnit(unit);
                    break;
                case PROTOSS_GATEWAY:
                    if(warpgate){
                        actions().unitCommand(unit, Abilities.MORPH_WARP_GATE, false);
                    }
                    else{
                        trainUnit(unit);
                    }
                default:
                    break;
            }
        }

        private void trainUnit(Unit unit){
            if(countUnitType(Units.PROTOSS_ZEALOT) < zealotsMax){
                actions().unitCommand(unit, Abilities.TRAIN_ZEALOT, false);
            }
            else if(countUnitType(Units.PROTOSS_STALKER) < stalkersMax){
                actions().unitCommand(unit, Abilities.TRAIN_STALKER, false);
            }
//            else if (countUnitType(Units.PROTOSS_IMMORTAL) < immortalsMax) {
//                actions().unitCommand(unit, Abilities.TRAIN_ZEALOT, false);
//            }
        }

        private void warpUnit(Unit unit){
            if(countUnitType(Units.PROTOSS_ZEALOT) < zealotsMax){
                //todo
            }
            else if(countUnitType(Units.PROTOSS_STALKER) < stalkersMax){
                //todo
            }
            else if (countUnitType(Units.PROTOSS_IMMORTAL) < immortalsMax) {
                //todo
            }
        }

        private Optional<Unit> findNearestMineralPatch(Point2d start){
            List<UnitInPool> units = observation().getUnits(Alliance.NEUTRAL);
            double distance = Double.MAX_VALUE;
            Unit target = null;
            for (UnitInPool unitInPool : units) {
                Unit unit = unitInPool.unit();
                if(unit.getType().equals(Units.NEUTRAL_MINERAL_FIELD)) {
                    double d = unit.getPosition().toPoint2d().distance(start);
                    if (d < distance) {
                        distance = d;
                        target = unit;
                    }
                }
            }
            return Optional.ofNullable(target);
        }

        private float getRandomScalar(){
            return ThreadLocalRandom.current().nextFloat() *2 -1;
        }

        private int countUnitType(Units unitType){
            return observation().getUnits(Alliance.SELF, UnitInPool.isUnit(unitType)).size();
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
