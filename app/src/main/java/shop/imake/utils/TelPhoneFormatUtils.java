package shop.imake.utils;

import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelPhoneFormatUtils {
    public static void formatPhoneNumber(CharSequence s, int cursorPosition, int before, int count, EditText mEditText, TextWatcher mTextWatcher) {
        if (before == 0 && count == 1) {  //Entering values

            String val = s.toString();
            String a = "";
            String b = "";
            String c = "";
            if (val != null && val.length() > 0) {
                val = val.replace(" ", "");
                if (val.length() >= 3) {
                    a = val.substring(0, 3);
                } else if (val.length() < 3) {
                    a = val.substring(0, val.length());
                }
                if (val.length() >= 7 && val.length() < 11) {
                    b = val.substring(3, 7);
                    c = val.substring(7, val.length());
                } else if (val.length() > 3 && val.length() < 7) {
                    b = val.substring(3, val.length());
                } else if (val.length() >= 11) {
                    b = val.substring(3, 7);
                    c = val.substring(7, 11);
                }


                StringBuffer stringBuffer = new StringBuffer();
                if (a != null && a.length() > 0) {
                    stringBuffer.append(a);

                }
                if (b != null && b.length() > 0) {
                    stringBuffer.append(" ");
                    stringBuffer.append(b);

                }
                if (c != null && c.length() > 0) {
                    stringBuffer.append(" ");
                    stringBuffer.append(c);
                }

                mEditText.removeTextChangedListener(mTextWatcher);
                mEditText.setText(stringBuffer.toString());

                if (cursorPosition == 3 || cursorPosition == 8) {
                    cursorPosition = cursorPosition + 2;
                } else {
                    cursorPosition = cursorPosition + 1;
                }
                if (cursorPosition <= mEditText.getText().toString().length()) {
                    mEditText.setSelection(cursorPosition);
                } else {
                    mEditText.setSelection(mEditText.getText().toString().length());
                }
                mEditText.addTextChangedListener(mTextWatcher);
            } else {
                mEditText.removeTextChangedListener(mTextWatcher);
                mEditText.setText("");
                mEditText.addTextChangedListener(mTextWatcher);
            }

        }

//        if (count > 1) {
//
//            String val = s.toString();
//            int length = val.length();
//            StringBuffer sb = new StringBuffer(val);
//
//            if (length > 3 && length <= 7) {
//                sb.insert(3, " ");
//
//            } else if (length > 7 && length <= 11) {
//
//                sb.insert(3, " ").insert(8, " ");
//
//            } else if (length > 11) {
//                sb.substring(0, 11);
//            }
//
//            mEditText.removeTextChangedListener(mTextWatcher);
//            mEditText.setText(sb.toString());
//            mEditText.setSelection(sb.toString().length());
//            mEditText.addTextChangedListener(mTextWatcher);
//
//        }

        if (before == 1 && count == 0) {  //Deleting values

            String val = s.toString();
            String a = "";
            String b = "";
            String c = "";

            if (val != null && val.length() > 0) {
                val = val.replace(" ", "");
                if (cursorPosition == 3) {
                    val = removeCharAt(val, cursorPosition - 1, s.toString().length() - 1);
                } else if (cursorPosition == 8) {
                    val = removeCharAt(val, cursorPosition - 2, s.toString().length() - 2);
                }
                if (val.length() >= 3) {
                    a = val.substring(0, 3);
                } else if (val.length() < 3) {
                    a = val.substring(0, val.length());
                }
                if (val.length() >= 7) {
                    b = val.substring(3, 7);
                    c = val.substring(7, val.length());
                } else if (val.length() > 3 && val.length() < 7) {
                    b = val.substring(3, val.length());
                }
                StringBuffer stringBuffer = new StringBuffer();
                if (a != null && a.length() > 0) {
                    stringBuffer.append(a);

                }
                if (b != null && b.length() > 0) {
                    stringBuffer.append(" ");
                    stringBuffer.append(b);

                }
                if (c != null && c.length() > 0) {
                    stringBuffer.append(" ");
                    stringBuffer.append(c);
                }
                mEditText.removeTextChangedListener(mTextWatcher);
                mEditText.setText(stringBuffer.toString());
                if (cursorPosition == 3 || cursorPosition == 8) {
                    cursorPosition = cursorPosition - 1;
                }
                if (cursorPosition <= mEditText.getText().toString().length()) {
                    mEditText.setSelection(cursorPosition);
                } else {
                    mEditText.setSelection(mEditText.getText().toString().length());
                }
                mEditText.addTextChangedListener(mTextWatcher);
            } else {
                mEditText.removeTextChangedListener(mTextWatcher);
                mEditText.setText("");
                mEditText.addTextChangedListener(mTextWatcher);
            }

        }
    }

    public static String removeCharAt(String s, int pos, int length) {

        String value = "";
        if (length > pos) {
            value = s.substring(pos + 1);
        }
        return s.substring(0, pos) + value;
    }


    /**
     * 常用手机号的判断
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
