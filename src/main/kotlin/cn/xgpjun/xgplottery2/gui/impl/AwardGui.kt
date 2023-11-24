package cn.xgpjun.xgplottery2.gui.impl

import cn.xgpjun.xgplottery2.XgpLottery
import cn.xgpjun.xgplottery2.color
import cn.xgpjun.xgplottery2.gui.LotteryGui
import cn.xgpjun.xgplottery2.enums.PresetItem
import cn.xgpjun.xgplottery2.lottery.pojo.Lottery
import cn.xgpjun.xgplottery2.manager.setTag
import cn.xgpjun.xgplottery2.utils.MyItemBuilder
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

class AwardGui(per: InventoryHolder?, val lottery: Lottery) : LotteryGui(per) {
    override val inv = Bukkit.createInventory(this,9*6,"&6奖品信息".color())


    override fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        when(e.rawSlot){
            45 -> createNewAward(player)

        }
    }

    override fun reloadGui() {
        size = 1.coerceAtLeast(lottery.awards.size / 28)
        page = 1.coerceAtLeast(page.coerceAtMost(size))
        pageItem()
        border.forEach { inv.setItem(it, PresetItem.BORDER_GLASS.getItem()) }
        var index = 0

        lottery.awards.forEach { (name,award) ->
            inv.setItem(slot[index+((page-1)*28)],
                MyItemBuilder(award.item)
                    .setDisplayName("&f&l$name".color())
                    .addLore("&6点击以修改".color())
                    .getItem()
                    .setTag("AwardName",name))
            if (index++==28) return@forEach
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
                    return "&6输入奖品名称，cancel取消."
                }
                override fun isInputValid(context: ConversationContext, input: String): Boolean {
                    val c = ChatColor.stripColor(input)?.trim()
                    c?.let {
                        if (c == "cancel"){
                            return true
                        } else if (lottery.awards.containsKey(it)){
                            lottery.createNewAward(c)
                        }else{
                            context.forWhom.sendRawMessage("&c奖品已存在!")
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