package com.feliscape.sanguis.content.ritual;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BloodRitual {
    protected BlockPattern pattern;

    public BloodRitual() {
        this.pattern = createPattern();
    }

    protected abstract BlockPattern createPattern();

    public abstract Result activate(Level level, BlockPos pos, List<Player> nearbyPlayers, Player activatingPlayer);

    public boolean verify(Level level, BlockPos pos, Player player, List<ItemStack> itemStacks, ItemStack reagent){
        if (pattern == null) return false;
        if (!verifyItem(player, reagent)) return false;

        return pattern.find(level, pos) != null;
    }

    protected boolean verifyItem(Player player, ItemStack itemStack){
        return true;
    }

    public enum Result{
        FAIL,
        FAIL_CONSUME,
        SUCCESS,
        SUCCESS_CONSUME;

        public boolean consumesItem(){
            return this == FAIL_CONSUME || this == SUCCESS_CONSUME;
        }
        public boolean isSuccess(){
            return this == SUCCESS || this == SUCCESS_CONSUME;
        }
    }

    public static class LayeredBlockPatternBuilder{
        private static final Joiner COMMA_JOINED = Joiner.on(",");
        private final List<String[]> pattern = Lists.newArrayList();
        private final Map<Character, Predicate<BlockInWorld>> lookup = Maps.newHashMap();
        private int width;
        private int depth;

        private LayeredBlockPatternBuilder() {
            this.lookup.put(' ', block -> true);
        }

        /**
         * Adds a single layer to this pattern, on the xy-plane. (so multiple calls to this will increase the y-size by 1)
         */
        public LayeredBlockPatternBuilder layer(String... layer) {
            if (!ArrayUtils.isEmpty(layer) && !StringUtils.isEmpty(layer[0])) {
                if (this.pattern.isEmpty()) {
                    this.width = layer[0].length();
                    this.depth = layer.length;
                }

                if (layer.length != this.depth) {
                    throw new IllegalArgumentException(
                            "Expected layer with depth of " + this.depth + ", but was given one with a depth of " + layer.length + ")"
                    );
                } else {
                    for (String s : layer) {
                        if (s.length() != this.width) {
                            throw new IllegalArgumentException(
                                    "Not all rows in the given layer are the correct width (expected " + this.width + ", found one with " + s.length() + ")"
                            );
                        }

                        for (char c0 : s.toCharArray()) {
                            if (!this.lookup.containsKey(c0)) {
                                this.lookup.put(c0, null);
                            }
                        }
                    }

                    this.pattern.add(layer);
                    return this;
                }
            } else {
                throw new IllegalArgumentException("Empty pattern for layer");
            }
        }

        public static LayeredBlockPatternBuilder start() {
            return new LayeredBlockPatternBuilder();
        }

        public LayeredBlockPatternBuilder where(char symbol, Block block) {
            return this.where(symbol, b -> b.getState().is(block));
        }
        public LayeredBlockPatternBuilder where(char symbol, Supplier<? extends Block> block) {
            return this.where(symbol, b -> b.getState().is(block.get()));
        }
        public LayeredBlockPatternBuilder where(char symbol, TagKey<Block> tag) {
            return this.where(symbol, b -> b.getState().is(tag));
        }
        public LayeredBlockPatternBuilder where(char symbol, Predicate<BlockInWorld> blockMatcher) {
            this.lookup.put(symbol, blockMatcher);
            return this;
        }

        public BlockPattern build() {
            return new BlockPattern(this.createPattern());
        }

        @SuppressWarnings("unchecked")
        private Predicate<BlockInWorld>[][][] createPattern() {
            this.ensureAllCharactersMatched();
            Predicate<BlockInWorld>[][][] predicate = (Predicate<BlockInWorld>[][][])Array.newInstance(
                    Predicate.class, this.depth, this.pattern.size(), this.width
            );

            for (int i = 0; i < this.depth; i++) {
                for (int j = 0; j < this.pattern.size(); j++) {
                    for (int k = 0; k < this.width; k++) {
                        predicate[i][j][k] = this.lookup.get(this.pattern.get(j)[i].charAt(k));
                    }
                }
            }

            return predicate;
        }

        private void ensureAllCharactersMatched() {
            List<Character> list = Lists.newArrayList();

            for (Map.Entry<Character, Predicate<BlockInWorld>> entry : this.lookup.entrySet()) {
                if (entry.getValue() == null) {
                    list.add(entry.getKey());
                }
            }

            if (!list.isEmpty()) {
                throw new IllegalStateException("Predicates for character(s) " + COMMA_JOINED.join(list) + " are missing");
            }
        }
    }
}
