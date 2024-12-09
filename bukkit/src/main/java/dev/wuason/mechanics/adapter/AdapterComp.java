package dev.wuason.mechanics.adapter;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AdapterComp {
    private final String name;
    private final String type;

    public AdapterComp(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public abstract ItemStack getAdapterItem(String id);

    public abstract String getAdapterId(ItemStack itemStack);

    public abstract String getAdapterId(Block block);

    public abstract String getAdapterId(Entity entity);

    public abstract boolean existItemAdapter(String id);

    public abstract List<String> getAllItems();
}
