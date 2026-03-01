package com.feliscape.sanguis.client.screen.ability;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.client.render.SanguisRenderTypes;
import com.feliscape.sanguis.content.ability.ActiveVampireAbility;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.feliscape.sanguis.networking.payload.ability.SetActiveAbilityPayload;
import com.feliscape.sanguis.registry.SanguisKeyMappings;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class VampireAbilityWheel extends Screen {
    private static final ResourceLocation RADIAL_MENU = Sanguis.location("textures/gui/radial_menu.png");
    private static final ResourceLocation WHEEL = Sanguis.location("ability/wheel");
    private static final ResourceLocation ABILITY_SLOT = Sanguis.location("ability/ability_slot");

    public VampireAbilityWheel() {
        super(GameNarrator.NO_TITLE);
    }

    private List<ActiveVampireAbility> abilities = List.of();

    @Override
    protected void init() {
        super.init();
        if (minecraft == null || minecraft.player == null) return;
        abilities = minecraft.player.getData(VampireAbilityData.type()).getActiveAbilities();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // remove rendering of the background
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == SanguisKeyMappings.OPEN_ABILITY_WHEEL.get().getKey().getValue()){
            this.onClose();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (selectedAbility > -1){
            PacketDistributor.sendToServer(new SetActiveAbilityPayload(abilities.get(selectedAbility), this.minecraft.player.getId()));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int middleX = guiGraphics.guiWidth() / 2;
        int middleY = guiGraphics.guiHeight() / 2;

        guiGraphics.blitSprite(WHEEL, middleX - 52, middleY - 52, 104, 104);

        if (abilities.isEmpty()) return;

        selectedAbility = -1;
        renderAbilities(guiGraphics, mouseX, mouseY, partialTick);
        if (selectedAbility > -1) {
            guiGraphics.renderTooltip(font,
                    VampireAbilitiesScreen.getAbilityTooltip(abilities.get(selectedAbility)),
                    Optional.empty(),
                    mouseX, mouseY);
        }
    }

    private int selectedAbility = -1;

    private void renderWheel(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        if (abilities.isEmpty()) return;

        int middleX = guiGraphics.guiWidth() / 2;
        int middleY = guiGraphics.guiHeight() / 2;

        PoseStack poseStack = guiGraphics.pose();

        float angleStepSize = Mth.TWO_PI / (float) abilities.size();
        for (int i = 0; i < abilities.size(); i++){
            float theta = i * angleStepSize - Mth.HALF_PI;
            float minTheta = theta - Math.min(angleStepSize * 0.2F, 0.4F);
            float maxTheta = theta + Math.min(angleStepSize * 0.2F, 0.4F);

            poseStack.pushPose();
            VertexConsumer consumer = guiGraphics.bufferSource().getBuffer(SanguisRenderTypes.guiTriangle(RADIAL_MENU));


            consumer.addVertex(
                    middleX + Mth.cos(theta) * 18.0F,
                    middleY + Mth.sin(theta) * 18.0F,
                    0.0F
            )
                    .setUv(0.5F, 1.0F)
                    .setColor(0x003A3A3A)
            ;
            consumer.addVertex(
                            middleX + Mth.cos(maxTheta) * 70.0F,
                            middleY + Mth.sin(maxTheta) * 70.0F,
                            0.0F
                    )
                    .setUv(0.0F, 0.0F)
                    .setColor(0xFF3A3A3A)
            ;
            consumer.addVertex(
                            middleX + Mth.cos(minTheta) * 70.0F,
                            middleY + Mth.sin(minTheta) * 70.0F,
                            0.0F
                    )
                    .setUv(1.0F, 1.0F)
                    .setColor(0xFF3A3A3A)
            ;

            poseStack.popPose();
        }
    }

    private void renderAbilities(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        if (abilities.isEmpty()) return;

        int middleX = guiGraphics.guiWidth() / 2;
        int middleY = guiGraphics.guiHeight() / 2;

        double angleStepSize = Math.TAU / (double)abilities.size();
        for (int i = 0; i < abilities.size(); i++){
            var ability = abilities.get(i);

            double distance = 48;

            double theta = i * angleStepSize - Mth.HALF_PI;
            int x = Mth.floor(Math.cos(theta) * distance);
            int y = Mth.floor(Math.sin(theta) * distance);

            if (mouseX >= (middleX + x) - 10 && mouseY >= (middleY + y) - 10 && mouseX <= (middleX + x) + 10 && mouseY <= (middleY + y) + 10){
                selectedAbility = i;
            }

            guiGraphics.blitSprite(ABILITY_SLOT, middleX + x - 15, middleY + y - 15, 30, 30);
            VampireAbilitiesScreen.renderAbility(ability, guiGraphics, middleX + x - 8, middleY + y - 8);
        }
    }
}
