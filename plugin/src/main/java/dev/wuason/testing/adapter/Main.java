package dev.wuason.testing.adapter;

import dev.wuason.adapter.Adapter;
import dev.wuason.adapter.AdapterImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Adapter Plugin has been enabled!");

        Adapter.init(this);

        PluginCommand command = getCommand("adapter");
        if (command == null) {
            getLogger().severe("Command 'adapter' not found in plugin.yml!");
            return;
        }

        command.setExecutor((sender, c, s, args) -> {
            if (args.length == 0) {
                sender.sendMessage("§eUsage: §7/adapter §f<list|get|give|exist> ...");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "list" -> {
                    sender.sendMessage("§aAvailable adapters:");
                    AdapterImpl.getAdapters().forEach(adapter -> {
                        sender.sendMessage("§7 - §e" + adapter.getType() + "§7: §f" + adapter.getName() + " ( enabled: " + adapter.isEnabled() + " )");
                    });
                }

                case "exist" -> {
                    if (args.length < 2) {
                        sender.sendMessage("§eUsage: §7/adapter exist §f<adapter:id>");
                        return true;
                    }
                    String adapterId = args[1];
                    boolean exists = Adapter.exists(adapterId);
                    sender.sendMessage("§7Adapter §e" + adapterId + "§7 exists: " + (exists ? "§atrue" : "§cfalse"));
                }

                case "get" -> {
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage("§cOnly players can use this subcommand.");
                        return true;
                    }
                    String target = args.length >= 2 ? args[1].toLowerCase() : "hand";
                    switch (target) {
                        case "hand" -> {
                            ItemStack inHand = player.getInventory().getItemInMainHand();
                            if (inHand == null || inHand.getType() == Material.AIR) {
                                player.sendMessage("§cYou must hold an item in your main hand.");
                                return true;
                            }
                            String id = Adapter.getAdapterId(inHand);
                            String adv = Adapter.getAdvancedAdapterId(inHand);
                            if (id == null && adv == null) {
                                player.sendMessage("§eNo adapter id found for that item.");
                            } else {
                                if (id != null) player.sendMessage("§7AdapterId: §a" + id);
                                if (adv != null && !adv.equals(id)) player.sendMessage("§7AdvancedId: §b" + adv);
                            }
                        }
                        case "block" -> {
                            Block targetBlock = player.getTargetBlockExact(6);
                            if (targetBlock == null) {
                                player.sendMessage("§cLook at a block within 6 blocks.");
                                return true;
                            }
                            String id = Adapter.getAdapterId(targetBlock);
                            String adv = Adapter.getAdvancedAdapterId(targetBlock);
                            if (id == null && adv == null) {
                                player.sendMessage("§eNo adapter id found for that block.");
                            } else {
                                if (id != null) player.sendMessage("§7AdapterId: §a" + id);
                                if (adv != null && !adv.equals(id)) player.sendMessage("§7AdvancedId: §b" + adv);
                            }
                        }
                        default -> player.sendMessage("§eUsage: §7/adapter get §f<hand|block>");
                    }
                }

                case "give" -> {
                    if (args.length < 3) {
                        sender.sendMessage("§eUsage: §7/adapter give §f<player> <adapter:id> [amount]");
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found: §f" + args[1]);
                        return true;
                    }
                    String adapterId = args[2];
                    int amount = 1;
                    if (args.length >= 4) {
                        try {
                            amount = Math.max(1, Integer.parseInt(args[3]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§cInvalid amount: §f" + args[3]);
                            return true;
                        }
                    }

                    if (!Adapter.exists(adapterId)) {
                        sender.sendMessage("§cAdapter item does not exist: §f" + adapterId);
                        return true;
                    }

                    ItemStack base = Adapter.getItemStack(adapterId);
                    if (base == null) {
                        sender.sendMessage("§cFailed to get item for: §f" + adapterId);
                        return true;
                    }

                    int maxStack = Math.max(1, base.getMaxStackSize());
                    int remaining = amount;
                    while (remaining > 0) {
                        int giveNow = Math.min(maxStack, remaining);
                        ItemStack toGive = base.clone();
                        toGive.setAmount(giveNow);
                        target.getInventory().addItem(toGive);
                        remaining -= giveNow;
                    }

                    sender.sendMessage("§aGave §e" + amount + "§a of §f" + adapterId + " §ato §e" + target.getName() + "§a.");
                }

                default -> {
                    sender.sendMessage("§eUsage: §7/adapter §f<list|get|give|exist> ...");
                    return true;
                }
            }

            return true;
        });

        command.setTabCompleter((sender, c, label, args) -> {
            List<String> out = new ArrayList<>();
            if (args.length == 1) {
                out = Arrays.asList("list", "get", "give", "exist");
            } else if (args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case "get" -> out = Arrays.asList("hand", "block");
                    case "give" -> {
                        for (Player p : Bukkit.getOnlinePlayers()) out.add(p.getName());
                    }
                    case "exist" -> out = List.of("<adapter:id>");
                    default -> out = List.of();
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
                out = Adapter.getAllItems().stream()
                        .filter(id -> id.toLowerCase().startsWith(args[2].toLowerCase()))
                        .toList();
            } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
                out = List.of("1", "16", "32", "64");
            }
            return out;
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("Adapter Plugin has been disabled!");
    }
}