package dev.wuason.adapter.plugins;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class ItemsAdderImpl extends AdapterComp {

    public ItemsAdderImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        CustomStack customStack = CustomStack.getInstance(id);
        return customStack != null ? customStack.getItemStack() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        return customStack != null ? Utils.convert(getType(), customStack.getNamespacedID()) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        return customBlock != null ? Utils.convert(getType(), customBlock.getNamespacedID()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        CustomFurniture customFurniture = CustomFurniture.byAlreadySpawned(entity);
        return customFurniture != null ? customFurniture.getNamespacedID() : null;
    }

    @Override
    public String getAdvancedAdapterId(ItemStack itemStack) {
        return getAdapterId(itemStack);
    }

    @Override
    public String getAdvancedAdapterId(Block block) {
        return getAdapterId(block);
    }

    @Override
    public String getAdvancedAdapterId(Entity entity) {
        return getAdapterId(entity);
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }

    @Override
    public Set<String> getAllItems() {
        if (ItemsAdder.getAllItems() == null) return Set.of();
        return ItemsAdder.getAllItems().stream().map(s -> Utils.convert(getType(), s.getNamespacedID())).collect(Collectors.toUnmodifiableSet());
    }
}
