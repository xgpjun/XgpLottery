package cn.xgpjun.xgplottery2.enums

import cn.xgpjun.xgplottery2.manager.NMSManager
import org.bukkit.Sound

enum class Sounds {
    PLING{
        override fun get(): Sound {
            return if (NMSManager.versionToInt<9){
                Sound.valueOf("NOTE_PLING")
            }else if (NMSManager.versionToInt<13){
                Sound.valueOf("BLOCK_NOTE_PLING")
            }else{
                Sound.valueOf("BLOCK_NOTE_BLOCK_HARP")
            }
        }
    },
    LEVEL_UP {
        override fun get(): Sound {
            return if (NMSManager.versionToInt<9){
                Sound.valueOf("LEVEL_UP")
            }else{
                Sound.valueOf("ENTITY_PLAYER_LEVELUP")
            }
        }
    };

    abstract fun get():Sound
}