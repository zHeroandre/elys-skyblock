package me.zheroandre.elysskyblock.utils.gson;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class GsonUtils {

    public static String serialize(Object object) {
        return new Gson().toJson(object);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> T deserialize(String string, TypeToken<T> typeToken) {
        return new Gson().fromJson(string, typeToken.getType());
    }

}