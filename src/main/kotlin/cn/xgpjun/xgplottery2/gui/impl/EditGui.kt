package cn.xgpjun.xgplottery2.gui.impl

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.gui.LotteryGui
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.lottery.enums.SellType
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.*
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.conversations.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.UUID


class EditGui(per: InventoryHolder?, val lottery: Lottery) : LotteryGui(per) {
    companion object {
        val preInventory = HashMap<UUID, EditGui>()
    }
    override val inv: Inventory = Bukkit.createInventory(this,54,"&6奖池信息".color())

    override fun handleClick(e: InventoryClickEvent) {
        val slot =e.rawSlot
        val player = e.whoClicked as Player
        when(slot){
            20 -> singleAnimChange(player)
            22 -> valueChange(player)
            29 -> multipleAnimChange(player)
            24 -> getKeyItem(player)
            31 -> sellTypeChange(player)
            33 -> setAwards(player)
        }
    }

    override fun reloadGui() {
        val borderGlass = MyItemBuilder(PresetItem.GRAY_STAINED_GLASS_PANE.getItem())
            .setDisplayName(Message.ItemBorderName.get().color())
            .setLore(MessageL.ItemBorderLore.get().color())
        border.forEach { inv.setItem(it,borderGlass.getItem()) }
        PresetItem.PINK_STAINED_GLASS_PANE.getItem().type.let { borderGlass.setMaterial(it) }
        for (i in arrayListOf(
            10, 11, 12, 13, 14, 15, 16,
            19, 21, 23, 25,
            28, 30, 32, 34,
            37, 38, 39, 40, 41, 42, 43
        )) {
            inv.setItem(i,borderGlass.getItem())
        }
        val lore = arrayListOf(
            "&a此处展示了奖池的基本信息，奖品在子界面",
            "&a如需更改请点击下方gui物品",
            "&a单抽动画:&b${AnimManager.getSingleI18Name(lottery.animation)}",
            "&a十连抽动画:&b${AnimManager.getMultipleI18Name(lottery.multipleAnimation)}",
            "&a售卖价格:&b${lottery.value}&a${lottery.sellType.getName()}")
        val lotteryMessage = MyItemBuilder(PresetItem.SIGN.getItem())
            .setDisplayName("&f&l${lottery.name}奖池信息".color())
            .setLore(lore.color())
        inv.setItem(4,lotteryMessage.getItem())
        inv.setItem(20, MyItemBuilder(Material.PAINTING)
            .setDisplayName("&f&l更改单抽动画".color())
            .getItem())
        inv.setItem(29, MyItemBuilder(Material.ITEM_FRAME)
            .setDisplayName("&f&l更改十连抽动画".color())
            .getItem())
        inv.setItem(22, MyItemBuilder(Material.GOLD_NUGGET)
            .setDisplayName("&f&l更改售价".color())
            .getItem())
        val sellType = when(lottery.sellType){
            SellType.POINTS ->  Material.DIAMOND
            SellType.MONEY -> Material.GOLD_INGOT
            SellType.EXP -> if (NMSManager.versionToInt>12) Material.EXPERIENCE_BOTTLE else Material.valueOf("EXP_BOTTLE")
            SellType.NULL -> Material.PAPER
        }
        inv.setItem(31, MyItemBuilder(sellType)
            .setDisplayName("&f&l更改售卖方式".color())
            .getItem())
        inv.setItem(24, MyItemBuilder(lottery.key.type)
            .setDisplayName("&f&l获得一个钥匙".color())
            .getItem())
        inv.setItem(33, MyItemBuilder(Material.ENDER_CHEST)
            .setDisplayName("&f&l查看并设置奖品".color())
            .getItem())
    }

    override fun getInventory(): Inventory {
        reloadGui()
        return inv
    }

    fun singleAnimChange(player: Player){
        player.closeInventory()
        preInventory[player.uniqueId] = this
        AnimManager.singleAnim.values.forEach{
            val information = TextComponent(Message.SingleAnimChange.get(it.new().i18nName).color())
            information.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xl edit ${lottery.name} singleAnim ${it.name}")
            player.spigot().sendMessage(information)
        }
        TextComponent(Message.Cancel.get().color()).run {
            this.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xl edit")
        }
    }
    fun multipleAnimChange(player: Player){
        player.closeInventory()
        preInventory[player.uniqueId] = this

        AnimManager.multipleAnim.values.forEach{
            val information = TextComponent(Message.MultipleAnimChange.get(it.getConstructor().newInstance().i18nName))
            information.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xl edit ${lottery.name} multipleAnim ${it.name}")
            player.spigot().sendMessage(information)
        }
        TextComponent(Message.Cancel.get().color()).run {
            this.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xl edit")
        }
    }

    fun valueChange(player: Player){
        player.closeInventory()
        val conversation = ConversationFactory(XgpLottery.instance)
            .withFirstPrompt(object: ValidatingPrompt() {
                override fun getPromptText(context: ConversationContext): String {
                    return Message.ValueChange.get().color()
                }

                override fun isInputValid(context: ConversationContext, input: String): Boolean {
                    if (input.toDoubleOrNull()!=null){
                        val d = input.toDouble()
                        lottery.value = d
                        return true
                    }else if(input == "cancel"){
                        return true
                    }
                    return false
                }

                override fun acceptValidatedInput(context: ConversationContext, input: String): Prompt? {
                    player.openInventory(this@EditGui.inventory)
                    return END_OF_CONVERSATION
                }
            })
            .buildConversation(player)
        conversation.begin()
    }
    fun sellTypeChange(player: Player){
        player.closeInventory()
        preInventory[player.uniqueId] = this

        arrayListOf(SellType.POINTS,SellType.MONEY,SellType.EXP).forEach{
            val message = TextComponent(Message.SellTypeChange.get(it.getName()).color())
            message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xl edit ${lottery.name} sellType ${it.name}")
            player.spigot().sendMessage(message)
        }
        TextComponent(Message.Cancel.get().color()).run {
            this.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xl edit ${lottery.name}")
        }
    }
    fun getKeyItem(player: Player){
        player.inventory.addItem(lottery.key.clone())
    }
    fun setAwards(player: Player){
        player.openInventory(AwardGui(this,lottery).inventory)
    }
}

