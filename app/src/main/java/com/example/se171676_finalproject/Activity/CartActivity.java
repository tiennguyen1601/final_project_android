package com.example.se171676_finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.se171676_finalproject.Adapter.CartAdapter;
import com.example.se171676_finalproject.Api.CreateOrder;
import com.example.se171676_finalproject.Helper.ChangeNumberItemsListener;
import com.example.se171676_finalproject.Helper.ManagmentCart;
import com.example.se171676_finalproject.R;
import com.example.se171676_finalproject.databinding.ActivityCartBinding;
import org.json.JSONObject;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends BaseActivity {

    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        calculatorCart();
        setVarialbe();
        initCartList();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        // Set up payment button
        binding.checkOutBtn.setOnClickListener(v -> initiatePayment());
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        binding.cartView.setAdapter(new CartAdapter(
                new ChangeNumberItemsListener() {
                    @Override
                    public void changed() {
                        calculatorCart();
                    }
                },
                this,
                managmentCart.getListCart()
        ));
    }

    private void setVarialbe() {
        binding.backBtn.setOnClickListener(view -> finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;

        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;
        totalAmount = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100.0) / 100.0;

        binding.totalFeeTxt.setText(itemTotal + " VND");
        binding.deliveryTxt.setText(delivery + " VND");
        binding.taxTxt.setText(tax + " VND");
        binding.totalTxt.setText(totalAmount + " VND");
    }

    private void initiatePayment() {
        CreateOrder orderApi = new CreateOrder();
        String totalString = String.format("%.0f", totalAmount);

        try {
            JSONObject data = orderApi.createOrder(totalString);
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                        intent.putExtra("result", "Thanh toán thành công");
                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                        intent.putExtra("result", "Hủy thanh toán");
                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                        intent.putExtra("result", "Lỗi thanh toán");
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
