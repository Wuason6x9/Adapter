package dev.wuason.adapter.plugins;

import com.ssomar.executableblocks.api.ExecutableBlocksAPI;
import com.ssomar.executableblocks.executableblocks.ExecutableBlock;
import com.ssomar.executableblocks.executableblocks.ExecutableBlocksManager;
import com.ssomar.executableblocks.executableblocks.placedblocks.ExecutableBlockPlaced;
import dev.wuason.adapter.AdapterComp;
import dev.wuason.adapter.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;

public class ExecutableBlocksImpl extends AdapterComp {

    public ExecutableBlocksImpl(String name, String type) {
        super(name, type);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        ExecutableBlocksManager executableBlocksManager = ExecutableBlocksAPI.getExecutableBlocksManager();
        if (!executableBlocksManager.isValidID(id)) return null;
        ExecutableBlock executableBlock = executableBlocksManager.getExecutableBlock(id).orElse(null);
        return executableBlock == null ? null : executableBlock.buildItem(1, null, null);
    }

    @Override
    public String getAdapterId(Block block) {
        ExecutableBlockPlaced executableBlockPlaced = ExecutableBlocksAPI.getExecutableBlocksPlacedManager().getExecutableBlockPlaced(block).orElse(null);
        return executableBlockPlaced != null ? Utils.convert(getType(), executableBlockPlaced.getEB_ID()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null; // ExecutableBlocks doesn't support entities now
    }

    @Override
    public boolean existItemAdapter(String id) {
        return ExecutableBlocksAPI.getExecutableBlocksManager().isValidID(id) && ExecutableBlocksAPI.getExecutableBlocksManager().getExecutableBlock(id).isPresent();
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
    public String getAdvancedAdapterId(ItemStack itemStack) {
        return getAdapterId(itemStack);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        ExecutableBlock executableBlock = ExecutableBlocksAPI.getExecutableBlocksManager().getExecutableBlock(itemStack).orElse(null);
        return executableBlock != null ? Utils.convert(getType(), executableBlock.getId()) : null;
    }

    @Override
    public Set<String> getAllItems() {
        if (ExecutableBlocksAPI.getExecutableBlocksManager().getExecutableBlockIdsList() == null) return Set.of();
        return ExecutableBlocksAPI.getExecutableBlocksManager().getExecutableBlockIdsList().stream().map(id -> Utils.convert(getType(), id)).collect(Collectors.toUnmodifiableSet());
    }

}
