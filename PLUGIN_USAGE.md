# Pandora 插件使用文档（适用于 Paper 1.21.x）

> 说明：Pandora 使用 **目录式配置结构**。`mobs.yml` / `skills.yml` / `spawners.yml` 是顶层索引文件，用来控制加载哪些子文件，而真正的自定义内容应放在对应的 **子目录** 中：`mobs/`、`skills/`、`spawners/`。这样可以把不同副本/模块拆分到多个文件，便于管理和扩展。

## 1. 插件简介

Pandora 是一个 **Paper 1.21** 插件，用来实现类似 MythicMobs 的 **自定义怪物系统**，目前主要包含：

- 自定义 Mob 类型（`plugins/Pandora/mobs/` 下的配置）
- 技能系统（`plugins/Pandora/skills/` 下的配置）
- 自定义刷怪点 / 刷怪规则（`plugins/Pandora/spawners/` 下的配置）
- 通过事件（生成、攻击、受伤、死亡、击杀）触发技能

> 顶层 `mobs.yml` / `skills.yml` / `spawners.yml` 只用于控制要加载哪些子文件列表，而**不要直接把自定义内容堆在这些文件里**。

Pandora 不包含自定义物品模块，但可以在掉落配置中使用原版物品（如 `minecraft:diamond`）。

---

## 2. 环境要求

- 服务器内核：**Paper 1.21.x**（例如 1.21.1、1.21.4 等）
- Java 版本：**Java 21**
- 有管理权限的服务器控制台/账号（用于执行 `/pandora` 指令）

插件 `paper-plugin.yml` 已声明：

```yaml
api-version: '1.21'
```

构建时使用的 Paper API 依赖：

```gradle
io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT
```

只要你的服务端是 1.21 系列，就可以正常使用。

---

## 3. 安装与第一次启动

### 3.1 安装步骤

1. 将编译好的 `Pandora-*.jar` 放入服务器的 `plugins/` 目录。
2. 启动服务器（或 `/reload`，更推荐完整重启）。

### 3.2 首次启动时的目录结构

第一次加载 Pandora 时，它会自动在 `plugins/Pandora/` 下生成：

```text
plugins/Pandora/
  ├─ config.yml
  ├─ mobs.yml
  ├─ skills.yml
  ├─ spawners.yml
  ├─ mobs/
  │   └─ example_mobs.yml
  ├─ skills/
  │   └─ example_skills.yml
  └─ spawners/
      └─ example_spawners.yml
```

含义：

- `config.yml`：整体配置（例如 debug 开关，后续扩展用）。
- `mobs.yml`：用于 "索引" 应加载的 Mob 配置文件列表。
- `skills.yml`：用于 "索引" 应加载的 Skill 配置文件列表。
- `spawners.yml`：用于 "索引" 应加载的 Spawner 配置文件列表。
- `mobs/`：放所有实际的自定义怪物配置文件。
- `skills/`：放所有实际的技能配置文件。
- `spawners/`：放所有实际的刷怪点配置文件。

> 默认情况下，`example_mobs.yml` / `example_skills.yml` / `example_spawners.yml` 提供了一套演示配置，便于你快速体验。

---

## 4. 顶层索引文件的用法（mobs.yml / skills.yml / spawners.yml）

### 4.1 顶层 mobs.yml

作用：告诉 Pandora 要从 `plugins/Pandora/mobs/` 目录中加载哪些子文件。

参考示例：

```yaml
# 顶层 mobs.yml —— 只写文件名列表，不写具体 Mob 内容

files:
  - example_mobs.yml     # 会从 plugins/Pandora/mobs/example_mobs.yml 加载
  - dungeon_mobs.yml     # 你自己新增的地牢怪物配置
  - boss_mobs.yml        # 你自己新增的 Boss 配置
```

注意：

- 插件会在 `plugins/Pandora/mobs/` 下寻找这些文件。
- 若想禁用某一批怪，只需要把对应文件名从 `files:` 列表中删掉/注释即可。

### 4.2 顶层 skills.yml

