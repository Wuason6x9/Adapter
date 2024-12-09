package dev.wuason.mechanics.adapter.plugins;

import dev.wuason.mechanics.adapter.AdapterComp;
import dev.wuason.mechanics.adapter.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
        String nbt = getNBTData(itemStack);
        return Utils.convert(getType(), itemStack.getType().name().toLowerCase(Locale.ENGLISH)) + ((nbt.isEmpty() || nbt.equals("{}") || nbt.equals("[]")) ? "" : ":" + nbt);
    }

    @Override
    public String getAdapterId(Block block) {
        String blockState = block.getBlockData().getAsString().replace(block.getType().getKey().toString(), "");
        return Utils.convert(getType(), block.getType().name().toLowerCase(Locale.ENGLISH)) + (blockState.isEmpty() || blockState.equals("[]") ? "" : ":" + blockState);
    }

    @Override
    public String getAdapterId(Entity entity) {
        if (entity.getType() != EntityType.DROPPED_ITEM) return null;
        ItemStack itemStack = ((Item) entity).getItemStack();
        return getAdapterId(itemStack);
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }

    @Override
    public List<String> getAllItems() {
        return Arrays.stream(Material.values())
                .filter(Material::isItem).map(m -> {
                    return Utils.convert(getType(), m.name().toLowerCase(Locale.ENGLISH));
                }).toList();
    }

    private String[] getNBTData(String line) {
        String[] data = {line.toLowerCase(Locale.ENGLISH), null};
        if (line.contains(":")) {
            data[0] = line.substring(0, line.indexOf(":")).toLowerCase(Locale.ENGLISH);
            if (line.length() == line.indexOf(":") + 1) return data;
            String nbt = line.substring(line.indexOf(":") + 1);
            if (!nbt.isBlank()) {
                data[1] = nbt;
            }
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

}
