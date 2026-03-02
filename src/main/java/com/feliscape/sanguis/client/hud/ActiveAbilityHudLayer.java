package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.screen.ability.VampireAbilitiesScreen;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.HunterUtil;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class ActiveAbilityHudLayer extends HudLayer {
    public static final ResourceLocation LOCATION = Sanguis.location("active_ability");
    private static final ResourceLocation SLOT = Sanguis.location("ability/active_ability_slot");
    private static final ResourceLocation BACKGROUND_TOP = Sanguis.location("ability/active_ability");
    private static final ResourceLocation BACKGROUND_BOTTOM = Sanguis.location("ability/active_ability_bottom");

    @Override
    public void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {

        if (VampireUtil.isVampire(player) && player.hasData(VampireAbilityData.type())){
            VampireAbilityData abilities = player.getData(VampireAbilityData.type());

            if (abilities.getActiveAbility() == null) return;
            HumanoidArm arm = player.getMainArm();
            int distance = 91 + 38;
            int x = (guiGraphics.guiWidth() / 2 + (arm == HumanoidArm.RIGHT ? distance : -distance));
            int y = guiGraphics.guiHeight() - 11;

            guiGraphics.blitSprite(SLOT, x - 11, y - 11, 22, 22);
            VampireAbilitiesScreen.renderAbility(abilities.getActiveAbility(), guiGraphics, x - 8, y - 8);
        }
    }
}
