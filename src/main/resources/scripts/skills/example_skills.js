// 示例 JS 技能：对所有目标造成 2 点伤害，并在控制台输出一行日志。
//
// 对应配置示例：
//
// ExampleJsSkill:
//   Cooldown: 2
//   Skills:
//     - script{file=example_skills.js;func=exampleDamage} @playersinradius{r=5}
//
// Java 侧会把 ctx(技能上下文) 和 targets(List<LivingEntity>) 绑定到全局变量。

function exampleDamage() {
    if (typeof targets === 'undefined' || targets == null) {
        return false;
    }
    var console = ctx.getPlugin().getLogger();
    console.info('[Pandora JS] exampleDamage, targets=' + targets.size());

    var it = targets.iterator();
    while (it.hasNext()) {
        var t = it.next();
        t.damage(2.0);
    }
    return true;
}
