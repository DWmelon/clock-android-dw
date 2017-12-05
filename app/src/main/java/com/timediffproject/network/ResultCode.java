package com.timediffproject.network;

/**
 * Created by eddy on 2015/5/5.
 */
public class ResultCode {

    public static final int SUCCESS_0 = 0;//账号相关成功状态码
    public static final int SUCCESS_200 = 200;
    public static final int FAIL = 500;
    public static final int UNAUTHORIZED = 403;
    public static final int CAN_NOT_FIND = 404;
    public static final int NOT_MODIFY = 304;//未修改
    public static final int NO_MORE_DATA = 1000;//没有更多数据


    public static final int ERROR_UNSUPPORT_PROVINCE = 11003;//不支持该省份
    public static final int ERROR_LOGIN_FAIL = 10001;//internal error processing login
    public static final int ERROR_SYS_ERROR = 1001;//接口报错
    public static final int ERROR_MISS_PARAM = 10003;//缺少参数
    public static final int ERROR_TOKEN_INCORRECT = 11004;//token异常
    public static final int ERROR_DEFAULT_CODE = -1;
    public static final int ERROR_UNKNOW = -2;//未知异常
    public static final int ERROR_IDENTIFY_MSG = -4;//显示自定义msg
    public static final int ERROR_RESULT_NONE = -3;//jsonobject或code为空
    public static final int ERROR_RESULT_SERVER_ERROR = -5;//服务器异常 400~600

    public static final int ERROR_RETRIEVE_ACCOUNT_NO_EXIST = 11006;//找回密码，账号不存在
    public static final int ERROR_RETRIEVE_REPEAT_REQUEST = 11007;//找回密码，某时间段重复请求发送短信验证码
    public static final int ERROR_SIGN_ACCOUNT_EXIST = 11008;//账号已存在
    public static final int ERROR_LOGIN_PAW_ERROR = 11009;//密码错误

    public static final int ERROR_ACTIVATION_CARD_NUMWORRY = 11101;//卡号格式错误
    public static final int ERROR_ACTIVATION_CARD_NOEXIST = 11102;//卡号不存在
    public static final int ERROR_ACTIVATION_CARD_NOTMATCH = 11103;//卡号和密码不匹配
    public static final int ERROR_ACTIVATION_CARD_USED = 11104;//卡号已经被激活过了
    public static final int ERROR_ACTIVATION_CARD_OVERDUE = 11105;//卡号过期
    public static final int ERROR_ACTIVATION_ACCOUNT_SANMLEVEL = 11106;//该账号已经绑定其他同级别点卡
    public static final int ERROR_ACTIVATION_ACCOUNT_LOW = 11107;//该账号已绑定的卡比现在卡低级
    public static final int ERROR_ACTIVATION_ACCOUNT_HIGH = 11108;//该账号已绑定的卡比现卡高级
    public static final int ERROR_ACTIVATION_ACCOUNT_SAME = 11109;//账号已经绑定了相同的点卡

    public static final int ERROR_NEED_BIND_CARD = 11120;   //该功能需要点卡才能使用
    public static final int ERROR_NOT_BIND_CARD = 11121;    //账号未绑定点卡
    public static final int     ERROR_CARD_LEVEL_LOW = 11122;   //账号已经绑定的点卡级别不够使用该功能
    public static final int ERROR_CARD_EXPIRE = 11123;      //账号绑定的点卡已经过期，无法使用

    public static final int ERROR_PAY_PRODUCE_ID = 11130;      //商品id错误
    public static final int ERROR_PAY_ORDER_ID = 11131;      //订单号错误
    public static final int ERROR_PAY_FAIL = 11132;   //支付失败
    public static final int ERROR_PAY_SUCCESS = 11133;     //支付成功
    public static final int ERROR_PAY_DEALING = 11134;     //处理中
    public static final int ERROR_PAY_REFOUND = 11135;    //转入退款
    public static final int ERROR_PAY_NOT_PAY = 11136;    //未支付
    public static final int ERROR_PAY_HAS_CLOSE = 11137;       //已关闭
    public static final int ERROR_PAY_HAS_CANCEL = 11138;      //已撤销
    public static final int ERROR_PAY_WX_SERVICE = 11139;    //微信服务返回异常

    //账号相关
    public static final int ERROR_PHONE_NO_EXIST = 11201;    //账号不存在，可用于登录和忘记密码
    public static final int ERROR_PHONE_NO_EXIST_IPIN = 300019999;    //账号不存在，可用于登录和忘记密码
    public static final int ERROR_PHONE_CAPTCHA = 11202;    //验证码错误，
    public static final int ERROR_PHONE_PASSWORD = 11203;    //账号密码不匹配
    public static final int ERROR_PHONE_PASSWORD_IPIN = 300010008;    //账号密码不匹配
    public static final int ERROR_PHONE_SERVICE = 11204;    //服务器错误，比如账号和密码加密时发生异常了

    //志愿表相关
    public static final int ERROR_VOLUNTEER_ID_WROND = 11150;         //志愿表id错误
    public static final int ERROR_VOLUNTEER_NO_EXIST = 11151;         //删除志愿表时，该uid下没任何有效志愿表

    //优惠码相关
    public static final int ERROR_COUPON_NOT_EXIST = 11160;//优惠码不存在
    public static final int ERROR_COUPON_INVALID = 11161; //优惠码已过期(失效、停用)
    public static final int ERROR_COUPON_ACCOUMT_UNBIND_COUPON = 11162;//该账号未使用优惠码

    //拉取个人分数
    public static final int ERROR_NO_EXIST_USER_SCORE_INFO = 11180;//服务器端不存在该用户的分数信息

    public static final int ERROR_PERMISSION_DENIED = 11181;//没有权限查看

    //服务器正在升级维护中
    public static final int ERROR_SEVER_UPDATING = 20000;
}
