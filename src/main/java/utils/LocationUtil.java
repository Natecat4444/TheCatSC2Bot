package utils;

import com.github.ocraft.s2client.protocol.game.raw.StartRaw;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.spatial.Point2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import com.github.ocraft.s2client.bot.gateway.*;

import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;



public class LocationUtil {
    public Point2d getEnemyMain(){
//        ResponseGameInfo gameInfo = observation().getGameInfo();
//
//        Optional<StartRaw> startRaw = gameInfo.getStartRaw();
//        if (startRaw.isPresent()) {
//            Set<Point2d> startLocations = new HashSet<>(startRaw.get().getStartLocations());
//            startLocations.remove(observation().getStartLocation().toPoint2d());
//            if (startLocations.isEmpty()) return Optional.empty();
//            return Optional.of(new ArrayList<>(startLocations)
//                    .get(ThreadLocalRandom.current().nextInt(startLocations.size())));
//        } else {
//            return Optional.empty();
//        }
        return null;
    }

    public Set<Point2d> getOtherBases(){
        return null;
    }
}
