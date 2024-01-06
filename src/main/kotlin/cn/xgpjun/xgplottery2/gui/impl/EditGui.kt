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
    override val inv: Inventory = Bukkit.createInventory(this,54,"&6${lottery.name}".color())

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
            8 -> getPreviousInventory(player)
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
        inv.setItem(8,PresetItem.PREVIOUS_INVENTORY.getItem())
        val lore = MessageL.LotteryInfoLore.get(AnimManager.getSingleI18Name(lottery.animation),AnimManager.getMultipleI18Name(lottery.multipleAnimation)).color()
        val lotteryMessage = MyItemBuilder(PresetItem.SIGN.getItem())
            .setDisplayName(Message.LotteryInfoName.get(lottery.name).color())
            .setLore(lore)
        inv.setItem(4,lotteryMessage.getItem())
        inv.setItem(20, MyItemBuilder(Material.PAINTING)
            .setDisplayName(Message.ChangeAnimationName.get().color())
            .getItem())
        inv.setItem(29, MyItemBuilder(Material.ITEM_FRAME)
            .setDisplayName(Message.ChangeMultipleAnimationName.get().color())
            .getItem())
        inv.setItem(22, MyItemBuilder(Material.GOLD_NUGGET)
            .setDisplayName(Message.UnusedName.get().color())
            .getItem())
        val sellType = when(lottery.sellType){
            SellType.POINTS ->  Material.DIAMOND
            SellType.MONEY -> Material.GOLD_INGOT
            SellType.EXP -> if (NMSManager.versionToInt>12) Material.EXPERIENCE_BOTTLE else Material.valueOf("EXP_BOTTLE")
            SellType.NULL -> Material.PAPER
        }
        inv.setItem(31, MyItemBuilder(sellType)
            .setDisplayName(Message.UnusedName.get().color())
            .getItem())
        inv.setItem(24, MyItemBuilder(lottery.key.type)
            .setDisplayName(Message.GetKeyName.get().color())
            .getItem())
        inv.setItem(33, MyItemBuilder(Material.ENDER_CHEST)
            .setDisplayName(Message.EditAwardName.get().color())
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
            information.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 edit ${lottery.name} singleAnim ${it.newInstance().name}")
            player.spigot().sendMessage(information)
        }
        TextComponent(Message.Cancel.get().color()).run {
            this.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 edit")
        }
    }
    fun multipleAnimChange(player: Player){
        player.closeInventory()
        preInventory[player.uniqueId] = this

        AnimManager.multipleAnim.values.forEach{
            val information = TextComponent(Message.MultipleAnimChange.get(it.getConstructor().newInstance().i18nName).color())
            information.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 edit ${lottery.name} multipleAnim ${it.newInstance().name}")
            player.spigot().sendMessage(information)
        }
        TextComponent(Message.Cancel.get().color()).run {
            this.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 edit")
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
            message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 edit ${lottery.name} sellType ${it.name}")
            player.spigot().sendMessage(message)
        }
        TextComponent(Message.Cancel.get().color()).run {
            this.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 edit ${lottery.name}")
        }
    }
    fun getKeyItem(player: Player){
        player.inventory.addItem(lottery.key.clone().setTag("XL2KEY", lottery.virtualKeyName))
    }
    fun setAwards(player: Player){
        player.openInventory(AwardGui(this,lottery).inventory)
    }
}

