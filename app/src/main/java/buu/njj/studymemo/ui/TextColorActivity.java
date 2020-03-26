package buu.njj.studymemo.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import buu.njj.studymemo.R;

public class TextColorActivity extends AppCompatActivity{


	private ImageButton prevDifference;
	private ImageButton nextDifference;
	private String padFirst = "a";
	private String padSecond = "b";
	TextView textViewUp;
	TextView textViewDown;
	private String mcsContent;

	ArrayList<DiffPosition> diffPosUp;
	ArrayList<DiffPosition> diffPosDown;
	int curDiffPos = 0;
	String bzanswer,userinput;
	/*
	 * 当文本选择界面调用时，如果点击回退按钮，程序不会回到主界面而是直接退出
	 * 为了回到主界面，我们监听回退按钮点击事件，如果此时文本选择界面打开，则
	 * 关闭文本选择界面，回退到主界面
	 */
	@Override
	public void onBackPressed() 
	{
		int fragments = getFragmentManager().getBackStackEntryCount();
		if (fragments >= 1)
		{
			getFragmentManager().popBackStack();
		}
		else
		{
		    super.onBackPressed();
		}
	}
	

	

	
	
	private void luanchComparision()
	{
		if (textViewUp.getText().length() > 0 && textViewDown.getText().length() > 0)
		{
			StringDescription original = new StringDescription(padFirst + textViewUp.getText());
			StringDescription comparing = new StringDescription(padSecond + textViewDown.getText());
			HirschbergComparision comparer = new HirschbergComparision();
			mcsContent = comparer.hirschbergAlgorithm(original, comparing);
			diffPosUp = setDifferentTextPartToRed(mcsContent, textViewUp);
			diffPosDown = setDifferentTextPartToRed(mcsContent, textViewDown);
		}
	}
	
	private ArrayList<DiffPosition> setDifferentTextPartToRed(String mcs, TextView view)
	{
		ArrayList<DiffPosition> l = getDiffPostList(view.getText().toString(), mcs);
		
		SpannableStringBuilder builder = setDiffTextToRed(l, view.getText().toString());
		
		view.setText(builder);
		return l;
	}
	
	private SpannableStringBuilder setDiffTextToRed(ArrayList<DiffPosition> l, String content)
	{
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		for (int i = 0; i < l.size(); i++)
		{
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
			DiffPosition pos = l.get(i);
			builder.setSpan(redSpan, pos.diffBegin, pos.diffEnd+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return builder;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCompat.requestPermissions(TextColorActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

		setContentView(R.layout.activity_text_color);

		Intent it= getIntent();
		userinput =it.getStringExtra("useroffer");
		bzanswer  = it.getStringExtra("bzoffer");


		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public void onStart()
	{
		super.onStart();
		textViewUp = (TextView)findViewById(R.id.textView1);
		textViewDown = (TextView)findViewById(R.id.textView2);
		textViewUp.setMovementMethod(ScrollingMovementMethod.getInstance());

		textViewDown.setMovementMethod(ScrollingMovementMethod.getInstance());
		textViewDown.setText(userinput);
		textViewUp.setText(bzanswer);
		
	
		getControlInstance();
		luanchComparision();
		/*
		StringDescription original = new StringDescription(padFirst + "明天我要去国土资源厅详谈面试");
		StringDescription comparing = new StringDescription(padSecond + "在国土资源厅花了一些时间来面试");
		HirschbergComparision comparer = new HirschbergComparision();
		String mcs = comparer.hirschbergAlgorithm(original, comparing);
		ArrayList<DiffPosition> l = getDiffPostList("明天我要去国土资源厅详谈面试", mcs);
		Log.i(TAG, mcs);
		*/
		
	}
	

	

	
	private void getControlInstance()
	{
		nextDifference = (ImageButton)findViewById(R.id.go_down_img);
		nextDifference.setOnTouchListener(new ImageButtonOnTouchListener());
		
		prevDifference = (ImageButton)findViewById(R.id.go_up_img);
		prevDifference.setOnTouchListener(new ImageButtonOnTouchListener());
	}
	
	private class ImageButtonOnTouchListener implements View.OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			setTouchDownState(v, event);
			
			setDiffPost(v);
			
			setDiffTextBackground();
			
			return true;
		}
	}
	
	private void setTouchDownState(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			((ImageButton)v).getDrawable().setAlpha(150);
			
		}
		else 
		{
			((ImageButton)v).getDrawable().setAlpha(255);
		}
		((ImageButton)v).invalidate();
	}
	
	private void setDiffPost(View v)
	{
		if (v.getId() == R.id.go_down_img)
		{
			curDiffPos++;
		}
		else
		{
			curDiffPos--;
		}
		
		if (curDiffPos < 0)
		{
			curDiffPos = 0;
		}
		
		if (curDiffPos >= diffPosUp.size())
		{
			curDiffPos = diffPosUp.size() - 1;
		}
	}
	
	private void setDiffTextBackground()
	{
		SpannableStringBuilder builderUp = setDiffTextToRed(diffPosUp, textViewUp.getText().toString());
		SpannableStringBuilder builderDown = setDiffTextToRed(diffPosDown, textViewDown.getText().toString());


		DiffPosition posUp = null;
		DiffPosition posDown = null;
		try {
			posUp = diffPosUp.get(curDiffPos);
			posDown = diffPosDown.get(curDiffPos);

		BackgroundColorSpan bgColor = new BackgroundColorSpan(Color.YELLOW);
		builderUp.setSpan(bgColor, posUp.diffBegin, posUp.diffEnd + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builderDown.setSpan(bgColor, posDown.diffBegin, posDown.diffEnd + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		textViewUp.setText(builderUp);
		textViewDown.setText(builderDown);
		} catch (Exception e) {
			e.printStackTrace();
		}
	//	luanchComparision();
	}




	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_text_color,
					container, false);
			return rootView;
		}
	}

	ArrayList<DiffPosition> getDiffPostList(String content, String mcs)
	{
		ArrayList<DiffPosition> list = new ArrayList<DiffPosition>();
		boolean diffBegin = false;
		int posContent = 0;
		int posMCS = 0;
		DiffPosition curDiff = null;
		
		while (posMCS < mcs.length())
		{
			if (content.charAt(posContent) != mcs.charAt(posMCS))
			{
				if (diffBegin == false)
				{
					diffBegin = true;
			        curDiff = new DiffPosition();
			        curDiff.diffBegin = posContent;
			        list.add(curDiff);
				}
				
				posContent++;
			}
			else
			{
			    if (diffBegin == true)
			    {
			    	diffBegin = false;
			    	curDiff.diffEnd = posContent - 1;
			    }
			    
			    posMCS++;
			    posContent++;
			}	
		}
		
		return list;
	}
}
