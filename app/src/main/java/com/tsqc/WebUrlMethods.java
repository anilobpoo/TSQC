package com.tsqc;

/**
 * Created by apple on 23/02/18.
 */

public  class WebUrlMethods {

    //public static  String base_url="http://13.228.246.1/app/";
    public static  String base_url="https://www.obpoomail.com/app/";
    public static String PHOTOUPLOADURL=base_url+"photos/";

    public static String picurl_FabricAdapter = base_url + "menu/";

    public static final String REGISTER_URL_OrderAdapter = base_url+"updatedelete.php";

    public static final String REGISTER_URL_PurchaseOrder =base_url+"deleteitem.php" ;

    public static final String url_MyOrders = base_url+"qc/myorders.php?id=";
    public static final String urls_MyOrders = base_url+"freecount.php?id=";

    public static final String REGISTER_URL_PurchasedItems = base_url+"updatetotal.php" ;
    public static   final String url_PurchasedItems = base_url+"getbyorder.php?id=";


    public static final  String mesurl_Styles = base_url+"qc/verifysdetails.php";
    public static final  String url_Styles = base_url+"qc/getstyles.php?id=";
    public static final  String ourl_Styles = base_url+"qc/oupdate.php";


    public static final  String EMAIL_URL_ViewMeasurments = base_url+"PHPMailer/qc_email.php";
    public static final  String detailsurl_ViewMeasurments = base_url+"qc/verifydetails.php";
    public static final  String mesurl_ViewMeasurments = base_url+"qc/verifymdetails.php";
   // public static final  String urls_ViewMeasurments = base_url+"getbyorder.php?id=";


    //public static  final String urls_ViewOrder =base_url+"getbyorder.php?id=";
    public static  final String detailsurl_ViewOrder =base_url+"qc/verifydetails.php";
    public static  final String mesurl_ViewOrder =base_url+"verify-order.php";

    //public static  final String url_ViewStyles = base_url+"getbyorder.php?id=";

    public static  final String url_ProductAuto =  base_url+"getbyproduct.php?cn=";

    public static  String profileurl_Splashscreen=base_url+"profile.php?id=";

    public static final String REGISTER_URL_Login =base_url+"qlogin.php" ;


    public static final String url_ViewOrder = base_url+"order.php?id=";
    public static final String picurl_ViewOrder = base_url+"photos/";


    public static String getpurchaseditem=base_url+"getpurchased-items.php?cid=";
    public static String oupdate=base_url+"qc/oupdate.php";
    public static String qcmail=base_url+"PHPMailer/view_qc.php";


}