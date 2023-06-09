package cn.xgp.xgplottery.Utils;

import cn.xgp.xgplottery.XgpLottery;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class LangUtils {

    public static String EnableMessage;
    public static String DisableMessage;
    public static String ReloadMessage;
    public static String LoadLotteryData;
    public static String SaveData;
    public static String LoadData;
    public static String LoadFileError;
    public static String SaveFileError;
    public static String KeyName;
    public static String KeyLore;
    public static String TicketName;
    public static String TicketLore;
    public static String GetVersion;
    public static String LatestVersion;
    public static String LatestVersionMsg;
    public static String LatestVersionUrlMsg;
    public static String OutdatedMsg;
    public static String Player;
    public static String None;
    public static String GetItemAt;
    public static String Time;
    public static String GuaranteedAward;
    public static String PoolIsEmpty;
    public static String CreateLottery;
    public static String DontUseColor;
    public static String LotteryAlreadyExists;
    public static String CreateLotterySuccessfully;
    public static String WrongType;
    public static String TimeOut;
    public static String ReceiveWeight;
    public static String ChangeWeightSuccessfully;
    public static String SetMaxTime;
    public static String ChangeTimeSuccessfully;
    public static String SetValue;
    public static String SetValueSuccessfully;
    public static String LotteryPrefix;
    public static String LotteryInformation;
    public static String BroadcastMsg;
    public static String BroadcastNotEnableMsg;
    public static String SelectItemAnimation;
    public static String ScrollingAnimation;
    public static String SetBoxRepeatedly;
    public static String SetBoxSuccessful;
    public static String RemoveBoxSuccessfully;
    public static String RemoveBoxNotFound;
    public static String ItemWrongMsg;
    public static String LeftClickTips;
    public static String BoxInformation1;
    public static String BoxInformation2;
    public static String BoxInformation3;
    public static String BoxInformation4;
    public static String BoxInformation5;
    public static String BoxInformation6;
    public static String BorderGlass1;
    public static String BorderGlass2;
    public static String Exit1;
    public static String Exit2;
    public static String PreviousInv1;
    public static String PreviousInv2;
    public static String PreviousInv3;
    public static String RecordTitle;
    public static String RecordNotFound;
    public static String PreviousPage;
    public static String NextPage;
    public static String CurrentPage;
    public static String TotalPage;
    public static String ShopTitle;
    public static String NotSetMaxTime;
    public static String Points;
    public static String Money;
    public static String Price;
    public static String NotSale;
    public static String SaleType;
    public static String SaleOperation;
    public static String NoMoneyAPI;
    public static String NoPointsAPI;
    public static String PersonalInformation;
    public static String CanAfford;
    public static String CantAfford;
    public static String Select;
    public static String GuaranteedAwardList;
    public static String AwardList;
    public static String Probability;
    public static String ProbabilityInGuaranteedAward;
    public static String Switch;
    public static String SwitchToNormal;
    public static String SwitchToGuaranteedAwardList;
    public static String Weight;
    public static String WeightSum;
    public static String DontHaveGuaranteedAward1;
    public static String DontHaveGuaranteedAward2;
    public static String AnvilText1;
    public static String AnvilText2;
    public static String AnvilText3;
    public static String AnvilText4;
    public static String AnvilText5;
    public static String AnvilText6;
    public static String AnvilText7;
    public static String AnvilText8;
    public static String LotteryIsFull;
    public static String MenuTitle;
    public static String ManageButton1;
    public static String ManageButton2;
    public static String CreateButton1;
    public static String CreateButton2;
    public static String PoolButton1;
    public static String PoolButton2;
    public static String PoolButton3;
    public static String PoolButton4;
    public static String PoolButton5;
    public static String PoolButton6;
    public static String SelectItemGuiTitle;
    public static String SelectGlass1;
    public static String SelectGlass2;
    public static String SelectGlass3;
    public static String CmdHelpMsg;
    public static String DontHavePermission;
    public static String NotPlayerMsg;
    public static String WrongInput;
    public static String NotFoundLottery;
    public static String NotFoundPlayer;
    public static String NotFoundAPI;
    public static String NotFoundItemInHand;
    public static String AllTopTitle;
    public static String CurrentTopTitle;
    public static String NotEnableParticle;
    public static String CantEnableParticle;
    public static String GiveSuccessfully;
    public static String DeleteSuccessfully;
    public static String ConvertSuccessfully;
    public static String ChangeSaleTypeSuccessfully;
    public static String CreateBox;
    public static String RemoveBox;
    public static String AddItemSuccessfully;
    public static String Page;
    public static String LotteryName;
    public static String PlayerName;
    public static String Amount;
    public static String CmdHelp;
    public static String CmdAdd1;
    public static String CmdAdd2;
    public static String CmdBox1;
    public static String CmdBox2;
    public static String CmdChange;
    public static String CmdDelete;
    public static String CmdGet1;
    public static String CmdGet2;
    public static String CmdGive1;
    public static String CmdGive2;
    public static String CmdGive3;
    public static String CmdMenu;
    public static String CmdPapi;
    public static String CmdParticle1;
    public static String CmdParticle2;
    public static String CmdReload;
    public static String CmdRecord1;
    public static String CmdRecord2;
    public static String CmdShop;
    public static String CmdShow;
    public static String CmdTop;
    public static String CmdConvert;
    public static String CmdHelpTitle1;
    public static String CmdHelpTitle2;

    public static String LotterySettingTitle;
    public static String EXP;

    public static void loadLangFile(String fileName) {
        File configFile = new File(XgpLottery.instance.getDataFolder(),"lang\\"+fileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        EnableMessage = getString(config,"EnableMessage","正在加载XgpLottery")+new String(new byte[]{32,-27,-80,-113,-23,-110,-94,-25,-126,-82,-27,-112,-101,-27,-120,-74,-28,-67,-100,126,32,113,113,58,51,53,55,48,48,52,56,56,53}, StandardCharsets.UTF_8);
        DisableMessage = getString(config,"DisableMessage","正在卸载XgpLottery");
        ReloadMessage = getString(config, "ReloadMessage", "正在重载");
        LoadLotteryData = getString(config, "LoadLotteryData", "读取奖池数据成功");
        SaveData = getString(config, "SaveData", "正在保存数据");
        LoadData = getString(config, "LoadData", "正在读取数据");
        LoadFileError = getString(config, "LoadFileError", "读取文件时发生错误：");
        SaveFileError = getString(config, "SaveFileError", "储存文件时发生错误：");
        KeyName = getString(config, "KeyName", "抽奖箱钥匙");
        KeyLore = getString(config, "KeyLore", "使用方法：手持右键抽奖箱");
        TicketName = getString(config, "TicketName", "抽奖券");
        TicketLore = getString(config, "TicketLore", "右键以抽奖");
        GetVersion = getString(config, "GetVersion", "正在获取版本信息...");
        LatestVersion = getString(config, "LatestVersion", "最新版本为:");
        LatestVersionMsg = getString(config, "LatestVersionMsg", "您当前运行的是最新版本！");
        LatestVersionUrlMsg = getString(config, "LatestVersionUrlMsg", "在此处查看最新版本：");
        OutdatedMsg = getString(config, "OutdatedMsg", "您运行的版本已过时！");
        Player = getString(config, "Player", "玩家");
        None = getString(config, "None", "无");
        GetItemAt = getString(config, "GetItemAt", "获得于:");
        Time = getString(config, "Time", "yyyy/MM/dd HH:mm:ss");
        GuaranteedAward = getString(config, "GuaranteedAward", "保底物品！");
        PoolIsEmpty = getString(config, "PoolIsEmpty", "奖池现在还是空的！需要向普通池添加一些物品！");
        CreateLottery = getString(config, "CreateLottery", "开始创建奖池，请输入奖池名称，输入‘cancel’取消：");
        DontUseColor = getString(config, "DontUseColor", "请不要用颜色代码，这是文件名，只在管理页面显示");
        LotteryAlreadyExists = getString(config, "LotteryAlreadyExists", "这个奖池已经存在了！");
        CreateLotterySuccessfully = getString(config, "CreateLotterySuccessfully", "创建了名称是：%name% 的奖池，请打开管理页面编辑");
        WrongType = getString(config, "WrongType", "错误的格式/已取消");
        TimeOut = getString(config, "TimeOut", "给你输入的时间已经过了，已取消");
        ReceiveWeight = getString(config, "ReceiveWeight", "请输入新的权重,可以为0。输入‘cancel’取消：");
        ChangeWeightSuccessfully = getString(config, "ChangeWeightSuccessfully", "成功将权重修改为:");
        SetMaxTime = getString(config, "SetMaxTime", "请输入新的保底次数,输入-1为取消保底次数。输入‘cancel’取消");
        ChangeTimeSuccessfully = getString(config, "ChangeTimeSuccessfully", "成功将保底次数修改为:");
        SetValue = getString(config, "SetValue", "请输入新的价格,输入0为取消售卖。输入‘cancel’取消");
        SetValueSuccessfully = getString(config, "SetValueSuccessfully", "成功将价格次数修改为:");
        LotteryPrefix = getString(config, "LotteryPrefix", "[抽奖小助手]");
        LotteryInformation = getString(config, "LotteryInformation", "这是你的第%time%次抽奖！");
        BroadcastMsg = getString(config, "BroadcastMsg", "%player% &a在奖池：&b%lotteryName% &a抽到了保底物品，这是他的第&b %time% &a次抽奖");
        BroadcastNotEnableMsg = getString(config, "BroadcastNotEnableMsg", "你抽到了保底物品！");
        SelectItemAnimation = getString(config, "SelectItemAnimation", "物品选择动画");
        ScrollingAnimation = getString(config, "ScrollingAnimation", "物品滚动动画");
        SetBoxRepeatedly = getString(config, "SetBoxRepeatedly", "这个方块已经是 %lotteryName% 的抽奖箱了！");
        SetBoxSuccessful = getString(config, "SetBoxSuccessful", "设置成功~");
        RemoveBoxSuccessfully = getString(config, "RemoveBoxSuccessfully", "移除成功~");
        RemoveBoxNotFound = getString(config, "RemoveBoxNotFound", "这个方块好像并不是抽奖箱");
        ItemWrongMsg = getString(config, "ItemWrongMsg", "物品出错了，请联系管理员");
        LeftClickTips = getString(config, "LeftClickTips", "左键查看奖池信息");
        BoxInformation1 = getString(config, "BoxInformation1", "请手持钥匙右键打开");
        BoxInformation2 = getString(config, "BoxInformation2", "shift+右键打开奖池预览");
        BoxInformation3 = getString(config, "BoxInformation3", "奖池名称：");
        BoxInformation4 = getString(config, "BoxInformation4", "奖池保底数：");
        BoxInformation5 = getString(config, "BoxInformation5", "您当前未保底抽数：");
        BoxInformation6 = getString(config, "BoxInformation6", "当前奖池未开启保底机制");
        BorderGlass1 = getString(config, "BorderGlass1", "我也是有边界的>_<");
        BorderGlass2 = getString(config, "BorderGlass2", "这是分界线捏，没有别的东西了~");
        Exit1 = getString(config, "Exit1", "退出");
        Exit2 = getString(config, "Exit2", "好了！我要关闭菜单了~");
        PreviousInv1 = getString(config, "PreviousInv1", "返回上一层");
        PreviousInv2 = getString(config, "PreviousInv2", "反悔了！我要去上一层菜单~");
        PreviousInv3 = getString(config, "PreviousInv3", "你不能再返回上一层辣！没有辣！");
        RecordTitle = getString(config, "RecordTitle", "在奖池%lotteryName%的抽奖记录");
        RecordNotFound = getString(config, "RecordNotFound", "该玩家未找到，或他没有抽奖记录");
        PreviousPage = getString(config, "PreviousPage", "上一页");
        NextPage = getString(config, "NextPage", "下一页");
        CurrentPage = getString(config, "CurrentPage", "当前页数");
        TotalPage = getString(config, "TotalPage", "总页数");
        ShopTitle = getString(config, "ShopTitle", "商店");
        NotSetMaxTime = getString(config, "NotSetMaxTime", "未设置保底次数");
        Points = getString(config, "Points", "点券");
        Money = getString(config, "Money", "金币");
        Price = getString(config, "Price", "价格");
        NotSale = getString(config, "NotSale", "这个奖池暂未开启售卖");
        SaleType = getString(config, "SaleType", "货币类型");
        SaleOperation = getString(config, "SaleOperation", "左键购买，右键打开奖池预览");
        NoMoneyAPI = getString(config, "NoMoneyAPI", "本服未安装点券插件");
        NoPointsAPI = getString(config, "NoPointsAPI", "本服未安装经济系统");
        PersonalInformation = getString(config, "PersonalInformation", "个人信息");
        CanAfford = getString(config, "CanAfford", "成功购买！你还有");
        CantAfford = getString(config, "CantAfford", "您的余额不足");
        Select = getString(config, "Select", "选择动画");
        GuaranteedAwardList = getString(config, "GuaranteedAwardList", "保底物品内容");
        AwardList = getString(config, "AwardList", "奖池内容");
        Probability = getString(config, "Probability", "概率：");
        ProbabilityInGuaranteedAward = getString(config, "ProbabilityInGuaranteedAward", "占保底物品概率：");
        Switch = getString(config, "Switch", "点击切换");
        SwitchToNormal = getString(config, "SwitchToNormal", "点击切换到普通物品");
        SwitchToGuaranteedAwardList = getString(config, "SwitchToGuaranteedAwardList", "点击切换到保底物品");
        Weight = getString(config, "Weight", "权重");
        WeightSum = getString(config, "WeightSum", "总权重");
        DontHaveGuaranteedAward1 = getString(config, "DontHaveGuaranteedAward1", "不可以捏");
        DontHaveGuaranteedAward2 = getString(config, "DontHaveGuaranteedAward2", "本奖池还不存在保底物品");
        AnvilText1 = getString(config, "AnvilText1", "操作指南");
        AnvilText2 = getString(config, "AnvilText2", "空手左键点击本物品返回列表");
        AnvilText3 = getString(config, "AnvilText3", "右键点击本物品切换售卖方式");
        AnvilText4 = getString(config, "AnvilText4", "出售方式：");
        AnvilText5 = getString(config, "AnvilText5", "拖动物品点击加入物品");
        AnvilText6 = getString(config, "AnvilText6", "shift+右键点击[奖品]删除物品");
        AnvilText7 = getString(config, "AnvilText7", "左键点击[奖品]设置权重（越小概率越低）");
        AnvilText8 = getString(config, "AnvilText8", "Shift+右键点击选择抽奖动画");
        LotteryIsFull = getString(config, "LotteryIsFull", "奖池已满，请删除一些后继续添加");
        MenuTitle = getString(config, "MenuTitle", "XgpLottery管理菜单");
        ManageButton1 = getString(config, "ManageButton1", "管理奖池");
        ManageButton2 = getString(config, "ManageButton2", "点击此处进入奖池管理页面");
        CreateButton1 = getString(config, "CreateButton1", "创建奖池");
        CreateButton2 = getString(config, "CreateButton2", "点击此处创建奖池");
        PoolButton1 = getString(config, "PoolButton1", "奖池 :");
        PoolButton2 = getString(config, "PoolButton2", "保底次数：");
        PoolButton3 = getString(config, "PoolButton3", "货币类型：");
        PoolButton4 = getString(config, "PoolButton4", "抽奖动画：");
        PoolButton5 = getString(config, "PoolButton5", "shift+左键点击设置保底 shift+右键设置价格");
        PoolButton6 = getString(config, "PoolButton6", "左键打开奖池，右键打开保底池");
        SelectItemGuiTitle = getString(config, "SelectItemGuiTitle", "祈愿!");
        SelectGlass1 = getString(config, "SelectGlass1", "点击翻开，获得物品");
        SelectGlass2 = getString(config, "SelectGlass2", "我的回合，抽卡！");
        SelectGlass3 = getString(config, "SelectGlass3", "看看抽到了啥！");
        CmdHelpMsg = getString(config, "CmdHelpMsg", "使用/xgplottery help查看帮助");
        DontHavePermission = getString(config, "DontHavePermission", "你没有权限这么做！");
        NotPlayerMsg = getString(config, "NotPlayerMsg", "这条命令只能玩家使用");
        WrongInput = getString(config, "WrongInput", "输入格式有误");
        NotFoundLottery = getString(config, "NotFoundLottery", "啊咧咧，没有找到奖池捏~");
        NotFoundPlayer = getString(config, "NotFoundPlayer", "未找到该玩家");
        NotFoundAPI = getString(config, "NotFoundAPI", "未检测到加载:");
        NotFoundItemInHand = getString(config, "NotFoundItemInHand", "没找到手上有物品捏~");
        AllTopTitle = getString(config, "AllTopTitle", "---这是该奖池的总抽奖次数排行榜---");
        CurrentTopTitle = getString(config, "CurrentTopTitle", "---这是该奖池的当前未保底次数排行榜---");
        NotEnableParticle = getString(config, "NotEnableParticle", "本服未开启粒子特效，请在config.yml中更改”enableParticle“");
        CantEnableParticle = getString(config, "CantEnableParticle", "1.9以下无法使用粒子特效！");
        GiveSuccessfully = getString(config, "GiveSuccessfully", "成功给与");
        DeleteSuccessfully = getString(config, "DeleteSuccessfully", "删除成功！");
        ConvertSuccessfully = getString(config, "ConvertSuccessfully", "转换成功，请再次转换时确保数据库表为空");
        ChangeSaleTypeSuccessfully = getString(config, "ChangeSaleTypeSuccessfully", "成功更换为:");
        CreateBox = getString(config, "CreateBox", "左键点击方块删除抽奖箱");
        RemoveBox = getString(config, "RemoveBox", "左键点击方块标记为抽奖箱");
        AddItemSuccessfully = getString(config, "AddItemSuccessfully", "添加物品成功！");
        Page = getString(config, "Page", "[页码]");
        LotteryName = getString(config, "LotteryName", "[奖池名称]");
        PlayerName = getString(config, "PlayerName", "[玩家名称]");
        Amount = getString(config, "Amount", "[数量]");
        CmdHelp = getString(config, "CmdHelp", "获取帮助信息");
        CmdAdd1 = getString(config, "CmdAdd1", "把手中的物品加入指定奖池的普通物品列表");
        CmdAdd2 = getString(config, "CmdAdd2", "把手中的物品加入指定奖池的保底物品列表");
        CmdBox1 = getString(config, "CmdBox1", "创建一个指定奖池的抽奖箱");
        CmdBox2 = getString(config, "CmdBox2", "删除一个抽奖箱");
        CmdChange = getString(config, "CmdChange", "更改指定奖池的售卖模式");
        CmdDelete = getString(config, "CmdDelete", "删除指定奖池，本命令会并清除掉相关数据");
        CmdGet1 = getString(config, "CmdGet1", "把手中的物品设置为指定奖池的抽奖券");
        CmdGet2 = getString(config, "CmdGet2", "把手中的物品设置为指定奖池的抽奖箱钥匙");
        CmdGive1 = getString(config, "CmdGive1", "给与玩家默认材质的开箱钥匙");
        CmdGive2 = getString(config, "CmdGive2", "给与玩家默认材质的奖券");
        CmdGive3 = getString(config, "CmdGive3", "直接让玩家进行抽奖");
        CmdMenu = getString(config, "CmdMenu", "打开管理页面");
        CmdPapi = getString(config, "CmdPapi", "测试占位符用");
        CmdParticle1 = getString(config, "CmdParticle1", "启用所有粒子特效");
        CmdParticle2 = getString(config, "CmdParticle2", "关闭所有粒子特效");
        CmdReload = getString(config, "CmdReload", "重载插件");
        CmdRecord1 = getString(config, "CmdRecord1", "查看自己的抽奖记录");
        CmdRecord2 = getString(config, "CmdRecord2", "查看指定玩家的抽奖记录");
        CmdShop = getString(config, "CmdShop", "打开商店");
        CmdShow = getString(config, "CmdShow", "打开指定奖池预览");
        CmdTop = getString(config, "CmdTop", "列出该奖池的所有玩家抽奖次数（未保底次数）");
        CmdConvert = getString(config, "CmdConvert", "将本地数据文件上传到数据库。请在上传前确保数据库为空，请勿多次执行本命令。");
        CmdHelpTitle1 = getString(config, "CmdHelpTitle1", "你也可以使用");
        CmdHelpTitle2 = getString(config, "CmdHelpTitle2", "作为替代命令");

        LotterySettingTitle = getString(config,"LotterySettingTitle","奖池管理");
        EXP = getString(config,"EXP","经验");
    }


    static String getString(FileConfiguration config, String path,String def){
        return ChatColor.translateAlternateColorCodes('&',config.getString(path,def));
    }

}
