# Pandora 插件使用文档

> 版本 1.2.0 · 适用于 Paper / Folia 1.21.x · Java 21
>
> Pandora 是一款类似 MythicMobs 的自定义实体插件：通过 YAML 配置定义自定义生物、技能（机制/条件/目标选择器）、定点刷怪点与随机生成，并内置 Nashorn JavaScript 扩展。
>
> 插件采用**目录式配置**，自动扫描并加载 `mobs/`、`skills/`、`spawners/`、`random-spawners/` 目录下所有 `.yml` 文件，文件名可自由命名。

## 目录

1. [安装与环境要求](#1-安装与环境要求)
2. [目录结构](#2-目录结构)
3. [全局配置 config.yml](#3-全局配置-configyml)
4. [自定义实体 mobs/*.yml](#4-自定义实体-mobsyml)
5. [技能 skills/*.yml](#5-技能-skillsyml)
6. [Mechanics 机制速查](#6-mechanics-机制速查)
7. [Targeters 目标选择器](#7-targeters-目标选择器)
8. [Conditions 条件速查](#8-conditions-条件速查)
9. [定点刷怪点 spawners/*.yml](#9-定点刷怪点-spawnersyml)
10. [随机生成 random-spawners/*.yml](#10-随机生成-random-spawnersyml)
11. [指令与权限](#11-指令与权限)
12. [JavaScript 扩展](#12-javascript-扩展)
13. [API（供开发者）](#13-api供开发者)
14. [Folia 兼容性](#14-folia-兼容性)
15. [故障排查](#15-故障排查)

---

## 1. 安装与环境要求

| 项目 | 要求 |
|------|------|
| 服务端 | Paper / Folia 1.21.x，或 Paper 26.1 / 26.2 |
| Java | 21 |
| 前置插件 | 无 |
| 权限 | `pandora.admin`（默认 OP） |

安装：将 `Pandora-1.2.1.jar` 放入 `plugins/` 并重启。首次启动会自动生成 `config.yml` 及示例文件。

**多版本支持（1.21.x / 26.1 / 26.2）**：插件主产物以 Paper 1.21.8 API 编译，同时通过 `verifyCompat26` 构建任务针对 Paper 26.1 与 26.2 API 做额外编译校验，保证同一 jar 在这三个版本系列上均可加载运行。开发者可执行 `gradlew verifyCompat26` 手动触发校验（构建时自动执行）。

## 2. 目录结构

```text
plugins/Pandora/
  ├─ config.yml                 # 全局配置
  ├─ mobs/                      # 自定义实体（示例：example_mob.yml）
  ├─ skills/                    # 技能定义（示例：example_skill.yml）
  ├─ spawners/                  # 定点刷怪点（示例：example_spawner.yml）
  ├─ random-spawners/           # 随机生成（示例：example_random_spawn.yml）
  └─ scripts/                   # JavaScript 脚本
      ├─ skills/
      ├─ conditions/
      ├─ spawners/
      └─ mobs/
```

- **添加配置**：在对应目录新建 `.yml` 后执行 `/pandora reload`
- **禁用配置**：重命名为 `.yml.disabled`
- **注意**：同一目录的不同文件中不能出现重复 ID（重复键会被跳过并警告）

## 3. 全局配置 config.yml

```yaml
debug: true                      # 调试日志

javascript:
  enabled: true                  # 启用 JavaScript 脚本
  debug: false                   # 输出脚本执行详情

spawning:
  enabled: true                  # 启用刷怪系统
  check-interval: 20             # 全局检查频率（tick）
  default-interval: 10s          # 默认刷新间隔
  default-spawn-amount: 1        # 默认每次刷新数量
  default-max-mobs: 5            # 默认最大实体数
  default-radius: 5.0            # 默认刷新半径
  despawn:
    enabled: true                # 启用自然消失
    distance: 128                # 消失距离（方块）
    delay: 30                    # 消失延迟（秒）
    clear-on-restart: false      # 重启时清理
  conditions:
    min-players: 1
    player-radius: 32.0
  worlds:
    mode: whitelist              # whitelist（白名单）或 blacklist（黑名单）
    list:
      - world
      - world_nether
      - world_the_end
```

**时间格式**：`10s`（秒）、`5000ms`（毫秒）、`200`（tick）。1 秒 = 20 tick = 1000 毫秒。

**世界过滤**：白名单模式只在列表中的世界刷新；黑名单模式在列表外的所有世界刷新。世界名大小写敏感。

## 4. 自定义实体 mobs/*.yml

```yaml
SkeletonKing:
  Type: WITHER_SKELETON          # 必填。原版实体类型枚举名，也支持中文名
  Display: '&cSkeleton King'     # 可选。显示名，支持 & 颜色码
  Health: 200                    # 可选。最大生命值，默认 20
  Damage: 10                     # 可选。基础攻击力，默认 2
  Armor: 5                       # 可选。护甲值，默认 0
  Faction: Undead                # 可选。阵营，用于 ?faction 条件与同阵营互不索敌
  Level: 3                       # 可选。实体等级（见下方「等级系统」）
  ThreatTable: true              # 可选。启用威胁表（见下方「威胁表」）
  Timer: 10s                     # 可选。onTimer 触发间隔（10s / 200 / 5000ms）

  DamageModifiers:               # 可选。伤害调整：伤害原因(大写) -> 倍率
    PROJECTILE: 1.5              # 额外受到 50% 箭矢伤害
    DROWNING: 0.0                # 免疫溺水
    FIRE_TICK: 0.5               # 火焰伤害减半

  Options:
    Silent: true                 # 静音
    PreventOtherDrops: true      # 清空原版掉落（仅在无自定义 Drops 时生效）
    Despawn: true                # 是否可自然消失
    MovementSpeed: 0.3           # 移动速度
    NoAI: false                  # 关闭原版 AI
    Glowing: false               # 发光
    Invisible: false             # 隐身
    HasGravity: true             # 受重力
    Collidable: true             # 可碰撞
    FollowRange: 32.0            # 跟随/攻击范围
    KnockbackResistance: 0.5     # 击退抗性（0-1）
    AttackSpeed: 1.0             # 攻击速度

  Equipment:                     # 装备
    MainHand: IRON_SWORD         # 材质 ID 或中文名
    OffHand: SHIELD
    Helmet: WITHER_SKELETON_SKULL
    Chestplate: IRON_CHESTPLATE
    Leggings: IRON_LEGGINGS
    Boots: IRON_BOOTS
    MainHandDropChance: 0.05     # 各槽位掉落概率（0=不掉，1=必掉）
    HelmetDropChance: 0.02

  Skills:                        # 触发器 -> 技能 ID 列表
    onSpawn:                     # 生成时
      - FireNova
    onHurt:                      # 被伤害时
      - SelfHeal
    onAttack:                    # 攻击时
      - ConeFlame
    onKill:                      # 击杀目标时
      - SummonMinions
    onDeath:                     # 死亡时
      - DeathExplosion
    onCombat:                    # 确立仇恨目标时
      - WarCry
    onTimer:                     # 按 Timer 间隔定时
      - FireNova
    onSignal:                    # 收到信号时（配合 sendsignal）
      - CallReinforcements

  AI:                            # 可选。JS AI（优先于静态 Skills）
    Script: mobs/ai/boss_ai.js   # 相对 scripts/ 目录
    Function: decideSkills

  Drops:                         # 设置后替换原版掉落
    - minecraft:diamond 1-3 0.15 # 格式：<材质> [数量或范围] [概率0-1]
    - minecraft:bone 2-5 1.0
```

### 威胁表（ThreatTable）

开启后，实体记录每位玩家对其造成的累计伤害，并**锁定累计伤害最高的玩家为目标**，避免玩家车轮战。相关技能：

- `threatchange{action=ADD;amount=1}` / `threatmod{...}`：修改目标威胁值（action 支持 ADD / SET / MULTIPLY）
- `threatclear{}`：清空自身威胁表

### 等级系统（Level）

- 配置 `Level: 3` 或技能 `setlevel{l=5}`
- 默认每级血量 +25%、近战伤害 +25%（与 MythicMobs 默认一致）
- 条件 `?moblevel{level=1;maxLevel=10}` 检测等级范围
- 随机生成可用 `MinLevel / MaxLevel` 随机分配等级

### 伤害调整（DamageModifiers）

键为 Bukkit `EntityDamageEvent.DamageCause` 枚举名（大写），值为伤害倍率，覆盖所有原版伤害类型（含溺水、摔落等非实体伤害）。

### 阵营（Faction）

同阵营的 Pandora 实体之间**不会互相索敌**；配合 `?faction{f=...}` 条件可实现阵营对峙玩法。

## 5. 技能 skills/*.yml

```yaml
FireNova:
  Cooldown: 5                    # 冷却秒数，默认 0
  Skills:                        # 技能行列表
    - particles{p=FLAME;amount=60;r=3} @self
    - damage{a=10} @playersinradius{r=5} ?chance{c=0.8}
  Js:                            # 可选。JS 整体执行，优先于 Skills 行
    Script: skills/fire_nova.js  # 相对 scripts/ 目录
    Function: execute
```

**技能行语法**：

```text
<mechanic>{key=value;key2=value2} @<targeter>{key=value} ?<condition>{key=value} ?<condition2>{...}
```

- 参数以 `;` 分隔，参数名支持多个别名（详见各速查表）
- `@targeter` 省略时默认 `@self`
- `?condition` 可叠加多个，全部满足才执行

## 6. Mechanics 机制速查

### 伤害 / 治疗

| 机制 | 关键参数（别名） | 默认 | 说明 |
|------|------------------|------|------|
| `damage` | amount|a | 1 | 固定伤害 |
| `basedamage` | multiplier|m | 1 | 基于攻击力的倍数伤害 |
| `damagepercent` | percent|p; currentHealth|ch | 0.1; false | 百分比伤害（可选基于当前血量） |
| `physicaldamage` | multiplier|m; forcedDamage|fd | 1; 0 | 物理伤害 |
| `consume` | damage|d; heal|h | 1; 1 | 吸血 |
| `heal` | amount|a | 5 | 固定治疗 |
| `healpercent` | multiplier|m | 0.1 | 百分比治疗 |

### 状态 / 属性

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `potion` | type|t; duration|d; level|lvl | 药水效果 |
| `ignite` / `setfire` | ticks|t | 点燃 |
| `freeze` | ticks|t | 冰冻 |
| `attribute` | attribute|attr; amount; duration; operation | 属性修改 |
| `attributemodifier` | attribute; operation; name; amount; duration | 命名属性修饰符 |
| `recoil` | duration|d | 后坐力 |

### 移动 / 位移

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `velocity` | y|yv | 垂直速度 |
| `push` / `knockback` | velocity|v; velocityY|yv | 击退 |
| `forcepull` / `pull` | spread|s; spreadV|vs | 拉拽 |
| `jump` | velocity|v | 跳跃 |
| `lunge` | v; yv | 突进 |
| `disengage` | v; yv | 后撤 |
| `leap` | v; noise|n | 跳跃（带随机偏移） |
| `goto` | speed|s; spreadH; spreadV | 向目标移动 |
| `teleport` | radius|r | 随机传送 |
| `teleportto` | location|c; world|w; yaw; pitch; relative | 传送到指定位置 |

### 视觉 / 音效 / UI

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `particles` | particle|p; amount|c; radius|r | 粒子效果 |
| `lightning` / `strike` | damage|d | 落雷（可带伤害） |
| `effect:lightning` | localized; localizedRadius | 闪电视觉效果 |
| `effect:enderbeam` / `enderbeam` | duration|d; yOffset | 末影光束 |
| `effect:geyser` / `geyser` | height|h; type|t | 间歇泉 |
| `effect:guardianbeam` / `guardianbeam` | duration; startYOffset; targetYOffset | 守卫者光束 |
| `effect:itemspray` / `itemspray` | items|i; amount; force; yForce; yOffset; gravity; allowPickup | 物品喷射 |
| `firework` | type|t; power|p; flicker; trail; colors|c; fadedColors | 烟花 |
| `hologram` | text|t; stayTime|stay; yOffset|y | 全息文字（隐形盔甲架） |
| `message` | message|m | 聊天消息 |
| `messagejson` | message|m | JSON 消息 |
| `actionbar` / `actionmessage` | message|m | 动作栏消息 |
| `title` / `titlemessage` | title|t; subtitle|s; fadeIn|fi; stay; fadeOut|fo | 标题消息 |
| `sound` / `playsound` | sound|s; volume|v; pitch|p | 播放音效 |
| `barcreate` / `barset` / `barremove` | name|n; display|d; value|v; color|c; style|s | Boss 血条 |
| `bossborder` | radius|r | Boss 边界 |
| `log` | message|m; level|l | 控制台日志 |
| `bloodyscreen` | duration|d; cancel|c | 血屏效果 |

### 召唤 / 抛射

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `summon` | type|t; amount|a; radius|r | 召唤实体（原版类型或 Pandora Mob ID） |
| `explosion` / `explode` | yield|y; blockdamage|bd; fire|f | 爆炸 |
| `arrowvolley` | amount; spread; fireTicks; removeDelay; velocity; pickup | 箭雨 |
| `volley` | amount; radius; yOffset; pickup | 齐射 |
| `projectile` | type; velocity|v; maxRange|mr; gravity; horizontalRadius|hr; hitPlayers|hp; hitNonPlayers|hnp; hitSelf; startYOffset; onHitSkill|oh | 自定义抛射物（NORMAL/ARROW/SNOWBALL，命中触发技能） |
| `totem` | velocity; maxRange; hitRadius; hitPlayers; hitNonPlayers; onHitSkill | 图腾抛射物 |
| `modifyprojectile` | trait|t; action|a; value|v | 修改抛射物 |
| `setprojectilebulletmodel` | value|v | 抛射物模型数据 |

### 物品 / 装备

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `giveitem` | items|i; amount|a | 给予物品 |
| `dropitem` | items|i; amount|a | 掉落物品 |
| `giveitemfromslot` | slot|s | 给予指定槽位物品 |
| `giveitemfromtarget` | items|i; amount|a | 从目标获取物品 |
| `stealitem` | — | 偷取目标手持物品 |
| `equip` | items|e（格式 `材质:槽位`） | 装备物品 |
| `cquip` | item|i; slot|s | 设置快捷栏物品 |
| `removehelditem` | amount|a | 移除手持物品 |
| `consumeslotitem` | slot|s; amount|a | 消耗槽位物品 |
| `fillchest` | items|i; amount|a; shouldEmptyChest | 填充箱子 |
| `breakblock` | doDrops|d; doEffect|e | 破坏方块 |
| `breakblockandgiveitem` | doDrops; doEffect; items|i; amount | 破坏方块并给物品 |
| `bonemeal` | blockFace|f | 骨粉催熟 |

### 命令 / 流程控制

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `command` / `cmd` | command|c; asCaster|ac; asTarget|at; asOp|op | 执行命令 |
| `delay` | ticks|t | 延迟 |
| `wait` | interval|i; timeout|tt; cancelSkill|cs | 等待 |
| `metaskill` / `metavariableskill` | skill | 调用子技能 |
| `script` | file; func|function | 调用 JS 脚本 |
| `remove` | — | 移除施法者实体 |
| `activatespawner` | spawner|s | 立即触发指定刷怪点 |
| `gcd` | ticks|t | 设置全局冷却（攻击冷却） |

### 记分板 / 世界 / 实体操作

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `modifyscore` | objective|o; entry|e; action|a; value|v | 修改记分板 |
| `modifytargetscore` | objective|o; action; value | 修改目标分数 |
| `modifyglobalscore` | objective|o; entry|e; action; value | 修改全局分数 |
| `weather` / `resetweather` | type|t; duration; personal | 天气 |
| `time` / `settime` | mode|m; amount|t | 修改时间 |
| `look` | headOnly|ho | 朝向目标 |
| `setrotation` / `rotatetowards` / `matchrotation` | yaw; pitch; relative | 旋转朝向 |
| `feed` | amount|a; saturation|s | 恢复饥饿 |
| `oxygen` | amount|a | 设置氧气 |
| `giveexperiencelevels` / `takeexperiencelevels` | amount|a | 给予/扣除经验等级 |
| `wolfsit` | sit | 狼坐下 |
| `saddle` | take|t | 鞍 |
| `animatearmorstand` | head; body; leftArm; rightArm; leftLeg; rightLeg; useDegrees | 盔甲架动画 |
| `doppleganger` | hasNamePlate; usePlayerName | 分身 |
| `settextdisplay` | text|t | 文本展示实体 |
| `setdisplayentityitem` | item|i | 展示实体物品 |
| `setinteractionsize` | height|h; width|w | 交互实体大小 |
| `displaytransformation` | action; transformationType; values | 展示实体变换 |
| `addtrade` | action|m; slot; ingredient|i; ingredient2|i2; result|r; maxuses|u; expReward; villagerExperience | 村民交易 |
| `enderdragonsetphase` / `enderdragonsetrespawnphase` / `enderdragonspawnportal` | phase / withPortals | 末影龙相关 |
| `damagetag` | tag|t | 打伤害标签 |

### 信号 / 威胁 / 等级 / 变量（Wiki 移植功能）

| 机制 | 关键参数 | 说明 |
|------|----------|------|
| `sendsignal` | signal|s | 向目标实体发送信号，触发其 `onSignal` 技能 |
| `threatchange` / `threatmod` | action|a (ADD/SET/MULTIPLY); amount | 修改威胁值 |
| `threatclear` | — | 清空自身威胁表 |
| `setlevel` | level|l | 设置实体等级 |
| `variableset` | variable|v; value|val; type|t (STRING/INTEGER/DOUBLE/BOOLEAN); global|g | 设置变量 |
| `variableadd` | variable|v; value|a; global|g | 数值变量加法 |
| `unsetvariable` / `varunset` / `variableremove` | variable|v; global|g | 删除变量 |

## 7. Targeters 目标选择器

语法：`@<targeter>{参数}`，省略时默认 `@self`。

| 名称 | 别名 | 参数 | 默认 | 说明 |
|------|------|------|------|------|
| @self | — | 无 | — | 施法者自身 |
| @target | — | 无 | — | 当前仇恨目标 |
| @playersinradius | @pir | radius|r | 5 | 半径内所有玩家 |
| @area | @eir, @entitiesinradius | radius|r | 5 | 半径内所有实体 |
| @near | — | radius|r; limit|l | 5; 3 | 最近的 N 个实体 |
| @cone | — | radius|r; angle|a | 5; 60 | 以施法者朝向为中心的锥形范围 |

## 8. Conditions 条件速查

语法：`?<condition>{参数}`，可叠加多个，全部满足才执行。

### 概率 / 通用

| 条件 | 参数 | 说明 |
|------|------|------|
| `chance` | chance|c|p (0-1) | 触发概率 |
| `js` | file; func|function | JS 自定义条件 |
| `stringequals` / `stringempty` / `stringnotempty` | value1/value2 等 | 字符串比较 |
| `plugin` | plugin|p | 插件是否已加载 |
| `skilloncooldown` | skill|s | 技能是否在冷却 |

### 生命 / 距离

| 条件 | 说明 |
|------|------|
| `healthabove` / `healthbelow`（amount|a，百分比） | 血量百分比高于/低于阈值 |
| `healthpercent`（minPercent; maxPercent） | 血量百分比范围 |
| `health`（amount; maxAmount） | 绝对血量范围 |
| `distance`（min; max|d） | 与目标距离范围 |
| `playerwithin` / `playernotwithin`（distance|d） | 附近是否有玩家 |
| `targetwithin` / `targetnotwithin`（distance|d） | 目标是否在距离内 |
| `threatwithin` / `threatnotwithin`（distance|d; amount|a） | 威胁目标是否在距离内 |
| `distancefromspawn` / `distancefromlocation` / `distancefromtrackedlocation` | 与出生点/指定位置的距离 |
| `cuboid`（location1; location2; relative） | 是否在长方体区域内 |
| `xdiff` / `ydiff` / `zdiff`（difference|d; maxDiff） | 坐标差范围 |

### 世界 / 环境

| 条件 | 说明 |
|------|------|
| `world`（world|w） | 指定世界 |
| `dimension`（dimension|d: NORMAL/NETHER/THE_END） | 维度 |
| `worldtime`（time|t: from~to） | 游戏时间范围 |
| `biome` / `biometype` | 生物群系 |
| `altitude` / `height` / `heightabove` / `heightbelow` | Y 坐标范围 |
| `lightlevel` / `lightlevelfromblocks` | 光照等级 |
| `lunarphase`（phases|p: 0-7） | 月相 |
| `structure`（structures|s; radius|r） | 是否在结构附近 |
| `moisturelevel` | 耕地湿度 |
| `localdifficulty`（PEACEFUL/EASY/NORMAL/HARD） | 本地难度 |

### 实体属性 / 方块 / 物品

| 条件 | 说明 |
|------|------|
| `entitytype`（type|t） | 实体类型匹配 |
| `faction`（faction|f） | 阵营匹配 |
| `name`（name|n） | 名称匹配 |
| `moving` / `velocity` / `motionx` / `motiony` / `motionz` / `directionalvelocity` / `fallspeed` | 移动/速度检测 |
| `pitch` / `yaw` / `fieldofview`(fov) | 视角/视野 |
| `size` / `color` / `stance` / `timealive` | 大小/颜色/姿态/存活时间 |
| `onblock` / `blocktype` / `blocktypeinradius` / `triggerblocktype` | 方块类型 |
| `holding` / `wearing` / `hasitem` / `itemtype` / `triggeritemtype` / `entityitemtype` / `entitymaterialtype` / `itemissimilar` | 物品类型 |
| `hasenchantment`（type; level） | 附魔检测 |

### 药水 / 伤害 / 玩家 / 计数

| 条件 | 说明 |
|------|------|
| `haspotioneffect`（type; level; duration） | 药水效果 |
| `hastag`（tag|t） | PDC 标签 |
| `lastdamagecause` / `damagecause`（cause|c） | 最后受伤原因 |
| `damagetag` / `damageamount` | 伤害标签/数值 |
| `bowtension`（force|f） | 弓拉力 |
| `playersinworld` / `playersonline` / `playerkills` | 玩家数量/击杀数 |
| `haspermission`（permission|p） | 权限 |
| `foodlevel` / `foodsaturation` / `level` / `enchantinglevel` | 饥饿/经验/附魔等级 |
| `livinginradius` / `mobsinradius` / `mobsinworld` / `mobsinchunk` / `mobsnearorigin` / `targets` | 实体计数 |
| `score` / `globalscore`（objective|o; value） | 记分板 |
| `serverversion` / `serverversionafterorequal` / `serverversionbefore` / `servernmsversion` | 服务器版本 |
| `enderdragonphase` | 末影龙阶段 |
| `signal`（signal|s） | 最近收到的信号（配合 sendsignal） |
| `variable`（variable|v; value|val; global|g） | 变量值匹配 |
| `moblevel`（level|l; maxLevel） | 实体等级范围 |

## 9. 定点刷怪点 spawners/*.yml

```yaml
UndeadCamp1:
  Mob: SkeletonKing              # 必填。引用 mobs/ 中的 Mob ID
  World: world                   # 世界名（需在 config.yml 世界过滤允许范围内）
  X: 100                         # 中心坐标
  Y: 64
  Z: 200
  Radius: 10                     # 刷新半径（范围内随机生成）
  MaxMobs: 3                     # 最大同时存在数
  Interval: 20s                  # 刷新间隔（10s / 200 / 5000ms）
  SpawnAmount: 1                 # 每次刷新数量
  Enabled: true                  # 是否启用
  Despawn:                       # 消失配置
    Distance: 128                # 玩家离开多远后可能消失
    Delay: 30                    # 超过距离多久消失（秒）
    RemoveWhenFarAway: true      # 是否允许自然消失
  Conditions:                    # 刷新条件（全部满足才刷新）
    - playersInRadius{r=30;min=1}    # 30 格内至少 1 名玩家
    - time{from=13000;to=23000}      # 游戏时间在范围内（0-23999）
    - js{file=custom.js;func=canSpawn}  # 自定义 JS 条件
```

未指定字段自动继承 `config.yml` 的 `spawning.*` 默认值。

**示例：夜晚刷新的亡灵营地**

```yaml
UndeadCamp1:
  Mob: SkeletonKing
  World: world
  X: 100
  Y: 64
  Z: 200
  Radius: 10
  MaxMobs: 3
  Interval: 20s
  Despawn: { Distance: 128, Delay: 30, RemoveWhenFarAway: true }
  Conditions:
    - playersInRadius{r=30;min=1}
    - time{from=13000;to=23000}
```

**Boss 刷新点（永不消失）**

```yaml
BossSpawner:
  Mob: DragonBoss
  World: world
  X: 0
  Y: 100
  Z: 0
  Radius: 5
  MaxMobs: 1
  Interval: 300s
  Despawn: { RemoveWhenFarAway: false }
  Conditions:
    - playersInRadius{r=50;min=3}
```

## 10. 随机生成 random-spawners/*.yml

类似 MythicMobs 的随机生成机制：定期在玩家周围随机位置按配置生成实体。

```yaml
DesertBandit:
  Mob: FireZombie                # 引用 mobs/ 中的 Mob ID
  Worlds:                        # 允许的世界（留空 = 所有世界）
    - world
  Biomes:                        # 群系关键字（如 DESERT、JUNGLE，留空 = 所有群系）
    - DESERT
  MinChunkDistance: 4            # 距玩家最小区块距离
  MaxChunkDistance: 10           # 距玩家最大区块距离
  MaxMobsPerWorld: 10            # 每世界此类实体上限
  Chance: 0.3                    # 每次尝试生成的概率 0-1
  MinLevel: 1                    # 生成实体的随机等级范围（MaxLevel=0 表示不使用等级）
  MaxLevel: 5
  Despawn: DESPAWN               # DESPAWN(远离消失) / CHUNK(区块卸载消失) / NEVER(永不消失)
  Enabled: true
```

随机生成不会强制加载未加载的区块，也不会在水面或空中生成。

## 11. 指令与权限

主命令 `/pandora`（别名 `/pd`），所有指令需要 `pandora.admin`（默认 OP）。

| 指令 | 说明 |
|------|------|
| `/pandora reload` | 重载所有配置（config、mobs、skills、spawners、random-spawners） |
| `/pandora spawn <mobId> [数量]` | 在玩家位置生成自定义实体 |
| `/pandora spawner list` | 列出所有刷怪点 |
| `/pandora spawner info <id>` | 查看刷怪点详情 |
| `/pandora spawner tp <id>` | 传送到刷怪点 |
| `/pandora spawner create <id> <mobId> [radius] [interval]` | 在当前位置创建刷怪点 |
| `/pandora spawner remove <id>` | 删除刷怪点 |
| `/pandora debug` | 查看准星指向实体的调试信息 |

## 12. JavaScript 扩展

Pandora 内置 Nashorn 引擎，可用 JS 编写技能逻辑、条件、AI 决策与刷怪条件。在 `config.yml` 中 `javascript.enabled: true` 启用；脚本放于 `plugins/Pandora/scripts/`，修改后 `/pandora reload`。

### 12.1 用法一览

| 场景 | 配置方式 | 绑定变量 | 返回值 |
|------|----------|----------|--------|
| 技能行调用 JS | `- script{file=skills/x.js;func=fn} @self` | `ctx`（上下文）、`targets`（目标列表） | true/false |
| 条件调用 JS | `?js{file=conditions/x.js;func=fn}` | `ctx`、`target` | true（允许）/false |
| 技能整体执行 | Skill 的 `Js: {Script:...; Function:...}` | `ctx`、`skill`、`caster`、`target` | false = 退回 YAML 技能行，其他 = 已处理 |
| Mob AI 决策 | Mob 的 `AI: {Script:...; Function:...}` | `entity`、`target`、`trigger`、`mobType` | 技能名列表（空列表退回静态 Skills） |
| 刷怪点条件 | Conditions 中 `- js{file=...;func=...}` | `center`（Location）、`spawnerId` | true = 允许刷怪 |

### 12.2 示例：技能脚本

```js
// scripts/skills/example_skills.js
function fireNova() {
    if (typeof targets === 'undefined' || targets == null) return false;
    var caster = ctx.getCaster();
    if (caster == null) return false;

    var it = targets.iterator();
    while (it.hasNext()) {
        it.next().damage(10.0, caster);
    }
    caster.getWorld().spawnParticle(org.bukkit.Particle.FLAME,
        caster.getLocation().add(0, 1.0, 0), 50, 1.5, 0.5, 1.5, 0.02);
    return true;
}
```

### 12.3 示例：Mob AI（多阶段 Boss）

```js
// scripts/mobs/ai/dragon_boss_ai.js
var Attribute = Java.type('org.bukkit.attribute.Attribute');

function decideSkills() {
    var trigger = String(trigger);
    if (trigger === 'ON_HURT') {
        var maxAttr = entity.getAttribute(Attribute.MAX_HEALTH);
        var ratio = maxAttr != null && maxAttr.getValue() > 0
            ? entity.getHealth() / maxAttr.getValue() : 1.0;
        if (ratio > 0.6) return ['FireNova'];                 // 第一阶段
        if (ratio > 0.3) return ['FireNova', 'SelfHeal'];     // 第二阶段
        return ['SelfHeal', 'SummonMinions', 'FireNova'];     // 狂暴阶段
    }
    return [];  // 其他触发器退回静态 Skills
}
```

### 12.4 注意事项

```js
// 通过 Java.type() 访问任意 Bukkit API 类
var Material = Java.type('org.bukkit.Material');
var Bukkit = Java.type('org.bukkit.Bukkit');

// Java String 比较：先转 JS 字符串
var t = String(trigger);
if (t === 'ON_HURT') { ... }

// 遍历 Java 集合
var it = collection.iterator();
while (it.hasNext()) { var item = it.next(); }

// 调试输出
ctx.getPlugin().getLogger().info('[JS] 目标: ' + target.getName());
```

- 脚本执行失败会自动退回 YAML 技能行，不会崩溃服务器
- 避免 `ON_TIMER` 中执行重计算；缓存 `Java.type()` 结果到文件顶部
- 可开启 `javascript.debug: true` 查看脚本执行详情

## 13. API（供开发者）

依赖 Pandora jar 后可监听以下自定义事件（对应 MythicMobs API 事件表）：

| 事件 | 触发时机 |
|------|----------|
| `org.gsn.pandora.api.PandoraMobSpawnEvent` | 自定义实体生成 |
| `org.gsn.pandora.api.PandoraMobDeathEvent` | 自定义实体死亡 |
| `org.gsn.pandora.api.PandoraMobDespawnEvent` | 自定义实体从世界消失（非死亡移除） |

每个事件提供 `getEntity()`（LivingEntity）与 `getMobType()`（MobType 配置对象）。

## 14. Folia 兼容性

Pandora 通过内置 `FoliaScheduler` 自动检测运行环境：在 Folia 上所有任务自动切换到 Region-aware 调度器，无需额外配置。注意：

- 跨区域传送（teleportto）在 Folia 上可能需要异步处理
- JS 脚本中避免直接操作非当前区域的实体

## 15. 故障排查

| 问题 | 排查步骤 |
|------|----------|
| Mob 不生成 | 1) Mob ID 拼写正确 2) `spawning.enabled: true` 3) 刷怪点 `Enabled: true` 4) 世界在过滤允许列表 5) Conditions 是否满足 6) `MaxMobs` 是否已满 |
| 刷怪太快/太慢 | 调整刷怪点 `Interval`、全局 `check-interval`、`SpawnAmount` |
| 实体不消失 | 检查 `Despawn.RemoveWhenFarAway`、`Distance`、`Delay` |
| 技能不触发 | 1) 触发器名拼写（onSpawn/onHurt/onAttack/onKill/onDeath/onCombat/onTimer/onSignal） 2) Cooldown 是否过长 3) 条件是否满足 4) 控制台是否有"未注册的 Mechanic/Targeter/Condition"警告 |
| 某世界刷怪点不工作 | 检查 `spawning.worlds.mode/list`（世界名大小写敏感）与刷怪点 `World` |
| JS 脚本报错 | 开启 `javascript.debug: true` 查看详细日志 |
| 配置重载后无效 | 确认 YAML 缩进正确、同目录无重复 ID，重新 `/pandora reload` |

**调试技巧**：

- `config.yml` 设置 `debug: true`
- `/pandora debug` 查看准星指向实体信息
- 技能行中使用 `- log{m=技能触发了;l=INFO} @self` 输出调试日志
```