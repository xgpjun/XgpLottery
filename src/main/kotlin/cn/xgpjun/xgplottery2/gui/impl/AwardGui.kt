package cn.xgpjun.xgplottery2.gui.impl

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.gui.LotteryGui
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.Message
import cn.xgpjun.xgplottery2.manager.MessageL
import cn.xgpjun.xgplottery2.manager.getTag
import cn.xgpjun.xgplottery2.manager.setTag
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.ValidatingPrompt
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.ceil
import kotlin.math.min

class AwardGui(per: InventoryHolder?, val lottery: Lottery) : LotteryGui(per) {
    override val inv = Bukkit.createInventory(this,9*6,Message.AwardGuiTitle.get().color())

    companion object {
        val preInventory = HashMap<UUID, AwardGui>()
    }

    override fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        when(e.rawSlot){
            49 -> createNewAward(player)
            8 ->{
                getPreviousInventory(player)
            }
        }
        e.currentItem?.getTag("AwardName")?.let { name->
            lottery.awards[name]?.let { award ->
                try {
                    val message = TextComponent()
                    message.addExtra(Message.Prefix.get().color())
                    val text = TextComponent(Message.ChangeAwardItem.get().color())
                    text.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xgplottery2 edit ${lottery.name} award ${award.name}")
                    message.addExtra(text)
                    player.spigot().sendMessage(message)
                }catch (_:Throwable){
                    player.sendMessage("&cThe server version is too low, please hold the item and manually enter /xgplottery2 edit ${lottery.name} award ${award.name}".color())
                }
                player.closeInventory()
                preInventory[player.uniqueId] = this
            }
        }
    }

    override fun reloadGui() {
        inv.clear()
        size = ceil(lottery.awards.size.toDouble() / 28).toInt().coerceAtLeast(1)
        page = 1.coerceAtLeast(page.coerceAtMost(size))
        pageItem()

        inv.setItem(49,MyItemBuilder(
            PresetItem.ORANGE_STAINED_GLASS_PANE.getItem())
            .setDisplayName(Message.ItemBorder2Name.get().color())
            .setLore(MessageL.ItemBorder2Lore.get().color())
            .getItem())

        lottery.awards
            .toList()
            .slice((page - 1) * 28 until min(page*28,lottery.awards.size))
            .forEachIndexed { index,(name,award) ->
            inv.setItem(slot[index],
                MyItemBuilder(award.item)
                    .setDisplayName("&f&l$name".color())
                    .addLore(Message.ClickToChange.get().color())
                    .getItem()
                    .setTag("AwardName",name))
        }
    }

    override fun getInventory(): Inventory {
        reloadGui()

        return inv
    }
    fun createNewAward(player: Player){
        player.closeInventory()
        val conversation = ConversationFactory(XgpLottery.instance)
            .withFirstPrompt(object: ValidatingPrompt() {
                override fun getPromptText(context: ConversationContext): String {
                    return Message.NewAward.get().color()
                }
                override fun isInputValid(context: ConversationContext, input: String): Boolean {
                    val c = ChatColor.stripColor(input)?.trim()
                    c?.let {
                        if (c == "cancel"){
                            return true
                        } else if (!lottery.awards.containsKey(c)){
                            lottery.createNewAward(c)
                        }else{
                            context.forWhom.sendRawMessage(Message.Existed.get().color())
                        }
                    }
                    return true
                }

                override fun acceptValidatedInput(context: ConversationContext, input: String): Prompt? {
                    player.openInventory(this@AwardGui.inventory)
                    return END_OF_CONVERSATION
                }
            })
            .buildConversation(player)
        conversation.begin()
    }
}