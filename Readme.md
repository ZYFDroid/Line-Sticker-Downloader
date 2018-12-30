Line Emoji Downloader
=


<br>
一个可以下载line emoji小表情的软件.主分支用于下载普通贴纸<br>
An app that can download LINE stickers.
<br>
Recipe: There is a WebView at the buttom of the layout, It is 1dp in height. It load the shop page and get the image link through WebViewClient. Then parse the sticker id, and download the higher resolution edition through the sticker id.<br>
原理:界面下方有个1dp高的WebView，加载商店页面时通过WebWiewClient截取资源的url，然后从url里获取贴纸的内部代码，然后根据这个代码下载贴纸。很简单的一个小工具
<br>
国内需要翻墙

=

<br>
警告:请不要用于非法用途
