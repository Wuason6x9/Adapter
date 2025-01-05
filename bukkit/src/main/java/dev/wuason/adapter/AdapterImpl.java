package dev.wuason.adapter;

import dev.wuason.adapter.plugins.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class AdapterImpl {

    private static boolean initialized = false;

    public static ItemStack getItemStack(String adapterId) {
        return getAdapterData(adapterId).map(aData -> aData.adapter().getAdapterItem(aData.id())).orElse(null);
    }

    public static List<ItemStack> getItemsStack(List<String> itemsId) {
        return itemsId.stream().map(AdapterImpl::getItemStack).filter(Objects::nonNull).toList();
    }

    public static Set<String> getAllItems() {
        return compatibilities.values().stream().map(AdapterComp::getAllItems).filter(Objects::nonNull).flatMap(Set::stream).collect(Collectors.toUnmodifiableSet());
    }

    public static boolean isSimilar(String adapterId1, String adapterId2) {
        Objects.requireNonNull(adapterId1, "item1 cannot be null");
        Objects.requireNonNull(adapterId2, "item2 cannot be null");
        try {
            ItemStack i1 = getItemStack(adapterId1);
            ItemStack i2 = getItemStack(adapterId2);
            return i1 != null && i2 != null && i1.isSimilar(i2);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isSimilar(@NotNull String adapterId, @NotNull ItemStack itemStack) {
        Objects.requireNonNull(adapterId, "adapterId cannot be null");
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        ItemStack i1 = getItemStack(adapterId);
        return i1 != null && i1.isSimilar(itemStack);
    }

    public static Optional<String> anySimilar(@NotNull List<String> adapterIds, @NotNull ItemStack itemStack) {
        Objects.requireNonNull(adapterIds, "adapterIds cannot be null");
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        return Optional.ofNullable(adapterIds.stream().filter(a -> isSimilar(a, itemStack)).findFirst().orElse(null));
    }

    public static Optional<ItemStack> anySimilar(@NotNull List<ItemStack> items, @NotNull String adapterId) {
        Objects.requireNonNull(items, "items cannot be null");
        Objects.requireNonNull(adapterId, "adapterId cannot be null");
        return Optional.ofNullable(items.stream().filter(i -> isSimilar(adapterId, i)).findFirst().orElse(null));
    }

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

    public static String getAdapterId(Block block) {
        return block == null ? null : compatibilities.values()
                .stream()
                .map(adapter -> adapter.getAdapterId(block))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }

    public static String getAdapterId(Entity entity) {
        return entity == null ? null : compatibilities.values()
                .stream()
                .map(impl -> impl.getAdapterId(entity))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }

    public static String getAdvancedAdapterId(ItemStack item) {
        if (item == null) return null;
        ItemStack i = item.clone();
        return compatibilities.values()
                .stream()
                .map(adapter -> adapter.getAdvancedAdapterId(i))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }

    public static String getAdvancedAdapterId(Block block) {
        return block == null ? null : compatibilities.values()
                .stream()
                .map(adapter -> adapter.getAdvancedAdapterId(block))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }

    public static String getAdvancedAdapterId(Entity entity) {
        return entity == null ? null : compatibilities.values()
                .stream()
                .map(impl -> impl.getAdvancedAdapterId(entity))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
                ;
    }


    public static boolean exists(String itemId) {
        return getAdapterData(itemId).map(aData -> aData.adapter().existItemAdapter(aData.id())).orElse(false);
    }

    public static boolean isValid(String itemId) {
        return getAdapterData(itemId).isPresent();
    }

    public static List<String> isValidAdapterIds(List<String> i) {
        return i.stream().filter(a -> !isValid(a)).toList();
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

    public static List<String> getAllReferences(String pluginName) {
        return Collections.unmodifiableList(compatibilities_references.get(getAdapterByName(pluginName)));
    }

    public static AdapterComp getVanillaAdapter() {
        return getAdapter("mc");
    }

    private final static HashMap<String, AdapterComp> compatibilities = new HashMap<>();
    private final static HashMap<String, AdapterComp> compatibilities_types = new HashMap<>();
    private final static HashMap<String, AdapterComp> compatibilities_aliases = new HashMap<>();
    private final static HashMap<AdapterComp, List<String>> compatibilities_references = new HashMap<>();


    @FunctionalInterface
    private interface AdapterCreator {
        AdapterComp create(String pluginName, String type);
    }

    public static void registerAdapter(String pluginName, String type, boolean checkLoaded, List<String> aliases, AdapterCreator creator) {
        PluginManager pm = Bukkit.getPluginManager();
        if (checkLoaded && pm.getPlugin(pluginName) == null) return;
        try {
            AdapterComp adapter = creator.create(pluginName, type);
            compatibilities.put(pluginName.toLowerCase(Locale.ENGLISH), adapter);
            compatibilities_types.put(type.toLowerCase(Locale.ENGLISH), adapter);
            aliases.stream().map(a -> a.toLowerCase(Locale.ENGLISH)).forEach(a -> compatibilities_aliases.put(a, adapter));
            compatibilities_references.put(adapter, new ArrayList<>());
            compatibilities_references.get(adapter).add(type.toLowerCase(Locale.ENGLISH));
            compatibilities_references.get(adapter).addAll(aliases.stream().map(a -> a.toLowerCase(Locale.ENGLISH)).toList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Optional<AdapterData> getAdapterData(String adapterId) { // format: type:id
        if (adapterId == null || adapterId.isBlank() || !adapterId.contains(":")) return Optional.empty();
        String type = adapterId.substring(0, adapterId.indexOf(":")).toLowerCase(Locale.ENGLISH);
        String id = adapterId.substring(adapterId.indexOf(":") + 1);
        if (type.isBlank() || id.isBlank()) return Optional.empty();
        AdapterComp adapter = existAdapter(type) ? getAdapter(type) : getAdapterByAlias(type);
        return adapter != null ? Optional.of(new AdapterData(adapter, id, type)) : Optional.empty();
    }

    public static void init(Plugin plugin) {

        if (initialized) throw new IllegalStateException("Adapter has already been initialized");
        initialized = true;

        String packageName = "dev:wuason:adapter".replace(":", ".");

        if (AdapterImpl.class.getPackage().getName().equals(packageName)) {
            plugin.getLogger().severe("---------------------------------------------");
            plugin.getLogger().severe("---------------------------------------------");
            plugin.getLogger().severe("You need remap the package of adapter library to your package.");
            plugin.getLogger().severe("Remap: " + packageName + " -> your.package.libs.adapter");
            plugin.getLogger().severe("Info: https://github.com/Wuason6x9/Adapter");
            plugin.getLogger().severe("---------------------------------------------");
            plugin.getLogger().severe("---------------------------------------------");
            
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                if (p != plugin && p.getClass().getClassLoader() instanceof PluginClassLoader loader) {
                    try {
                        Class<?> clazz = loader.loadClass("dev.wuason.adapter.AdapterImpl");
                        if (clazz.getClassLoader() != AdapterImpl.class.getClassLoader()) {
                            plugin.getLogger().severe("---------------------------------------------");
                            plugin.getLogger().severe("Plugin: " + p.getName() + " is using the adapter library. Maybe problems will occur.");
                            plugin.getLogger().severe("To fix this, remap the package of the adapter library to your package.");
                            plugin.getLogger().severe("Remap: " + packageName + " -> your.package.libs.adapter");
                            plugin.getLogger().severe("Info: https://github.com/Wuason6x9/Adapter");
                            plugin.getLogger().severe("---------------------------------------------");
                        }
                    } catch (ClassNotFoundException ignore) {
                    }
                }
            }
        }

        Builder.of("vanilla", "mc", VanillaImpl::new)
                .aliases("minecraft")
                .addNameAsAlias()
                .checkLoaded(false)
                .register();
        Builder.of("ItemsAdder", "ia", ItemsAdderImpl::new)
                .addNameAsAlias()
                .register();
        Builder.of("Nexo", "nx", (pluginName, type) -> {
                    try {
                        Class<?> clazz = Class.forName("dev.wuason.adapter.plugins.NexoImpl");
                        return (AdapterComp) clazz.getConstructor(String.class, String.class).newInstance(pluginName, type);
                    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                             IllegalAccessException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                })
                .addNameAsAlias()
                .register();
        Builder.of("Oraxen", "or", OraxenImpl::new)
                .addNameAsAlias()
                .aliases("oxn")
                .register();
        Builder.of("CustomItems", "ci", CustomItemsImpl::new)
                .aliases("cui")
                .addNameAsAlias()
                .register();
        Builder.of("ExecutableBlocks", "eb", ExecutableBlocksImpl::new)
                .addNameAsAlias()
                .register();
        Builder.of("ExecutableItems", "ei", ExecutableItemsImpl::new)
                .addNameAsAlias()
                .register();
        Builder.of("MMOItems", "mmoI", MMOItemsImpl::new)
                .addNameAsAlias()
                .register();
        Builder.of("MythicCrucible", "crucible", MythicCrucibleImpl::new)
                .addNameAsAlias()
                .aliases("mythicC", "mCrucible")
                .register();
        Builder.of("MythicMobs", "mythic", MythicMobsImpl::new)
                .addNameAsAlias()
                .aliases("mMobs", "mythicM")
                .register();
        Builder.of("StorageMechanic", "sm", StorageMechanicImpl::new)
                .addNameAsAlias()
                .aliases("storageM", "sMechanic")
                .register();
    }


    private static class Builder {
        private final String pluginName;
        private final String type;
        private boolean checkLoaded = true;
        private final List<String> aliases = new ArrayList<>();
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
            this.aliases.addAll(Arrays.asList(aliases));
            return this;
        }

        public Builder addNameAsAlias() {
            aliases.add(pluginName.toLowerCase());
            return this;
        }

        public void register() {
            registerAdapter(pluginName, type, checkLoaded, aliases, creator);
        }
    }
}