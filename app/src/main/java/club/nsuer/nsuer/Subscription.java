package club.nsuer.nsuer;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class Subscription extends Activity {


    private SessionManager session;
    private Context context;
    private String paymenttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);


        LinearLayout year1 = findViewById(R.id.year1);
        LinearLayout lifetime = findViewById(R.id.lifetime);


        session = new SessionManager(Subscription.this);


         context = this;

        year1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPayment(1);


            }
        });



        lifetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openPayment(2);

            }
        });


        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    private void openPayment(final int type){



        final Dialog dialog = new Dialog(Subscription.this, R.style.WideDialogComments);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.subscription_payment);
        dialog.show();

        if(type == 1) {
            ((TextView) dialog.findViewById(R.id.packname)).setText("1 year membership");
            ((TextView) dialog.findViewById(R.id.price)).setText("100 TK");
            ((TextView) dialog.findViewById(R.id.price2)).setText("Please send 100 TK to the selected number");
           }
        else {

            ((TextView) dialog.findViewById(R.id.packname)).setText("Lifetime membership");
            ((TextView) dialog.findViewById(R.id.price)).setText("250 TK");
            ((TextView) dialog.findViewById(R.id.price2)).setText("Please send 250 TK to the selected number");

        }


        final Button button = dialog.findViewById(R.id.button);

        final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        final RadioButton bkash = dialog.findViewById(R.id.bkash);
        final RadioButton rocket = dialog.findViewById(R.id.rocket);

        final EditText input = dialog.findViewById(R.id.trxid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String trxid = input.getText().toString();

                if(trxid.equals(""))
                {
                    Toast.makeText(context, "Enter TrxID/TnxID", Toast.LENGTH_LONG).show();
                    return;
                }

                String uid = session.getUid();
                String account_type = "1";

                if(type==1)
                    account_type = "1";
                else
                    account_type = "2";






                paymenttype = "1";


                if(bkash.isChecked())
                    paymenttype = "1";
                else if (rocket.isChecked())
                    paymenttype = "2";
                else
                {
                    Toast.makeText(context, "Please select bKash or Rocket", Toast.LENGTH_LONG).show();
                    return;
                }





                if(Utils.isNetworkAvailable(Subscription.this)){


                    HashMap<String, String> parametters = new HashMap<String, String>();
                    parametters.put("account_type", account_type);
                    parametters.put("payment_type", paymenttype);
                    parametters.put("trxID", trxid);
                    parametters.put("uid", session.getUid());
                    JSONParser parser = new JSONParser("https://nsuer.club/subscription/verify.php", "GET", parametters);
                    parser.setListener(new JSONParser.ParserListener() {
                        @Override
                        public void onSuccess(JSONObject result) {


                                dialog.dismiss();

                                Toast.makeText(context, "Your payment verification info is submitted. You account will be activated withing few hours", Toast.LENGTH_LONG).show();
                                finish();

                                session.setPremium(true);



                        }
                        @Override
                        public void onFailure() {
                        }
                    });

                    parser.execute();

                }





            }
        });






    }

}
