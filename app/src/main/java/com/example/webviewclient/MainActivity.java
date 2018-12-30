package com.example.webviewclient;

import android.graphics.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import android.view.*;
import android.provider.*;

public class MainActivity extends BaseActivity 
{
	//These code may seems stupid, since it was modified from a AndroidPlayground(A place where I test android functions) project.
	//这些代码看起来可能很蠢(变量命名等)，因为这个是从一个临时项目修改而成的
	
	//Recipe: There is a WebView at the buttom of the layout, It is 1dp in height. It load the shop page and get the image link through WebViewClient. Then parse the sticker id, and download the higher resolution edition through the sticker id.
	//原理:界面下方有个1dp高的WebView，加载商店页面时通过WebWiewClient截取资源的url，然后从url里获取贴纸的内部代码，然后根据这个代码下载贴纸。很简单的一个小工具
	
	//警告:请不要用于非法用途
	WebView wv;
	TextView tv;
	String s="";
	Button c;
	CheckBox dt;
	boolean isdownloading=false;
	String stickerid="";
	@Override
	public void onPrepareUi()
	{
		setContentView(R.layout.main);
		wv=(WebView)findViewById(R.id.wv);
		tv=(TextView)findViewById(R.id.tv);
		dt=(CheckBox)findViewById(R.id.dt);
		wv.setWebViewClient(new WebViewClient(){
			
				@Override  
				public void onPageStarted(WebView view, String url, Bitmap favicon) {  
					super.onPageStarted(view, url, favicon);  
					s="";
				}  
				@Override
				public void onPageFinished(WebView p1,String url){
					
					super.onPageFinished(p1,url);
					if(!url.contains("baidu")){
					Thread t=new Thread(new Runnable(){

							@Override
							public void run()
							{
								try
								{
									Thread.sleep(500);
								}
								catch (InterruptedException e)
								{}
								saveResulet();
							}
						});
						t.start();
						}
				}
			
			
			@Override
				public WebResourceResponse shouldInterceptRequest(WebView view,  String url){
					if(url.contains("/sticon/") && url.endsWith(".png")){
					s=s+(url)+"\n";
					outp("Found URL:"+url);
				}
					
					return super.shouldInterceptRequest(view,url);  
				}  
			
		});
	}

	@Override
	public void onUiPrepared()
	{
		// 雨道汉化: 实现这个方法
		super.onUiPrepared();
		showHelp("How to download:\nFirst you need the sticker id .You can go to http://yabeline.tw  , search your favourite sticker, remember its id, and download through this app.","helpflag");
	}

int err=0;
int suc=0;
	public void onUiPrepared(View p1)
	{
		// TODO: Implement this method
		c=(Button)p1;
		err=0;
		suc=0;
		tv.setText("");
		isdownloading=true;
		c.setEnabled(false);
		stickerid=((EditText)findViewById(R.id.ed)).getText().toString().trim();
		wv.loadUrl("http://yabeline.tw/Emoji_Data.php?Number="+stickerid);
		outp("Starting download......");
	}

