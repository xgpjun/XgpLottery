
# XgpLottery2

> **2.1.0 更新提示：**初步兼容 Minecraft 1.20.5+，要求依赖 [NBTAPI](https://github.com/tr7zw/Item-NBT-API)。
> 旧版本更新暂未测试，也许会导致包括数据丢失等未知的错误。
> 暂时能用就不要更新。

## 运行依赖与物品数据

服务器需要安装 [NBTAPI 2.16.0 或更新版本](https://modrinth.com/plugin/nbtapi/versions)，
并选择支持当前 Minecraft 版本的 NBTAPI。NBTAPI 是必需前置，不会打包进本插件的 JAR。
插件字节码兼容 Java 8，保留 Minecraft 1.7.10 和 1.12.2 的旧版适配。
Minecraft 1.20.5/1.21 服务器本身仍需要 Java 21；其他版本请遵循服务端的 Java 要求。
ProtocolLib、PlaceholderAPI 等可选前置需要选择兼容当前服务端及 Java 版本的发行版，
Gradle 中的 `compileOnly` 版本仅用于编译，不会打包进插件。

物品标记（如 `XL2KEY`）通过 NBTAPI 读写，在 1.20.5+ 保存到 `minecraft:custom_data`。
`key.internal` 和 `awards.*.item.internal` 继续使用完整物品的 SNBT 字符串，
包含组件和 `DataVersion`，同时用于累计奖励和旧版插件导入。
NBTAPI 会在读取时升级旧版本数据；没有 `DataVersion` 的旧 `Count/tag` 格式按 1.20.4 转换。
升级前请备份 `plugins/XgpLottery2`，并检查旧物品的名称、Lore、附魔和自定义标签。
现有配置中的 `nms` 选项已停用，无需指定 NMS 包装器。

使用 JDK 17 构建：`./gradlew build`（Windows：`gradlew.bat build`）。
Kotlin 的 `jvmTarget` 和 Java 编译目标均为 1.8，并限制使用 Java 8 的 JDK API。
JDK 17 用于读取较新的编译期服务端 API，不会提高插件的 Java 运行时要求。
部署 `build/libs/XgpLottery-2.1.0.jar`，不要使用 `-plain.jar`。
部署后应确认钥匙右键兑换、GUI 点击、奖品保存及重载均正常。

![crate1.gif](https://s2.loli.net/2024/01/07/sOIm3Yzj65ntUWA.gif)


> 🔮 [插件交流群: 851355982](https://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=SY19EvEiFZvqx_mwGSvYn5N-NkgoiqBd&authKey=ch4cQrooW0WGSG9IlomgJH43qyhE8W8alImhOM%2FUbhcTSTb7f1nH%2Fq5k7KlhRhX8&noverify=0&group_code=851355982)
>
> 👆 加群闲聊 !

> 🎮 [MCBBS发布贴](https://www.mcbbs.net/thread-1445345-1-1.html)
>
> 👆 评分给作者更新的动力！



> 💸富佬随意：
>
> [![](https://s2.loli.net/2023/07/12/ZEBA3zusOvFNWLj.png?padding=true?align=center)](https://afdian.net/a/xgp666)


教程文档: https://xgpjun.github.io/XL-wiki/intro

![](https://bstats.org/signatures/bukkit/xgplottery.svg#center)
