package dev.wuason.adapter.plugins;

import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class Oraxen1Impl extends AdapterComp {

    public Oraxen1Impl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        return OraxenItems.exists(id) ? OraxenItems.getItemById(id).build() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return OraxenItems.exists(itemStack) ? Utils.convert(getType(), OraxenItems.getIdByItem(itemStack)) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        return OraxenBlocks.isOraxenBlock(block) ? Utils.convert(getType(), OraxenBlocks.getOraxenBlock(block.getBlockData()).getItemID()) : OraxenFurniture.isFurniture(block) ? Utils.convert(getType(), OraxenFurniture.getFurnitureMechanic(block).getItemID()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return OraxenFurniture.isFurniture(entity) ? Utils.convert(getType(), OraxenFurniture.getFurnitureMechanic(entity).getItemID()) : null;
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
        return OraxenItems.exists(id);
    }

    @Override
    public Set<String> getAllItems() {
        if (OraxenItems.getNames() == null) return Set.of();
        return OraxenItems.getNames().stream().map(name -> Utils.convert(getType(), name)).collect(Collectors.toUnmodifiableSet());
    }

}
