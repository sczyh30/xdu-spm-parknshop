package io.spm.parknshop.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Eric Zhao
 */
public final class JsonUtils {

  private static final Gson gson = new GsonBuilder().create();

  public static String toJson(Object t) {
    return gson.toJson(t);
  }

  public static <T> T parse(String json, Class<T> clazz) {
    return gson.fromJson(json, clazz);
  }

  private JsonUtils() {}
}
