package com.feliscape.sanguis.data.datagen.ability;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.ability.VampireAbilityHolder;
import com.feliscape.sanguis.data.ability.VampireAbilityWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SanguisVampireAbilities extends  VampireAbilityProvider{
    public static final List<VampireAbilityWrapper> ENTRIES = new ArrayList<>();

    public static final VampireAbilityWrapper ROOT = create("root", b -> b.costs(0));
    public static final VampireAbilityWrapper BAT_FORM = create("bat_form", b -> b.after(ROOT).costs(1));

    private static VampireAbilityWrapper create(String id, UnaryOperator<VampireAbilityWrapper.Builder> b) {
        var wrapper = new VampireAbilityWrapper(Sanguis.location(id), b);
        ENTRIES.add(wrapper);
        return wrapper;
    }

    public SanguisVampireAbilities(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper);
    }

    @Override
    protected void generateAbilities(Consumer<VampireAbilityHolder> consumer, HolderLookup.Provider lookupProvider, ExistingFileHelper existingFileHelper) {
        for (VampireAbilityWrapper wrapper : ENTRIES){
            wrapper.save(lookupProvider, consumer, existingFileHelper);
        }
    }
}
