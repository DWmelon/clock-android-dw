package com.timediffproject.network;

/**
 * Created by eddy on 2015/4/22.
 */
public class UrlConstantV2 {

    public static class PARAM{

        //筛选条件
        public static final String PROVINCE_FILTER = "province_filter";
        public static final String SCH_TYPE_FILTER = "sch_type_filter";//工科等
        public static final String DIPLOMA_FILTER = "diploma_filter";
        public static final String BATCH_FILTER = "batch_filter";
        public static final String SCH_LEVEL_FILTER = "sch_level_filter";//可选参数211 985 normal

        //我能上的学校
        public static final String DIFFICULT_LIST = "difficult_list";//概率过滤条件chong\wen\bao\impossible\incalculable，默认chong\wen\bao
        public static final String LOC_LIST = "loc_list";//省份城市id数组
        public static final String SECOND_MAJOR_LIST = "second_major_list";//专业二级类别数组，如果选中一级类别的话请传所包含的所有二级类别数组
        public static final String SCH_MASTER_RANGE = "sch_master_range";//学校读研比例 [0.4,0.5]
        public static final String SCH_GENDER_RANGE = "sch_gender_range";//学校女生比例 [0.4,0.5]
        public static final String SCH_ABROAD_RANGE = "sch_abroad_range";//学校出国比例 [0.4,0.5]


        public static final String FEMALE_RATIO_FILTER = "female_ratio";//女生比例 gt60（女生多）, lt40（男生多）, in40-60（男女均衡）
        public static final String MASTER_RATIO_FILTER = "master_ratio";//读研比例
        public static final String ABROAD_RATIO_FILTER = "abroad_ratio";//出国比例

        public static final String ORDER_TYPE = "order_type";//排序方式，默认total_rank
                                                            //total_rank\safe_ratio\famous\female_ratio\xinchou(salary)\score\industry_gini\job_ratio
        public static final String ORDER = "order";//降序desc\升序asc， 默认desc
        public static final String PAGE = "page";
        public static final String PAGE_LEN = "page_len";

        public static final String START = "start";
        public static final String DATA_TYPE = "data_type";

        public static final String KEYWORD = "keyword";

        //学校
        public static final String SCH_ID = "sch_id";
        public static final String SCH_TYPE = "sch_type";
        public static final String SCH_LEVEL = "sch_level";
        public static final String DIPLOMA_ID = "diploma_id";//本专科
        public static final String SELECT_BATCH = "select_batch";//批次


        //专业
        public static final String MAJOR_ID = "major_id";

        //个人信息
        public static final String TARGET_PLATFORM = "target_platform";//测评
        public static final String WEN_LI = "wenli";
        public static final String PROVINCE_ID = "province_id";
        public static final String BATCH = "score_batch";
        public static final String BATCH_ZY = "zy_batch";
        public static final String TOKEN = "token";
        public static final String CARD_TOKEN = "card_token";
        public static final String SCORE_RANK = "score_rank";
        public static final String SCORE = "score";
        public static final String SCORE_TYPE = "score_type";
        public static final String SCORE_BATCH = "score_batch";
        public static final String SELECT_COURSE_1 = "zj_opt_course";//浙江所选科目
        public static final String SELECT_COURSE_2 = "course_ids";//浙江所选科目
        public static final String SELECT_COURSE_3 = "select_course";//浙江所选科目
        public static final String CP_RESULT = "cp_result";//测评
        public static final String ACCOUNT = "account";


        //江苏
        public final static String REQ_COURSE = "req_course";//江苏必填科目
        public final static String REQ_LEVEL = "req_level";//江苏必填科目成绩
        public final static String OPT_COURSE = "opt_course";//江苏选填科目
        public final static String OPT_LEVEL = "opt_level";//江苏选填科目成绩

        public final static String CONVERT_TYPE = "convert_type";//江苏选填科目成绩

        //志愿表
        public final static String ZYB_CONTENT = "xz_content";
        public final static String ZYB_TYPE = "zyb_type";
        public final static String ZYB_SHOW_TYPE = "show_type";


        //支付
        public final static String PAY_ORDER_TYPE = "order_type";
        public final static String PAY_CARD_NO = "card_no";
        public final static String PAY_CARD_PWD = "card_pwd";

    }

    public static class KEY{

        public static final String CODE = "code";
        public static final String DATA = "data";
        public static final String MSG = "msg";

        public static final String GOODS_ID = "goods_id";
        public static final String GOODS_LIST = "goods_list";

