package org.gsn.pandora.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对应配置中的一行技能，例如：
 * damage{a=5} @PlayersInRadius{r=5} ~onAttack ?chance{p=0.5}
 */
public class SkillLine {

    private final String mechanicName;
    private final Map<String, String> mechanicArgs;
    private final String targeterName;
    private final Map<String, String> targeterArgs;
    private final TriggerType triggerType;
    private final List<ConditionSpec> conditions;

    public SkillLine(String mechanicName,
                     Map<String, String> mechanicArgs,
                     String targeterName,
                     Map<String, String> targeterArgs,
                     TriggerType triggerType,
                     List<ConditionSpec> conditions) {
        this.mechanicName = mechanicName;
        this.mechanicArgs = mechanicArgs;
        this.targeterName = targeterName;
        this.targeterArgs = targeterArgs;
        this.triggerType = triggerType;
        this.conditions = conditions != null ? conditions : new ArrayList<>();
    }

    public String getMechanicName() {
        return mechanicName;
    }

    public Map<String, String> getMechanicArgs() {
        return mechanicArgs;
    }

    public String getTargeterName() {
        return targeterName;
    }

    public Map<String, String> getTargeterArgs() {
        return targeterArgs;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public List<ConditionSpec> getConditions() {
        return conditions;
    }

    public static class ConditionSpec {
        private final String name;
        private final Map<String, String> args;

        public ConditionSpec(String name, Map<String, String> args) {
            this.name = name;
            this.args = args;
        }

        public String getName() {
            return name;
        }

        public Map<String, String> getArgs() {
            return args;
        }
    }
}
