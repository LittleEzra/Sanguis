package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.entity.projectile.GoldenQuarrel;
import com.feliscape.sanguis.content.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SanguisItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Sanguis.MOD_ID);

    public static final DeferredItem<VampireFangItem> BLOODY_FANG = ITEMS.registerItem("bloody_fang", VampireFangItem::new);
    public static final DeferredItem<Item> VAMPIRE_BLOOD = ITEMS.registerItem("vampire_blood", Item::new);
    public static final DeferredItem<InfectantItem> ACTIVATED_VAMPIRE_BLOOD = ITEMS.registerItem("activated_vampire_blood", InfectantItem::new);
    public static final DeferredItem<BloodBottleItem> BLOOD_BOTTLE = ITEMS.registerItem("blood_bottle",
            p -> new BloodBottleItem(p.stacksTo(1)
                    .component(SanguisDataComponents.BLOOD, 6)
                    .component(SanguisDataComponents.MAX_BLOOD, 6)
            ));

    public static final DeferredItem<Item> SYRINGE = ITEMS.registerItem("syringe", Item::new);
    public static final DeferredItem<GarlicInjectionItem> GARLIC_INJECTION = ITEMS.registerItem("garlic_injection",
            p -> new GarlicInjectionItem(p.stacksTo(1)));
    public static final DeferredItem<AcidInjectionItem> ACID_INJECTION = ITEMS.registerItem("acid_injection",
            p -> new AcidInjectionItem(p.stacksTo(1)));
    public static final DeferredItem<StakeItem> WOODEN_STAKE = ITEMS.registerItem("wooden_stake",
            p -> new StakeItem(p.stacksTo(1).attributes(SwordItem.createAttributes(Tiers.WOOD, 1, -1.6F))));
    public static final DeferredItem<GoldenQuarrelItem> GOLDEN_QUARREL = ITEMS.registerItem("golden_quarrel",
            p -> new GoldenQuarrelItem(p));

    public static final DeferredItem<GarlicItem> GARLIC = ITEMS.registerItem("garlic",
            p -> new GarlicItem(SanguisBlocks.GARLIC.get(), p.food(SanguisFoods.GARLIC)));
    public static final DeferredItem<Item> GARLIC_FLOWER = ITEMS.registerItem("garlic_flower",
            p -> new Item(p));

    public static final DeferredItem<DeferredSpawnEggItem> VAMPIRE_SPAWN_EGG = ITEMS.registerItem("vampire_spawn_egg",
            p -> new DeferredSpawnEggItem(SanguisEntityTypes.VAMPIRE, 0x404450, 0xb9214d, p));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
