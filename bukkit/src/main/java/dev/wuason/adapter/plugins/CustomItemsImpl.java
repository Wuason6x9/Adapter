
package dev.wuason.adapter.plugins;

import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;
import dev.wuason.customItemsAPI.CustomItemsAPI;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomItemsImpl extends AdapterComp {

    public CustomItemsImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public Set<String> getAllItems() {
        if (CustomItemsAPI.getAllItems() == null) return Set.of();
        return CustomItemsAPI.getAllItems().stream().map(id -> Utils.convert(getType(), id)).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        return CustomItemsAPI.getCustomItem(id) == null ? null : CustomItemsAPI.Companion.getCustomItem(id);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return CustomItemsAPI.isCustomItem(itemStack) ? Utils.convert(getType(), CustomItemsAPI.Companion.getCustomItemID(itemStack)) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        String cuiId = CustomItemsAPI.getCustomItemIDAtBlock(block);
        return cuiId != null ? Utils.convert(getType(), cuiId) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null; //Not implemented
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
        return CustomItemsAPI.getCustomItem(id) != null;
    }
}
