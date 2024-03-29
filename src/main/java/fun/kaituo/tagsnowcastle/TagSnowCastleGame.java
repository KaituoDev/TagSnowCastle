package fun.kaituo.tagsnowcastle;


import fun.kaituo.gameutils.Game;
import fun.kaituo.gameutils.PlayerQuitData;
import fun.kaituo.gameutils.event.PlayerEndGameEvent;
import fun.kaituo.gameutils.utils.ItemStackBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;


public class TagSnowCastleGame extends Game implements Listener {
    private static final TagSnowCastleGame instance = new TagSnowCastleGame((TagSnowCastle) Bukkit.getPluginManager().getPlugin("TagSnowCastle"));
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    Scoreboard tag3 = Bukkit.getScoreboardManager().getNewScoreboard();
    TagSnowCastle plugin;
    List<Player> humans = new ArrayList<>();
    List<Player> devils = new ArrayList<>();
    long startTime;
    long gameTime;
    Team team;
    Location[] locations = new Location[]{
            new Location(world, -1005, 69, 1000),
            new Location(world, -991, 65, 1005),
            new Location(world, -1019, 66, 1014),
            new Location(world, -1010, 67, 1005),
            new Location(world, -1018, 66, 992),
            new Location(world, -983, 64, 997),
            new Location(world, -998, 54, 1009),
            new Location(world, -986, 56, 1012),
            new Location(world, -1015, 54, 1015),
            new Location(world, -1017, 51, 996),
            new Location(world, -981, 57, 986),
            new Location(world, -986, 48, 981),
            new Location(world, -1000, 68, 1013),
            new Location(world, -1001, 53, 998),
            new Location(world, -991, 57, 998)};
    boolean running = false;
    int countDownSeconds = 10;


    HashMap<Player, List<Long>> coolDown = new HashMap<>();
    HashMap<ArmorStand, ArmorStand> armourStandMap = new HashMap<>();
    HashMap<ArmorStand, Player> playerMap = new HashMap<>();

    ItemStack feather = generateItemStack(Material.FEATHER, "§l§b渡渡快跑", new String[]{"可以调整移动速度", "§7模仿那只渡渡的跑法的话，应该就能以势不可挡的步伐冲刺了吧"});
    ItemStack cooked_chicken = generateItemStack(Material.COOKED_CHICKEN, "§l§b渡渡快跑", new String[]{"可以调整移动速度", "§7模仿那只渡渡的跑法的话，应该就能以势不可挡的步伐冲刺了吧"});
    ItemStack glass_bottle = generateItemStack(Material.GLASS_BOTTLE, "§l§7透明身体", new String[]{"将身体变得透明", "§7欺骗敌人"});
    ItemStack nether_star = generateItemStack(Material.NETHER_STAR, "§l§c还魂", new String[]{"使友方复活", "§7已经被消费掉的东西是无法返还的...", "§7是呢，除非回溯时间"});
    ItemStack clock = generateItemStack(Material.CLOCK, "§b§l兔子的怀表", new String[]{"防止一次收到的伤害", "§7被规矩束缚的白兔绝不会在任何一场审判中迟到"});
    ItemStack coal = generateItemStack(Material.COAL, "§l§8污秽的黑之魂", new String[]{"回复所有生命值，但会降低最大生命上限", "§7这个国家洋溢着大量的黑之魂！太棒了！"});
    ItemStack potion = generateItemStack(Material.POTION, "§l§1星水", new String[]{"使用后一段时间内不会受到伤害，但效果结束后会被强制击倒", "§7没有门扉的箱庭会有漂流者顺流而来，其中似乎也混杂着外来者"});
    ItemStack dragon_breath = generateItemStack(Material.DRAGON_BREATH, "§l§d心跳悦动酒", new String[]{"瞬间恢复一定生命值，但会获得两倍回复量的发光时间", "§7梅贝尔最爱喝的酒！"});
    ItemStack honey_bottle = generateItemStack(Material.HONEY_BOTTLE, "§l§6黄金的蜂蜜酒", new String[]{"减少一半的当前生命值，获得减少数量两倍的加速时间", "§7带翼的贵妇人相当喜欢这个蜂蜜酒"});
    ItemStack enchanted_book = generateItemStack(Material.ENCHANTED_BOOK, "§l§8§k爱丽丝·里德尔的誓约", new String[]{"持有者速度永久增加，但无法被复活", "§7§m我将永远爱着你"});
    ItemStack mushroom_stew = generateItemStack(Material.MUSHROOM_STEW, "§2比尔的便当", new String[]{"可以为已损失生命值最多的友方角色恢复全部生命值","","§7§o充满女子力的便当，篮子里塞满了蛋和各种各样的果物"});


