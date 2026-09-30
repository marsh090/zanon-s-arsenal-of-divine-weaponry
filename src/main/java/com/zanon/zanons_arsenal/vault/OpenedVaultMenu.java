package com.zanon.zanons_arsenal.vault;

import com.zanon.zanons_arsenal.registry.ModMenus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OpenedVaultMenu extends AbstractContainerMenu {
    private final Container vault;

    public OpenedVaultMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(1));
    }

    public OpenedVaultMenu(int containerId, Inventory inventory, Container vault) {
        super(ModMenus.OPENED_VAULT.get(), containerId);
        this.vault = vault;
        checkContainerSize(vault, 1);
        vault.startOpen(inventory.player);
        this.addSlot(new Slot(vault, 0, 80, 20));
        this.addStandardInventorySlots(inventory, 8, 51);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.vault.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex < this.vault.getContainerSize()) {
                if (!this.moveItemStackTo(stack, this.vault.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, this.vault.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clicked;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.vault.stopOpen(player);
    }
}
