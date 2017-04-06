package com.hj.weichatauto.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.hj.weichatauto.utils.PerformClickUtils;
import com.hj.weichatauto.utils.Utils;

import java.io.File;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

public class AutoWeiChatFriendService extends AccessibilityService {


    public static final String WECHAT_VERSION_28 = "6.3.28";

    //用户昵称，防止重复进入详细资料
    public String username = "";
    public String lastusername = "";

    public ArrayList<String> userPhoneList = new ArrayList<>();
    public String[] addFriendStr;

    public int position = 0;

    public int index = 0;

    public boolean istrue = true;

    private int WaitTime = 4;

    //当辅助服务被打开时执行此方法
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        System.out.println("辅助服务被打开了");

        if (!Utils.getBoolData(getApplicationContext(), Utils.ISTRUE)) {
            Utils.setBoolData(getApplicationContext(), Utils.ISTRUE, true);
        }

        getPhoneData();

        getAddFrendStr();

        System.out.println("");
    }

    private void getAddFrendStr() {
        String addstr = Utils.getStrData(getApplicationContext(), Utils.ADDSTR);

        if (!addstr.equals("")) {

            addFriendStr = addstr.split(";");
        }
    }

    private void getPhoneData() {

        position = 0;

        try {

            if (userPhoneList.size() != 0) {
                userPhoneList.clear();
            }

            File file = new File(Utils.getStrData(getApplicationContext(), Utils.FILESTR));

            Workbook book = Workbook.getWorkbook(file);

            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();

            for (int i = 0; i < Rows; ++i) {
                String content = (sheet.getCell(1, i)).getContents();

                if (!"".equals(content)) {

                    userPhoneList.add(content);
                }
            }

            for (int i = 0; i < Rows; ++i) {
                String phonetic = (sheet.getCell(4, i)).getContents();

                if (!"".equals(phonetic)) {

                    userPhoneList.add(phonetic);
                }
            }

            for (int i = 0; i < Rows; ++i) {

                String property = (sheet.getCell(7, i)).getContents();

                if (!"".equals(property)) {

                    userPhoneList.add(property);
                }
            }

            for (int i = 0; i < Rows; ++i) {

                String property = (sheet.getCell(10, i)).getContents();

                if (!"".equals(property)) {

                    userPhoneList.add(property);
                }
            }

            book.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < userPhoneList.size(); i++) {

            if (userPhoneList.get(i).equals(Utils.getStrData(getApplicationContext(), Utils.PHONEPROCESS))) {
                position = i;
                break;
            }

            if (i == userPhoneList.size() - 1) {
                position = 0;
            }
        }
    }

    ////监听手机当前窗口状态改变 比如 Activity 跳转,内容变化,按钮点击等事件
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        //获取当前activity的类名:
        String currentWindowActivity = accessibilityEvent.getClassName().toString();
        System.out.println("页面发生变化了" + currentWindowActivity);

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && Utils.getBoolData(getApplicationContext(), Utils.ISTRUE) &&
                !"文件循环完毕".equals(Utils.getStrData(getApplicationContext(), Utils.PHONEPROCESS))) {


            if ("com.tencent.mm.plugin.search.ui.FTSAddFriendUI".equals(currentWindowActivity)) {
                //输入好友界面

                if (addFriendStr == null) {
                    getAddFrendStr();
                }

                if (Utils.getStrData(getApplicationContext(), Utils.PHONEPROCESS).equals(Utils.PROCESSSTR)) {

                    getPhoneData();
                }

                if (position == userPhoneList.size()) {
                    Utils.setBoolData(getApplicationContext(), Utils.ISTRUE, false);
                    Toast.makeText(getApplicationContext(), "循环结束", Toast.LENGTH_SHORT).show();

                    Utils.setStrData(getApplicationContext(), Utils.PHONEPROCESS, "文件循环完毕");

                    return;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PerformClickUtils.findViewIdAndClick(this, "com.tencent.mm:id/g_");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PerformClickUtils.findViewIdAndSetText(this, "com.tencent.mm:id/g9", userPhoneList.get(position++));

                Utils.setStrData(getApplicationContext(), Utils.PHONEPROCESS, userPhoneList.get(position - 1));
                System.out.println("==============" + userPhoneList.get(position - 1));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PerformClickUtils.findViewIdAndClick(this, "com.tencent.mm:id/aul");

                istrue = true;

            } else if ("com.tencent.mm.plugin.profile.ui.ContactInfoUI".equals(currentWindowActivity)) {
                //好友详情界面

                username = PerformClickUtils.findViewIdAndGetText(this, "com.tencent.mm:id/lg");

                String isFriend = PerformClickUtils.findViewIdAndGetText(this, "com.tencent.mm:id/abc");

                if (!username.equals("")) {

                    if (!lastusername.equals(username) && !isFriend.equals("发消息")) {

                        lastusername = username;
                        PerformClickUtils.findViewIdAndClick(this, "com.tencent.mm:id/abb");

                    } else {
                        PerformClickUtils.findTextAndClick(this, "返回");
                    }

                }

            } else if ("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI".equals(currentWindowActivity)) {
                //好友权限界面

                if (istrue) {

                    istrue = false;

                    if (addFriendStr != null) {

                        PerformClickUtils.findViewIdAndSetText(this, "com.tencent.mm:id/c89", addFriendStr[index++ % addFriendStr.length]);
                    }

                    try {
                        Thread.sleep(WaitTime * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    PerformClickUtils.findTextAndClick(this, "发送");

                } else {

                    istrue = true;
                }
            }

        }

    }

    //辅助服务被关闭 执行此方法
    @Override
    public void onInterrupt() {

        System.out.println("辅助服务被关闭");

    }

}
