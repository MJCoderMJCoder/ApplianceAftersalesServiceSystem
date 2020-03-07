package com.lzf.applianceafter_salesservicesystem.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import com.lzf.applianceafter_salesservicesystem.R;
import com.lzf.applianceafter_salesservicesystem.fragment.MaintenanceUserFragment;
import com.lzf.applianceafter_salesservicesystem.fragment.PartFragment;
import com.lzf.applianceafter_salesservicesystem.util.SPUtil;

import java.io.Serializable;

@SuppressLint("NewApi")
public class UserHomeActivity extends Activity implements
		PartFragment.OnFragmentInteractionListener,
		MaintenanceUserFragment.OnFragmentInteractionListener {

	private long exitTime = 0; // 保存点击的时间
	// UI Object
	private TextView maintenance_request;
	private TextView part;
	private FrameLayout ly_content;

	// Fragment Object
	private FragmentManager fManager;
	private PartFragment partFragment;
	private MaintenanceUserFragment maintenanceRequestFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);

		maintenance_request = (TextView) findViewById(R.id.maintenance_request_user);
		part = (TextView) findViewById(R.id.part_user);
		ly_content = (FrameLayout) findViewById(R.id.ly_content);

		fManager = getFragmentManager();

		maintenance_request.performClick(); // 模拟一次点击，既进去后选择第一项

	}

	/**
	 * 重置所有文本的选中状态
	 */
	private void setSelected() {
		maintenance_request.setSelected(false);
		part.setSelected(false);
	}

	/**
	 * 隐藏所有Fragment
	 */
	private void hideAllFragment(FragmentTransaction fragmentTransaction) {
		if (partFragment != null)
			fragmentTransaction.hide(partFragment);
		if (maintenanceRequestFragment != null)
			fragmentTransaction.hide(maintenanceRequestFragment);
	}

	public void doClick(View view) {
		System.out.println("lzf" + view.getId() + "");
		Log.v("lzf", view.getId() + "");
		if (view.getId() == R.id.exit) {
			SPUtil.clear(this);
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
		} else {
			FragmentTransaction fTransaction = fManager.beginTransaction();
			hideAllFragment(fTransaction);
			Log.v("lzf", "fTransaction");
			switch (view.getId()) {
			case R.id.part_user:
				setSelected();
				part.setSelected(true);
				if (partFragment == null) {
					partFragment = PartFragment.newInstance(null);
					fTransaction.add(R.id.ly_content, partFragment);
				} else {
					partFragment.onResume();
					fTransaction.show(partFragment);
				}
				break;
			case R.id.maintenance_request_user:
				setSelected();
				maintenance_request.setSelected(true);
				if (maintenanceRequestFragment == null) {
					maintenanceRequestFragment = MaintenanceUserFragment
							.newInstance("维修申请", "维修申请");
					fTransaction.add(R.id.ly_content,
							maintenanceRequestFragment);
				} else {
					maintenanceRequestFragment.onResume();
					fTransaction.show(maintenanceRequestFragment);
				}
				break;
			}
			fTransaction.commit();
		}
	}

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			this.finish();
			System.exit(0); // 正常退出
		}
	}

	@Override
	public void onFragmentInteraction(Class activityClass,
			Serializable serializable) {
		if ("ModifyMaintenanceActivity".equals(activityClass.getSimpleName())) {
			Intent intent = new Intent(this, ModifyMaintenanceActivity.class);
			intent.putExtra("maintenance", serializable);
			this.startActivity(intent);
		} else if ("MainActivity".equals(activityClass.getSimpleName())) {
			SPUtil.clear(this);
			startActivity(new Intent(this, activityClass));
			this.finish();
		} else if ("UserDetailActivity".equals(activityClass.getSimpleName())) {
			Intent intent = new Intent(this, activityClass);
			intent.putExtra("maintenance", serializable);
			startActivity(intent);
		}
	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}
}
