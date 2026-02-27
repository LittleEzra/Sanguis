package com.feliscape.sanguis.content.menu;

import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.registry.SanguisMenuTypes;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VampireAbilitiesMenu extends AbstractContainerMenu {
    private VampireAbilityData abilities = null;

    List<VampireAbility> obtainedAbilities = new ArrayList<>();
    List<VampireAbility> unobtainedAbilities = new ArrayList<>();

    public VampireAbilitiesMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, inventory.player);
    }

    public VampireAbilitiesMenu(int id, Inventory inventory, Player player) {
        super(SanguisMenuTypes.VAMPIRE_ABILITIES.get(), id);
        if (VampireUtil.isVampire(player)) {
            abilities = player.getData(VampireAbilityData.type());
            obtainedAbilities = new ArrayList<>(abilities.getObtainedAbilities());

            var allAbilityHolders = player.level().registryAccess()
                    .registryOrThrow(SanguisRegistries.Keys.VAMPIRE_ABILITIES).holders();
            unobtainedAbilities = new ArrayList<>(allAbilityHolders.map(Holder::value).filter(a -> !obtainedAbilities.contains(a)).toList());
        }

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    public List<VampireAbility> getObtainedAbilities() {
        return obtainedAbilities;
    }

    public List<VampireAbility> getUnobtainedAbilities() {
        return unobtainedAbilities;
    }

    public VampireAbilityData getAbilities(){
        return abilities;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();

            if (slotItem.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotItem.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotItem);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return VampireUtil.isVampire(player);
    }

    public void moveAbility(int index, boolean unobtained) {
        var selectedAbilities = unobtained ? unobtainedAbilities : obtainedAbilities;
        var otherAbilities = !unobtained ? unobtainedAbilities : obtainedAbilities;
        var ability = selectedAbilities.get(index);

        selectedAbilities.remove(ability);
        otherAbilities.add(ability);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
    }

    public int getCurrentCost() {
        int i = 0;
        for (VampireAbility a : obtainedAbilities){
            i += a.getCost();
        }
        return i;
    }

    public int getMaxCost() {
        if (abilities != null) return abilities.getSkillPoints();
        return 0;
    }
}