	@Override
	public void onFrame()
	{
		// TODO: Implement this method
	}
	
	
	public void saveResulet(){
		
		runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					// TODO: Implement this method
					wv.loadUrl("http://www.baidu.com");
				}
			});
		
		String[] urls=s.split("\n");
		for(int i=0;i<urls.length;i++){
			try{
				if(urls[i].trim().isEmpty()){continue;}
				outp("Saving image... " + urls[i]);
				GetImageInputStream(urls[i]);
				//outp("保存成功:"+ urls[i]);
				suc++;
				
			}
			catch(Exception e){
				outp("Error :Unable to save image:" + urls[i]);
				outp("Caused by:" + e.toString());
				err++;
			}
		}
		outp(""+suc+" downloaded,"+err+" failed to download.");
		outp("The stickers have been download to:/sdcard/Pictures/LineDown/"+stickerid);
		isdownloading=false;
		runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					// 雨   道   汉   化: 实现这个方法
					c.setEnabled(true);
				}
			});
	}
	
	
	public void outp(String str){
		runOnUiThread(new ArgRunnable(str,tv));
	}

	@Override
	public void onBackPressed()
	{
		// 雨道汉化: 实现这个方法
		if(isdownloading){tw("You can't exit while downloading.");}
		else{super.onBackPressed();}
		
	}
	
	
	/** 
     * 获取网络图片 
     * @param imageurl 图片网络地址 
     * @return Bitmap 返回位图 
     */  
    public void GetImageInputStream(String imageurl) throws MalformedURLException, IOException{  
        SavaImage(imageurl,"/sdcard/Pictures/LineDown/"+stickerid+"/");
            
        
    }  
	
	
	public void SavaImage(String imageurl, String path) throws FileNotFoundException, IOException{  
	
	
		//getHd(imageurl);
		pccid=imageurl.split("/iphone/",2)[1].split("\\.png",2)[0];
	
	
        File file=new File(path);  
        //文件夹不存在，则创建它  
        if(!file.exists()){  
            file.mkdirs();  
        }
		
		String fln=""+System.currentTimeMillis();
		if(null!=pccid){fln=pccid;}
		
		if(!new File(path+fln+".png").exists()){
			URL url;  
			HttpURLConnection connection=null;  
			Bitmap bitmap=null;  
            url = new URL((imageurl));  
            connection=(HttpURLConnection)url.openConnection();  
            connection.setConnectTimeout(8000); //超时设置  
            connection.setDoInput(true);   
            connection.setUseCaches(false); //设置不使用缓存  
            InputStream inputStream=connection.getInputStream();  
			
		writeToLocal(path+fln+".png",inputStream);
		
			inputStream.close();  
			outp("Successfully downloaded:"+imageurl);
		}else{
			outp("Skipped."+path+fln+".png");
			
		}
	
    }  
	private static void writeToLocal(String destination, InputStream input)  
	throws IOException {  
		int index;  
		byte[] bytes = new byte[1024];  
		FileOutputStream downloadFile = new FileOutputStream(destination);  
		while ((index = input.read(bytes)) != -1) {  
			downloadFile.write(bytes, 0, index);  
			downloadFile.flush();  
		}  
		downloadFile.close();  
		input.close();  
	}  
	
	public void insertImageAlbum(String picPath) {  
		try {  
			String[] picPaths = picPath.split("/");  
			String fileName = picPaths[picPaths.length - 1];  
			MediaStore.Images.Media.insertImage(this.getContentResolver(), picPath, fileName, null);  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (OutOfMemoryError e) {  
			e.printStackTrace();
		}  
	}  
	String pccid;
	public String getHdx(String url){
		if(!dt.isChecked()){return getHd2(url);}
		String hd="https://sdl-stickershop.line.naver.jp/stickershop/v1/sticker/【sid】/IOS/sticker_animation@2x.png";
		if(url.contains("/v1/sticker/") && url.contains("/android")){
			pccid=url.split("/v1/sticker/",2)[1].split("/android",2)[0];
			return hd.replace("【sid】",url.split("/v1/sticker/",2)[1].split("/android",2)[0]);
		}
		pccid=null;
		return url;
	}
	public String getHd2(String url){
		String hd="https://sdl-stickershop.line.naver.jp/stickershop/v1/sticker/【sid】/IOS/sticker@2x.png";
		if(url.contains("/v1/sticker/") && url.contains("/android")){
			
			pccid=url.split("/v1/sticker/",2)[1].split("/android",2)[0];
			
			
			return hd.replace("【sid】",url.split("/v1/sticker/",2)[1].split("/android",2)[0]);
		}
		pccid=null;
		return url;
	}
	
}

class ArgRunnable implements Runnable
{
	private String arg;
	private TextView text;
	public ArgRunnable(String marg,TextView mText){arg=marg;text=mText;}
	
	@Override
	public void run()
	{
		text.setText(arg +"\n"+text.getText());
	}
	
	
}