        public static final String SCORE_RANK = "score_rank";
        public static final String SCORE = "score";
        public static final String SCORE_TYPE = "score_type";
        public static final String SCORE_BATCH = "score_batch";
        public static final String BATCH = "batch";
        public static final String SCORE_RANK_BATCH = "score_rank_batch";
        public static final String MODIFY_COUNT = "modify_score_countdown";
        public static final String USER_ID = "user_id";
        public static final String PROVINCE_ID = "province_id";
        public static final String WEN_LI = "wenli";
        public static final String SCORE_DIPLOMA_ID = "score_diploma_id";
        public static final String DIPLOMA = "diploma";

        //学校
        public final static String SCH_ID = "sch_id";
        public final static String SCH_NAME = "sch_name";
        public final static String CITY_VIEW = "city_view";
        public final static String IS_211 = "is_211";
        public final static String IS_985 = "is_985";
        public static final String SCH_TYPE = "sch_type";
        public static final String SCH_LEVEL = "sch_level";
        public static final String SCH_LOGO = "sch_logo";
        public static final String SCH_RANK = "sch_rank";
        //专业
        public final static String MAJOR_ID = "major_id";

        //江苏
        public final static String REQ_COURSE = "req_course";//江苏必填科目
        public final static String REQ_LEVEL = "req_level";//江苏必填科目成绩
        public final static String OPT_COURSE = "opt_course";//江苏选填科目
        public final static String OPT_LEVEL = "opt_level";//江苏选填科目成绩

        //浙江
        public final static String SELECT_COURSE = "zj_opt_course";//浙江科目

        public final static String CP_MBTI = "mbti";
        public final static String CP_HOLLAND = "holland";
        public final static String CP_TYPE = "type";
        public final static String CP_JOBTYPE = "jobType";
        public final static String CP_SUMMARY = "summary";
        public final static String CP_DATE = "date";

        public final static String LOCATION = "location";

        public final static String COUPON_PRICE = "coupon_price";

    }

    public static class VALUE{
        public static final String CONVERT_TO_SCORE = "to_score";
        public static final String CONVERT_TO_RANK = "to_rank";
        public static final String CONVERT_SCORE = "score";
        public static final String CONVERT_RANK = "rank";

        public static final String DIFFICULT_CHONG = "chong";
        public static final String DIFFICULT_WEN = "wen";
        public static final String DIFFICULT_BAO = "bao";
        public static final String DIFFICULT_IMPOSSIBLE = "impossible";

        public static final String WEN_STR = "文科";
        public static final String LI_STR = "理科";

        public static final String WEN_C = "wen";
        public static final String LI_C = "li";

        public static final Integer WEN = 1;
        public static final Integer LI = 2;

        public static final String SCORE = "score";
        public static final String RANK = "rank";

        public static final String ORDER_TYPE_TOTAL_RANK = "total_rank";
        public static final String ORDER_TYPE_SAFE_RATIO = "safe_ratio";
        public static final String ORDER_TYPE_FAMOUS = "famous";
        public static final String ORDER_TYPE_FEMALE_RATIO = "female_ratio";
        public static final String ORDER_TYPE_XINCHOU = "xinchou";
        public static final String ORDER_TYPE_SCORE = "score";
        public static final String ORDER_TYPE_INDUSTRY_GINI = "industry_gini";
        public static final String ORDER_TYPE_JOB_RATIO = "job_ratio";
        public static final String ORDER_TYPE_MASTER_RATIO = "master_ratio";
        public static final String ORDER_TYPE_ABROAD_RATIO = "abroad_ratio";

        //支付
        public static final String PAY_BY_WECHAT = "wechat";
        public static final String PAY_BY_ALIPAY = "alipay";
    }

    public static class BUNDLE{

        public final static String IS_SHOW_BAR_RIGHT = "is_show_bar_right";
        public final static String IS_FROM_BIND_DIALOG = "is_from_bind";
        public final static String IS_ACTIVATE_FAIL = "is_activate_fail";
        public final static String IS_FROM_ACTIVATE = "is_from_activate";
        public final static String IS_FROM_VIP_SHOP = "is_from_vip_shop";
        public final static String TARGET_VIP_INFO = "target_vip_info";
        public final static String TARGET_VIP_LEVEL = "target_vip_level";

    }

    public static class REQUEST{
        public static final int SIGN_HAND_ZYB = 111;
    }



}

