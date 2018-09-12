package com.example.vinamra.anganwadi_supervisor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Vinamra on 4/5/2018.
 */

public class StockBillViewActivity extends AppCompatActivity {
    private TextView viewbillno,viewbilldate,viewbilltime,deliverypnameTxtView,helpernameTxtView;
    private DismissibleImageView billPicDismissableView,billItemsDismissableView1,billItemsDismissableView2,deliverypsignDismissableView,helpersignDismissableView;
    private String billno,billdate,helpername,billtime,billpicurl,billitems1picurl,billitems2picurl,helpersignatureurl,deliverypname,deliverypsign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockbillview);
        intialize();
    }

    private void intialize() {
        viewbillno=(TextView)findViewById(R.id.viewbillno);
        viewbilldate=(TextView)findViewById(R.id.viewbilldate);
        viewbilltime=(TextView)findViewById(R.id.viewbilltime);
        billPicDismissableView=(DismissibleImageView)findViewById(R.id.billPicDismissableView);
        billPicDismissableView.setAdjustViewBounds(true);
        billPicDismissableView.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        billItemsDismissableView1=(DismissibleImageView)findViewById(R.id.billItemsDismissableView1);
        billItemsDismissableView1.setAdjustViewBounds(true);
        billItemsDismissableView1.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        billItemsDismissableView2=(DismissibleImageView)findViewById(R.id.billItemsDismissableView2);
        billItemsDismissableView2.setAdjustViewBounds(true);
        billItemsDismissableView2.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        deliverypnameTxtView=(TextView)findViewById(R.id.deliverypnameTxtView);
        deliverypsignDismissableView=(DismissibleImageView)findViewById(R.id.deliverypsignDismissableView);
        deliverypsignDismissableView.setAdjustViewBounds(true);
        deliverypsignDismissableView.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        helpernameTxtView=(TextView)findViewById(R.id.helpernameTxtView);
        helpersignDismissableView=(DismissibleImageView)findViewById(R.id.helpersignDismissableView);
        helpersignDismissableView.setAdjustViewBounds(true);
        helpersignDismissableView.setScaleType(ImageButton.ScaleType.CENTER_CROP);

        Bundle bundle=getIntent().getExtras();
        billno = bundle.getString("billno");
        billdate = bundle.getString("billdate");
        helpername = bundle.getString("helpername");
        billtime = bundle.getString("billtime");
        billpicurl = bundle.getString("billpicurl");
        billitems1picurl = bundle.getString("billitems1picurl");
        billitems2picurl = bundle.getString("billitems2picurl");
        //helperid = bundle.getString("helperid");
        helpersignatureurl = bundle.getString("helpersignatureurl");
        deliverypname = bundle.getString("deliverypname");
        deliverypsign = bundle.getString("deliverypsign");

        viewbillno.setText(billno);
        viewbilldate.setText(billdate);
        viewbilltime.setText(billtime);
        deliverypnameTxtView.setText(deliverypname);
        helpernameTxtView.setText(helpername);

        Picasso.get()
                .load(billpicurl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(billPicDismissableView);
        Picasso.get()
                .load(billitems1picurl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(billItemsDismissableView1);
        Picasso.get()
                .load(billitems2picurl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(billItemsDismissableView2);
        Picasso.get()
                .load(helpersignatureurl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(helpersignDismissableView);
        Picasso.get()
                .load(deliverypsign)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(deliverypsignDismissableView);

    }
}
