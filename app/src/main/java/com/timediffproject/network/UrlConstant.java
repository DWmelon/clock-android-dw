package com.timediffproject.network;

/**
 * Created by eddy on 2015/4/22.
 */
public class UrlConstant {

    public static final String CHAR_SET = "UTF-8";

    public static final String BASE_URL2 = "http://www.ipin.com";
    public static final String NEW_DOMAIN_URL_V2 = "http://m.wmzy.com";
    public static final String TEST_URL = "http://192.168.1.30:8088";
    public static final String TEST_PRE_URL = "http://test.gaokao.ipin.com";
    public static final String IP_CHINA_TELECOM1 = "183.56.160.172:10001";
    public static final String IP_CHINA_TELECOM2 = "183.56.160.173:10001";
    public static final String IP_CHINANET1 = "122.13.148.250:10001";
    public static final String IP_CHINANET2 = "122.13.148.251:10001";


    public static final String SHARE_URL = "http://m.wmzy.com.com";
    public static final String SHARE_DOWNLOAD_URL = "http://m.wmzy.com/download";
    //分享到qq空间显示的图片
    public static final String SHARE_ICON_QQZONE = "http://m.wmzy.com/images/icon_logo.png";
    //访问是否显示活动页面url
    public static final String ACTIVITY_URL = "http://m.wmzy.com/api/query_activity?";

    public static final String RANKING_SCH_URL_H5 = "http://m.wmzy.com/tools/school_ranking_list?";
    public static final String RANKING_MAJOR_URL_H5 = "http://m.wmzy.com/tools/major_ranking_list?";
    public static final String HELPER_CENTER_H5 = "http://m.wmzy.com/tools/help_center_list?";
    public static final String TOOLS_V5_BASE_LINE_H5 = "http://m.wmzy.com/tools/score_line2?";

    public static final String PARAM_KEY_VERSION_CODE = "_vcode";
    public static final String PARAM_KEY_VERSION_NAME = "_vname";
    public static final String PARAM_KEY_BRAND = "_brand";
    public static final String PARAM_KEY_CHANNEL = "_channel";
    public static final String PARAM_KEY_NET_TYPE = "_net_type";
    public static final String PARAM_KEY_SCREEN_WIDTH = "_width";
    public static final String PARAM_KEY_SCREEN_HEIGHT = "_height";
    public static final String PARAM_KEY_MODEL = "_model";
    public static final String PARAM_KEY_BROAD = "_brand";
    public static final String PARAM_KEY_SDK = "_sdk";
    public static final String PARAM_KEY_DEVICE_UID = "_deviceuid";
    public static final String PARAM_KEY_APP_ID = "_appid";
    public static final String PARAM_KEY_PLATFORM = "_platform";//平台 ios or android
    public static final String PARAM_KEY_INTERFACE_VERSION = "interface_v";//接口版本号
    public static final String PARAM_KEY_SYS_VERSION = "_sys_version";//系统版本号
    public static final String PARAM_KEY_LOCATION = "loc_name";//生源地
    public static final String PARAM_KEY_PROVINCE_ID = "province_id";//省份ID
    public static final String PARAM_KEY_CLEAR_VISIT_HISIORY = "clear_visit_history";
    public static final String PARAM_KEY_WEN_LI = "wl";//文理科
    public static final String PARAM_KEY_WEN_LI_OLD = "wenli";//文理科该参数只在获取各个年份分数线时使用，历史原因为修改为wl
    public static final String PARAM_KEY_SCORE = "score";//分数
    public static final String PARAM_KEY_SCORE_RANK = "score_rank";//分数对应省排名
    public static final String PARAM_KEY_SCORE_TYPE = "score_type";//分数类型
    public static final String PARAM_KEY_SELECT_COURSE = "select_course";//选考科目
    public static final String PARAM_KEY_YEAR = "year";//年份
    public static final String PARAM_KEY_TOKEN = "token";//

    public static final String PARAM_KEY_SCH_ID = "sch_id";//学校id
    public static final String PARAM_KEY_SCH_NAME = "sch_name";//学校名字
    public static final String PARAM_KEY_DIPLOMA = "diploma";//本专科  bk or zk
    public static final String PARAM_KEY_MAJOR_ID = "major_id";//专业id
    public static final String PARAM_KEY_START = "start";
    public static final String PARAM_KEY_COUNT = "count";
    public static final String PARAM_KEY_KEY_WORD = "keyword";
    public static final String PARAM_KEY_DIFFICULT_LEVEL = "difficult_level";