作用：告诉 Pandora 要从 `plugins/Pandora/skills/` 目录中加载哪些技能配置文件。

示例：

```yaml
files:
  - example_skills.yml
  - dungeon_skills.yml
  - boss_skills.yml
```

### 4.3 顶层 spawners.yml

作用：告诉 Pandora 要从 `plugins/Pandora/spawners/` 目录中加载哪些刷怪点配置文件。

示例：

```yaml
files:
  - example_spawners.yml
  - dungeon_spawners.yml
  - world_boss_spawners.yml
```

> 总结：**顶层 yml ≈ 加载清单**，真正的内容都放到对应子目录里。

---

## 5. 子目录中的配置文件结构

> 从 Pandora 0.x 开始，除了 YAML 配置以外，你还可以使用 **JavaScript** 来扩展技能、条件和刷怪逻辑。详见文档第 9 节《JavaScript 扩展与脚本技能》。

以下章节讲的 `mobs.yml` / `skills.yml` / `spawners.yml` 内容格式，指的是放在子目录里的 **自定义文件**（例如 `mobs/example_mobs.yml`、`skills/dungeon_skills.yml` 等）。

### 5.1 自定义 Mobs（`mobs/*.yml`）

#### 5.1.1 基本结构

```yaml
MobId:
  Type: <原版实体类型>
  Display: '<显示名，支持 & 颜色码>'
  Health: <血量>
  Damage: <基础攻击力>
  Armor: <护甲值>
  Faction: <阵营字符串，可选>
  Options:
    Silent: <true/false>
    PreventOtherDrops: <true/false>
    Despawn: <true/false>
    MovementSpeed: <移动速度 double>
    NoAI: <true/false>
    Glowing: <true/false>
    Invisible: <true/false>
    HasGravity: <true/false>
    Collidable: <true/false>
    FollowRange: <跟随攻击范围 double>
    KnockbackResistance: <击退抗性 double>
    AttackSpeed: <攻击速度 double>
  Skills:
    <触发器名称>:
      - <Skill 名称>
      - <Skill 名称>
  AI:
    Script: mobs/ai/example_ai.js
    Function: decideSkills
  Drops:
    - <minecraft:item> <最小数量>-<最大数量> <概率>
```

字段说明：

- `MobId`：自定义 Mob 的 ID，在 `spawners/*.yml` 和 `/pandora spawn` 里使用。
- `Type`：Bukkit 原版实体类型，如 `ZOMBIE`、`SKELETON`、`WITHER_SKELETON` 等；也可以直接写常见的中文名（例如 `僵尸`、`骷髅`、`凋零骷髅`），Pandora 会尝试自动匹配。
- `Faction`：阵营字符串，可选，例如 `Undead`、`Human`，后续可以在技能/条件中用于阵营判断。
- `Drops` 中的物品 ID：可以写原版枚举 ID（如 `DIAMOND`）、完整命名空间（如 `minecraft:diamond`，按物品枚举解析），也可以写一些常见中文名（如 `钻石`、`面包`），插件会做简单映射。
- `OnBlock` 条件中的 `type`：同样支持原版枚举 ID（`STONE`）或常见中文名（`石头` 等）。
- `Display`：显示名称，支持 `&c` 之类的颜色代码。
- `Health`：最大生命值。
- `Damage`：基础攻击力（通过 `ATTACK_DAMAGE` 属性设置）。
- `Armor`：护甲值（通过 `ARMOR` 属性设置）。
- `Options`：
  - `Silent`：是否静音（不发出声音）。
  - `PreventOtherDrops`：是否清空原版掉落，只保留你配置的掉落。
  - `Despawn`：`true` = 可自然消失，`false` = 不自然消失。
  - `MovementSpeed`：移动速度（`MOVEMENT_SPEED` 属性）。
  - `NoAI`：是否关闭原版 AI。
  - `Glowing` / `Invisible` / `HasGravity` / `Collidable` / `FollowRange` / `KnockbackResistance` / `AttackSpeed`：对应 Bukkit 实体属性控制。
