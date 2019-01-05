package cc.lzsou.fingerboard;

public class FingerBoardConfig {
    /**
     * 密码框参数
     */
    public static final String PASSWORD_MODE_KEY="cc.lzsou.fingerboard.Password";
    /**
     * 1次密码
     */
    public static final int PASSWORD_MODE_ONCE=0;
    /**
     * 2次密码
     */
    public static final int PASSWORD_MODE_BOTH=1;



    //keybord 使用方法
//    public class MainActivity extends AppCompatActivity {
//
//        EditText editText1;
//        EditText editText2;
//
//        private Context context;
//        private KeyboardManager keyboardManagerNumber;
//        private NumberKeyboard numberKeyboard;
//
//        private KeyboardManager keyboardManagerAbc;
//        private ABCKeyboard abcKeyboard;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            context = this;
//            editText1 = (EditText) findViewById(R.id.edit1);
//            editText2 = (EditText) findViewById(R.id.edit2);
//            editText1.setInputType(InputType.TYPE_CLASS_TEXT);
//            editText2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
//
//            keyboardManagerNumber = new KeyboardManager(this);
//            initNumberKeyboard();
//            keyboardManagerNumber.bindToEditor(editText2, numberKeyboard);
//
//            keyboardManagerAbc = new KeyboardManager(this);
//            keyboardManagerAbc.bindToEditor(editText1, new ABCKeyboard(context, ABCKeyboard.DEFAULT_ABC_XML_LAYOUT));
//        }
//
//        private void initNumberKeyboard() {
//            numberKeyboard = new NumberKeyboard(context,NumberKeyboard.DEFAULT_NUMBER_XML_LAYOUT);
//            numberKeyboard.setEnableDotInput(true);
//            numberKeyboard.setActionDoneClickListener(new NumberKeyboard.ActionDoneClickListener() {
//                @Override
//                public void onActionDone(CharSequence charSequence) {
//                    if(TextUtils.isEmpty(charSequence) || charSequence.toString().equals("0") || charSequence.toString().equals("0.0")) {
//                        Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
//                    }else {
//                        onNumberkeyActionDone();
//                    }
//                }
//            });
//
//            numberKeyboard.setKeyStyle(new BaseKeyboard.KeyStyle() {
//                @Override
//                public Drawable getKeyBackound(Keyboard.Key key) {
//                    if(key.iconPreview != null) {
//                        return key.iconPreview;
//                    } else {
//                        return ContextCompat.getDrawable(context,R.drawable.key_number_bg);
//                    }
//                }
//
//                @Override
//                public Float getKeyTextSize(Keyboard.Key key) {
//                    if(key.codes[0] == context.getResources().getInteger(R.integer.action_done)) {
//                        return convertSpToPixels(context, 20f);
//                    }
//                    return convertSpToPixels(context, 24f);
//                }
//
//                @Override
//                public Integer getKeyTextColor(Keyboard.Key key) {
//                    if(key.codes[0] == context.getResources().getInteger(R.integer.action_done)) {
//                        return Color.WHITE;
//                    }
//                    return null;
//                }
//
//                @Override
//                public CharSequence getKeyLabel(Keyboard.Key key) {
//                    return null;
//                }
//            });
//        }
//
//        public float convertSpToPixels(Context context, float sp) {
//            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
//            return px;
//        }
//
//        public void onNumberkeyActionDone() {
//            editText1.requestFocus();
//        }
//    }

}
