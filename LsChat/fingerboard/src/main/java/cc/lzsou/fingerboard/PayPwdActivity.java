package cc.lzsou.fingerboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import cc.lzsou.fingerboard.adapter.PayPwdAdapter;

public class PayPwdActivity extends Activity {

    private  int mode;
    private TextView titleView;
    private GridView gridView;
    private PayPwdAdapter adapter;
    private EditText editView1,editView2,editView3,editView4,editView5,editView6;

    private String password1="",password2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypwd);
        mode=getIntent().getIntExtra(FingerBoardConfig.PASSWORD_MODE_KEY,FingerBoardConfig.PASSWORD_MODE_ONCE);
        titleView=(TextView)findViewById(R.id.titleView);
        editView1=(EditText)findViewById(R.id.editView1);
        editView2=(EditText)findViewById(R.id.editView2);
        editView3=(EditText)findViewById(R.id.editView3);
        editView4=(EditText)findViewById(R.id.editView4);
        editView5=(EditText)findViewById(R.id.editView5);
        editView6=(EditText)findViewById(R.id.editView6);
        gridView=(GridView)findViewById(R.id.gridView);
        adapter = new PayPwdAdapter(this);
        adapter.addOnItemClickListener(new PayPwdAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String text) {
               if(text.toUpperCase().equals("C"))clearPassword();
               else if(text.toUpperCase().equals("")){}
               else inputPassword(text);
            }
        });
        gridView.setAdapter(adapter);
    }

    private void inputPassword(String str){
        String s1 = editView1.getText().toString();
        String s2 = editView2.getText().toString();
        String s3 = editView3.getText().toString();
        String s4 = editView4.getText().toString();
        String s5 = editView5.getText().toString();
        String s6 = editView6.getText().toString();
        String password = s1+s2+s3+s4+s5+s6;
        if(TextUtils.isEmpty(s1))editView1.setText(str);
        else if(TextUtils.isEmpty(s2))editView2.setText(str);
        else if(TextUtils.isEmpty(s3))editView3.setText(str);
        else if(TextUtils.isEmpty(s4))editView4.setText(str);
        else if(TextUtils.isEmpty(s5))editView5.setText(str);
        else if(TextUtils.isEmpty(s6)){
            editView6.setText(str);
            if(mode==FingerBoardConfig.PASSWORD_MODE_ONCE){//1次密码
                done(password);
            }
            else if(mode==FingerBoardConfig.PASSWORD_MODE_BOTH) {//2次密码
                if(TextUtils.isEmpty(password1)){
                    password1=password;
                    titleView.setText("请再次输入密码");
                    defaultEditView();
                }else {
                    password2=password;
                    if(!password2.equals(password1)){
                        titleView.setText("两次密码不一致，重新输入");
                        password1="";
                        password2="";
                        defaultEditView();
                        return;
                    }
                    done(password);
                }
            }
        }
    }

    private void done(String password){
        Intent intent = new Intent();
        intent.putExtra("data",password);
        setResult(RESULT_OK,intent);
        finish();
    }
    private void defaultEditView(){
        editView1.setText("");
        editView2.setText("");
        editView3.setText("");
        editView4.setText("");
        editView5.setText("");
        editView6.setText("");
    }
    private void clearPassword(){
        String s1 = editView1.getText().toString();
        String s2 = editView2.getText().toString();
        String s3 = editView3.getText().toString();
        String s4 = editView4.getText().toString();
        String s5 = editView5.getText().toString();
        String s6 = editView6.getText().toString();
        if(!TextUtils.isEmpty(s6))editView6.setText("");
        else if (!TextUtils.isEmpty(s5))editView5.setText("");
        else if (!TextUtils.isEmpty(s4))editView4.setText("");
        else if (!TextUtils.isEmpty(s3))editView3.setText("");
        else if (!TextUtils.isEmpty(s2))editView2.setText("");
        else if (!TextUtils.isEmpty(s1))editView1.setText("");
    }
}
