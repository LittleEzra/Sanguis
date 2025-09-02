package com.feliscape.sanguis.client.screen;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.client.book.BookChapter;
import com.feliscape.sanguis.client.book.BookEntry;
import com.feliscape.sanguis.client.book.BookLink;
import com.feliscape.sanguis.client.book.GuideBookManager;
import com.feliscape.sanguis.client.book.chapter.ChapterEntry;
import com.feliscape.sanguis.client.book.chapter.LinkChapterEntry;
import com.feliscape.sanguis.client.book.widget.BookWidget;
import com.feliscape.sanguis.content.menu.GuideBookMenu;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuideBookScreen extends AbstractContainerScreen<GuideBookMenu> {
    public static final int PAGE_SIZE_IN_LINES = 18;
    public static final Component ARROW = Component.literal("→ ");
    public static final float ITEM_SCALE = 0.6667F;
    public static final int INDENT_SIZE = 8;

    private static final ResourceLocation BACKGROUND_LOCATION = Sanguis.location("textures/gui/container/daemonologie.png");
    private static final ResourceLocation INDEX_ART = Sanguis.location("textures/gui/guide_book/index_art.png");

    private static final int TEXT_LINK_HOVER_COLOR = 0xff6785a6;
    private static final int TEXT_LINK_COLOR = 0xff454d5b;
    private static final int TEXT_LINK_DISABLED_COLOR = 0xff9a9ca1;

    @Nullable
    private BookEntry currentEntry;
    @Nullable
    private BookChapter currentChapter;

    private FlipBackButton flipBackButton;
    private FlipForwardButton flipForwardButton;
    private MoveUpButton moveUpButton;

    private int pageNumber = 0;
    private int hoveredDirection = -1;

    public GuideBookScreen(GuideBookMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.imageWidth = 336;
        this.imageHeight = 209;
        super.init();
        //this.openEntry(SanguisClient.reloadListeners().getGuideBookManager().getEntry(GuideBookManager.ROOT_LOCATION));
        flipBackButton = this.addRenderableWidget(new FlipBackButton(this.leftPos + 9, this.topPos + 191, 21, 12));
        flipForwardButton = this.addRenderableWidget(new FlipForwardButton(this.leftPos + 306, this.topPos + 191, 21, 12));
        moveUpButton = this.addRenderableWidget(new MoveUpButton(this.leftPos + 171, this.topPos + 202, 15, 20));
    }

    public void openEntry(@Nullable BookEntry entry){
        this.currentEntry = entry;
        if (currentEntry != null)
            currentEntry.init(this);
        pageNumber = 0;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }
    public void openEntry(@Nullable ResourceLocation location){
        Sanguis.LOGGER.debug("Going to entry {}", location);
        openEntry(location == null ? null : SanguisClient.reloadListeners().getGuideBookManager().getEntry(location));
    }
    public void openChapter(@Nullable BookChapter entry){
        this.currentChapter = entry;
        pageNumber = 0;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }
    public void openChapter(@Nullable ResourceLocation location){
        Sanguis.LOGGER.debug("Going to chapter {}", location);
        if (location == GuideBookManager.ROOT_LOCATION){
            goToIndex();
            return;
        }

        openChapter(location == null ? null : SanguisClient.reloadListeners().getGuideBookManager().getChapter(location));
    }

    private void goToIndex() {
        currentChapter = null;
        currentEntry = null;
        pageNumber = 0;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }

    private void flipForwards(){
        if (currentEntry == null) return;

        if (pageNumber < currentEntry.getPageCount() - 1){
            pageNumber += 2;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private void flipBack(){
        if (pageNumber >= 2){
            pageNumber -= 2;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
    protected void moveUp(){
        if (currentEntry != null){
            if (currentEntry.getParent() == BookEntry.EMPTY){
                this.openChapter(currentEntry.getChapter());
            } else{
                this.openEntry(currentEntry.getParent());
            }
            currentEntry = null;
            this.pageNumber = 0;
        } else if (currentChapter != null){
            this.goToIndex();
            this.pageNumber = 0;
        }
    }
    private boolean isOnIndex(){
        return currentChapter == null && currentEntry == null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentEntry != null && currentEntry.mouseClick(this, pageNumber, mouseX, mouseY)){
            return true;
        }
        if (hoveredDirection > -1) {
            if (currentChapter != null){
                var entries = currentChapter.getEntries();
                if (hoveredDirection >= entries.size()){
                    openEntry((ResourceLocation) null);
                } else if (entries.get(hoveredDirection) instanceof LinkChapterEntry link){
                    openEntry(link.getLocation());
                }
                return true;
            } else if (isOnIndex()){
                var chapterToLoad = SanguisClient.reloadListeners().getGuideBookManager().getChapter(hoveredDirection);
                if (chapterToLoad != null) openChapter(chapterToLoad);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 512, 256);
        if (currentEntry != null){
            var art = currentEntry.getArt();
            boolean hasArt = false;
            if (art != BookEntry.EMPTY){
                guiGraphics.blit(art, this.leftPos + 35, this.topPos + 22, 0, 0, 114, 159, 114, 159);
                hasArt = true;
            }

            if (pageNumber > 0 || !hasArt){
                renderPage(currentEntry, guiGraphics, this.leftPos, partialTick, mouseX, mouseY, 0, !hasArt);
            }
            renderPage(currentEntry, guiGraphics, this.leftPos, partialTick, mouseX, mouseY, 1, !hasArt);
            hoveredDirection = -1;
        } else if (currentChapter != null){
            int hover = -1;
            var entries = currentChapter.getEntries();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(1.25F, 1.25F, 1.0F);
            guiGraphics.pose().translate(
                    (float) (this.leftPos + 24) / 1.25F,
                    (float) (this.topPos + 12) / 1.25F, 0.0F);
            guiGraphics.drawString(font, currentChapter.getTitle(),
                    0, 0, 0x3f3f3f, false);
            guiGraphics.pose().popPose();

            int y = this.topPos + 12 + 14;
            for (int i = 0; i < entries.size(); i++){
                ChapterEntry entry = entries.get(i);
                if (entry instanceof LinkChapterEntry link){
                    var output = renderLink(link, hover, y, i, guiGraphics, partialTick, mouseX, mouseY);
                    hover = output[0];
                    y = output[1];
                } else{
                    int x = this.leftPos + getXOffset(Mth.floor((float) i / PAGE_SIZE_IN_LINES)) + 5;
                    y = entry.render(x, y, i, guiGraphics, partialTick, mouseX, mouseY);
                }
            }
            hoveredDirection = hover;
        } else {
            renderIndex(guiGraphics, partialTick, mouseX, mouseY);
        }
    }

    private void renderIndex(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY){
        int y = this.topPos + 18;
        guiGraphics.blit(INDEX_ART, this.leftPos + 35, this.topPos + 22, 0, 0, 114, 159, 114, 159);
        List<BookChapter> chapters = SanguisClient.reloadListeners().getGuideBookManager().getAllChapters();
        int hover = -1;
        for (int i = 0; i < chapters.size(); i++){
            BookChapter entry = chapters.get(i);
            ItemStack icon = entry.getIcon();
            int x = this.leftPos + getXOffset(1) + 5;

            int startX = x;
            int startY = y;

            guiGraphics.drawString(font, ARROW, x, y, 0x3f3f3f, false);

            if (icon.isEmpty()) {
                if (hover == -1 && isHoveringAbsolute(startX, y, font.width(entry.getTitle()), 7, mouseX, mouseY)){
                    x += 5;
                    hover = i;
                }

                guiGraphics.drawString(font, entry.getTitle(), x + 14, y, 0x3f3f3f, false);
            } else{
                x += font.width(ARROW) - 3;
                PoseStack poseStack = guiGraphics.pose();

                if (hover == -1 && isHoveringAbsolute(x, y, (startX - x) + font.width(entry.getTitle()) + 14 + font.width(ARROW), 7, mouseX, mouseY)){
                    x += 5;
                    hover = i;
                }

                poseStack.pushPose();
                poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
                poseStack.translate(x / ITEM_SCALE, (y - 2) / ITEM_SCALE, 0.0F);
                guiGraphics.renderItem(icon, 0, 0);
                poseStack.popPose();

                guiGraphics.drawString(font, entry.getTitle(), x + 14, y, 0x3f3f3f, false);
            }
            y += 12;
        }
        hoveredDirection = hover;
    }

    private int[] renderLink(LinkChapterEntry link, int hover, int y, int i, GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY){

        ItemStack icon = link.getIcon();
        int x = this.leftPos + getXOffset(Mth.floor((float) i / PAGE_SIZE_IN_LINES)) + 5 + link.getIndent() * INDENT_SIZE;

        int startX = x;
        int startY = y;

        if (icon.isEmpty()) {
            Component component = ARROW.copy().append(link.getTitle());
            guiGraphics.drawString(font, component, x, y, 0x3f3f3f, false);
            y += 10;
        } else{
            guiGraphics.drawString(font, ARROW, x, y, 0x3f3f3f, false);
            x += font.width(ARROW) - 3;

            if (hover == -1 && isHoveringAbsolute(x, y, (startX - x) + font.width(link.getTitle()) + 14 + font.width(ARROW), 7, mouseX, mouseY)){
                x += 5;
                hover = i;
            }


            PoseStack poseStack = guiGraphics.pose();

            poseStack.pushPose();
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            poseStack.translate(x / ITEM_SCALE, (y - 2) / ITEM_SCALE, 0.0F);
            guiGraphics.renderItem(icon, 0, 0);
            poseStack.popPose();

            x += 14;
            guiGraphics.drawString(font, link.getTitle(), x, y, 0x3f3f3f, false);
            y += 12;
        }
        return new int[]{hover, y};
    }

    protected boolean isHoveringAbsolute(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double)(x - 1)
                && mouseX < (double)(x + width + 1)
                && mouseY >= (double)(y - 1)
                && mouseY < (double)(y + height + 1);
    }

    private int getXOffset(int page){
        return page % 2 == 0 ? 24 : 172;
    }

    private void renderPage(BookEntry entry, GuiGraphics guiGraphics, int xStart, float partialTick, int mouseX, int mouseY, int pageOffset, boolean startOnLeft) {
        if (pageOffset <= 0 && pageNumber <= 0 && !startOnLeft) return;

        int x = (xStart) + getXOffset(pageNumber + pageOffset);

        for (BookWidget widget : entry.getWidgets()){
            if (widget.getDisplayPage() == (pageNumber + pageOffset)){
                widget.render(guiGraphics, x, this.topPos + 18, partialTick);
            }
        }

        int startReadingAt = ((pageNumber + pageOffset) - (startOnLeft ? 0 : 1)) * PAGE_SIZE_IN_LINES;
        for (int i = startReadingAt; i < startReadingAt + PAGE_SIZE_IN_LINES; i++){
            int y = this.topPos + 14 + (i - startReadingAt) * 10;
            if (entry.getEntryText().size() > i){
                String printLine = entry.getEntryText().get(i);
                guiGraphics.drawString(this.font, printLine, x, y, 0x3f3f3f, false);
                for (BookLink link : entry.getBookLinks()){
                    if (link.getLineNumber() == i){
                        int fontWidth = font.width(printLine.substring(0, link.getCharacterStartsAt()));
                        Component component = Component.literal(link.getDisplayText()).withStyle(ChatFormatting.UNDERLINE);
                        guiGraphics.drawString(font, component, fontWidth + x, y,
                                (link.isHovered() ? TEXT_LINK_HOVER_COLOR : TEXT_LINK_COLOR), false);
                    }
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.flipForwardButton.active = currentEntry != null && pageNumber < currentEntry.getPageCount() - 1;
        this.flipBackButton.active = pageNumber >= 2;
        this.moveUpButton.active = currentEntry != null || currentChapter != null;

        if (currentEntry != null){
            currentEntry.mouseOver(this, pageNumber, mouseX, mouseY);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == this.minecraft.options.keyLeft.getKey().getValue() || keyCode == InputConstants.KEY_LEFT){
            this.flipBack();
            return true;
        }
        if (keyCode == this.minecraft.options.keyRight.getKey().getValue() || keyCode == InputConstants.KEY_RIGHT){
            this.flipForwards();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }



    protected static abstract class GuideBookButton extends AbstractButton{
        protected ResourceLocation sprite;
        protected ResourceLocation highlightedSprite;

        public GuideBookButton(int x, int y, int width, int height, ResourceLocation sprite, ResourceLocation highlightedSprite, Component message) {
            super(x, y, width, height, message);
            this.sprite = sprite;
            this.highlightedSprite = highlightedSprite;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (!this.isActive()) return;

            ResourceLocation resourcelocation;
            if (this.isHovered()) {
                resourcelocation = highlightedSprite;
            } else {
                resourcelocation = sprite;
            }

            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
    public class FlipBackButton extends GuideBookButton {
        private static final ResourceLocation BUTTON_HIGHLIGHTED_SPRITE = Sanguis.location("container/guide_book/flip_back_highlighted");
        private static final ResourceLocation BUTTON_SPRITE = Sanguis.location("container/guide_book/flip_back");

        public FlipBackButton(int x, int y, int width, int height) {
            super(x, y, width, height, BUTTON_SPRITE, BUTTON_HIGHLIGHTED_SPRITE, CommonComponents.EMPTY);
        }

        @Override
        public void onPress() {
            if (!this.isActive()) return;

            GuideBookScreen.this.flipBack();
        }
    }
    public class FlipForwardButton extends GuideBookButton {
        private static final ResourceLocation BUTTON_HIGHLIGHTED_SPRITE = Sanguis.location("container/guide_book/flip_forwards_highlighted");
        private static final ResourceLocation BUTTON_SPRITE = Sanguis.location("container/guide_book/flip_forwards");

        public FlipForwardButton(int x, int y, int width, int height) {
            super(x, y, width, height, BUTTON_SPRITE, BUTTON_HIGHLIGHTED_SPRITE, CommonComponents.EMPTY);
        }

        @Override
        public void onPress() {
            if (!this.isActive()) return;

            GuideBookScreen.this.flipForwards();
        }
    }
    public class MoveUpButton extends GuideBookButton {
        private static final ResourceLocation BUTTON_HIGHLIGHTED_SPRITE = Sanguis.location("container/guide_book/move_up_hovered");
        private static final ResourceLocation BUTTON_DISABLED_SPRITE = Sanguis.location("container/guide_book/move_up_disabled");
        private static final ResourceLocation BUTTON_SPRITE = Sanguis.location("container/guide_book/move_up");

        public MoveUpButton(int x, int y, int width, int height) {
            super(x, y, width, height, BUTTON_SPRITE, BUTTON_HIGHLIGHTED_SPRITE, CommonComponents.EMPTY);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

            ResourceLocation resourcelocation;
            if (!this.isActive()) {
                resourcelocation = MoveUpButton.BUTTON_DISABLED_SPRITE;
            } else if (this.isHovered()) {
                resourcelocation = MoveUpButton.BUTTON_HIGHLIGHTED_SPRITE;
            } else {
                resourcelocation = MoveUpButton.BUTTON_SPRITE;
            }

            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
        }

        @Override
        public void onPress() {
            if (!this.isActive()) return;

            GuideBookScreen.this.moveUp();
        }
    }
}
