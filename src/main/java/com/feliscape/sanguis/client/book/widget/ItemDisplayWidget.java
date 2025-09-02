package com.feliscape.sanguis.client.book.widget;

import com.feliscape.sanguis.Sanguis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemDisplayWidget extends BookWidget{
    private static final ResourceLocation FRAME = Sanguis.location("textures/gui/guide_book/item_display.png");

    public static final MapCodec<ItemDisplayWidget> CODEC = RecordCodecBuilder.mapCodec(
            inst -> codecStart(inst).and(
                    ItemStack.CODEC.fieldOf("item").forGetter(ItemDisplayWidget::getItemStack)
    ).apply(inst, ItemDisplayWidget::new));

    private ItemStack itemStack;

    public ItemDisplayWidget(int displayPage, WidgetType<?> type, int x, int y, ItemStack itemStack) {
        super(displayPage, type, x, y);
        this.itemStack = itemStack;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        guiGraphics.blit(FRAME, x + 43, this.getY() + y, 0, 0, 0, 50, 20, 50, 20);
        guiGraphics.renderItem(itemStack, x + 60, this.getY() + y + 2);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
