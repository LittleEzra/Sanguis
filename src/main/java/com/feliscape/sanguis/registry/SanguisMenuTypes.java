package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.menu.ActiveQuestsMenu;
import com.feliscape.sanguis.content.menu.GuideBookMenu;
import com.feliscape.sanguis.content.menu.QuestBoardMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(Registries.MENU, Sanguis.MOD_ID);

    public static final Supplier<MenuType<ActiveQuestsMenu>> ACTIVE_QUESTS = MENU_TYPES.register("active_quests",
            () -> IMenuTypeExtension.create(ActiveQuestsMenu::new));
    public static final Supplier<MenuType<QuestBoardMenu>> QUEST_BOARD = MENU_TYPES.register("quest_board",
            () -> IMenuTypeExtension.create(QuestBoardMenu::new));
    public static final Supplier<MenuType<GuideBookMenu>> GUIDE_BOOK = MENU_TYPES.register("guide_book",
            () -> IMenuTypeExtension.create(GuideBookMenu::new));

    public static void register(IEventBus eventBus){
        MENU_TYPES.register(eventBus);
    }
}
