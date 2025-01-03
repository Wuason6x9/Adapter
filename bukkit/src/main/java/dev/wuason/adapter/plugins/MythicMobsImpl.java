package dev.wuason.adapter.plugins;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class MythicMobsImpl extends AdapterComp {

    public MythicMobsImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        MythicItem mythicItem = MythicBukkit.inst().getItemManager().getItem(id).orElse(null);
        return mythicItem != null ? BukkitAdapter.adapt(mythicItem.generateItemStack(1)) : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return MythicBukkit.inst().getItemManager().isMythicItem(itemStack) ? Utils.convert(getType(), MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemStack)) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
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
        return MythicBukkit.inst().getItemManager().getItem(id).isPresent();
    }

    @Override
    public Set<String> getAllItems() {
        if (MythicBukkit.inst().getItemManager().getItems().isEmpty()) {
            return Set.of();
        }
        return MythicBukkit.inst().getItemManager().getItems().stream().map(MythicItem::getInternalName).collect(Collectors.toUnmodifiableSet());
    }
}
