package dev.wuason.adapter.plugins;

import dev.wuason.storagemechanic.api.StorageMechanicAPI;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class StorageMechanicImpl extends AdapterComp {

    public StorageMechanicImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        return StorageMechanicAPI.getItem(id);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        String id = StorageMechanicAPI.getId(itemStack);
        return id != null ? Utils.convert(getType(), id) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        String id = StorageMechanicAPI.getId(block);
        return id != null ? Utils.convert(getType(), id) : null;
    }

    @Override
    public String getAdapterId(Entity entity) { // StorageMechanic does not support entities
        return null;
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
        return StorageMechanicAPI.existItem(id);
    }

    @Override
    public Set<String> getAllItems() {
        return StorageMechanicAPI.getAllItems().stream().map(name -> Utils.convert(getType(), name)).collect(Collectors.toUnmodifiableSet());
    }
}
