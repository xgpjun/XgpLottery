package cn.xgp.xgplottery.Lottery;

import cn.xgp.xgplottery.Utils.LangUtils;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SellType {
    @SerializedName("POINTS")
    POINTS(LangUtils.Points),
    @SerializedName("MONEY")
    MONEY(LangUtils.Money),
    @SerializedName("EXP")
    EXP(LangUtils.EXP);

    private final String sellType;

    public static SellType fromString(String value) {
        for (SellType type : SellType.values()) {
            if (type.sellType.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SellType: " + value);
    }

}
