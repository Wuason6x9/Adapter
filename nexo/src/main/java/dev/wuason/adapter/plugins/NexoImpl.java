package dev.wuason.adapter.plugins;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class NexoImpl extends AdapterComp {

    public NexoImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public Set<String> getAllItems() {
        if (NexoItems.names() == null) return Set.of();
        return NexoItems.names().stream().map(name -> Utils.convert(getType(), name)).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        return NexoItems.exists(id) ? NexoItems.itemFromId(id).build() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return NexoItems.exists(itemStack) ? Utils.convert(getType(), NexoItems.idFromItem(itemStack)) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        return NexoBlocks.isCustomBlock(block) ? Utils.convert(getType(), NexoBlocks.customBlockMechanic(block.getBlockData()).getItemID()) : NexoFurniture.furnitureMechanic(block) != null ? Utils.convert(getType(), NexoFurniture.furnitureMechanic(block).getItemID()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return NexoFurniture.isFurniture(entity) ? Utils.convert(getType(), NexoFurniture.furnitureMechanic(entity).getItemID()) : null;
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
        return NexoItems.exists(id);
    }

}