    List<ItemStack> gadgets = Arrays.asList(feather, glass_bottle, nether_star, clock, potion, honey_bottle, coal, dragon_breath);
    List<Integer> gadgetWeights = Arrays.asList(10, 5, 1, 10, 1, 10, 5, 10);

    int totalWeight;

    private TagSnowCastleGame(TagSnowCastle plugin) {
        this.plugin = plugin;
        initializeGame(plugin, "TagSnowCastle", "§f白雪城", new Location(world, -1000, 76, 1010));
        initializeButtons(new Location(world, -1000, 77, 1015), BlockFace.NORTH,
                new Location(world, -1007, 77, 1010), BlockFace.EAST);
        initializeGameRunnable();
        players = TagSnowCastle.players;
        tag3.registerNewObjective("tag3", "dummy", "鬼抓人");
        tag3.getObjective("tag3").setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i : gadgetWeights) {
            totalWeight += i;
        }
    }

    public static TagSnowCastleGame getInstance() {
        return instance;
    }

    @EventHandler
    public void clearCoolDown(PlayerDeathEvent pde) {
        clearCoolDown(pde.getEntity());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent ede) { //受击特效
        if (!(ede.getEntity() instanceof Player)) {
            return;
        }
        if (ede.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if ((((EntityDamageByEntityEvent) ede).getDamager() instanceof Player)) {
                if (humans.contains(((EntityDamageByEntityEvent) ede).getDamager())) {
                    return;
                }
            }
        } //友军不能触发特效
        Player p = (Player) ede.getEntity();
        if (scoreboard.getTeam("tag3cheshirecat").hasPlayer(p)) {
            if (ede.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                ((Player) ede.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false));
                ede.setCancelled(true);
            } else if (p.getInventory().contains(Material.CLOCK, 1)) {
                removeItem(p, Material.CLOCK);
                ede.setCancelled(true);
            }
        } else if (p.getInventory().contains(Material.CLOCK, 1)) {
            removeItem(p, Material.CLOCK);
            ede.setCancelled(true);
        }
        if (scoreboard.getTeam("tag3kelti").hasPlayer(p)) {
            if (checkCoolDown(p, 60)) {
                p.sendMessage("§b获得暂时隐身！");
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0, false, false));
            }
        }
        if (scoreboard.getTeam("tag3redhat").hasPlayer(p)) {
            p.sendMessage("§c获得生命恢复与发光！");
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0, false, false));
        }
        if (scoreboard.getTeam("tag3dodo").hasPlayer(p)) {
            p.sendMessage("§a获得加速！");
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
        }
    }

    private void initializePlayer(Player p) {
        switch (scoreboard.getPlayerTeam(p).getName()) {
            case "tag3norden" -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 10000000, 4, true, false));
            }
            case "tag3kelti" -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 10000000, 0, false, false));
            }
            case "tag3mabel" -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000000, 0, false, false));
            }
            case "tag3dodo" -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 0, false, false));
            }
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 0, false, false));
    }

    private void revivePlayer(PlayerInteractAtEntityEvent piaee) {
        ArmorStand s = (ArmorStand) piaee.getRightClicked();
        if ((armourStandMap.get(s) == null)) {
            return;
        }
        Player p = playerMap.get(s);
        if (!(players.contains(p) && !humans.contains(p))) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Location l = s.getLocation();
            l.setY(l.getY() + 1.4);
            humans.add(p);
            p.teleport(l);
            p.setGameMode(GameMode.ADVENTURE);
            initializePlayer(p);
        }, 1);
        armourStandMap.get(s).remove();
        s.remove();
        armourStandMap.remove(s);
        playerMap.remove(s);
    }

    @EventHandler
    public void armorStandOperation(PlayerInteractAtEntityEvent piaee) {
        if (!piaee.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            return;
        }
        if (!(players.contains(piaee.getPlayer()))) {
            return;
        }
        if (!(piaee.getRightClicked() instanceof ArmorStand)) {
            return;
        }
        if (piaee.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            piaee.setCancelled(true);
        }

        ItemStack item = piaee.getPlayer().getInventory().getItemInMainHand();
        if (item.getType().equals(Material.BOOK)) {
            revivePlayer(piaee);
            item.setAmount(item.getAmount() - 1);
            piaee.getPlayer().getInventory().addItem(enchanted_book);
            piaee.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 0));

        } else if (item.getType().equals(Material.NETHER_STAR)) {
            revivePlayer(piaee);
            item.setAmount(item.getAmount() - 1);
        }
    }

    @EventHandler
    public void destroyChest(PlayerInteractEvent pie) {
        if (!pie.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }// 不是右键方块
        Player executor = pie.getPlayer();
        if (!(devils.contains(executor))) {
            return;
        } //不是鬼
        Block block = pie.getClickedBlock();
        if (block.getType().equals(Material.TRAPPED_CHEST)) {
            if (pie.getItem() == null) {
                return;
            }//没有物品
            if (!pie.getItem().getType().equals(Material.POPPED_CHORUS_FRUIT)) {
                return;
            }
            pie.setCancelled(true);
            if (((Chest) (block.getState())).getBlockInventory().isEmpty()) {
                executor.sendMessage("§c这个箱子是空的！");
            } else if (checkCoolDown(executor, 600)) {
                ((Chest) (block.getState())).getBlockInventory().clear();
                executor.sendMessage("§a成功破坏箱子内的所有道具！");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent pie) { //右键特效
        if (!pie.getAction().equals(Action.RIGHT_CLICK_AIR) && !pie.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }// 不是右键
        Player executor = pie.getPlayer();
        if (!players.contains(executor)) {
            return;
        }//不在tag3里
        if (devils.contains(executor)) { //是鬼
            return;
        } else if (humans.contains(executor)) { //是人
            if (pie.getClickedBlock() != null) {
                if (pie.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                    if (!pie.getPlayer().isSneaking()) {
                        executor.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0));
                        return;
                    }
                }
            }
            if (pie.getItem() == null) {
                return;
            }//没有物品
            //这里开始添加内容

            switch (pie.getItem().getType()) {
                case MUSHROOM_STEW -> {
                    Player humanWhoLostMostHealth = executor;
                    double lostHealth = 0;
                    for (Player p : humans) {
                        if (scoreboard.getTeam("tag3dodo").hasPlayer(p) || scoreboard.getTeam("tag3bill").hasPlayer(p)) {
                            continue;
                        }
                        double newLostHealth = p.getMaxHealth() - p.getHealth();
                        if (newLostHealth >= lostHealth) {
                            humanWhoLostMostHealth = p;
                            lostHealth = newLostHealth;
                        }
                    }
                    if (lostHealth == 0) {
                        clearCoolDown(executor);
                        executor.sendMessage("§c己方可选目标玩家全部满血！技能未生效！");
                    } else {
                        pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                        humanWhoLostMostHealth.setHealth(humanWhoLostMostHealth.getMaxHealth());
                        executor.sendMessage("§a为 §f" + humanWhoLostMostHealth.getName() + " §a恢复全部生命值！");
                        humanWhoLostMostHealth.sendMessage("§2" + executor.getName() + " §f为你恢复了全部生命值！");
                    }
                }


                case COAL -> {
                    if (scoreboard.getTeam("tag3norden").hasPlayer(executor)) {
                        executor.sendMessage("§c生命全部恢复！");
                        pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                    } else {
                        if (executor.getMaxHealth() > 3) {
                            executor.sendMessage("§c最大生命值减少，生命全部恢复！");
                            executor.setMaxHealth(executor.getMaxHealth() - 3);
                            if (!scoreboard.getTeam("tag3dodo").hasPlayer(executor)) {
                                executor.setHealth(executor.getMaxHealth());
                            } else {
                                executor.sendMessage("§c你无法通过这种方式恢复生命值！");
                            }
                            pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                        } else {
                            executor.sendMessage("§c生命上限过低，无法使用！");
                        }
                    }
                }
                case POTION -> {
                    pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                    executor.sendMessage("§c30秒内无敌，30秒后将被强制击倒！");
                    executor.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 254, false, false));
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        executor.setHealth(0);
                    }, 600);
                }
                case GLASS_BOTTLE -> {
                    pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                    executor.sendMessage("§c隐身10秒！");
                    executor.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0, false, false));
                }
                case DRAGON_BREATH -> {
                    pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                    executor.sendMessage("§c回复一半最大生命值，获得与回复量相同的发光时长！");
                    if (executor.getHealth() * 2 < executor.getMaxHealth()) {
                        executor.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (int) (executor.getMaxHealth() * 10), 0, false, false));
                        if (!scoreboard.getTeam("tag3dodo").hasPlayer(executor)) {
                            executor.setHealth(executor.getHealth() + executor.getMaxHealth() / 2);
                        }else {
                            executor.sendMessage("§c你无法通过这种方式恢复生命值！");
                        }
                    } else {
                        executor.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (int) ((executor.getMaxHealth() - executor.getHealth()) * 20), 0, false, false));
                        if (!scoreboard.getTeam("tag3dodo").hasPlayer(executor)) {
                            executor.setHealth(executor.getMaxHealth());
                        } else {
                            executor.sendMessage("§c你无法通过这种方式恢复生命值！");
                        }
                    }
                }
                case HONEY_BOTTLE -> {
                    pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                    executor.sendMessage("§e减少一半的当前生命值，获得与减少量两倍相同的加速时间！");
                    executor.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (executor.getHealth() * 20), 0, false, false));
                    executor.setHealth(executor.getHealth() / 2);
                }
                case FEATHER -> {
                    pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                    executor.getInventory().addItem(cooked_chicken);
                    executor.sendMessage("§b获得加速！");
                    executor.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0, false, false));
                    if (scoreboard.getTeam("tag3dodo").hasPlayer(executor)) {
                        executor.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
                    }
                }
                case COOKED_CHICKEN -> {
                    pie.getItem().setAmount(pie.getItem().getAmount() - 1);
                    executor.sendMessage("§b获得加速！");
                    executor.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false));
                }
            }
        }
    }

    @EventHandler
    public void cancelItemMovement(InventoryClickEvent ice) {
        if (!(ice.getWhoClicked() instanceof Player)) {
            return;
        }
        if (ice.getCurrentItem() == null) {
            return;
        }
        Player p = (Player) ice.getWhoClicked();
        if (devils.contains(p)) {
            ice.setCancelled(true);
        } else if (humans.contains(p)) {
            if (ice.getCurrentItem().getType().equals(Material.BOOK)
                    || ice.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)
                    || ice.getCurrentItem().getType().equals(Material.HEART_OF_THE_SEA)
                    || ice.getCurrentItem().getType().equals(Material.RABBIT_FOOT)
                    || ice.getCurrentItem().getType().equals(Material.ENDER_EYE)
                    || ice.getCurrentItem().getType().equals(Material.STRING)
                    || ice.getCurrentItem().getType().equals(Material.RED_DYE)
                    || ice.getCurrentItem().getType().equals(Material.POPPED_CHORUS_FRUIT)) {
                ice.setCancelled(true);
            }
        }
    }

    public void removeItem(Player p, Material material) {
        p.getInventory().all(material).get(0);
        for (Map.Entry entry : p.getInventory().all(material).entrySet()) {
            ItemStack i = (ItemStack) entry.getValue();
            i.setAmount(i.getAmount() - 1);
            break;
        }
    }

    public void clearCoolDown(Player p) {
        p.setLevel(0);
        p.setExp(0);
        if (coolDown.get(p) != null) {
            coolDown.get(p).set(0, 0L);
            coolDown.get(p).set(1, 0L);
        }
    }

    private void setCoolDown(Player p, long remainingCoolDown, long totalCoolDown) {
        coolDown.get(p).set(0, remainingCoolDown);
        coolDown.get(p).set(1, totalCoolDown);
    }

    private boolean checkCoolDown(Player p, long cd) {
        if (coolDown.get(p).get(0) == 0) {
            coolDown.get(p).set(0, cd);
            coolDown.get(p).set(1, cd);
            return true;
        } else {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§4§l技能冷却中！"));
            return false;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde) {
        if (!players.contains(pde.getEntity())) {
            return;
        }
        devils.remove(pde.getEntity());
        humans.remove(pde.getEntity());
        pde.getEntity().setGameMode(GameMode.SPECTATOR);
        for (Player p : players) {
            p.sendMessage("§f" + pde.getEntity().getName() + " §c的灵魂被收割了！");
        }
    }

    @EventHandler
    public void freezeGui(EntityDamageByEntityEvent edbee) {
        if (!(edbee.getDamager() instanceof Player)) {
            return;
        }
        if (!(edbee.getEntity() instanceof Player)) {
            return;
        }
        Player victim = (Player) edbee.getEntity();
        Player damager = (Player) edbee.getDamager();
        if (humans.contains(damager) && devils.contains(victim)) {
            if (scoreboard.getTeam("tag3eunice").hasPlayer(damager)) {
                if (damager.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)) {
                    if (checkCoolDown(damager,  1200)) {
                        damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false));
                        damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,  150, 1, false, false));
                        damager.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,  100, 0, false, false));
                        damager.sendMessage("§c获得加速、恢复和隐身！");
                    }
                }
            } else {
                edbee.setCancelled(true);
            }
        } else if (humans.contains(damager) && humans.contains(victim)) {
            edbee.setCancelled(true);
        } else if (devils.contains(damager) && humans.contains(victim)) {
            int freezeTime = 60;
            if (scoreboard.getTeam("tag3miranda").hasPlayer(damager)) { //米兰达
                freezeTime = 110;
            }
            Location l = damager.getLocation().clone();
            int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                damager.teleport(l);
            }, 1, 1);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Bukkit.getScheduler().cancelTask(id);
            }, freezeTime);
            damager.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, freezeTime, 254, false, false));
        }
    }

    //seems not useful
    /*
    @EventHandler
    public void cancelPickup(PlayerPickupItemEvent ppie) {
        Player p = ppie.getPlayer();
        if (humans.contains(p)) {
            if (ppie.getItem().getItemStack().getType().equals(Material.CLOCK)
                    || ppie.getItem().getItemStack().getType().equals(Material.EMERALD)) {
                ppie.setCancelled(true);
            }
        } else if (devils.contains(p)) {
            if (ppie.getItem().getItemStack().getType().equals(Material.DIAMOND)
                    || ppie.getItem().getItemStack().getType().equals(Material.RED_DYE)
                    || ppie.getItem().getItemStack().getType().equals(Material.HEART_OF_THE_SEA)) {
                ppie.setCancelled(true);
            }
        }
    }

     */

    public ItemStack generateItemStack(Material material, String name, String[] lore) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore.clone()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void summonCorpse(PlayerDeathEvent pde) {
        if (!(players.contains(pde.getEntity()))) {
            return;
        }
        if ((devils.contains(pde.getEntity()))) {
            return;
        }
        if (pde.getEntity().getInventory().contains(Material.ENCHANTED_BOOK, 1)) {
            for (Player p : players) {
                p.sendMessage("§f" + pde.getEntity().getName() + " §c 永远葬身于寒冷");
            }
            return;
        }
        Location l = pde.getEntity().getLocation().clone();

        ArmorStand ice = (ArmorStand) world.spawnEntity(l, EntityType.ARMOR_STAND);
        ice.setBasePlate(false);
        ice.setInvisible(true);
        ice.setVelocity(new Vector(0, -50, 0));

        l.setY(0);

        ArmorStand head = (ArmorStand) world.spawnEntity(l, EntityType.ARMOR_STAND);
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        skullMeta.setOwningPlayer(pde.getEntity());
        headItem.setItemMeta(skullMeta);
        head.setBasePlate(false);
        head.setSmall(true);
        head.getEquipment().setHelmet(headItem);
        head.setGravity(false);
        head.setCustomName(pde.getEntity().getName());
        head.setCustomNameVisible(true);
        head.setInvisible(true);
        EulerAngle angle = new EulerAngle(Math.PI, 0, 0);
        head.setLeftLegPose(angle);
        head.setRightLegPose(angle);

        armourStandMap.put(ice, head);
        playerMap.put(ice, pde.getEntity());

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ice.setGravity(false);
            Location iceLocation = ice.getLocation().clone();
            iceLocation.setY(iceLocation.getY() - 1.4);
            ice.teleport(iceLocation);
            iceLocation.setY(iceLocation.getY() + 0.75);
            head.teleport(iceLocation);
        }, 5);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ice.getEquipment().setHelmet(new ItemStack(Material.ICE));
            head.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 550, 0));
        }, 6);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (head.isValid()) {
                head.remove();
                ice.remove();
                for (Player p : players) {
                    p.sendMessage("§f" + pde.getEntity().getName() + " §c 永远葬身于寒冷");
                }
            }
        }, 600);
    }

    @EventHandler
    public void preventDroppingItem(PlayerDropItemEvent pdie) {
        if (!(players.contains(pdie.getPlayer()))) {
            return;
        }
        if (!pdie.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            return;
        }
        switch (pdie.getItemDrop().getItemStack().getType()) {
            case RABBIT_FOOT, BOOK, STRING, HEART_OF_THE_SEA, ENCHANTED_BOOK, ENDER_EYE, RED_DYE, POPPED_CHORUS_FRUIT,
                    BLAZE_POWDER, TOTEM_OF_UNDYING, END_ROD, IRON_SWORD, SWEET_BERRIES, NETHERITE_AXE, NETHERITE_SWORD-> pdie.setCancelled(true);
            default -> {
            }
        }
    }

    @EventHandler
    public void preventRegen(EntityRegainHealthEvent erhe) {
        if (!(erhe.getEntity() instanceof Player)) {
            return;
        }
        if (!(players.contains(erhe.getEntity()))) {
            return;
        }
        if (!((Player) erhe.getEntity()).getGameMode().equals(GameMode.ADVENTURE)) {
            return;
        }
        if (erhe.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
            erhe.setCancelled(true);
        } else if (erhe.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.EATING)) {
            erhe.setCancelled(true);
        }
    }

    @Override
    protected void quit(Player p) {
        if (players.contains(p)) {
            return;
        }
        PlayerQuitData quitData = new PlayerQuitData(p, this, gameUUID);
        quitData.getData().put("team", whichGroup(p));
        gameUtils.setPlayerQuitData(p.getUniqueId(), quitData);
        players.remove(p);
        humans.remove(p);
        devils.remove(p);
    }


    @Override
    protected boolean rejoin(Player p) {
        if (!running) {
            p.sendMessage("§c游戏已经结束！");
            return false;
        }
        if (!gameUtils.getPlayerQuitData(p.getUniqueId()).getGameUUID().equals(gameUUID)) {
            p.sendMessage("§c游戏已经结束！");
            return false;
        }
        PlayerQuitData pqd = gameUtils.getPlayerQuitData(p.getUniqueId());
        pqd.restoreBasicData(p);
        players.add(p);
        team.addPlayer(p);
        p.setScoreboard(tag3);
        if (pqd.getData().get("team") != null) {
            ((List<Player>) pqd.getData().get("team")).add(p);
        }
        gameUtils.setPlayerQuitData(p.getUniqueId(), null);
        return true;
    }

    @Override
    protected boolean join(Player player) {
        player.setBedSpawnLocation(hubLocation, true);
        player.teleport(hubLocation);
        scoreboard.getTeam("tag3norden").addPlayer(player);
        return true;
    }

    @Override
    protected void forceStop() {
        if (running) {
            endGame("§c游戏被强制终止", new ArrayList<>());
        }
    }

    private List<Player> whichGroup(Player p) {
        if (humans.contains(p)) {
            return humans;
        } else if (devils.contains(p)) {
            return devils;
        } else {
            return null;
        }
    }

    private void endGame(String msg, List<Player> winningPlayers) {
        List<Player> winningPlayersCopy = new ArrayList<>(winningPlayers);
        List<Player> playersCopy = new ArrayList<>(players);
        for (Player p : winningPlayersCopy) {
            spawnFireworks(p);
        }
        for (Player p : playersCopy) {
            p.sendTitle(msg, null, 5, 50, 5);
            p.resetPlayerWeather();
            p.resetPlayerTime();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                p.teleport(new Location(world, -1000, 76, 1010));
                Bukkit.getPluginManager().callEvent(new PlayerEndGameEvent(p, this));
            }, 100);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                scoreboard.getTeam("tag3norden").addPlayer(p);
            }, 101);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Entity e : world.getNearbyEntities(new Location(world, -1000, 128, 1000), 200, 200, 200)) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
            clearChests();
            for (Map.Entry<ArmorStand, ArmorStand> entry : armourStandMap.entrySet()) {
                entry.getKey().remove();
                entry.getValue().remove();
            }
            armourStandMap.clear();
            playerMap.clear();
            removeSpectateButton();
            placeStartButton();
            HandlerList.unregisterAll(this);
            tag3.getObjective("tag3").getScore("剩余人数").setScore(0);
            tag3.getObjective("tag3").getScore("剩余时间").setScore(0);
        }, 100);
        players.clear();
        humans.clear();
        devils.clear();
        team.unregister();
        running = false;
        gameUUID = UUID.randomUUID();
        cancelGameTasks();
    }


    private void initializeGameRunnable() {
        gameRunnable = () -> {
            gameTime = TagSnowCastle.gameTime;
            team = tag3.registerNewTeam("tag3");
            team.setNameTagVisibility(NameTagVisibility.NEVER);
            team.setCanSeeFriendlyInvisibles(false);
            team.setAllowFriendlyFire(true);
            for (Player p : getPlayersNearHub(50, 50, 50)) {
                if (scoreboard.getPlayerTeam(p) == null) {
                    continue;
                }
                switch (scoreboard.getPlayerTeam(p).getName()) {
                    case "tag3kelti", "tag3cheshirecat", "tag3mabel", "tag3norden", "tag3redhat", "tag3alice",
                            "tag3dodo", "tag3leaf","tag3bill","tag3eunice" -> {
                        humans.add(p);
                    }
                    case "tag3lindamayer", "tag3miranda" -> {
                        devils.add(p);
                    }
                }
                players.add(p);
                team.addPlayer(p);
            }
            if (players.size() < 2) {
                for (Player p : players) {
                    p.sendMessage("§c至少需要2人才能开始游戏！");
                }
                players.clear();
                humans.clear();
                team.unregister();

            } else if (humans.size() == 0) {
                for (Player p : players) {
                    p.sendMessage("§c至少需要1个人类才能开始游戏！");
                }
                players.clear();
                humans.clear();
                team.unregister();
            } else if (devils.size() == 0) {
                for (Player p : players) {
                    p.sendMessage("§c至少需要1个鬼才能开始游戏！");
                }
                players.clear();
                humans.clear();
                team.unregister();
            } else {
                startTime = getTime(world) + countDownSeconds * 20L + 400;
                running = true;
                removeStartButton();
                startCountdown(countDownSeconds);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    for (Player p : players) {
                        p.setPlayerWeather(WeatherType.DOWNFALL);
                        p.setPlayerTime(18000, false);
                        p.getInventory().clear();
                    }
                });
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Bukkit.getPluginManager().registerEvents(this, plugin);
                    for (Player p : humans) {
                        p.teleport(new Location(world, -1000, 53, 975));
                    }
                    for (Player p : players) {
                        initializePlayer(p);
                        p.setScoreboard(tag3);
                    }

                }, countDownSeconds * 20L);
                for (int i = 0; i < 5; i++) {
                    int finalI = i;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for (Player p : players) {
                            p.sendTitle("§a" + (5 - finalI), null, 2, 16, 2);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
                        }
                    }, countDownSeconds * 20L + 300 + i * 20);
                }


                taskIds.add(
                        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
                            for (Player p : players) {
                                if (coolDown.get(p) == null) {
                                    List<Long> l = new ArrayList<>(2);
                                    l.add(0,0L);
                                    l.add(1,0L);
                                    coolDown.put(p, l);
                                } else {
                                    List<Long> l = coolDown.get(p);
                                    //p.sendMessage(String.valueOf(l.get(0)));
                                    if (l.get(0) > 0) {
                                        l.set(0, l.get(0) - 1);
                                    }
                                    if (l.get(1) != 0) {
                                        p.setLevel((int) Math.ceil(((float)l.get(0)) / 20));
                                        p.setExp(((float)(l.get(1) - l.get(0))) / l.get(1));
                                    }

                                }
                            }
                        }, 1,1 ));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    placeSpectateButton();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "function tag3:go");
                    for (Player p : devils) {
                        p.teleport(new Location(world, -1000, 53, 975));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 4, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0, false, false));
                        ItemStack skull = new ItemStackBuilder(Material.WITHER_SKELETON_SKULL).addEnchantment(Enchantment.BINDING_CURSE, 1).build();
                        ItemStack chestPlate = new ItemStackBuilder(Material.NETHERITE_CHESTPLATE).addEnchantment(Enchantment.BINDING_CURSE, 1).setUnbreakable(true).build();
                        ItemStack leggings = new ItemStackBuilder(Material.NETHERITE_LEGGINGS).addEnchantment(Enchantment.BINDING_CURSE, 1).setUnbreakable(true).build();
                        ItemStack boots = new ItemStackBuilder(Material.NETHERITE_BOOTS).addEnchantment(Enchantment.BINDING_CURSE, 1).setUnbreakable(true).build();
                        p.getInventory().setItem(EquipmentSlot.HEAD, skull);
                        p.getInventory().setItem(EquipmentSlot.CHEST, chestPlate);
                        p.getInventory().setItem(EquipmentSlot.LEGS, leggings);
                        p.getInventory().setItem(EquipmentSlot.FEET, boots);
                    }
                    for (Player p : players) {
                        if (scoreboard.getTeam("tag3leaf").hasPlayer(p)) {
                            p.getInventory().addItem(coal);
                        } else if (scoreboard.getTeam("tag3bill").hasPlayer(p)) {
                            p.getInventory().addItem(mushroom_stew);
                        }
                        p.sendTitle("§e游戏开始！", null, 2, 16, 2);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 2f);
                    }

                }, countDownSeconds * 20L + 400);

                taskIds.add(
                        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                            for (Player p : humans) {
                                if (p.getInventory().contains(Material.ENDER_EYE)) {
                                    for (Player victim : players) {
                                        victim.sendMessage("§7梅贝尔§f在场，所有鬼发光5秒！");
                                        if (devils.contains(victim)) {
                                            victim.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0, false, false));
                                        }
                                    }
                                    return;
                                }

                            }
                        }, countDownSeconds * 20L + 400 + 600, 600));

                taskIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    for (Player p : players) {
                        if (devils.contains(p)) {
                            for (Player victim : players) {
                                if (humans.contains(victim)) {
                                    if (p.getLocation().distance(victim.getLocation()) < 10) {
                                        victim.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 2f, 0f);
                                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            victim.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 2f, 0f);
                                        }, 3);
                                    }
                                    if (p.getLocation().distance(victim.getLocation()) < 5) {
                                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            victim.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 2f, 0f);
                                        }, 10);
                                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            victim.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 2f, 0f);
                                        }, 13);
                                    }

                                }
                            }
                        }
                    }
                }, countDownSeconds * 20L + 400, 20));

                taskIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    for (Player p : players) {
                        p.sendMessage("§a道具已刷新！");
                    }
                    for (Location loc : locations) {
                        double spawnChance = random.nextDouble();
                        if (spawnChance < 0.5) {//overall chance
                            int spawnNo = random.nextInt(totalWeight);
                            int counter = 0;
                            for (int i = 0; i < gadgets.size(); i++) {
                                counter += gadgetWeights.get(i);
                                if (spawnNo < counter) {
                                    ((Chest) (world.getBlockAt(loc).getState())).getBlockInventory().addItem(gadgets.get(i));
                                    break;
                                }
                            }
                        }
                    }
                }, countDownSeconds * 20L + 400 + 600, 1200)); //600 1200


                taskIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    for (Player p : humans) {
                        if (scoreboard.getTeam("tag3bill").hasPlayer(p)) {
                            int counter = 0;
                            for (ItemStack i : p.getInventory().all(Material.MUSHROOM_STEW).values()) {
                                counter += i.getAmount();
                            }
                            if (counter < 2) {
                                p.getInventory().addItem(mushroom_stew);
                                p.sendMessage("§a获得便当！");
                            }
                        }
                    }
                }, countDownSeconds * 20L + 400 + 1200, 1200));

                taskIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    for (Player p : humans) {
                        if (scoreboard.getTeam("tag3leaf").hasPlayer(p)) {
                            int spawnNo = random.nextInt(totalWeight);
                            int counter = 0;
                            for (int i = 0; i < gadgets.size(); i++) {
                                counter += gadgetWeights.get(i);
                                if (spawnNo < counter) {
                                    p.getInventory().addItem(gadgets.get(i));
                                    p.sendMessage("§a获得随机道具！");
                                    break;
                                }
                            }
                        }
                    }
                }, countDownSeconds * 20L + 400 + 900, 900));

                taskIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    long time = getTime(world);
                    if (time - startTime > gameTime) {
                        endGame("§e时间到，人类获胜！", humans);
                        return;
                    }
                    if (humans.size() <= 0) {
                        endGame("§e无人幸存，鬼获胜！", devils);
                        return;
                    }
                    if (devils.size() <= 0) {
                        endGame("§e鬼不复存在，人类获胜！", humans);
                        return;
                    }
                    tag3.getObjective("tag3").getScore("剩余人数").setScore(humans.size());
                    tag3.getObjective("tag3").getScore("剩余时间").setScore((int) ((gameTime - (time - startTime)) / 20));
                }, countDownSeconds * 20L + 400, 1));
            }
        };
    }



    public void clearChests() {
        for (Location l : locations) {
            ((Chest) (world.getBlockAt(l).getState())).getBlockInventory().clear();
        }
    }
}

