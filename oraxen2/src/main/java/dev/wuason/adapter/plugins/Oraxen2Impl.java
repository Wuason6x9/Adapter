package dev.wuason.adapter.plugins;

import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;

public class Oraxen2Impl extends AdapterComp {

    public Oraxen2Impl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        return OraxenItems.exists(id) ? OraxenItems.getItemById(id).build() : null;
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
    public String getAdapterId(Block block) {
        return OraxenBlocks.isCustomBlock(block) ? Utils.convert(getType(), OraxenBlocks.getCustomBlockMechanic(block.getLocation()).getItemID()) : OraxenFurniture.isFurniture(block.getLocation()) ? Utils.convert(getType(), OraxenFurniture.getFurnitureMechanic(block.getLocation()).getItemID()) : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return OraxenItems.exists(itemStack) ? Utils.convert(getType(), OraxenItems.getIdByItem(itemStack)) : null;
    }
}
