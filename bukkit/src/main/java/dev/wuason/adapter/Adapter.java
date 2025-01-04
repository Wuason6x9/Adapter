package dev.wuason.adapter;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * An interface representing an adapter system for handling item stacks, blocks, entities, and related components.
 * It provides methods to retrieve, validate, and compare adapter IDs and associated objects.
 * The adapter IDs are strings uniquely identifying items or objects in the adapter system.
 */
public interface Adapter {

    /**
     * Retrieves an ItemStack based on the provided Adapter ID.
     * The Adapter ID should be in the format "adapter:id".
     * If the adapter is not found or a format is invalid, throw an exception.
     * If the item is not found, return null.
     *
     * @param adapterId the ID of the item to retrieve, must not be null
     * @return the ItemStack corresponding to the provided item ID, or null if not found
     */
    static @Nullable ItemStack getItemStack(@NotNull String adapterId) { //format adapter:id
        return AdapterImpl.getItemStack(adapterId);
    }


    /**
     * Retrieves the Adapter ID associated with the given ItemStack.
     * The Adapter ID is a unique identifier used to represent the adapter tied to the provided ItemStack.
     *
     * @param itemStack the ItemStack for which the Adapter ID is to be retrieved; must not be null
     * @return the Adapter ID as a non-null String associated with the given ItemStack
     */
    static @NotNull String getAdapterId(@NotNull ItemStack itemStack) {
        return AdapterImpl.getAdapterId(itemStack);
    }


    /**
     * Retrieves the Adapter ID associated with the given Block.
     * The Adapter ID is a unique identifier used to represent the adapter tied to the provided block.
     *
     * @param block the Block for which the Adapter ID is to be retrieved; must not be null
     * @return the Adapter ID as a non-null String associated with the given Block
     */
    static @NotNull String getAdapterId(@NotNull Block block) {
        return AdapterImpl.getAdapterId(block);
    }

    /**
     * Retrieves the Adapter ID associated with the provided entity.
     * If the entity is null, the result will also be null.
     *
     * @param entity the entity for which to retrieve the Adapter ID, must not be null
     * @return the Adapter ID as a String, or null if the entity does not have an associated Adapter ID
     */
    static @Nullable String getAdapterId(@NotNull Entity entity) {
        return AdapterImpl.getAdapterId(entity);
    }

    /**
     * Retrieves the advanced adapter ID associated with the given ItemStack.
     * The advanced adapter ID provides a more detailed or specific identifier
     * for the adapter associated with the provided item.
     * If the adapter doesn't have an advanced adapter ID,
     * return GetAdapterId(itemStack)
     *
     * @param itemStack the ItemStack for which the advanced adapter ID is to be retrieved must not be null
     * @return the advanced adapter ID as a String, or null if the ItemStack has no associated advanced adapter ID
     */
    static @Nullable String getAdvancedAdapterId(@NotNull ItemStack itemStack) {
        return AdapterImpl.getAdvancedAdapterId(itemStack);
    }

    /**
     * Retrieves the advanced adapter ID associated with the given Block.
     * The advanced adapter ID provides a more detailed or specific identifier
     * for the adapter associated with the provided block.
     * If the adapter doesn't have an advanced adapter ID,
     * return GetAdapterId(block)
     *
     * @param block the Block for which the advanced adapter ID is to be retrieved must not be null
     * @return the advanced adapter ID as a String, or null if the Block has no associated advanced adapter ID
     */
    static @Nullable String getAdvancedAdapterId(@NotNull Block block) {
        return AdapterImpl.getAdvancedAdapterId(block);
    }

    /**
     * Retrieves the advanced adapter ID associated with the given entity.
     * The advanced adapter ID provides a more specific or detailed identifier
     * for the adapter tied to the provided entity.
     * If the adapter doesn't have an advanced adapter ID,
     * return GetAdapterId(entity)
     *
     * @param entity the entity for which the advanced adapter ID is to be retrieved; must not be null
     * @return the advanced adapter ID as a String, or null if the entity does not have an associated advanced adapter ID
     */
    static @Nullable String getAdvancedAdapterId(@NotNull Entity entity) {
        return AdapterImpl.getAdvancedAdapterId(entity);
    }

    /**
     * Checks if an adapter with the specified adapter ID exists.
     *
     * @param adapterId the ID of the adapter to check, must not be null
     * @return true if an adapter with the specified adapter ID exists, false otherwise
     */
    static boolean exists(@NotNull String adapterId) {
        return AdapterImpl.exists(adapterId);
    }


    /**
     * Retrieves a set of all item identifiers available within the adapter.
     * Each identifier in the set represents a unique item managed by the adapter.
     * Format: "adapter:id"
     *
     * @return a non-null set containing all item identifiers as strings
     */
    static @NotNull Set<String> getAllItems() {
        return AdapterImpl.getAllItems();
    }