- `Skills`：静态触发器 → 技能名列表（见后文 Trigger 说明）。
- `AI`：Mob 级别 AI 配置（见下文 5.1.3），允许用 JavaScript 决定在不同触发器下要释放哪些 Skill。
- `Drops`：掉落配置，格式为 `<物品 ID> <最小数量>-<最大数量> <概率>`。

#### 5.1.2 Mob 级 AI：静态 Skills + JavaScript AI

从设计上，Pandora 将 **Mob 看作“AI 层”**：决定“这个怪在什么事件（触发器）下要尝试释放哪些技能”。

你有两种方式定义 Mob 的 AI：

1. **静态 Skills 映射（纯 YAML）**：
   - 直接在 `Skills:` 下写 `onSpawn` / `onHurt` / `onAttack` 等触发器对应的 Skill 列表。
2. **JavaScript AI（动态决策）**：
   - 在 `AI:` 段中指定脚本和函数，让 JS 在运行时根据血量、距离、时间、阵营等情况返回要释放的技能列表。

一个典型示例：

```yaml
SkeletonKing:
  Type: WITHER_SKELETON
  Display: '&cSkeleton King'
  Health: 200
  Damage: 10
  Armor: 5
  Faction: Undead
  Options:
    Silent: true
    PreventOtherDrops: true
    Despawn: true
    MovementSpeed: 0.3

  # 方式一：静态 Skills（保留原有写法）
  Skills:
    onSpawn:
      - Roar
      - FireNova
    onHurt:
      - FireNova
      - SelfHeal

  # 方式二：JS AI（会优先于静态 Skills 生效）
  AI:
    Script: mobs/ai/skeleton_king_ai.js   # 相对于 plugins/Pandora/scripts 目录
    Function: decideSkills                # JS 中的函数名
```

> 插件会优先尝试调用 JS AI：
> - 若 JS 返回非空技能列表，则按该列表释放技能；
> - 若无 JS 配置或 JS 执行失败 / 返回空列表，则退回到 `Skills:` 中定义的静态触发器映射。

**JS AI 函数签名（示例）：**

```js
// 文件：plugins/Pandora/scripts/mobs/ai/skeleton_king_ai.js

function decideSkills(bindings) {
    var entity = bindings.entity;             // 当前 Mob（LivingEntity）
    var trigger = String(bindings.trigger);   // 触发器名称，例如 "ON_SPAWN"、"ON_HURT"
    var mobType = bindings.mobType;          // org.gsn.pandora.mobs.MobType

    if (trigger === 'ON_SPAWN') {
        // 刚刷出时释放怒吼 + 火焰新星
        return ['Roar', 'FireNova'];
    }

    if (trigger === 'ON_HURT') {
        var maxAttr = entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH);
        if (maxAttr != null) {
            var max = maxAttr.getValue();
            var ratio = entity.getHealth() / max;
            if (ratio < 0.3) {
                // 血量低于 30% 时改为自疗 + 召唤小弟
                return ['SelfHeal', 'SummonMinions'];
            }
        }
        return ['FireNova'];
    }

    // 其他触发器默认不释放技能
    return [];
}
```

脚本环境中可用的绑定变量包括：

- `entity`：当前 Mob 实体（`LivingEntity`）。
- `target`：触发事件中的目标实体（可以为 `null`）。
- `trigger`：触发器名称（字符串，如 `ON_SPAWN` / `ON_HURT` 等）。
- `mobType`：当前 Mob 的定义对象（`MobType`）。

> 利用 JS AI，你可以把复杂的仇恨逻辑、阶段切换、团本 Boss 行为都写在 JavaScript 中，保持 YAML 配置简洁。

#### 5.1.3 触发器（Triggers）

示例：

```yaml
Skills:
  onSpawn:
    - FireNova
  onHurt:
    - FireNova
    - SelfHeal
  onAttack:
    - ConeFlame
  onKill:
    - SummonMinions
```

目前支持：

- `onSpawn`：Pandora 生成该 Mob 时触发。
- `onAttack`：该 Mob 攻击其他生物时触发。
- `onHurt`：该 Mob 被伤害时触发。
- `onDeath`：该 Mob 死亡时触发。
- `onKill`：该 Mob 杀死目标时触发。
- `onCombat`：该 Mob 确立仇恨目标时触发（视监听器实现）。

