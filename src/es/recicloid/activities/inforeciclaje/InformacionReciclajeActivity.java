package es.recicloid.activities.inforeciclaje;

import es.uca.recicloid.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
public class InformacionReciclajeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion_reciclaje);
		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("file:///android_asset/web-recic/index.html");
		setContentView(webview);
	}
	
}
