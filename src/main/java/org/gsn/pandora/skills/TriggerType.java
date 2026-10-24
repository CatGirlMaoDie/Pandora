package org.gsn.pandora.skills;

import java.util.Locale;

public enum TriggerType {
    ON_SPAWN,
    ON_ATTACK,
    ON_HURT,
    ON_DEATH,
    ON_KILL,
    ON_COMBAT,
    ON_TIMER,
    ON_SIGNAL;

    public static TriggerType fromConfigString(String raw) {
        if (raw == null) {
            return null;
        }
        String s = raw.trim();
        if (s.startsWith("~")) {
            s = s.substring(1);
        }
        s = s.toLowerCase(Locale.ROOT);
        switch (s) {
            case "onspawn":
            case "spawn":
                return ON_SPAWN;
            case "onattack":
            case "attack":
                return ON_ATTACK;
            case "onhurt":
            case "hurt":
                return ON_HURT;
            case "ondeath":
            case "death":
                return ON_DEATH;
            case "onkill":
            case "kill":
                return ON_KILL;
            case "oncombat":
            case "combat":
                return ON_COMBAT;
            case "ontimer":
            case "timer":
                return ON_TIMER;
            case "onsignal":
            case "signal":
                return ON_SIGNAL;
            default:
                return null;
        }
    }
}
