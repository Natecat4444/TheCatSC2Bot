package constants;

import com.github.ocraft.s2client.protocol.data.Units;

import java.util.HashSet;
import java.util.Set;

public class MiningConstants {
    public static final Set<Units> open_geyser_types = new HashSet(){{
        add(Units.NEUTRAL_VESPENE_GEYSER);
        add(Units.NEUTRAL_SPACE_PLATFORM_GEYSER);
        add(Units.NEUTRAL_RICH_VESPENE_GEYSER);
        add(Units.NEUTRAL_SHAKURAS_VESPENE_GEYSER);
        add(Units.NEUTRAL_PROTOSS_VESPENE_GEYSER);
        add(Units.NEUTRAL_PURIFIER_VESPENE_GEYSER);
    }};

    public static final Set<Units> assimilators= new HashSet(){{
        add(Units.PROTOSS_ASSIMILATOR);
        add(Units.PROTOSS_ASSIMILATOR_RICH);
    }};
//        Set.of(Units.NEUTRAL_VESPENE_GEYSER, Units.NEUTRAL_SPACE_PLATFORM_GEYSER);
}
