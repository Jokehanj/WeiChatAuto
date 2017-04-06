package com.hj.weichatauto.activitys;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hj.weichatauto.R;
import com.hj.weichatauto.utils.Utils;

import static com.hj.weichatauto.utils.Utils.isAccessibilitySettingsOn;

public class MainActivity extends AppCompatActivity {

    private Button btn_AutoAddFriend;
    private TextView tv_file;
    private Button btn_GetFile;
    private EditText et_addStr;
    private Button btn_save;
    private EditText tv_addFriendProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //可使不可截屏
        //getWindow().addFlags(WindowManager.LayoutParams. FLAG_SECURE);

        btn_AutoAddFriend = (Button) findViewById(R.id.btn_AutoAddFriend);
        btn_GetFile = (Button) findViewById(R.id.btn_GetFile);
        tv_file = (TextView) findViewById(R.id.tv_file);
        et_addStr = (EditText) findViewById(R.id.et_addStr);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_addFriendProgress = (EditText) findViewById(R.id.tv_addFriendProgress);

        btn_AutoAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_file.getText().toString().equals("")) {

                    Toast.makeText(MainActivity.this, "请先选择文件", Toast.LENGTH_SHORT).show();
                } else {

                    if (!isAccessibilitySettingsOn(MainActivity.this)) {

                        Toast.makeText(MainActivity.this, "辅助功能处于关闭状态，请手动开启", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    } else {

                        //模拟home
//                    Intent intent= new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                    startActivity(intent);


//                    //跳转到微信
//                    Intent intent2 = new Intent();
//                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//                    intent2.setAction(Intent.ACTION_MAIN);
//                    intent2.addCategory(Intent.CATEGORY_LAUNCHER);
//                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent2.setComponent(cmp);
//                    startActivity(intent2);

                        if (Utils.getBoolData(MainActivity.this, Utils.ISTRUE)) {

                            Utils.setBoolData(MainActivity.this, Utils.ISTRUE, false);
                            Toast.makeText(MainActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
                            btn_AutoAddFriend.setText("已关闭，点击开启");
                        } else {

                            Utils.setBoolData(MainActivity.this, Utils.ISTRUE, true);
                            Toast.makeText(MainActivity.this, "已开启", Toast.LENGTH_SHORT).show();
                            btn_AutoAddFriend.setText("已开启，点击关闭");
                        }
                    }
                }
            }
        });

        btn_GetFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("application/vnd.ms-excel;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 10086);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addstr = et_addStr.getText().toString();

                if ("".equals(addstr)) {
                    Toast.makeText(MainActivity.this, "不填写则以默认显示", Toast.LENGTH_SHORT).show();
                    Utils.setStrData(MainActivity.this, Utils.ADDSTR, addstr);
                } else {

                    Utils.setStrData(MainActivity.this, Utils.ADDSTR, addstr);
                    Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                    if (!tv_addFriendProgress.getText().toString().equals("")) {
                        Utils.setStrData(MainActivity.this,Utils.PHONEPROCESS,tv_addFriendProgress.getText().toString());
                    }

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 10086) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            tv_file.setText(img_path);
            Utils.setStrData(MainActivity.this, Utils.FILESTR, img_path);
            Utils.setStrData(MainActivity.this, Utils.PHONEPROCESS, Utils.PROCESSSTR);
            tv_addFriendProgress.setText(Utils.PROCESSSTR);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isAccessibilitySettingsOn(MainActivity.this) && Utils.getBoolData(this, Utils.ISTRUE)) {
            btn_AutoAddFriend.setText("已开启，点击关闭");
        } else {
            btn_AutoAddFriend.setText("已关闭，点击开启");
        }

        tv_file.setText(Utils.getStrData(MainActivity.this, Utils.FILESTR));

        et_addStr.setText(Utils.getStrData(MainActivity.this, Utils.ADDSTR));
        tv_addFriendProgress.setText(Utils.getStrData(MainActivity.this, Utils.PHONEPROCESS));
    }
}
