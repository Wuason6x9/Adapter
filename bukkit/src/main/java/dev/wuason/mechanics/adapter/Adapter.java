package dev.wuason.mechanics.adapter;

import dev.wuason.mechanics.adapter.plugins.VanillaImpl;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Adapter {

    //****************************************
    //************ GET ITEMS STACK ***********
    //****************************************

    /**
     * Retrieves an ItemStack based on the given itemId.
     *
     * @param itemId the identifier of the item, in the format "type:id" (e.g. "mc:dirt")
     * @return the corresponding ItemStack, or null if it doesn't exist
     */

    public static ItemStack getItemStack(String itemId) {
        String[] data = Utils.process(itemId);
        return (data == null) ? null : getAdapter(data[0]).getAdapterItem(data[1]);
    }

    /**
     * Retrieves a list of ItemStacks based on the given item IDs.
     *
     * @param itemsId the list of item IDs, each in the format "type:id" (e.g. "mc:dirt")
     * @return a list of corresponding ItemStacks, or an empty list if no items exist
     */

    public static List<ItemStack> getItemsStack(List<String> itemsId) {
        return itemsId.stream().map(Adapter::getItemStack).filter(Objects::nonNull).toList();
    }

    /**
     * Retrieves a comprehensive list of all item names from all registered adapters.
     *
     * @return a list of strings representing all item names available from the adapters
     */
    public static List<String> getAllItems() {
        return compatibilities.values().stream().map(AdapterComp::getAllItems).flatMap(List::stream).toList();
    }

    //****************************************
    //************ Is Similar Item ***********
    //****************************************

    /**
     * Determines if two items are similar.
     *
     * @param item1 the first item to compare
     * @param item2 the second item to compare
     * @return true if the items are similar, false otherwise
     */
    public static boolean isSimilar(String item1, String item2) {
        try {
            ItemStack i1 = getItemStack(item1);
            ItemStack i2 = getItemStack(item2);
            return i1 != null && i2 != null && i1.isSimilar(i2);
        } catch (Exception e) {
            return false;
        }
    }

    //****************************************
    //************  COMPARE ITEMS   **********
    //****************************************

    /**
     * Compares the given adapter ID and ItemStack to determine if they are similar.
     *
     * @param adapterId the identifier of the adapter, in the format "type:id" (e.g. "mc:dirt")
     * @param itemStack the ItemStack to compare
     * @return true if the adapter ID and ItemStack are similar, false otherwise
     */
    public static boolean compareItems(@NotNull String adapterId, @NotNull ItemStack itemStack) {
        Objects.requireNonNull(adapterId, "adapterId cannot be null");
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        ItemStack i1 = getItemStack(adapterId);
        return i1 != null && i1.isSimilar(itemStack);
    }

    /**
     * Compares a list of adapter identifiers with a given ItemStack to find the first adapter that is similar to the item.
     *
     * @param adapters a non-null list of adapter identifiers, each in the format "type:id"
     * @param itemStack a non-null ItemStack to compare against the adapters
     * @return the first adapter identifier that is similar to the ItemStack, or null if no match is found
     * @throws NullPointerException if either adapters or itemStack is null
     */
    public static String compareItems(@NotNull List<String> adapters, @NotNull ItemStack itemStack) {
        Objects.requireNonNull(adapters, "adapters cannot be null");
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        return adapters.stream().filter(a -> compareItems(a, itemStack)).findFirst().orElse(null);
    }

    //****************************************
    //************ GET ADAPTER ID ************
    //****************************************

    /**
     * Retrieves the adapter ID for the given ItemStack.
     *
     * @param item the input ItemStack
     * @return the adapter ID for the given ItemStack, or null if it cannot be computed
     */
    public static String getAdapterId(ItemStack item) {
        if (item == null) return null;
        ItemStack i = item.clone();
        return compatibilities.values()
                .stream()
                .map(adapter -> adapter.getAdapterId(i))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }

    /**
     * Retrieves the adapter ID for the given Block.
     *
     * @param block the Block to get the adapter ID for
     * @return the adapter ID for the given Block, or null if the Block is null
     */
    public static String getAdapterId(Block block) {
        return block == null ? null : compatibilities.values()
                .stream()
                .map(adapter -> adapter.getAdapterId(block))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }

    /**
     * Retrieves the adapter ID for the given Entity.
     *
     * @param entity the Entity to get the adapter ID for
     * @return the adapter ID for the given Entity, or null if the Entity is null
     */

    public static String getAdapterId(Entity entity) {
        return entity == null ? null : compatibilities.values()
                .stream()
                .map(impl -> impl.getAdapterId(entity))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }

    //****************************************
    //************ VALIDATIONS ***************
    //****************************************

    /**
     * Checks if a given item ID exists.
     *
     * @param itemId the item ID to check, in the format "type:id" (e.g. "mc:dirt")
     * @return true if the item ID exists, false otherwise
     */

    public static boolean exists(String itemId) {
        String normalized = normalize(itemId);
        return normalized != null && compatibilities.values().stream().anyMatch(impl -> impl.existItemAdapter(normalized));
    }

    /**
     * Checks if a given item ID is a valid adapter ID.
     *
     * @param itemId the item ID to check, in the format "type:id" (e.g. "mc:dirt")
     * @return true if the item ID is a valid adapter ID, false otherwise
     */
    public static boolean isValid(String itemId) {
        return Utils.process(itemId) != null;
    }


    /**
     * Checks if a list of adapter IDs is valid.
     *
     * @param i the list of adapter IDs to check
     * @return true if all adapter IDs in the list are valid, false otherwise
     */
    public static boolean isValidAdapterIds(List<String> i) {
        return i.stream().allMatch(Adapter::isValid);
    }

    public static AdapterComp getAdapter(String type) {
        return compatibilities_types.get(type.toLowerCase(Locale.ENGLISH));
    }

    public static AdapterComp getAdapterByName(String pluginName) {
        return compatibilities.get(pluginName.toLowerCase(Locale.ENGLISH));
    }

    public static boolean existAdapter(String type) {
        return compatibilities_types.containsKey(type.toLowerCase(Locale.ENGLISH));
    }

    public static boolean existAdapterByName(String pluginName) {
        return compatibilities.containsKey(pluginName.toLowerCase(Locale.ENGLISH));
    }

    public static boolean existAdapterByAlias(String alias) {
        return compatibilities_aliases.containsKey(alias.toLowerCase(Locale.ENGLISH));
    }

    public static List<String> getAdapterListTypes() {
        return new ArrayList<>(compatibilities_types.keySet());
    }

    public static List<String> getAdapterListPlugins() {
        return new ArrayList<>(compatibilities.keySet());
    }

    public static List<AdapterComp> getAdapters() {
        return new ArrayList<>(compatibilities.values());
    }

    public static AdapterComp getAdapterByAlias(String alias) {
        return compatibilities_aliases.get(alias.toLowerCase(Locale.ENGLISH));
    }

    private static String normalize(String itemId) {
        String[] data = Utils.process(itemId);
        return data == null ? null : data[0] + ":" + data[1];
    }

    private final static HashMap<String, AdapterComp> compatibilities = new HashMap<>();
    private final static HashMap<String, AdapterComp> compatibilities_types = new HashMap<>();
    private final static HashMap<String, AdapterComp> compatibilities_aliases = new HashMap<>();


    @FunctionalInterface
    private interface AdapterCreator {
        AdapterComp create(String pluginName, String type);
    }

    public static void registerAdapter(String pluginName, String type, boolean checkLoaded, String[] aliases, AdapterCreator creator) {
        PluginManager pm = Bukkit.getPluginManager();
        if (pm.getPlugin(pluginName) == null) return;
        try {
            AdapterComp adapter = creator.create(pluginName, type);
            compatibilities.put(pluginName.toLowerCase(Locale.ENGLISH), adapter);
            compatibilities_types.put(type.toLowerCase(Locale.ENGLISH), adapter);
            for (String alias : aliases) {
                compatibilities_aliases.put(alias.toLowerCase(Locale.ENGLISH), adapter);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        Builder.of("vanilla", "mc", VanillaImpl::new)
                .aliases("minecraft")
                .checkLoaded(false)
                .register();
    }


    private static class Builder {
        private final String pluginName;
        private final String type;
        private boolean checkLoaded = true;
        private String[] aliases = new String[0];
        private final AdapterCreator creator;

        public static Builder of(String pluginName, String type, AdapterCreator creator) {
            return new Builder(pluginName, type, creator);
        }

        private Builder(String pluginName, String type, AdapterCreator creator) {
            this.pluginName = pluginName;
            this.type = type;
            this.creator = creator;
        }

        public Builder checkLoaded(boolean checkLoaded) {
            this.checkLoaded = checkLoaded;
            return this;
        }

        public Builder aliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        public void register() {
            registerAdapter(pluginName, type, checkLoaded, aliases, creator);
        }
    }
}