    //推荐列表
    public static final String PARAM_KEY_BATCH = "batch";//专业id
    public static final String PARAM_KEY_FILTER_TYPE = "filter_type";//筛选类型
    public static final String PARAM_KEY_CP_RESULT = "cp_result";//测评类型
    public static final String PARAM_KEY_SCH_SORTBY = "sch_sortby";//学校排序类型
    public static final String PARAM_KEY_SCH_ORDERBY = "sch_orderby";//排序顺序
    public static final String PARAM_KEY_LOC_PROVINCE = "loc_province";//省份
    public static final String PARAM_KEY_LOC_CITY = "loc_city";//城市
    public static final String PARAM_KEY_SCH_TYPE = "sch_type";//学校类型
    public static final String PARAM_KEY_SCH_LEVEL = "sch_level";//学校层次
    public static final String PARAM_KEY_SCH_GENDER_LEVEL = "sch_gender_level";//学校男女比例
    public static final String PARAM_KEY_MASTER_LEVEL = "master_level";//读研比例
    public static final String PARAM_KEY_SCH_ABROAD_LEVEL = "sch_abroad_level";//学校出国比例
    public static final String PARAM_KEY_MAJOR_SORTBY = "major_sortby";//专业排序类型
    public static final String PARAM_KEY_MAJOR_ORDERBY = "major_orderby";//专业排序方式
    public static final String PARAM_KEY_MAJOR_GENDER_LEVEL = "major_gender_level";//专业排序方式
    public static final String PARAM_KEY_MAJOR_ABROAD_LEVEL = "major_abroad_level";//专业出国比例
    public static final String PARAM_KEY_ZHINENG_ID = "zhineng_id";
    public static final String PARAM_KEY_ZHINENG_LIST = "zhineng_list";
    public static final String PARAM_KEY_CATE = "job_cate";
    public static final String PARAM_KEY_MAJOR_TYPE = "major_type";
    public static final String PARAM_KEY_IS_ACTIVIE = "is_active";//过滤条件十分生效

    public static final String PARAM_KEY_MAJOR_SECOND_CATE = "major_second_cate";//专业二级大类key
    public static final String PARAM_KEY_CP_TYPE = "cp_type";//职业性格测试类型

    public static final String PARAM_KEY_JOB_CATE = "job_cate";//行业
    public static final String PARAM_KEY_SORT = "sort";//职业列表排序方式
    public static final String PARAM_KEY_TYPE = "type";//职业列表数据类型

    public static final String PARAM_KEY_PAY_TYPE = "pay_type";//支付类型
    public static final String PARAM_KEY_GOODS_ID = "goods_id";//商品id
    public static final String PARAM_KEY_DEBUG = "debug";//商品id
    public static final String PARAM_ORDER_ID = "order_id";//订单id
    public static final String PARAM_KEY_APPLICATION_FORM_LIST = "zyb_list";//志愿表列表
    public static final String PARAM_KEY_APPLICATION_FORM_ID = "zyb_id";//志愿表id
    public static final String PARAM_KEY_APPLICATION_INFO = "content";//志愿表详情
    public static final String PARAM_KEY_ZYB_INFO_XZ = "zyb_info_zj";//浙江省志愿表详情
    public static final String PARAM_KEY_MAJOR_IDS = "major_ids";//专业ids
    public static final String PARAM_KEY_ZY_PRIORITY = "priority";//生成志愿表优先类型
    public static final String PARAM_CHECK_SUM = "check_sum";//生成志愿表的校验字段
    public static final String PARAM_REQ_SELECT_LEVEL = "req_select_level";//必选科目
    public static final String PARAM_OPT_SELECT_LEVEL = "opt_select_level";//选测科目
    public static final String PARAM_KEY_MAJOR_RECOMMEND_SCH_TYPE = "data_type";//根据专业推荐学校的类型
    public static final String PARAM_ARTICLE_ID = "article_id";//资讯id
    public static final String PARAM_OFFSET = "offset";//评论偏移量
    public static final String PARAM_KEY_CONTENT = "content";//评论内容
    public static final String PARAM_KEY_REPLY_TO = "reply_to";//评论回复的uid
    public static final String PARAM_KEY_BASE_SCORE = "ysy_score";//浙江-语数英分数
    public static final String PARAM_KEY_GENERAL_SCORE = "zh_score";//浙江-综合分数
    public static final String PARAM_KEY_OPTIONAL_SCORE = "zx_score";//浙江-自选分数
    public static final String PARAM_KEY_TECH_SCORE = "js_score";//浙江-技术分数
    public static final String PARAM_KEY_NOTIFYID = "notify_id";//消息id
    public static final String PARAM_COMMENTS = "comments";//只要数字
    public static final String PARAM_LASTTIMESTAMP = "lasttimestamp";//上次拉取消息列表的时间
    public static final String PARAM_KEY_PROMO_CODE = "coupon_code";//优惠码
    public static final String PARAM_SCORE_RANK = "score_rank";//成绩排名
    public static final String PARAM_CONVERT_TYPE = "convert_type";//分数转换
    public static final String PARAM_HELPER_TYPE = "list_type";//帮助中心


    //志愿表
    public static final String PARAM_KEY_ZYB_NAME = "zyb_name";
    public static final String PARAM_KEY_ZYB_REMARKS = "remarks";
    public static final String PARAM_KEY_ZYB_TYPE = "zyb_type";

}
