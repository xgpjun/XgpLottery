package cn.xgp.xgplottery.Listener;

import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.XgpLottery;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener implements Listener {

    @EventHandler
    public void onAdminLogin(PlayerJoinEvent e){
        if(!ConfigSetting.msgToAdmin){return;}
        Player player = e.getPlayer();
        if(player.isOp()||player.hasPermission("xgplottery.manager")){
            Bukkit.getScheduler().runTaskLaterAsynchronously(XgpLottery.instance,()->{
                player.sendMessage(ChatColor.DARK_RED + "[XgpLottery] 有新版本可用！ 请去发布贴下载~");
                //友情链接
                sendUrl("https://www.mcbbs.net/thread-1445345-1-1.html",player);
                sendUrl("https://github.com/xgpjun/XgpLottery/releases",player);
                if(!ConfigSetting.msg.equals("1.0.0"))
                    player.sendMessage(ChatColor.BLUE+"[XgpLottery] 新版本信息："+ChatColor.GREEN+ ConfigSetting.msg);
            },60);
        }
    }

    void sendUrl(String url, Player player){

        TextComponent urlMsg = new TextComponent(net.md_5.bungee.api.ChatColor.AQUA+url);
        urlMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(net.md_5.bungee.api.ChatColor.GOLD+"点击访问！")}));
        urlMsg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,url));
        TextComponent message = new TextComponent(net.md_5.bungee.api.ChatColor.BLUE+"下载地址:");
        message.addExtra(urlMsg);
        player.spigot().sendMessage(message);
    }
}