    /**
     * Validates whether the given adapter ID corresponds to a valid adapter.
     * The adapter ID should be in the format "adapter:id".
     *
     * @param adapterId the ID of the adapter to validate, must not be null
     * @return true if the adapter ID is valid, false otherwise
     */
    static boolean isValid(@NotNull String adapterId) {
        return AdapterImpl.isValid(adapterId);
    }


    /**
     * Validates a list of adapter IDs to check which ones are valid.
     * If the adapter ID is invalid, it will be added to the list of invalid adapter IDs.
     * If the list is empty, all adapter IDs are valid.
     *
     * @param adapterIds a non-null list of adapter IDs to validate
     * @return a list of invalid adapter IDs that were found in the input list
     */
    static List<String> isValidAdapterIds(@NotNull List<String> adapterIds) {
        return AdapterImpl.isValidAdapterIds(adapterIds);
    }

    /**
     * Compares two items to determine if they are similar.
     *
     * @param adapterId1 the first item to compare, must not be null
     * @param adapterId2 the second item to compare, must not be null
     * @return true if the two items are similar, false otherwise
     */
    static boolean isSimilar(@NotNull String adapterId1, @NotNull String adapterId2) {
        return AdapterImpl.isSimilar(adapterId1, adapterId2);
    }

    /**
     * Checks whether the provided ItemStack is similar to the item associated with the given adapter ID.
     *
     * @param adapterId the ID of the adapter to compare against, must not be null
     * @param itemStack the ItemStack to compare, must not be null
     * @return true if the ItemStack is similar to the item associated with the adapter ID, false otherwise
     */
    static boolean isSimilar(@NotNull String adapterId, @NotNull ItemStack itemStack) {
        return AdapterImpl.isSimilar(adapterId, itemStack);
    }


    /**
     * Checks if any of the provided adapter IDs corresponds to an item that is similar
     * to the given ItemStack. Similarity is determined based on implementation-specific criteria.
     *
     * @param adapterIds a non-null list of adapter IDs to check for similarity
     * @param itemStack a non-null ItemStack to compare against the items associated with the adapter IDs
     * @return an Optional containing the first adapter ID considered similar to the given ItemStack,
     *         or an empty Optional if no similar items are found
     */
    static @NotNull Optional<String> anySimilar(@NotNull List<String> adapterIds, @NotNull ItemStack itemStack) {
        return AdapterImpl.anySimilar(adapterIds, itemStack);
    }

    /**
     * Finds any ItemStack in the provided list of ItemStacks that is similar
     * to the item associated with the specified adapter ID.
     *
     * @param itemStacks the list of ItemStacks to search through, must not be null
     * @param adapterId the adapter ID to compare against, must not be null
     * @return an Optional containing a similar ItemStack if found, or an empty Optional if no similar ItemStack exists
     */
    static @NotNull Optional<ItemStack> anySimilar(@NotNull List<ItemStack> itemStacks, @NotNull String adapterId) {
        return AdapterImpl.anySimilar(itemStacks, adapterId);
    }

    /**
     * Retrieves the vanilla adapter implementation of the {@link AdapterComp}.
     *
     * @return an instance of {@link AdapterComp} representing the vanilla adapter, never null
     */
    static @NotNull AdapterComp getVanillaAdapter() {
        return AdapterImpl.getVanillaAdapter();
    }

    /**
     * Retrieves an AdapterComp instance based on the specified type.
     *
     * @param type the type of the adapter to retrieve, must not be null
     * @return a non-null AdapterComp instance corresponding to the specified type
     */
    static @NotNull AdapterComp getAdapter(@NotNull String type) {
        return AdapterImpl.getAdapter(type);
    }

    /**
     * Retrieves an {@link AdapterComp} instance based on the provided alias.
     * The alias is used as a unique identifier to fetch the corresponding adapter.
     *
     * @param alias the alias of the adapter to retrieve; must not be null
     * @return a non-null {@link AdapterComp} instance corresponding to the provided alias
     */
    static @NotNull AdapterComp getAdapterByAlias(@NotNull String alias) {
        return AdapterImpl.getAdapterByAlias(alias);
    }

    /**
     * Retrieves an adapter instance based on its associated plugin name.
     * The plugin name should match the name associated with an available adapter.
     *
     * @param pluginName the name of the plugin for which the adapter is to be retrieved must not be null
     * @return a non-null instance of {@link AdapterComp} corresponding to the plugin name
     */
    static @NotNull AdapterComp getAdapterByName(@NotNull String pluginName) {
        return AdapterImpl.getAdapterByName(pluginName);
    }

    /**
     * Initializes the adapter with the provided plugin instance.
     * This method is used to set up the adapter implementation for the specified plugin.
     *
     * @param plugin the plugin instance to be used for initialization, must not be null
     */
    static void init(Plugin plugin) {
        AdapterImpl.init(plugin);
    }

}
