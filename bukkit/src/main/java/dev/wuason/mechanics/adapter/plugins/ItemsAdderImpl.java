package dev.wuason.mechanics.adapter.plugins;

import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.ItemsAdder;
import dev.wuason.mechanics.adapter.AdapterComp;
import dev.wuason.mechanics.adapter.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        return customStack != null ? convert(customStack.getNamespacedID()) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        return customBlock != null ? convert(customBlock.getNamespacedID()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        CustomFurniture customFurniture = CustomFurniture.byAlreadySpawned(entity);

        return customFurniture != null ? customFurniture.getNamespacedID() : null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }

    @Override
    public List<String> getAllItems() {
        return ItemsAdder.getAllItems().stream().map(s -> Utils.convert(getType(), s.getNamespacedID())).toList();
    }
}
