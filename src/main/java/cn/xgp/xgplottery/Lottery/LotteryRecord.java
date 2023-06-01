package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.ConfigSetting;
import cn.xgp.xgplottery.Utils.SerializeUtils;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class LotteryRecord {
    private List<ItemStack> record;
    private UUID uuid;
    private String lotteryName;

    public LotteryRecord(UUID uuid,String lotteryName){
        this.uuid = uuid;
        this.lotteryName = lotteryName;
        this.record = SerializeUtils.loadLotteryRecord(uuid,lotteryName);
        if(record == null){
            record = new ArrayList<>();
        }
    }

    public static void addRecord(ItemStack itemStack,UUID uuid,boolean special,String lotteryName){
        LotteryRecord record = new LotteryRecord(uuid,lotteryName);
        int recordAmount = ConfigSetting.recordAmount;
        Player player = Bukkit.getPlayer(uuid);

        if( player!=null&&player.hasPermission("xgplottery.record.*")){
            String permission = player.getEffectivePermissions().stream()
                    .filter(p -> p.getPermission().startsWith("xgplottery.record."))
                    .findFirst()
                    .map(PermissionAttachmentInfo::getPermission)
                    .orElse("");
            String regex = "xgplottery\\.record\\.(\\d+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(permission);
            if (matcher.find()) {
                recordAmount = Integer.parseInt(matcher.group(1));
            }
        }

        record.getRecord().add(new MyItem(itemStack).addRecordInfo(special));
        record.deleteExcessiveRecord(recordAmount);

        SerializeUtils.saveLotteryRecord(record);
    }

    void deleteExcessiveRecord(int maxAmount){
        if(record.size()<=maxAmount)
            return;
        int deleteAmount = record.size() - maxAmount ;
        if (deleteAmount > 0) {
            record.subList(0, deleteAmount).clear();
        }
    }

}
