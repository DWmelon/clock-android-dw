package com.timediffproject.module.money;

import java.util.List;

public class EMoneyResultModel2 {


    /**
     * code : 0
     * msg : null
     * data : {"sourceCoin":"CNY","sourceNation":"中国","updateTime":"2018-11-24 01:24:45","moneyRatioList":[{"coinNation":"阿尔巴尼亚","coinName":"ALL","value":15.749,"updateTime":"2018-11-24 01:24:45"}]}
     */

    private int code;
    private Object msg;
    private DataModel data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public static class DataModel {
        /**
         * sourceCoin : CNY
         * sourceNation : 中国
         * updateTime : 2018-11-24 01:24:45
         * moneyRatioList : [{"coinNation":"阿尔巴尼亚","coinName":"ALL","value":15.749,"updateTime":"2018-11-24 01:24:45"}]
         */

        private String sourceCoin;
        private String sourceNation;
        private String updateTime;
        private List<MoneyRatioModel> moneyRatioList;

        public String getSourceCoin() {
            return sourceCoin;
        }

        public void setSourceCoin(String sourceCoin) {
            this.sourceCoin = sourceCoin;
        }

        public String getSourceNation() {
            return sourceNation;
        }

        public void setSourceNation(String sourceNation) {
            this.sourceNation = sourceNation;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public List<MoneyRatioModel> getMoneyRatioList() {
            return moneyRatioList;
        }

        public void setMoneyRatioList(List<MoneyRatioModel> moneyRatioList) {
            this.moneyRatioList = moneyRatioList;
        }

        public static class MoneyRatioModel {
            /**
             * coinNation : 阿尔巴尼亚
             * coinName : ALL
             * value : 15.749
             * updateTime : 2018-11-24 01:24:45
             */

            private String coinNation;
            private String coinName;
            private double value;
            private String updateTime;

            public String getCoinNation() {
                return coinNation;
            }

            public void setCoinNation(String coinNation) {
                this.coinNation = coinNation;
            }

            public String getCoinName() {
                return coinName;
            }

            public void setCoinName(String coinName) {
                this.coinName = coinName;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
