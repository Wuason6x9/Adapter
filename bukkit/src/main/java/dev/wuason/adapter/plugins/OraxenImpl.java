package dev.wuason.adapter.plugins;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class OraxenImpl extends AdapterComp {

    private boolean oraxen2 = false;
    private AdapterComp selected;

    public OraxenImpl(String name, String type) {
        super(name, type);
        if (Bukkit.getPluginManager().getPlugin(getName()).getDescription().getVersion().startsWith("2")) {
            oraxen2 = true;
        }

        try {
            Class<?> oraxenComp = Class.forName(String.format("dev.wuason.mechanics.adapter.plugins.Oraxen%sImpl", oraxen2 ? "2" : "1"));
            selected = (AdapterComp) oraxenComp.getConstructor(String.class, String.class).newInstance(name, type);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        return selected.getAdapterItem(id);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return selected.getAdapterId(itemStack);
    }

    @Override
    public String getAdapterId(Block block) {
        return selected.getAdapterId(block);
    }

    @Override
    public String getAdapterId(Entity entity) {
        return selected.getAdapterId(entity);
    }

    @Override
    public String getAdvancedAdapterId(ItemStack itemStack) {
        return selected.getAdvancedAdapterId(itemStack);
    }

    @Override
    public String getAdvancedAdapterId(Block block) {
        return selected.getAdvancedAdapterId(block);
    }

    @Override
    public String getAdvancedAdapterId(Entity entity) {
        return selected.getAdvancedAdapterId(entity);
    }

    @Override
    public boolean existItemAdapter(String id) {
        return selected.existItemAdapter(id);
    }

    @Override
    public Set<String> getAllItems() {
        return selected.getAllItems();
    }

    public boolean isOraxen2() {
        return oraxen2;
    }
}
