// 示例 JS 条件：
// 只有当目标血量低于 50% 且与施法者距离小于 10 格时返回 true。
//
// 使用方式：
//   - damage{a=6} @target ?js{file=example_conditions.js;func=lowHpAndNear}

var Attribute = Java.type('org.bukkit.attribute.Attribute');

function lowHpAndNear() {
    if (typeof target === 'undefined' || target == null) {
        return false;
    }
    var maxAttr = target.getAttribute(Attribute.MAX_HEALTH);
    if (maxAttr == null) {
        return false;
    }
    var max = maxAttr.getValue();
    if (max <= 0) {
        return false;
    }
    var ratio = target.getHealth() / max;
    if (ratio > 0.5) {
        return false;
    }
    var caster = ctx.getCaster();
    if (caster == null) {
        return true;
    }
    var dist = caster.getLocation().distance(target.getLocation());
    return dist <= 10.0;
}