### 5.2 自定义 Skills（`skills/*.yml`）

#### 5.2.1 Skill 定义结构：行为层（支持 JavaScript Skill）

从设计上，Pandora 将 **Skill 看作“行为层”**：每个 Skill 描述一段具体的行为逻辑（伤害、治疗、粒子、召唤等）。

你可以用两种方式定义 Skill：

1. **纯 YAML Skill 行（Mechanic + Targeter + Condition）**。
2. **整条 Skill 交给 JavaScript 实现**。

##### 1）纯 YAML Skill 行

```yaml
SkillId:
  Cooldown: <冷却秒数，可选，默认 0>
  Skills:
    - <技能行 1>
    - <技能行 2>
    - ...
```

示例：

```yaml
SelfHeal:
  Cooldown: 8
  Skills:
    - heal{a=8} @self ?healthbelow{amount=0.5}
    - potion{type=REGENERATION;d=200;lvl=1} @self
```

##### 2）整条 Skill 使用 JavaScript 实现

在 Skill 下新增 `Js:` 段：

```yaml
FireNova:
  Cooldown: 5

  # 可选：保留原有 YAML 技能行，作为兜底或示例
  Skills:
    - damage{a=10} @playersinradius{r=5}
    - particles{p=FLAME;amount=50;r=2;oy=1.0} @self

  # JS Skill：优先使用 JS 执行整个技能逻辑
  Js:
    Script: skills/fire_nova_skill.js
    Function: executeFireNova
```

> 当 `Js` 存在时，Pandora 会优先调用 JS 函数来实现整个 Skill：
> - JS 正常执行时，Skill 的行为完全由脚本决定；
> - 若无 `Js` 配置或脚本执行失败，则退回到 `Skills:` 中的 YAML Skill 行逻辑。

**JS Skill 函数示例：**

```js
// 文件：plugins/Pandora/scripts/skills/fire_nova_skill.js

function executeFireNova(bindings) {
    var ctx = bindings.ctx;        // SkillExecutionContext
    var caster = bindings.caster;  // LivingEntity

    if (caster == null) {
        return;
    }
    var world = caster.getWorld();
    if (world == null) {
        return;
    }

    // 半径 5 内所有玩家
    var radius = 5.0;
    var nearby = caster.getNearbyEntities(radius, radius, radius);
    var it = nearby.iterator();
    while (it.hasNext()) {
        var e = it.next();
        if (e instanceof org.bukkit.entity.Player) {
            e.damage(10.0, caster);
        }
    }

    // 粒子 + 音效
    world.spawnParticle(
        org.bukkit.Particle.FLAME,
        caster.getLocation().add(0, 1.0, 0),
        50,
        1.5, 0.5, 1.5,
        0.02
    );
    world.playSound(
        caster.getLocation(),
        org.bukkit.Sound.ENTITY_BLAZE_SHOOT,
        1.0, 1.0
    );
}
```

脚本环境中可用绑定变量示例：

- `ctx`：SkillExecutionContext（可获取触发类型、插件实例等）。
- `skill`：当前 Skill 对象。
- `caster`：施法者实体。
- `target`：事件目标实体（可能为 `null`）。

> Mob 级 AI 决定“**什么时候放什么 Skill**”，Skill JS 决定“**这个 Skill 具体做什么**”，两层组合即可实现完整的自定义 AI 行为树。

```yaml
SkillId:
  Cooldown: <冷却秒数，可选，默认 0>
  Skills:
    - <技能行 1>
    - <技能行 2>
    - ...
```

示例：

