package io.spm.parknshop.payment.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.google.gson.JsonObject;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.payment.config.AlipayConfig;
import io.spm.parknshop.payment.domain.PaymentRefundResult;
import io.spm.parknshop.payment.domain.PaymentTransferResult;
import io.spm.parknshop.trade.domain.PaymentResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class AlipayService {

  private final AlipayClient alipayClient;

  public AlipayService() {
    this.alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAY_URL, AlipayConfig.APP_ID,
      AlipayConfig.MERCHANT_PRIVATE_KEY, "json", AlipayConfig.CHARSET,
      AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
  }

  public Mono<String> invokeBuyPayment(Long paymentId, String payOrderName, double totalAmount) {
    //设置请求参数
    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
    alipayRequest.setReturnUrl(AlipayConfig.BUY_PAY_RETURN_URL);
    alipayRequest.setNotifyUrl(AlipayConfig.BUY_PAY_NOTIFY_URL);

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

  public Mono<String> invokeAdPayment(Long paymentId, double totalAmount) {
    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
    alipayRequest.setReturnUrl(AlipayConfig.AD_PAY_RETURN_URL);
    alipayRequest.setNotifyUrl(AlipayConfig.AD_PAY_NOTIFY_URL);

    String out_trade_no = paymentId.toString();
    String total_amount = String.valueOf(totalAmount);
    String subject = "PARKnSHOP.com";
    String body = "Advertisement payment from PARKnSHOP.com";

    alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
      + "\"total_amount\":\""+ total_amount +"\","
      + "\"subject\":\""+ subject +"\","
      + "\"body\":\""+ body +"\","
      + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

    //请求
    return async(() -> alipayClient.pageExecute(alipayRequest).getBody());
  }

  public Mono<PaymentResult> cancelPayment(Long paymentId, String alipayTradeNo, String operator) {
    AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
    String outTradeNo = paymentId.toString();
    request.setBizContent("{" +
      //"\"trade_no\":\"" + alipayTradeNo + "\"," +
      "\"out_trade_no\":\"" + outTradeNo + "\"," +
      "\"operator_id\":\"" + operator + "\"" +
      "  }");
    //请求
    return async(() -> alipayClient.execute(request))
      .flatMap(response -> {
        if (response.isSuccess()) {
          return Mono.just(new PaymentResult(ALIPAY_SUCCESS_CODE, response.getMsg()));
        } else {
          return Mono.error(new ServiceException(PAYMENT_CANCEL_PAY_FAIL, "Cancel pay fail: " + response.getMsg()));
        }
      });
  }

  public Mono<PaymentRefundResult> processRefund(String originalPaymentId, String alipayTradeNo, double refundAmount, String refundReason,
                                                 String outRefundRequestNo, String storeId) {
    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

    JsonObject bizObj = new JsonObject();
    bizObj.addProperty("out_trade_no", originalPaymentId);
    bizObj.addProperty("trade_no", alipayTradeNo);
    bizObj.addProperty("refund_amount", refundAmount);
    bizObj.addProperty("refund_reason", refundReason);
    bizObj.addProperty("out_request_no", outRefundRequestNo);
    bizObj.addProperty("store_id", storeId);
    request.setBizContent(bizObj.toString());
    //请求
    return async(() -> alipayClient.execute(request))
      .flatMap(response -> {
        if (response.isSuccess()) {
          return Mono.just(new PaymentRefundResult(response.getTradeNo(), response.getOutTradeNo(),
            outRefundRequestNo, Double.valueOf(response.getRefundFee())).setChanged(response.getFundChange().equals("Y")));
        } else {
          return Mono.error(new ServiceException(PAYMENT_REFUND_FAIL, "Refund fail: " + response.getMsg()));
        }
      });
  }

  public Mono<PaymentTransferResult> processTransfer(String transferBizNo, String alipayAccount, double amount) {
    AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

    JsonObject bizObj = new JsonObject();
    bizObj.addProperty("out_biz_no", transferBizNo);
    bizObj.addProperty("payee_type", "ALIPAY_LOGONID");
    bizObj.addProperty("payee_account", alipayAccount);
    bizObj.addProperty("amount", String.valueOf(amount));
    bizObj.addProperty("payer_show_name", "PARKnSHOP.com");
    bizObj.addProperty("remark", String.format("HK$%.2f sales money from PARKnSHOP.com", amount));
    request.setBizContent(bizObj.toString());
    //请求
    return async(() -> alipayClient.execute(request))
      .flatMap(response -> {
        if (response.isSuccess()) {
          return Mono.just(new PaymentTransferResult().setTransferTransactionId(response.getOrderId()));
        } else {
          return Mono.error(new ServiceException(PAYMENT_TRANSFER_TRANSACTION_FAIL, "Transfer tranaction fail: " + response.getMsg()));
        }
      });
  }

  private static final int ALIPAY_SUCCESS_CODE = 10000;

}
