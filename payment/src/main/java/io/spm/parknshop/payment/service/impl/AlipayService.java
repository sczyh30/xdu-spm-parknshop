package io.spm.parknshop.payment.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import io.spm.parknshop.payment.config.AlipayConfig;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;

@Service
public class AlipayService {

  private final AlipayClient alipayClient;

  public AlipayService() {
    this.alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
      AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
      AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
  }

  public Mono<String> invokePayment(Long paymentId, String payOrderName, double totalAmount) {
    AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

    //设置请求参数
    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
    alipayRequest.setReturnUrl(AlipayConfig.return_url);
    alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

    //商户订单号，商户网站订单系统中唯一订单号，必填
    String out_trade_no = paymentId.toString();
    //付款金额，必填
    String total_amount = String.valueOf(totalAmount);
    //订单名称，必填
    String subject = payOrderName;
    //商品描述，可空
    String body = "Products from PARKnSHOP.com";

    alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
      + "\"total_amount\":\""+ total_amount +"\","
      + "\"subject\":\""+ subject +"\","
      + "\"body\":\""+ body +"\","
      + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

    //请求
    return async(() -> alipayClient.pageExecute(alipayRequest).getBody());
  }

}
