package io.spm.parknshop.payment.config;

public final class AlipayConfig {

  // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
  public static String APP_ID = "";

  // 商户私钥，您的PKCS8格式RSA2私钥
  public static String MERCHANT_PRIVATE_KEY = "";

  // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
  public static String ALIPAY_PUBLIC_KEY = "";

  // 签名方式
  public static String SIGN_TYPE = "RSA2";

  // 字符编码格式
  public static String CHARSET = "utf-8";

  // 支付宝网关（正式环境）
  public static String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

  // BUY 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
  public static String BUY_PAY_NOTIFY_URL = "http://localhost:8080/pay/buy/notify_callback";
  // BUY 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
  public static String BUY_PAY_RETURN_URL = "http://localhost:8080/pay/buy/notify_callback";

  public static String AD_PAY_NOTIFY_URL = "http://localhost:8080/pay/ad/notify_callback";
  public static String AD_PAY_RETURN_URL = "http://localhost:8080/pay/ad/notify_callback";
}
