// 示例 JS 刷怪点条件：
// 只在夜晚 (13000-23000) 且附近实体数量少于 30 时允许刷怪。
//
// 使用方式：
//   Conditions:
//     - js{file=example_spawners.js;func=nightAndNotCrowded}

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
