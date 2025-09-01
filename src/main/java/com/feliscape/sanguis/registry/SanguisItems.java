package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.entity.projectile.GoldenQuarrel;
import com.feliscape.sanguis.content.item.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SanguisItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Sanguis.MOD_ID);

    public static final DeferredItem<GuideBookItem> DAEMONOLOGIE = ITEMS.registerItem("daemonologie",
            p -> new GuideBookItem(p.stacksTo(1)));

    public static final DeferredItem<Item> BAT_WING = ITEMS.registerItem("bat_wing", Item::new);
    public static final DeferredItem<VampireFangItem> BLOODY_FANG = ITEMS.registerItem("bloody_fang", VampireFangItem::new);
    public static final DeferredItem<Item> VAMPIRE_BLOOD = ITEMS.registerItem("vampire_blood", Item::new);
    public static final DeferredItem<InfectantItem> ACTIVATED_VAMPIRE_BLOOD = ITEMS.registerItem("activated_vampire_blood", InfectantItem::new);
    public static final DeferredItem<BloodBottleItem> BLOOD_BOTTLE = ITEMS.registerItem("blood_bottle",
            p -> new BloodBottleItem(p.stacksTo(1)
                    .component(SanguisDataComponents.BLOOD, 6)
                    .component(SanguisDataComponents.MAX_BLOOD, 6)
            ));
    public static final DeferredItem<ParasolItem> PARASOL = ITEMS.registerItem("parasol", p ->
            new ParasolItem(p.stacksTo(1)));
    public static final DeferredItem<BloodSoakedCoinItem> BLOOD_SOAKED_COIN = ITEMS.registerItem("blood_soaked_coin",
            p -> new BloodSoakedCoinItem(p.stacksTo(1)));
    public static final DeferredItem<BlackenedCoinItem> BLACKENED_COIN = ITEMS.registerItem("blackened_coin",
            p -> new BlackenedCoinItem(p.stacksTo(1)
                    .component(DataComponents.FIRE_RESISTANT, Unit.INSTANCE)));

    public static final DeferredItem<Item> STEEL_BLEND = ITEMS.registerItem("steel_blend", Item::new);
    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.registerItem("steel_ingot", Item::new);

    public static final DeferredItem<Item> SYRINGE = ITEMS.registerItem("syringe", Item::new);
    public static final DeferredItem<GarlicInjectionItem> GARLIC_INJECTION = ITEMS.registerItem("garlic_injection",
            p -> new GarlicInjectionItem(p.stacksTo(1)));
    public static final DeferredItem<AcidInjectionItem> ACID_INJECTION = ITEMS.registerItem("acid_injection",
            p -> new AcidInjectionItem(p.stacksTo(1)));
    public static final DeferredItem<StakeItem> WOODEN_STAKE = ITEMS.registerItem("wooden_stake",
            p -> new StakeItem(Tiers.WOOD, p.stacksTo(1).attributes(SwordItem.createAttributes(Tiers.WOOD, 1, -2.3F))));
    public static final DeferredItem<StakeItem> REINFORCED_STAKE = ITEMS.registerItem("reinforced_stake",
            p -> new StakeItem(Tiers.IRON, p.stacksTo(1).attributes(SwordItem.createAttributes(Tiers.IRON, 0, -2.3F))));

    public static final DeferredItem<CleaverItem> STEEL_CLEAVER = ITEMS.registerItem("steel_cleaver",
            p -> new CleaverItem(SanguisTiers.STEEL, 0.25F, p.stacksTo(1).attributes(SwordItem.createAttributes(SanguisTiers.STEEL, 6, -3.0F))));
    public static final DeferredItem<CleaverItem> DIAMOND_CLEAVER = ITEMS.registerItem("diamond_cleaver",
            p -> new CleaverItem(Tiers.DIAMOND, 0.35F, p.stacksTo(1).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 7, -3.0F))));

    public static final DeferredItem<GoldenQuarrelItem> GOLDEN_QUARREL = ITEMS.registerItem("golden_quarrel",
            p -> new GoldenQuarrelItem(p));

    public static final DeferredItem<GarlicItem> GARLIC = ITEMS.registerItem("garlic",
            p -> new GarlicItem(SanguisBlocks.GARLIC.get(), p.food(SanguisFoods.GARLIC)));
    public static final DeferredItem<Item> GARLIC_FLOWER = ITEMS.registerItem("garlic_flower",
            p -> new Item(p));

    public static final DeferredItem<DeferredSpawnEggItem> VAMPIRE_SPAWN_EGG = ITEMS.registerItem("vampire_spawn_egg",
            p -> new DeferredSpawnEggItem(SanguisEntityTypes.VAMPIRE, 0x404450, 0xb9214d, p));
    public static final DeferredItem<DeferredSpawnEggItem> VAMPIRE_HUNTER_SPAWN_EGG = ITEMS.registerItem("vampire_hunter_spawn_egg",
            p -> new DeferredSpawnEggItem(SanguisEntityTypes.VAMPIRE_HUNTER, 0x404355, 0x4865e1, p));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
