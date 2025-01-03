package dev.wuason.adapter.plugins;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythiccrucible.MythicCrucible;
import io.lumine.mythiccrucible.items.CrucibleItem;
import io.lumine.mythiccrucible.items.blocks.CustomBlockItemContext;
import io.lumine.mythiccrucible.items.furniture.Furniture;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class MythicCrucibleImpl extends AdapterComp {


    public MythicCrucibleImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        CrucibleItem item = MythicCrucible.inst().getItemManager().getItem(id).orElse(null);
        return item != null ? BukkitAdapter.adapt(item.getMythicItem().generateItemStack(1)) : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        CompoundTag compoundTag = MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().getNBTData(itemStack);
        return compoundTag.containsKey("MYTHIC_TYPE") ? Utils.convert(getType(), compoundTag.getString("MYTHIC_TYPE")) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        CustomBlockItemContext customBlock = MythicCrucible.inst().getItemManager().getCustomBlockManager().getBlockFromBlock(block).orElse(null);
        if (customBlock != null) {
            return Utils.convert(getType(), customBlock.getCrucibleItem().getInternalName());
        }
        Furniture furniture = MythicCrucible.inst().getItemManager().getFurnitureManager().getFurniture(block).orElse(null);
        return furniture != null ? Utils.convert(getType(), furniture.getFurnitureData().getItem().getInternalName()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return MythicCrucible.inst().getItemManager().getFurnitureManager().getFurniture(entity.getUniqueId()).map(furniture -> Utils.convert(getType(), furniture.getFurnitureData().getItem().getInternalName())).orElse(null);
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
        return MythicCrucible.inst().getItemManager().getItem(id).isPresent();
    }

    @Override
    public Set<String> getAllItems() {
        if (MythicCrucible.inst().getItemManager().getItemNames() == null) return Set.of();
        return MythicCrucible.inst().getItemManager().getItemNames().stream().map(name -> Utils.convert(getType(), name)).collect(Collectors.toUnmodifiableSet());
    }
}