```yaml
SelfHeal:
  Cooldown: 8
  Skills:
    - heal{a=8} @self ?healthbelow{amount=0.5}
    - potion{type=REGENERATION;d=200;lvl=1} @self
```
```
#### 5.2.2 技能行语法

通用结构：

```text
<mechanicName>{参数列表} @Targeter{参数列表} ?Condition1{参数} ?Condition2{参数} ...
```

示例：

```text
damage{a=10} @playersinradius{r=5}
heal{a=8} @self ?healthbelow{amount=0.5}
damage{a=4} @target ?onblock{type=LAVA}
```

##### 参数列表格式

```text
key=value;key2=value2
```

例：

- `damage{a=10}`
- `potion{type=REGENERATION;d=200;lvl=1}`
- `playersinradius{r=5}`
- `onblock{type=LAVA}`

#### 5.2.3 内置 Mechanics 一览

- `damage{a=10}`：对目标造成固定伤害。
- `heal{a=8}`：治疗目标。
- `potion{type=...;d=...;lvl=...}`：添加药水效果。
- `message{m="..."}`：对玩家目标发送消息。
- `particles{p=...;amount=...;r=...}`：播放粒子效果。
- `summon{type=...;amount=...;radius=...}`：召唤原版实体。
- `teleport{r=...}`：随机传送目标。
- `velocity{y=...}`：在 Y 轴方向击飞目标。

#### 5.2.4 内置 Targeters 一览

- `@self`：施法者自身。
- `@playersinradius{r=5}`：半径内玩家。
- `@target`：事件目标（如被攻击者）。
- `@area{r=5}`：半径内所有生物。
- `@near{r=10;limit=3}`：最近的若干目标。
- `@cone{r=6;angle=100}`：施法者前方扇形区域。

#### 5.2.5 内置 Conditions 一览

- `?chance{p=0.3}`：概率触发。
- `?healthabove{amount=0.5}`：血量百分比大于某值。
- `?healthbelow{amount=0.5}`：血量百分比小于某值。
- `?distance{min=0;max=10}`：距离范围限制。
- `?world{name=world_nether}`：世界名限制。
- `?onblock{type=LAVA}`：目标脚下方块类型必须为指定类型。

### 5.3 自定义 Spawners（`spawners/*.yml`）

#### 5.3.1 基本结构

```yaml
SpawnerId:
  Mob: <MobId>
  World: <世界名>
  X: <整数坐标>
  Y: <整数坐标>
  Z: <整数坐标>
  Radius: <控制刷怪范围>
  MaxMobs: <最大同时存活数量>
  Interval: <刷怪间隔>
  Conditions:
    - playersInRadius{r=30;min=1}
    - time{from=13000;to=23000}
```

- `SpawnerId`：刷怪点 ID，用于指令和日志。
- `Mob`：引用 `mobs/*.yml` 中定义的 Mob ID。
- `World`：世界名称，如 `world`、`world_nether`。
- `X/Y/Z`：坐标。
- `Radius`：统计由该 Spawner 生成的怪的半径。
- `MaxMobs`：最大同时存活数量。
- `Interval`：刷怪间隔，支持：
  - 纯数字（tick）
  - `10s`（秒）
  - `200ms`（毫秒）

#### 5.3.2 Spawner 条件

- `playersInRadius{r=30;min=1}`：
  - 当半径 30 格内玩家数 ≥ 1 时才刷怪。
- `time{from=13000;to=23000}`：
  - 当世界时间在 [13000, 23000] 区间内时才刷怪，支持跨午夜。

---

## 6. 指令与权限

- `/pandora reload`
  - 重载 `config.yml`、顶层 `mobs.yml` / `skills.yml` / `spawners.yml` 以及所有子目录配置。

- `/pandora spawn <mobId> [数量]`
  - 在玩家脚下生成指定 Pandora Mob。

- `/pandora spawner list`
  - 列出当前加载的所有刷怪点。

- `/pandora spawner info <id>`
  - 查看某个刷怪点的详细信息。

- `/pandora spawner tp <id>`
  - 将玩家传送到刷怪点位置。

- `/pandora spawner create <id> <mobId> [radius] [interval]`
  - 在玩家当前位置创建一个刷怪点（只在内存中生效，持久化需手动写入对应 `spawners/*.yml`）。

- `/pandora spawner remove <id>`
  - 删除刷怪点（同样推荐同步到配置文件）。

- `/pandora debug`
  - 面向实体执行，查看该实体是否为 Pandora Mob 以及相关信息。

权限：

- `pandora.admin`：允许使用上述所有指令。

---

## 7. 推荐使用流程

1. 在 `mobs/`、`skills/`、`spawners/` 子目录里新增属于你服务器的配置文件，例如：
   - `mobs/dungeon_mobs.yml`
   - `skills/dungeon_skills.yml`
   - `spawners/dungeon_spawners.yml`

2. 在对应的顶层索引文件中增加：

```yaml
# mobs.yml
files:
  - example_mobs.yml
  - dungeon_mobs.yml

# skills.yml
files:
  - example_skills.yml
  - dungeon_skills.yml

# spawners.yml
files:
  - example_spawners.yml
  - dungeon_spawners.yml
```

3. `/pandora reload` 重载配置。
4. 使用 `/pandora spawn`、`/pandora spawner tp` 等指令测试实际效果。
5. 若要禁用/分批加载某些配置文件，只需要在顶层 `files:` 列表中移除文件名即可，无需移动/删除子文件。

---

## 8. 调试与排错

- 使用 `/pandora debug` 检查实体是否为 Pandora Mob。
- 使用 `/pandora spawner info <id>` 确认刷怪点是否满足条件（玩家范围、时间段、数量上限）。
- 修改 YAML 配置后务必 `/pandora reload` 或重启服务器。
- 修改 JavaScript 脚本（`plugins/Pandora/scripts/*.js`）后，建议也执行 `/pandora reload`，以便重新加载脚本。
- 若发现 YAML 报错或插件加载异常，请检查：
  - YAML 缩进是否正确。
  - 顶层 `files:` 列表中的文件名是否与子目录中的实际文件一致。
  - 是否误把实际配置写进了顶层 `mobs.yml` / `skills.yml` / `spawners.yml`（应写在子目录文件里）。

---

## 9. JavaScript 扩展与脚本技能

Pandora 内置了一个 JavaScript 脚本系统，可以让你用 JS 来编写复杂逻辑，而不必把所有东西都塞进 YAML。

### 9.1 前置条件与目录结构

- 配置开关：在 `plugins/Pandora/config.yml` 中：

```yaml
javascript:
  enabled: true   # 开启 / 关闭 JS 支持
  debug: false
```

- 若启用 JS，插件会在 `plugins/Pandora/scripts/` 目录下读取脚本文件（首启会自动释放一套示例脚本）：

```text
plugins/Pandora/
  ├─ scripts/
  │   ├─ skills/
  │   │   └─ example_skills.js
  │   ├─ conditions/
  │   │   └─ example_conditions.js
  │   └─ spawners/
  │       └─ example_spawners.js
```

### 9.2 在技能中使用 JS（Script Mechanic）

Pandora 提供一个脚本型 Mechanic：`script`。

基本语法：

```yaml
SomeSkill:
  Cooldown: 2
  Skills:
    - script{file=example_skills.js;func=exampleDamage} @playersinradius{r=5}
```

含义：

- `file=example_skills.js`：从 `plugins/Pandora/scripts/skills/example_skills.js` 读取脚本。
- `func=exampleDamage`：调用脚本中的 `exampleDamage()` 函数。
- 目标选择仍由 Targeter 决定，这里是 `@playersinradius{r=5}`。

示例脚本（`scripts/skills/example_skills.js`）：

```js
function exampleDamage() {
    if (typeof targets === 'undefined' || targets == null) {
        return false;
    }
    var it = targets.iterator();
    while (it.hasNext()) {
        var t = it.next();
        // 对每个目标造成 2 点伤害
        t.damage(2.0);
    }
    return true; // 返回 true 表示技能成功执行
}
```

> Java 侧会向脚本注入：
> - `ctx`：SkillExecutionContext，包含施法者、触发类型等信息。
> - `targets`：`java.util.List<org.bukkit.entity.LivingEntity>`，即本次技能命中的所有实体。

### 9.3 使用 JS 条件（Script Condition）

有时候你希望触发技能前先跑一段复杂逻辑判断，这时可以使用脚本条件 `?js{...}`。

示例 YAML：

```yaml
SmartStrike:
  Cooldown: 1
  Skills:
    - damage{a=6} @target ?js{file=example_conditions.js;func=lowHpAndNear}
```

对应脚本（`scripts/conditions/example_conditions.js`）：

```js
var Attribute = Java.type('org.bukkit.attribute.Attribute');

// 目标血量 < 50% 且与施法者距离 <= 10 格 才返回 true
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
```

> JS 条件函数返回 `true/false`：
> - `true`：允许执行后面的 Mechanic。
> - `false`：本次技能对该目标不会生效。

脚本环境中可用变量：

- `ctx`：SkillExecutionContext（例如 `ctx.getCaster()` 返回施法者）。
- `target`：当前正在检查的目标实体（`LivingEntity`）。

### 9.4 在 Spawner 条件中使用 JS

Spawner 条件除了 YAML 中的内置 `playersInRadius` 与 `time` 外，还支持 JS 条件：

```yaml
NightSpawner:
  Mob: SkeletonKing
  World: world
  X: 100
  Y: 64
  Z: 200
  Radius: 10
  MaxMobs: 5
  Interval: 10s
  Conditions:
    - playersInRadius{r=30;min=1}
    - js{file=example_spawners.js;func=nightAndNotCrowded}
```

对应脚本（`scripts/spawners/example_spawners.js`）：

```js
// 只在夜晚且附近实体数量 < 30 时允许刷怪
function nightAndNotCrowded() {
    if (center == null || center.getWorld() == null) {
        return false;
    }
    var world = center.getWorld();
    var time = world.getTime();
    if (time < 13000 || time > 23000) {
        return false;
    }
    var entities = world.getNearbyEntities(center, 16, 16, 16);
    return entities.size() < 30;
}
```

脚本环境中可用变量：

- `center`：当前 Spawner 中心位置（`org.bukkit.Location`）。
- `spawnerId`：当前刷怪点的 ID（字符串）。

### 9.5 调试 JS 脚本

- 确保 `config.yml` 中 `javascript.enabled: true`。
- 服务端控制台若出现：`未找到 JS 引擎 'nashorn'`，说明当前 Java 环境未提供 JS 引擎：
  - 需要自行通过插件/启动参数引入 Nashorn 或其他兼容 JS 引擎；
  - 否则所有 `script` / `?js` / `js{...}` 条目将被忽略。
- 修改 JS 后执行 `/pandora reload`，确保脚本缓存被清空并重新加载。
- 可以在脚本中使用 `ctx.getCaster().sendMessage("...")` 或 `center.getWorld().getPlayers()` 等方法来观察行为。

---

## 10. 总结

通过 YAML + JavaScript 的组合，你可以：

- 用 YAML 定义大部分结构和简单逻辑；
- 用 JS 实现复杂的技能连锁、血量/距离/环境判断；
- 用 JS 控制 Spawner 的刷新条件，使怪物行为更加智能、贴合你的服务器玩法。

如果你只想使用纯 YAML，也可以完全不配置任何 `script` / `?js` / `js{...}`，插件会按纯 YAML 模式正常工作。

---

## 8. 调试与排错

- 使用 `/pandora debug` 检查实体是否为 Pandora Mob。
- 使用 `/pandora spawner info <id>` 确认刷怪点是否满足条件（玩家范围、时间段、数量上限）。
- 修改配置后务必 `/pandora reload` 或重启服务器。
- 若发现 YAML 报错或插件加载异常，请检查：
  - YAML 缩进是否正确。
  - 顶层 `files:` 列表中的文件名是否与子目录中的实际文件一致。
  - 是否误把实际配置写进了顶层 `mobs.yml` / `skills.yml` / `spawners.yml`（应写在子目录文件里）。

---

通过“顶层索引 + 子目录拆分”的方式，你可以很方便地：

- 为不同副本/世界维护独立的 Mob/Skill/Spawner 配置文件；
- 按需启用/禁用某些文件，而不影响整个配置；
- 让配置结构更清晰、可维护性更强。
