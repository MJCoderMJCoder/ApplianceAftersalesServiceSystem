package com.lzf.applianceafter_salesservicesystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Bundle;
import com.lzf.applianceafter_salesservicesystem.R;
import com.lzf.applianceafter_salesservicesystem.util.DBUtil;
import com.lzf.applianceafter_salesservicesystem.util.LogUtil;
import com.lzf.applianceafter_salesservicesystem.util.SPUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddMaintainerActivity extends Activity {

    private EditText name;
    private EditText phone;
    private RadioGroup gender;
    private EditText address;
    private EditText pwd;
    private EditText pwd_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maintainer);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        gender = (RadioGroup) findViewById(R.id.gender);
        address = (EditText) findViewById(R.id.address);
        pwd = (EditText) findViewById(R.id.pwd);
        pwd_again = (EditText) findViewById(R.id.pwd_again);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                final String nameStr = name.getText().toString().trim();
                final String phoneStr = phone.getText().toString().trim();

                final String addressStr = address.getText().toString().trim();
                final String pwdStr = pwd.getText().toString().trim();
                String pwdAgainStr = pwd_again.getText().toString().trim();
                if (nameStr == null || "".equals(nameStr)) {
                    name.setFocusable(true);
                    name.setFocusableInTouchMode(true);
                    name.requestFocus();
                    Toast.makeText(AddMaintainerActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                } else if (phoneStr == null || "".equals(phoneStr)) {
                    phone.setFocusable(true);
                    phone.setFocusableInTouchMode(true);
                    phone.requestFocus();
                    Toast.makeText(AddMaintainerActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (addressStr == null || "".equals(addressStr)) {
                    address.setFocusable(true);
                    address.setFocusableInTouchMode(true);
                    address.requestFocus();
                    Toast.makeText(AddMaintainerActivity.this, "请输入地址", Toast.LENGTH_SHORT).show();
                } else if (pwdStr == null || "".equals(pwdStr)) {
                    pwd.setFocusable(true);
                    pwd.setFocusableInTouchMode(true);
                    pwd.requestFocus();
                    Toast.makeText(AddMaintainerActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (pwdAgainStr == null || "".equals(pwdAgainStr)) {
                    pwd_again.setFocusable(true);
                    pwd_again.setFocusableInTouchMode(true);
                    pwd_again.requestFocus();
                    Toast.makeText(AddMaintainerActivity.this, "请再次确认密码", Toast.LENGTH_SHORT).show();
                } else if (!pwdAgainStr.equals(pwdStr)) {
                    Toast.makeText(AddMaintainerActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Connection conn = null;
                            PreparedStatement ps = null;
                            ResultSet rs = null;
                            try {
                                conn = DBUtil.getConnection();
                                if (conn != null) {
                                    ps = conn.prepareStatement("INSERT INTO maintainer (maintainer_name, maintainer_phone, maintainer_gender, maintainer_address, maintainer_pwd) VALUES (?, ?, ?, ?, ?)");
                                    ps.setString(1, nameStr);
                                    ps.setString(2, phoneStr);
                                    String genderStr = "女";
                                    if (gender.getCheckedRadioButtonId() == R.id.man) {
                                        genderStr = "男";
                                    }
                                    ps.setString(3, genderStr);
                                    ps.setString(4, addressStr);
                                    ps.setString(5, pwdStr);
                                    int exeNums = ps.executeUpdate();
                                    LogUtil.logV("AddMaintainerActivity", "插入条数：" + exeNums);
                                    if (exeNums == 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(AddMaintainerActivity.this, "提交成功！", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                AddMaintainerActivity.this.finish();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AddMaintainerActivity.this, "抱歉，提交失败！", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddMaintainerActivity.this, "抱歉，数据库连接失败；请确认网络是否畅通", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } finally {
                                DBUtil.closeDB(conn, ps, rs);
                            }
                        }
                    }.start();
                }
                break;
            default:
                break;
        }
    }
}
