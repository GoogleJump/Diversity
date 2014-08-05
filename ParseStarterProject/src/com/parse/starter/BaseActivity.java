package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.team.diversity.android.R;

public class BaseActivity extends Activity {
	
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void showWarningDialog(int message) {

        final Dialog myDialog = new Dialog(context);
        myDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.one_button_dialog);
        myDialog.setCancelable(false);

        TextView dialog_title = (TextView) myDialog.findViewById(R.id.title);
        dialog_title.setText("Ooops!");
        
        TextView dialog_message = (TextView) myDialog.findViewById(R.id.message);
        dialog_message.setText(message);

        Button yes = (Button) myDialog.findViewById(R.id.dialog_yes);
        yes.setText("Got it!");
        
        yes.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

}
