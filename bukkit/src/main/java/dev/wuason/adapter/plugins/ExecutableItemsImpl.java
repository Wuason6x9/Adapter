package dev.wuason.adapter.plugins;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import com.ssomar.score.api.executableitems.config.ExecutableItemsManagerInterface;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ExecutableItemsImpl extends AdapterComp {

    public ExecutableItemsImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        ExecutableItemsManagerInterface executableItemsManager = ExecutableItemsAPI.getExecutableItemsManager();
        ExecutableItemInterface executableItem = executableItemsManager.getExecutableItem(id).orElse(null);
        return executableItem == null ? null : executableItem.buildItem(1, Optional.empty());
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        ExecutableItemsManagerInterface executableItemsManagerInterface = ExecutableItemsAPI.getExecutableItemsManager();
        ExecutableItemInterface executableItemInterface = executableItemsManagerInterface.getExecutableItem(itemStack).orElse(null);
        return executableItemInterface != null ? Utils.convert(getType(), executableItemInterface.getId()) : null;
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
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        ExecutableItemsManagerInterface executableItemsManagerInterface = ExecutableItemsAPI.getExecutableItemsManager();
        return executableItemsManagerInterface.getExecutableItem(id).isPresent();
    }

    @Override
    public Set<String> getAllItems() {
        ExecutableItemsManagerInterface executableItemsManagerInterface = ExecutableItemsAPI.getExecutableItemsManager();
        if (executableItemsManagerInterface.getExecutableItemIdsList() == null) return Set.of();
        return executableItemsManagerInterface.getExecutableItemIdsList().stream().map(id -> Utils.convert(getType(), id)).collect(Collectors.toUnmodifiableSet());
    }
}
