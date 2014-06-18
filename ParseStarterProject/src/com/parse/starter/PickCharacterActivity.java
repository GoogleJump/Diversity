package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.PopupWindow;
import android.util.Log;

public class PickCharacterActivity extends Activity implements OnClickListener {
	
	CharacterPagerAdapter pagerAdapter;
	String[] names;
	int[] pictures;
	private Button mainMenu;
	PopupWindow popup;
	ImageButton popupButton;
	Button insidePopupButton;
	TextView popupText;
	LinearLayout layoutOfPopup;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		
		//handling user
		User currentUser = null;
		if (User.getCurrentUser() instanceof User){
			currentUser = ((User) User.getCurrentUser());
		}
		if (currentUser == null) {
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}
		
		//handling swiping functionality
		Resources res = getResources();
		names = res.getStringArray(R.array.character_array);
		pictures = new int[] {R.drawable.surfer, R.drawable.grandma};
		
		pagerAdapter = new CharacterPagerAdapter(PickCharacterActivity.this,names,pictures);
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(pagerAdapter);
	
		//handing popup
		init();
		popupInit();
		
		//adding listeners to buttons
		addListenerOnMainMenuButton();
	}

	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_pick_character);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);

			}
		});
	}
	
    public void popupInit() { 
    	popup = new PopupWindow(layoutOfPopup); 
    	popup.setContentView(layoutOfPopup); 
    	if (popupButton == null){
    		Log.d("MyApp","it's null");
    	}
    	popupButton.setOnClickListener(this); 
    	insidePopupButton.setOnClickListener(this); 
//    	popup = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 

    }
    
    public void init() { 
    	View childView = findViewById(R.id.character_layout);
//    	int count = findViewById(R.id.pager_view).getChildCount();
    	if (childView == null){
    		Log.d("myApp","the childView is null");
    	}
    	popupButton = (ImageButton) childView.findViewById(R.id.character_picture);
    	popupText = new TextView(this); 	
    	insidePopupButton = new Button(this); 
    	layoutOfPopup = new LinearLayout(this); 
    	insidePopupButton.setText("OK"); 
    	popupText.setText("This is Popup Window.press OK to dismiss it."); 
    	popupText.setPadding(0, 0, 0, 20); 
    	layoutOfPopup.setOrientation(1); 
    	layoutOfPopup.addView(popupText); 
    	layoutOfPopup.addView(insidePopupButton); 
    }
    
	public void onClick(View v){
		if (R.id.character_picture == v.getId()) {
			popup.showAsDropDown(popupButton,0,0);
		}
		else{
			popup.dismiss();
		}
	}
    
	private class CharacterPagerAdapter extends PagerAdapter {
		
		Context context;
		String[] names;
		int[] pictures;
		LayoutInflater inflater;
		
		public CharacterPagerAdapter(Context context, String[] names, int[] pictures) {
			this.names = names;
			this.pictures = pictures;
		}
		
		public int getCount() {
			return names.length;
		}
		
		public boolean isViewFromObject(View view, Object object){
			return view == object;
//			return view == ((RelativeLayout) object);
		}
		
		public Object instantiateItem(ViewGroup container, int position) {		 
	        // Declare Variables
	        TextView name;
	        ImageButton picture;
	 
	        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	        inflater = LayoutInflater.from(context);
	        View itemView = inflater.inflate(R.layout.pickcharacter, container,
	                false);
	 
	        name = (TextView) itemView.findViewById(R.id.character_name);
	 
	        // Capture position and set to the TextViews
	        name.setText(names[position]);
	 
	        // Locate the ImageView in viewpager_item.xml
	        picture = (ImageButton) itemView.findViewById(R.id.character_picture);
	        // Capture position and set to the ImageView
	        picture.setImageResource(pictures[position]);
	 
	        // Add viewpager_item.xml to ViewPager
	        ((ViewPager) container).addView(itemView);
	 
	        return itemView;
	    }
	 
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        // Remove viewpager_item.xml from ViewPager
	        ((ViewPager) container).removeView((RelativeLayout) object);
	    }
	    
	}
}
