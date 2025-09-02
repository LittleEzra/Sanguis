package com.feliscape.sanguis.client.book.widget;

import com.feliscape.sanguis.client.registry.WidgetTypeRegistry;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;

public abstract class BookWidget {
    public static final Codec<BookWidget> TYPED_CODEC = WidgetTypeRegistry.CODEC
            .dispatch("type", BookWidget::getType, WidgetType::codec);

    private int displayPage;
    private WidgetType<?> type;
    private int x;
    private int y;

    protected static <T extends BookWidget> Products.P4<RecordCodecBuilder.Mu<T>, Integer, WidgetType<?>, Integer, Integer> codecStart(RecordCodecBuilder.Instance<T> instance){
        return instance.group(
                Codec.INT.fieldOf("display_page").forGetter(BookWidget::getDisplayPage),
                WidgetTypeRegistry.CODEC.fieldOf("type").forGetter(BookWidget::getType),
                Codec.INT.fieldOf("x").forGetter(BookWidget::getX),
                Codec.INT.fieldOf("y").forGetter(BookWidget::getY)
        );
    }

    public BookWidget(int displayPage, WidgetType<?> type, int x, int y) {
        this.displayPage = displayPage;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public int getDisplayPage() {
        return displayPage;
    }

    public WidgetType<?> getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void render(GuiGraphics guiGraphics, int x, int y, float partialTick);
}
