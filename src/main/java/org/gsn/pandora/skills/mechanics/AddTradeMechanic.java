package org.gsn.pandora.skills.mechanics;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.ItemStack;
import org.gsn.pandora.skills.Mechanic;
import org.gsn.pandora.skills.SkillExecutionContext;
import org.gsn.pandora.util.MaterialUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 修改村民交易。
 * action|mode|m=ADD  slot|s|index=0
 * ingredient|item|i=STONE  ingredient2|item2|i2=...  result|r=STONE
 * maxuses|uses|u=2147483647  expReward|exp=false  villagerExperience|villExp|vexp=0
 */
public class AddTradeMechanic implements Mechanic {

    public enum Action { ADD, REMOVE, REPLACE }

    private final Action action;
    private final int slot;
    private final ItemStack ingredient1;
    private final ItemStack ingredient2;
    private final ItemStack result;
    private final int maxUses;
    private final boolean expReward;
    private final int villagerExp;

    public AddTradeMechanic(String actionName, int slot,
                            String ing1, String ing2, String res,
                            int maxUses, boolean expReward, int villagerExp) {
        Action a = Action.ADD;
        try { a = Action.valueOf(actionName == null ? "ADD" : actionName.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ignored) {}
        this.action = a;
        this.slot = Math.max(0, slot);
        this.ingredient1 = parseMat(ing1, "STONE");
        this.ingredient2 = ing2 != null && !ing2.isBlank() ? parseMat(ing2, null) : null;
        this.result = parseMat(res, "STONE");
        this.maxUses = maxUses <= 0 ? Integer.MAX_VALUE : maxUses;
        this.expReward = expReward;
        this.villagerExp = villagerExp;
    }

    private ItemStack parseMat(String name, String def) {
        if (name == null || name.isBlank()) {
            return def != null ? new ItemStack(org.bukkit.Material.valueOf(def)) : null;
        }
        String upper = name.contains(":") ? name.substring(name.indexOf(':') + 1).toUpperCase(Locale.ROOT)
            : name.toUpperCase(Locale.ROOT);
        try { return new ItemStack(org.bukkit.Material.valueOf(upper)); }
        catch (IllegalArgumentException e) {
            var mat = MaterialUtil.matchMaterialByNameOrChinese(name);
            return mat != null ? new ItemStack(mat) : (def != null ? new ItemStack(org.bukkit.Material.valueOf(def)) : null);
        }
    }

    @Override
    public boolean execute(SkillExecutionContext context, List<LivingEntity> targets) {
        if (targets == null) return false;
        boolean any = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Villager v)) continue;
            List<MerchantRecipe> recipes = new ArrayList<>(v.getRecipes());
            switch (action) {
                case ADD -> {
                    MerchantRecipe recipe = new MerchantRecipe(result.clone(), maxUses);
                    recipe.addIngredient(ingredient1.clone());
                    if (ingredient2 != null) recipe.addIngredient(ingredient2.clone());
                    recipe.setExperienceReward(expReward);
                    recipe.setVillagerExperience(villagerExp);
                    if (slot >= recipes.size()) recipes.add(recipe);
                    else recipes.add(slot, recipe);
                }
                case REMOVE -> {
                    if (slot < recipes.size()) recipes.remove(slot);
                }
                case REPLACE -> {
                    MerchantRecipe recipe = new MerchantRecipe(result.clone(), maxUses);
                    recipe.addIngredient(ingredient1.clone());
                    if (ingredient2 != null) recipe.addIngredient(ingredient2.clone());
                    recipe.setExperienceReward(expReward);
                    recipe.setVillagerExperience(villagerExp);
                    if (slot < recipes.size()) recipes.set(slot, recipe);
                    else recipes.add(recipe);
                }
            }
            v.setRecipes(recipes);
            any = true;
        }
        return any;
    }
}
