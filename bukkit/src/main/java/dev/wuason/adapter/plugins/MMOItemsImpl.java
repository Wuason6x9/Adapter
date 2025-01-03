package dev.wuason.adapter.plugins;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.block.CustomBlock;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.BlockManager;
import net.Indyuce.mmoitems.manager.TemplateManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class MMOItemsImpl extends AdapterComp {
    public MMOItemsImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        String[] data = id.split(":");
        //TYPE
        Type type = Type.get(data[0].toUpperCase(Locale.ENGLISH));
        if (type == null) return null;
        //ID
        String itemId = data[1].toUpperCase(Locale.ENGLISH);
        MMOItem item = null;
        //ONLY ID & TYPE
        if (data.length < 3) {
            item = MMOItems.plugin.getMMOItem(type, itemId);
        }
        // ID & TYPE & LEVEL & TIER
        if (data.length < 5 && data.length > 2) {
            if (Utils.isNumber(data[2])) {
                int mmoLevel = Integer.parseInt(data[2]);
                ItemTier itemTier = MMOItems.plugin.getTiers().get(data[3].toUpperCase(Locale.ENGLISH));
                if (itemTier == null) return null;
                item = MMOItems.plugin.getMMOItem(type, itemId, mmoLevel, itemTier);
            }
        }
        return item != null ? item.newBuilder().build() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        Type type = MMOItems.getType(itemStack);
        String id = MMOItems.getID(itemStack);
        return type != null && id != null ? Utils.convert(getType(), type.getId() + ":" + id) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        BlockManager blockManager = MMOItems.plugin.getCustomBlocks();
        CustomBlock customBlock = blockManager.getFromBlock(block.getBlockData()).orElse(null);
        return customBlock != null ? getAdapterId(customBlock.getItem()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) { // MMOItems does not support entities
        return null;
    }

    @Override
    public String getAdvancedAdapterId(ItemStack itemStack) {
        if (!NBTItem.get(itemStack).hasType()) return null;
        LiveMMOItem i = new LiveMMOItem(itemStack);
        return Utils.convert(getType(), i.getType().getId() + ":" + i.getId() + ":" + i.getUpgradeLevel() + i.getTier() != null ? ":" + i.getTier().getId() : "");
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
        String data[] = id.split(":");
        if (data.length < 2) return false;
        Type type = MMOItems.plugin.getTypes().get(data[0].toUpperCase(Locale.ENGLISH));
        return type != null && MMOItems.plugin.getMMOItem(type, data[1].toUpperCase(Locale.ENGLISH)) != null;
    }

    @Override
    public Set<String> getAllItems() {
        TemplateManager tm = MMOItems.plugin.getTemplates();
        return MMOItems.plugin.getTypes().getAll().stream().flatMap(type -> {
            return tm.getTemplates(type).stream().map(template -> {
                return Utils.convert(getType(), type.getId() + ":" + template.getId());
            });
        }).collect(Collectors.toUnmodifiableSet());
    }


}