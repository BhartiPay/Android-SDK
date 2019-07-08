package com.tripmakerz.demo;/*
 * Created by  Ajeet Maurya on 7/2/18 2:54 PM.
 * Copyright  ©  2018 , Appnit Technologies Pvt. Ltd.  and/or its affiliates. All rights reserved.
 *
 *  Last Modified 7/2/18 2:54 PM.
 */



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.bhartipaysdk.sdk.PayConstant;
import com.bhartipaysdk.sdk.PaySdkInt;
import com.bhartipaysdk.util.MerchantParams;
import com.bhartipaysdk.util.PayReqParams;
import com.bhartipaysdk.util.PayResParams;
import com.tripmakerz.demo.R;


public class MainActivity extends AppCompatActivity {

    private TextView request;
    private CheckBox checkLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText amount = (EditText)findViewById(R.id.ed_amount);
        checkLive = (CheckBox)findViewById(R.id.checkBox);
        /* request = (TextView) findViewById(R.id.tv_request);*/
        Button pay = (Button)findViewById(R.id.button2);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"Enter vailid amount",Toast.LENGTH_LONG).show();
                    return;
                }

                getPay(amount.getText().toString().trim());
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // getString();
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPay(String amount) {
        try {
            MerchantParams merchantParams;
            merchantParams = new MerchantParams();

            merchantParams.setMerAmount((Integer.valueOf(amount)*100)+"");
            merchantParams.setMerCurrencyCode("356");
            merchantParams.setMerCustAddress("Gurgaon");
            merchantParams.setMerCustEmail("neeeeeraj.kumar@bhartipay.com");

            merchantParams.setMerCustPhone("9999999999");
            merchantParams.setMerCustZip("122016");
            if(checkLive.isChecked()) {
                merchantParams.setMerName("BHARTIPAY Live");
                merchantParams.setMerCustName("BHARTIPAY LIVE");
                merchantParams.setMerPayId("1804071319261021");//1804071319261021//test 1806050627121026
                merchantParams.setMerKey("6275179d6d2a4ad5");//6275179d6d2a4ad5//test 4964b2d80d114aed
            }else{
                merchantParams.setMerName("BHARTIPAY Uat");
                merchantParams.setMerCustName("BHARTIPAY UAT");
                merchantParams.setMerPayId("1806050627121026");//1804071319261021//test 1806050627121026
                merchantParams.setMerKey("4964b2d80d114aed");//6275179d6d2a4ad5//test 4964b2d80d114aed
            }
            merchantParams.setMerOrderId(System.currentTimeMillis() + "");

            merchantParams.setMerProductDesc("BHARTIPAY Demo Transaction");
            merchantParams.setMerTxnType("SALE");
            merchantParams.setActionBarTitle("BhartiPayPG");
            merchantParams.setProductionEnv(checkLive.isChecked());
            merchantParams.setActionBarColor(R.color.colorAccent);
            PayReqParams params = new PayReqParams();
            params.setMerchantParams(merchantParams);
            System.out.print("#######"+params);
            PaySdkInt.getInstance(MainActivity.this, params);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== PayConstant.PG_REQ_CODE && resultCode ==RESULT_OK) {
            PayResParams resParams = null;
            if (data != null && data.getExtras() != null && data.getParcelableExtra(PayConstant.PG_RESULT) != null) {
                resParams = (PayResParams)data.getParcelableExtra(PayConstant.PG_RESULT);
                if (resParams != null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage(			  "Status     :>"+resParams.getSTATUS()
                            +"\n"+"TxnId      :>"+resParams.getTXN_ID()
                            +"\n"+"Date :>"+resParams.getRESPONSE_DATE_TIME()
                            +"\n"+"Order ID  :>"+resParams.getORDER_ID()
                            +"\n"+"ResponseMsg    :>"+resParams.getRESPONSE_MESSAGE()
                            +"\n"+"ResponseCode:>"+resParams.getRESPONSE_CODE()
                            +"\n"+"Amount (in paisa):>"+resParams.getAMOUNT());
                    dialog.show();
                    System.out.println(requestCode + ":>>>>>>>>>>>>>.." + resultCode + ":>>>>>>>>>>>>.." + resParams.getRESPONSE_CODE());
                }
            }
        }else if(requestCode== PayConstant.PG_REQ_CODE && resultCode ==RESULT_CANCELED){
            PayResParams resParams = null;
            if (data != null && data.getExtras() != null ){
                int cancelCode = data.getIntExtra(PayConstant.PG_CANCEL_CODE,0);
                resParams = (PayResParams)data.getParcelableExtra(PayConstant.PG_RESULT);
                if(cancelCode==01 && resParams!=null){//transaction failed from bank

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("ResponseMsg    :>"+data.getStringExtra(PayConstant.PG_MESSAGE)
                            +"\n"+"ResponseCode:>"+resParams.getRESPONSE_CODE()
                            +"\n"+"Amount (in paisa) :>"+resParams.getAMOUNT());
                    dialog.show();
                }else if(cancelCode==02){ //transaction cancel by user
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("ResponseMsg    :>"+data.getStringExtra(PayConstant.PG_MESSAGE));

                    dialog.show();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
