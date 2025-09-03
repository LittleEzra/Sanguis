package com.feliscape.sanguis.client.book;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.book.widget.BookWidget;
import com.feliscape.sanguis.client.screen.GuideBookScreen;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookEntry {
    public static final ResourceLocation EMPTY = Sanguis.location("empty");
    public static final Codec<BookEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ComponentSerialization.CODEC.fieldOf("title").forGetter(entry -> entry.title),
            ResourceLocation.CODEC.fieldOf("chapter").forGetter(entry -> entry.chapter),
            ResourceLocation.CODEC.lenientOptionalFieldOf("parent", EMPTY).forGetter(entry -> entry.parent),
            ResourceLocation.CODEC.lenientOptionalFieldOf("text", EMPTY).forGetter(entry -> entry.text),
            ResourceLocation.CODEC.lenientOptionalFieldOf("art", EMPTY).forGetter(entry -> entry.art),
            BookWidget.TYPED_CODEC.listOf().optionalFieldOf("widgets", List.of()).forGetter(entry -> entry.widgets)
    ).apply(inst, BookEntry::new));

    private Component title;
    private ResourceLocation chapter;
    private ResourceLocation parent;
    private ResourceLocation text;
    private ResourceLocation art;
    private static Pattern linkPattern = Pattern.compile("\\{.*?}");
    private List<BookWidget> widgets;

    private int pageCount;

    private List<String> entryText = new ArrayList<>();
    private List<BookLink> bookLinks = new ArrayList<>();

    public BookEntry(Component title, ResourceLocation chapter, ResourceLocation parent, ResourceLocation text, ResourceLocation art, List<BookWidget> widgets) {
        this.title = title;
        this.chapter = chapter;
        this.parent = parent;
        this.text = text;
        this.art = art;
        this.widgets = widgets;
    }

    public static String getBookFileDirectory(){
        return "book/";
    }

    public void init(GuideBookScreen screen){
        this.entryText = resolveText(text, 40);
        this.pageCount = Mth.ceil(entryText.size() / (float)(GuideBookScreen.PAGE_SIZE_IN_LINES * 2));
    }

    private List<String> resolveText(ResourceLocation location, int maxLineSize){
        if (location == EMPTY) return List.of();

        String lang = Minecraft.getInstance().getLanguageManager().getSelected().toLowerCase();
        ResourceLocation fileRes;
        try {
            fileRes = location.withPrefix(getBookFileDirectory() + lang + "/");
            InputStream stream = Minecraft.getInstance().getResourceManager().open(fileRes);
            stream.close();
        } catch (Exception e){
            Sanguis.LOGGER.warn("Could not find language file for translation, defaulting to en_us");
            fileRes = location.withPrefix(getBookFileDirectory() + "en_us/");
        }

        List<String> strings = new ArrayList<>();
        Font font  = Minecraft.getInstance().font;
        try{
            BufferedReader reader = Minecraft.getInstance().getResourceManager().openAsReader(fileRes);
            List<String> readIn = IOUtils.readLines(reader);
            int currentLineCount = 0;
            bookLinks.clear();
            for (String readString : readIn){
                if (readString.equals("[PAGEBREAK]")){
                    int linesToAdd = GuideBookScreen.PAGE_SIZE_IN_LINES - strings.size() % GuideBookScreen.PAGE_SIZE_IN_LINES;
                    for (int i = 0; i < linesToAdd; i++){
                        strings.add("");
                        currentLineCount++;
                    }
                    continue;
                }

                Matcher matcher = linkPattern.matcher(readString);
                boolean skipLine = false;
                boolean noOverflow = false;
                while (matcher.find()){
                    String[] found = matcher.group().split("\\|");
                    String display = "";
                    if (found.length >= 1){
                        String linkTo = found[1].substring(0, found[1].length() - 1);
                        display = found[0].substring(1);
                        bookLinks.add(new BookLink(currentLineCount, matcher.start(), display, ResourceLocation.parse(linkTo), true));
                        readString = matcher.replaceFirst(display);
                    } else{
                        readString = display;
                        skipLine = true;
                    }
                    noOverflow = true;
                }
                if (readString.isEmpty() && !skipLine){
                    strings.add(readString);
                    currentLineCount++;
                }
                while(font.width(readString) > 140){
                    int spaceScanIndex = 0;
                    int lastSpace = -1;
                    int selectedSpace = -1;
                    while(spaceScanIndex < readString.length()){
                        if(font.width(readString.substring(0, spaceScanIndex)) > 140){
                            selectedSpace = noOverflow ? readString.length() : lastSpace;
                            break;
                        }

                        if (readString.charAt(spaceScanIndex) == ' '){
                            lastSpace = spaceScanIndex;
                        }
                        spaceScanIndex++;
                    }
                    int cutIndex = selectedSpace == -1 ? Math.min(maxLineSize, readString.length()) : selectedSpace;
                    strings.add(readString.substring(0, cutIndex));
                    currentLineCount++;
                    readString = readString.substring(cutIndex);
                    if (readString.startsWith(" ")) {
                        readString = readString.substring(1);
                    }
                }
                if (!readString.isEmpty()){
                    strings.add(readString);
                    currentLineCount++;
                }
            }
        } catch (Exception e){
            Sanguis.LOGGER.debug("Encountered an error while trying to load text file {}", location);
            e.printStackTrace();
        }

        return strings;
    }

    public void mouseOver(GuideBookScreen screen, int page, double mouseX, double mouseY){
        boolean hoverFlag = false;
        for (BookLink link : bookLinks){
            int minLine = page * GuideBookScreen.PAGE_SIZE_IN_LINES;
            link.setHovered(false);
            if (link.getLineNumber() >= minLine && link.getLineNumber() < minLine + GuideBookScreen.PAGE_SIZE_IN_LINES * 2){
                //Sanguis.LOGGER.debug("Found valid link");
                String line = entryText.get(link.getLineNumber());
                boolean startsOnLeft = art == EMPTY;
                int textStartX;
                if (startsOnLeft){
                    textStartX = (link.getLineNumber() > minLine + GuideBookScreen.PAGE_SIZE_IN_LINES) ? 172 : 24;
                } else{
                    textStartX = (link.getLineNumber() > minLine + GuideBookScreen.PAGE_SIZE_IN_LINES) ? 24 : 172;
                }


                int wordLength = Minecraft.getInstance().font.width(link.getDisplayText());
                int wordTop = 14 + (link.getLineNumber() % GuideBookScreen.PAGE_SIZE_IN_LINES) * 10;
                int wordLeft = textStartX + Minecraft.getInstance().font.width(line.substring(0, link.getCharacterStartsAt()));
                if (isHovering(screen.getGuiLeft(), screen.getGuiTop(), wordLeft, wordTop, wordLength, 8, mouseX, mouseY)){
                    link.setHovered(!hoverFlag);
                    hoverFlag = true;
                }
            }
        }
    }

    public boolean mouseClick(GuideBookScreen screen, int page, double mouseX, double mouseY){
        for (BookLink link : bookLinks){
            int minLine = page * GuideBookScreen.PAGE_SIZE_IN_LINES;
            if (link.isEnabled() && link.isHovered() && link.getLineNumber() >= minLine && link.getLineNumber() <= minLine + GuideBookScreen.PAGE_SIZE_IN_LINES * 2){
                screen.openEntry(link.getLinksTo());
                return true;
            }
        }
        return false;
    }

    protected boolean isHovering(int leftPos, int topPos, int x, int y, int width, int height, double mouseX, double mouseY) {
        mouseX -= (double)leftPos;
        mouseY -= (double)topPos;
        return mouseX >= (double)(x - 1)
                && mouseX < (double)(x + width + 1)
                && mouseY >= (double)(y - 1)
                && mouseY < (double)(y + height + 1);
    }

    public Component getTitle() {
        return title;
    }

    public ResourceLocation getChapter() {
        return chapter;
    }

    public ResourceLocation getParent() {
        return parent;
    }

    public ResourceLocation getText() {
        return text;
    }

    public ResourceLocation getArt() {
        return art;
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<String> getEntryText() {
        return entryText;
    }

    public List<BookLink> getBookLinks() {
        return bookLinks;
    }

    public List<BookWidget> getWidgets() {
        return widgets;
    }
}
