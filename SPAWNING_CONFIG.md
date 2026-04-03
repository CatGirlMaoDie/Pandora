# Pandora 实体刷新配置说明

本文档详细说明了 Pandora 插件中实体刷新系统的所有配置参数。

## 目录
- [全局配置 (config.yml)](#全局配置-configyml)
- [刷怪点配置 (spawners.yml)](#刷怪点配置-spawnersyml)
- [配置示例](#配置示例)

---

## 全局配置 (config.yml)

全局配置文件控制整个刷怪系统的默认行为。

### spawning 节点

```yaml
spawning:
  # 是否启用刷怪系统
  enabled: true

  # 全局刷新检查频率（tick，20 tick = 1 秒）
  check-interval: 20

  # 默认刷新间隔（当 spawners.yml 中未指定时使用）
  default-interval: 10s

  # 默认每次刷新数量
  default-spawn-amount: 1

  # 默认最大实体数量
  default-max-mobs: 5

  # 默认刷新半径
  default-radius: 5.0

  # 实体自然消失配置
  despawn:
    # 是否启用自然消失
    enabled: true

    # 默认消失距离（方块，玩家离开此距离后实体可能消失）
    distance: 128

    # 默认消失延迟（秒，实体超过距离后多久消失）
    delay: 30

    # 是否在服务器重启后清理刷怪点生成的实体
    clear-on-restart: false

  # 刷新条件默认值
  conditions:
    # 默认需要的最小玩家数
    min-players: 1

    # 默认玩家检测半径
    player-radius: 32.0

  # 世界过滤配置
  worlds:
    # 过滤模式：whitelist（白名单）或 blacklist（黑名单）
    # whitelist: 只在列表中的世界刷新
    # blacklist: 在除列表外的所有世界刷新
    mode: whitelist

    # 世界列表
    list:
      - world
      - world_nether
      - world_the_end
```

### 参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `spawning.enabled` | boolean | true | 是否启用整个刷怪系统 |
| `spawning.check-interval` | long | 20 | 刷怪系统检查频率（tick） |
| `spawning.default-interval` | string | "10s" | 默认刷新间隔 |
| `spawning.default-spawn-amount` | int | 1 | 默认每次刷新数量 |
| `spawning.default-max-mobs` | int | 5 | 默认最大实体数量 |
| `spawning.default-radius` | double | 5.0 | 默认刷新半径 |
| `spawning.despawn.enabled` | boolean | true | 是否启用自然消失 |
| `spawning.despawn.distance` | double | 128.0 | 默认消失距离 |
| `spawning.despawn.delay` | int | 30 | 默认消失延迟（秒） |
| `spawning.despawn.clear-on-restart` | boolean | false | 重启时是否清理实体 |
| `spawning.conditions.min-players` | int | 1 | 默认最小玩家数 |
| `spawning.conditions.player-radius` | double | 32.0 | 默认玩家检测半径 |
| `spawning.worlds.mode` | string | "whitelist" | 世界过滤模式（whitelist/blacklist） |
| `spawning.worlds.list` | list | ["world"] | 世界过滤列表 |

---

## 刷怪点配置 (spawners.yml)

每个刷怪点可以单独配置其行为。

### 基本配置

```yaml
SpawnerID:
  # 要刷新的实体 ID（在 mobs.yml 中定义）
  Mob: EntityID

  # 世界名称
  World: world

  # 刷怪点中心坐标
  X: 100
  Y: 64
  Z: 200

  # 刷新半径（实体会在此范围内随机生成）
  Radius: 10

  # 此刷怪点最多同时存在的实体数量
  MaxMobs: 3

  # 刷新间隔（支持格式：10s=10秒, 200=200tick, 5000ms=5000毫秒）
  Interval: 20s

  # 每次刷新生成的实体数量（可选，默认为 1）
  SpawnAmount: 1

  # 是否启用此刷怪点（可选，默认为 true）
  Enabled: true
```

### 实体消失配置

```yaml
SpawnerID:
  # ... 基本配置 ...

  # 实体消失配置（可选）
  Despawn:
    # 玩家离开多远后实体可能消失（方块）
    Distance: 128

    # 超过距离后多久消失（秒）
    Delay: 30

    # 是否允许自然消失（true/false）
    RemoveWhenFarAway: true
```

### 刷新条件配置

```yaml
SpawnerID:
  # ... 基本配置 ...

  # 刷新条件（可选，所有条件都满足才会刷新）
  Conditions:
    # 范围内至少有指定数量的玩家
    - playersInRadius{r=30;min=1}

    # 游戏时间在指定范围内（0-23999）
    # 13000-23000 表示夜晚
    - time{from=13000;to=23000}

    # 自定义 JavaScript 条件
    - js{file=custom_condition.js;func=canSpawn}
```

### 参数说明

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `Mob` | string | 是 | - | 实体 ID |
| `World` | string | 否 | "world" | 世界名称 |
| `X` | double | 是 | - | X 坐标 |
| `Y` | double | 是 | - | Y 坐标 |
| `Z` | double | 是 | - | Z 坐标 |
| `Radius` | double | 否 | 5.0 | 刷新半径 |
| `MaxMobs` | int | 否 | 5 | 最大实体数量 |
| `Interval` | string | 否 | "10s" | 刷新间隔 |
| `SpawnAmount` | int | 否 | 1 | 每次刷新数量 |
| `Enabled` | boolean | 否 | true | 是否启用 |
| `Despawn.Distance` | double | 否 | 128.0 | 消失距离 |
| `Despawn.Delay` | int | 否 | 30 | 消失延迟（秒） |
| `Despawn.RemoveWhenFarAway` | boolean | 否 | true | 是否自然消失 |

---

## 配置示例

### 示例 1：夜晚刷新的亡灵营地

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
  SpawnAmount: 1
  Enabled: true
  Despawn:
    Distance: 128
    Delay: 30
    RemoveWhenFarAway: true
  Conditions:
    - playersInRadius{r=30;min=1}
    - time{from=13000;to=23000}
```

**说明**：
- 只在夜晚（游戏时间 13000-23000）刷新
- 需要至少 1 个玩家在 30 方块范围内
- 最多同时存在 3 个实体
- 每 20 秒检查一次是否需要刷新
- 玩家离开 128 方块后，30 秒后实体消失

### 示例 2：快速刷新的火焰深坑

```yaml
FirePit:
  Mob: FireZombie
  World: world
  X: 110
  Y: 64
  Z: 210
  Radius: 8
  MaxMobs: 6
  Interval: 10s
  SpawnAmount: 2
  Enabled: true
  Despawn:
    Distance: 96
    Delay: 20
    RemoveWhenFarAway: true
  Conditions:
    - playersInRadius{r=25;min=1}
```

**说明**：
- 全天候刷新（无时间限制）
- 每次刷新生成 2 个实体
- 最多同时存在 6 个实体
- 每 10 秒检查一次
- 玩家离开 96 方块后，20 秒后实体消失

### 示例 3：Boss 刷新点

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
  SpawnAmount: 1
  Enabled: false
  Despawn:
    RemoveWhenFarAway: false
  Conditions:
    - playersInRadius{r=50;min=3}
```

**说明**：
- 默认禁用（需要手动启用）
- 需要至少 3 个玩家在 50 方块范围内
- 只刷新 1 个 Boss
- 每 5 分钟检查一次
- Boss 永不自然消失

### 示例 4：全天候守卫哨站

```yaml
GuardPost:
  Mob: GuardSoldier
  World: world
  X: 50
  Y: 64
  Z: 50
  Radius: 12
  MaxMobs: 8
  Interval: 30s
  SpawnAmount: 2
  Enabled: true
  Despawn:
    Distance: 150
    Delay: 60
    RemoveWhenFarAway: true
  Conditions:
    - playersInRadius{r=40;min=1}
```

**说明**：
- 全天候刷新
- 每次刷新 2 个守卫
- 最多 8 个守卫
- 每 30 秒检查一次
- 玩家离开 150 方块后，60 秒后消失

---

## 刷新条件详解

### playersInRadius

检查范围内是否有足够的玩家。

```yaml
- playersInRadius{r=半径;min=最小玩家数}
```

**参数**：
- `r`: 检测半径（方块）
- `min`: 最小玩家数

**示例**：
```yaml
- playersInRadius{r=30;min=1}  # 30 方块内至少 1 个玩家
- playersInRadius{r=50;min=3}  # 50 方块内至少 3 个玩家
```

### time

检查游戏时间是否在指定范围内。

```yaml
- time{from=开始时间;to=结束时间}
```

**参数**：
- `from`: 开始时间（0-23999）
- `to`: 结束时间（0-23999）

**时间参考**：
- 0-12000: 白天
- 13000-23000: 夜晚
- 18000: 午夜

**示例**：
```yaml
- time{from=13000;to=23000}  # 夜晚
- time{from=0;to=12000}      # 白天
- time{from=23000;to=1000}   # 跨越午夜
```

### js

使用自定义 JavaScript 脚本作为条件。

```yaml
- js{file=脚本文件;func=函数名}
```

**参数**：
- `file`: 脚本文件名（位于 `plugins/Pandora/scripts/spawners/` 目录）
- `func`: 函数名（默认为 `canSpawn`）

**示例**：
```yaml
- js{file=custom_condition.js;func=canSpawn}
```

**脚本示例** (`scripts/spawners/custom_condition.js`)：
```javascript
function canSpawn(center, spawnerId) {
    // center: 刷怪点中心位置
    // spawnerId: 刷怪点 ID

    // 自定义逻辑
    var world = center.getWorld();
    var time = world.getTime();

    // 只在特定时间刷新
    return time >= 18000 && time <= 22000;
}
```

---

## 时间格式说明

`Interval` 参数支持多种时间格式：

| 格式 | 示例 | 说明 |
|------|------|------|
| 秒 | `10s` | 10 秒 |
| 毫秒 | `5000ms` | 5000 毫秒 = 5 秒 |
| Tick | `200` | 200 tick = 10 秒 |

**注意**：1 秒 = 20 tick = 1000 毫秒

---

## 世界过滤详解

世界过滤功能允许你控制实体刷新系统在哪些世界中生效。

### 配置方式

```yaml
spawning:
  worlds:
    mode: whitelist  # 或 blacklist
    list:
      - world
      - world_nether
      - world_the_end
```

### 过滤模式

#### 白名单模式 (whitelist)

只在列表中的世界启用刷新系统。

```yaml
spawning:
  worlds:
    mode: whitelist
    list:
      - world
      - custom_world
```

**效果**：
- ✅ `world` - 允许刷新
- ✅ `custom_world` - 允许刷新
- ❌ `world_nether` - 不允许刷新
- ❌ `other_world` - 不允许刷新

#### 黑名单模式 (blacklist)

在除列表外的所有世界启用刷新系统。

```yaml
spawning:
  worlds:
    mode: blacklist
    list:
      - lobby
      - spawn
```

**效果**：
- ❌ `lobby` - 不允许刷新
- ❌ `spawn` - 不允许刷新
- ✅ `world` - 允许刷新
- ✅ `world_nether` - 允许刷新
- ✅ `custom_world` - 允许刷新

### 使用场景

#### 场景 1：只在主世界刷新

```yaml
spawning:
  worlds:
    mode: whitelist
    list:
      - world
```

#### 场景 2：在所有世界刷新，除了大厅和出生点

```yaml
spawning:
  worlds:
    mode: blacklist
    list:
      - lobby
      - spawn
      - safe_zone
```

#### 场景 3：只在特定的 RPG 世界刷新

```yaml
spawning:
  worlds:
    mode: whitelist
    list:
      - rpg_world_1
      - rpg_world_2
      - dungeon_world
```

### 注意事项

1. **世界名称大小写敏感**：确保世界名称与服务器中的完全一致
2. **默认行为**：如果列表为空，默认只允许 `world` 世界
3. **刷怪点配置优先**：即使世界在过滤列表中，刷怪点的 `World` 配置仍然有效
4. **动态加载**：修改配置后需要重载插件（`/pandora reload`）

---

## 常见配置场景

### 1. 禁用某个刷怪点

```yaml
SpawnerID:
  Enabled: false
  # ... 其他配置 ...
```

### 2. 禁用整个刷怪系统

在 `config.yml` 中：
```yaml
spawning:
  enabled: false
```

### 3. 实体永不消失

```yaml
SpawnerID:
  Despawn:
    RemoveWhenFarAway: false
```

### 4. 快速刷新大量实体

```yaml
SpawnerID:
  Interval: 5s
  SpawnAmount: 5
  MaxMobs: 20
```

### 5. 只在有多个玩家时刷新

```yaml
SpawnerID:
  Conditions:
    - playersInRadius{r=50;min=5}
```

### 6. 只在特定世界刷新

在 `config.yml` 中：
```yaml
spawning:
  worlds:
    mode: whitelist
    list:
      - world
      - custom_world
```

### 7. 禁止在大厅世界刷新

在 `config.yml` 中：
```yaml
spawning:
  worlds:
    mode: blacklist
    list:
      - lobby
      - spawn
```

---

## 世界与刷怪点的关系

### 全局世界过滤 vs 刷怪点世界配置

- **全局世界过滤**（`config.yml` 中的 `spawning.worlds`）：控制整个刷新系统在哪些世界生效
- **刷怪点世界配置**（`spawners.yml` 中的 `World`）：指定刷怪点所在的世界

### 工作流程

1. 系统首先检查全局世界过滤
2. 如果世界不在允许列表中，跳过该世界的所有刷怪点
3. 如果世界在允许列表中，检查该世界的刷怪点配置
4. 执行刷怪点的条件检查和刷新逻辑

### 示例

**config.yml**:
```yaml
spawning:
  worlds:
    mode: whitelist
    list:
      - world
      - rpg_world
```

**spawners.yml**:
```yaml
MainWorldSpawner:
  World: world
  # ... 其他配置 ...
  # ✅ 会刷新（world 在白名单中）

RPGWorldSpawner:
  World: rpg_world
  # ... 其他配置 ...
  # ✅ 会刷新（rpg_world 在白名单中）

NetherSpawner:
  World: world_nether
  # ... 其他配置 ...
  # ❌ 不会刷新（world_nether 不在白名单中）
```

---

## 性能优化建议

1. **合理设置检查间隔**：`check-interval` 不要设置得太小，建议 20-40 tick
2. **控制刷怪点数量**：过多的刷怪点会影响性能
3. **使用条件限制**：通过条件避免不必要的刷新
4. **合理设置消失距离**：避免实体堆积
5. **禁用不需要的刷怪点**：使用 `Enabled: false`

---

## 故障排查

### 实体不刷新

1. 检查 `spawning.enabled` 是否为 `true`
2. 检查刷怪点的 `Enabled` 是否为 `true`
3. 检查 `Conditions` 是否满足
4. 检查 `MaxMobs` 是否已达上限
5. 检查实体 ID 是否在 `mobs.yml` 中定义

### 实体刷新太快/太慢

1. 调整 `Interval` 参数
2. 调整 `check-interval` 参数
3. 检查 `SpawnAmount` 设置

### 实体不消失

1. 检查 `Despawn.RemoveWhenFarAway` 是否为 `true`
2. 检查 `Despawn.Distance` 和 `Despawn.Delay` 设置
3. 确认玩家确实离开了指定距离

### 某个世界的刷怪点不工作

1. 检查 `spawning.worlds.mode` 和 `spawning.worlds.list` 配置
2. 确认世界名称拼写正确（大小写敏感）
3. 如果使用白名单模式，确保世界在列表中
4. 如果使用黑名单模式，确保世界不在列表中
5. 检查刷怪点的 `World` 配置是否正确

### 所有世界都不刷新

1. 检查 `spawning.enabled` 是否为 `true`
2. 检查 `spawning.worlds.list` 是否为空
3. 检查世界过滤模式是否配置正确

---

## 更新日志

- **v1.0**: 初始版本，支持基本刷新功能
- **v1.1**: 添加完整的刷新控制系统
  - 添加 `SpawnAmount` 参数（每次刷新数量）
  - 添加 `Enabled` 参数（单独控制刷怪点启用状态）
  - 添加 `Despawn` 配置（实体自然消失机制）
  - 添加全局配置和默认值支持
  - 添加世界过滤功能（白名单/黑名单模式）
