package com.feliscape.sanguis.client.book;

import net.minecraft.resources.ResourceLocation;

public class BookLink {
    private int lineNumber;
    private int characterStartsAt;
    private String displayText;
    private ResourceLocation linksTo;
    private boolean enabled;
    private boolean hovered = false;

    public BookLink(int lineNumber, int characterStartsAt, String displayText, ResourceLocation linksTo, boolean enabled) {
        this.lineNumber = lineNumber;
        this.characterStartsAt = characterStartsAt;
        this.displayText = displayText;
        this.linksTo = linksTo;
        this.enabled = enabled;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getCharacterStartsAt() {
        return characterStartsAt;
    }

    public String getDisplayText() {
        return displayText;
    }

    public ResourceLocation getLinksTo() {
        return linksTo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
