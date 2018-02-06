package com.ady.test.span;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ady.test.R;

/**
 * Created by zhouxinyuan on 2018/1/20.
 */

public class SpanActivity extends Activity {

  private static final String TAG = "span";

  private TextView mSpanText;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_span);
    mSpanText = findViewById(R.id.span_text);
    mSpanText.setText("default");
//    testHtml();
//    testSpan();

//    setBSpan();
//    setImageSpan();
    setClickSpan();
  }

  void testHtml() {
    String originText = "#重磅消息#近日谷歌放出Android N的第二个开发者预览版(Developer Preview)";
    String effect1 = "<font color='#ff0000'>#重磅消息#</font><br/>近日谷歌放出 Android N 的第二个开发者预览版<a href='http://developer.android.com/index.html'>(Developer Preview)</a>";
    String effect2 = "<font color='#303f9f'>#重磅消息#</font><br/>近日谷歌放出 Android N 的第二个开发者预览版<a href='http://developer.android.com/index.html'>(Developer Preview)</a>";
    StringBuilder sb = new StringBuilder(originText);
    sb.append("<br/><br/><br/><br/>");
    sb.append(effect1);
    sb.append("<br/><br/><br/><br/>");
    sb.append(effect2);
    mSpanText.setText(Html.fromHtml(sb.toString()));
    mSpanText.setMovementMethod(LinkMovementMethod.getInstance());
  }

  void testSpan() {
    String originText = "#重磅消息#近日谷歌放出Android N的第二个开发者预览版(Developer Preview)";
    SpannableStringBuilder sb = new SpannableStringBuilder(originText);
    sb.append("\r\n").append("\r\n").append("\r\n");
    getEffect1Span(sb);
    sb.append("\r\n").append("\r\n").append("\r\n");
    getEffect2Span(sb);
    mSpanText.setText(sb);
    mSpanText.setMovementMethod(LinkMovementMethod.getInstance());
  }

  void getEffect1Span(SpannableStringBuilder sb) {
    String source1 = "#重磅消息#";
    SpannableString span = new SpannableString(source1);
    span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, source1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    sb.append(span);

    String source2 = "近日谷歌放出AndroidN的第二个开发者预览版";
    sb.append(source2);

    String source3 = "(Developer Preview)";
    SpannableString clickSpan = new SpannableString(source3);
    clickSpan.setSpan(new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        Toast.makeText(SpanActivity.this, source3, Toast.LENGTH_SHORT).show();
      }
    }, 0, source3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    sb.append(clickSpan);
  }

  void getEffect2Span(SpannableStringBuilder sb) {
    String source1 = "#重磅消息#近日谷歌放出Android N的第二个开发者预览版";
    SpannableString span = new SpannableString(source1);
    span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    sb.append(span);

    String source2 = "(Developer Preview)";
    SpannableString clickSpan = new SpannableString(source2);
    clickSpan.setSpan(new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        Toast.makeText(SpanActivity.this, source2, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
      }
    }, 0, source2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    sb.append(clickSpan);
  }

  private void setBSpan() {
    final String source3 = "近日谷歌放出Android N的第二个开发者预览版";
    SpannableString bSpan = new SpannableString(source3);
    bSpan.setSpan(new BulletSpan(), 0, source3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    mSpanText.setText(bSpan);
  }

  private void setImageSpan() {
    String s = "12345";
    SpannableString imageSpan = new SpannableString(s);
    Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
    imageSpan.setSpan(new CustomImageSpan(d, 2), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    mSpanText.setText(imageSpan);
  }

  private void setClickSpan() {
    String s = "aaa12345aaa";
    SpannableString clickSpan = new SpannableString(s);
    Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//    clickSpan.setSpan(new ImageSpan(d), 3, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    clickSpan.setSpan(new ClickableSpan() {
      @Override
      public void onClick(View widget) {
      }

      @Override
      public void updateDrawState(TextPaint ds) {
//        Log.d(TAG, "updateDrawState: old color = " + ds.linkColor);
//        ds.linkColor = R.color.colorPrimaryDark;
        ds.bgColor = R.color.transparent;
        ds.setColor(ds.linkColor);
//        ds.setUnderlineText(true);
        super.updateDrawState(ds);
      }
    }, 3, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    mSpanText.setText(clickSpan);
    mSpanText.setMovementMethod(LinkMovementMethod.getInstance());
  }

}
