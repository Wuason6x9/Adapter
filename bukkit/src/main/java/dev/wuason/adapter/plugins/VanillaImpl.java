package dev.wuason.adapter.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class VanillaImpl extends AdapterComp {

    private Method toStringCompound;

    public VanillaImpl(String name, String type) {
        super(name, type);
        try {
            Class<?> itemMetaClass = Class.forName("org.bukkit.inventory.meta.ItemMeta");
            toStringCompound = itemMetaClass.getDeclaredMethod("getAsComponentString");
            toStringCompound.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        String[] nbtData = getNBTData(id);
        if(Material.getMaterial(nbtData[0].toUpperCase(Locale.ENGLISH),false) == null) return null;
        String item = "minecraft:" + nbtData[0] + (nbtData[1] == null ? "" : nbtData[1]);
        return Bukkit.getItemFactory().createItemStack(item);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return Utils.convert(getType(), itemStack.getType().name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String getAdapterId(Block block) {
        return Utils.convert(getType(), block.getType().name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String getAdapterId(Entity entity) {
        if (entity.getType() != EntityType.DROPPED_ITEM) return null;
        ItemStack itemStack = ((Item) entity).getItemStack();
        return getAdapterId(itemStack);
    }

    @Override
    public String getAdvancedAdapterId(ItemStack itemStack) {
        String nbt = getNBTData(itemStack);
        return Utils.convert(getType(), itemStack.getType().name().toLowerCase(Locale.ENGLISH)) + ((nbt.isEmpty() || nbt.equals("{}") || nbt.equals("[]")) ? "" : ":" + nbt);
    }

    @Override
    public String getAdvancedAdapterId(Block block) {
        String blockState = block.getBlockData().getAsString().replace(block.getType().getKey().toString(), "");
        return Utils.convert(getType(), block.getType().name().toLowerCase(Locale.ENGLISH)) + (blockState.isEmpty() || blockState.equals("[]") ? "" : ":" + blockState);
    }

    @Override
    public String getAdvancedAdapterId(Entity entity) {
        if (entity.getType() != EntityType.DROPPED_ITEM) return null;
        ItemStack itemStack = ((Item) entity).getItemStack();
        return getAdvancedAdapterId(itemStack);
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }

    @Override
    public Set<String> getAllItems() {
        return Arrays.stream(Material.values())
                .filter(Material::isItem).map(m -> {
                    return Utils.convert(getType(), m.name().toLowerCase(Locale.ENGLISH));
                }).collect(Collectors.toUnmodifiableSet());
    }

    private static final char[] NBT = {'{', '}'};
    private static final char[] COMPONENT = {'[', ']'};

    private String[] getNBTData(String line) {
        String[] data = {line.toLowerCase(Locale.ENGLISH), null};
        char[] selected = toStringCompound != null ? COMPONENT : NBT;
        if (line.contains(String.valueOf(selected[0])) && line.contains(String.valueOf(selected[1]))) {
            int start = line.indexOf(selected[0]);
            int end = line.lastIndexOf(selected[1]);
            data[0] = line.substring(0, start).toLowerCase(Locale.ENGLISH).trim();
            data[1] = line.substring(start, end + 1).trim();
        }
        return data;
    }

    private String getNBTData(ItemStack itemStack) {
        if (toStringCompound != null && itemStack.hasItemMeta()) {
            try {
                return (String) toStringCompound.invoke(itemStack.getItemMeta());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else if (itemStack.hasItemMeta()) {
            return itemStack.getItemMeta().getAsString();
        }
        return "{}";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
