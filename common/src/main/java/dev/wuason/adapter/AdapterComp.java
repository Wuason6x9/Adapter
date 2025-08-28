package dev.wuason.adapter;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class AdapterComp {
    private final @NotNull String name;
    private final @NotNull String type;

    public AdapterComp(@NotNull String name, @NotNull String type) {
        this.name = name;
        this.type = type;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getType() {
        return type;
    }

    public abstract @Nullable ItemStack getAdapterItem(@NotNull String id);

    public abstract @Nullable String getAdapterId(@NotNull ItemStack itemStack);

    public abstract @Nullable String getAdapterId(@NotNull Block block);

    public abstract @Nullable String getAdapterId(@NotNull Entity entity);

    public abstract @Nullable String getAdvancedAdapterId(@NotNull ItemStack itemStack);

    public abstract @Nullable String getAdvancedAdapterId(@NotNull Block block);

    public abstract @Nullable String getAdvancedAdapterId(@NotNull Entity entity);

    public abstract boolean existItemAdapter(@NotNull String id);

    public abstract @Nullable Set<String> getAllItems();

    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(getName());
    }
}
