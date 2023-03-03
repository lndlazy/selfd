package net.xnzn.app.selfdevice.setting.bean;

import java.io.Serializable;

public class SelfSettingBean {


    private SelfServiceMetadate metadata;


    public SelfServiceMetadate getMetadata() {
        return metadata;
    }

    public void setMetadata(SelfServiceMetadate metadata) {
        this.metadata = metadata;
    }


    @Override
    public String toString() {
        return "SelfSettingBean{" +
                "metadata=" + metadata +
                '}';
    }

    public static class SelfServiceMetadate extends BaseMetadate implements Serializable {


        //显示参数   show
        /**
         * 用户照片不显示
         */
        private String showNoPhoto;
        /**
         * 用户余额不显示
         */
        private String showNoBalance;
        /**
         * 补贴余额，个人余额不显示
         */
        private String showNoPersonAndSubBalance;


        //摄像头参数     camera
        /**
         * 摄像头型号
         */
        private String camerType;
        /**
         * 成像角度
         */
        private String camerImageAngle;
        /**
         * 算法角度
         */
        private String camerArithmeticAngle;
        /**
         * 算法摄像头
         */
        private String camerArithmetic;


        //核身方式  identity
        /**
         * 卡  开启1  关闭2
         */
        private String identityCard;
        /**
         * 码
         */
        private String identityQR;
        /**
         * 脸
         */
        private String identityFace;


        //打印参数  print

        /**
         * 打印机类型
         */
        private String printType;
        /**
         * 打印  1是 2否
         */
        private String printIsor;
        /**
         * 打印份数
         */
        private String printCount;
        /**
         * 打印诺诺发票
         */
        private String printNuonuo;


        //人脸参数   face
        /**
         * 人脸识别分数
         */
        private String faceRecognitionScore;
        /**
         * 人脸大小
         */
        private String faceSize;


        //读卡扫码参数  cardQR
        /**
         * 读卡器型号
         */
        private String cardReaderType;

        /**
         * 扫码器型号
         */
        private String QRType;


        public String getShowNoPhoto() {
            return showNoPhoto;
        }

        public void setShowNoPhoto(String showNoPhoto) {
            this.showNoPhoto = showNoPhoto;
        }

        public String getShowNoBalance() {
            return showNoBalance;
        }

        public void setShowNoBalance(String showNoBalance) {
            this.showNoBalance = showNoBalance;
        }

        public String getShowNoPersonAndSubBalance() {
            return showNoPersonAndSubBalance;
        }

        public void setShowNoPersonAndSubBalance(String showNoPersonAndSubBalance) {
            this.showNoPersonAndSubBalance = showNoPersonAndSubBalance;
        }

        public String getCamerType() {
            return camerType;
        }

        public void setCamerType(String camerType) {
            this.camerType = camerType;
        }

        public String getCamerImageAngle() {
            return camerImageAngle;
        }

        public void setCamerImageAngle(String camerImageAngle) {
            this.camerImageAngle = camerImageAngle;
        }

        public String getCamerArithmeticAngle() {
            return camerArithmeticAngle;
        }

        public void setCamerArithmeticAngle(String camerArithmeticAngle) {
            this.camerArithmeticAngle = camerArithmeticAngle;
        }

        public String getCamerArithmetic() {
            return camerArithmetic;
        }

        public void setCamerArithmetic(String camerArithmetic) {
            this.camerArithmetic = camerArithmetic;
        }

        public String getIdentityCard() {
            return identityCard;
        }

        public void setIdentityCard(String identityCard) {
            this.identityCard = identityCard;
        }

        public String getIdentityQR() {
            return identityQR;
        }

        public void setIdentityQR(String identityQR) {
            this.identityQR = identityQR;
        }

        public String getIdentityFace() {
            return identityFace;
        }

        public void setIdentityFace(String identityFace) {
            this.identityFace = identityFace;
        }

        public String getPrintType() {
            return printType;
        }

        public void setPrintType(String printType) {
            this.printType = printType;
        }

        public String getPrintIsor() {
            return printIsor;
        }

        public void setPrintIsor(String printIsor) {
            this.printIsor = printIsor;
        }

        public String getPrintCount() {
            return printCount;
        }

        public void setPrintCount(String printCount) {
            this.printCount = printCount;
        }

        public String getPrintNuonuo() {
            return printNuonuo;
        }

        public void setPrintNuonuo(String printNuonuo) {
            this.printNuonuo = printNuonuo;
        }

        public String getFaceRecognitionScore() {
            return faceRecognitionScore;
        }

        public void setFaceRecognitionScore(String faceRecognitionScore) {
            this.faceRecognitionScore = faceRecognitionScore;
        }

        public String getFaceSize() {
            return faceSize;
        }

        public void setFaceSize(String faceSize) {
            this.faceSize = faceSize;
        }

        public String getCardReaderType() {
            return cardReaderType;
        }

        public void setCardReaderType(String cardReaderType) {
            this.cardReaderType = cardReaderType;
        }

        public String getQRType() {
            return QRType;
        }

        public void setQRType(String QRType) {
            this.QRType = QRType;
        }

        @Override
        public String toString() {
            return "SelfServiceMetadate{" +
                    "showNoPhoto='" + showNoPhoto + '\'' +
                    ", showNoBalance='" + showNoBalance + '\'' +
                    ", showNoPersonAndSubBalance='" + showNoPersonAndSubBalance + '\'' +
                    ", camerType='" + camerType + '\'' +
                    ", camerImageAngle='" + camerImageAngle + '\'' +
                    ", camerArithmeticAngle='" + camerArithmeticAngle + '\'' +
                    ", camerArithmetic='" + camerArithmetic + '\'' +
                    ", identityCard='" + identityCard + '\'' +
                    ", identityQR='" + identityQR + '\'' +
                    ", identityFace='" + identityFace + '\'' +
                    ", printType='" + printType + '\'' +
                    ", printIsor='" + printIsor + '\'' +
                    ", printCount='" + printCount + '\'' +
                    ", printNuonuo='" + printNuonuo + '\'' +
                    ", faceRecognitionScore='" + faceRecognitionScore + '\'' +
                    ", faceSize='" + faceSize + '\'' +
                    ", cardReaderType='" + cardReaderType + '\'' +
                    ", QRType='" + QRType + '\'' +
                    '}';
        }

    }

} 