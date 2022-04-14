package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.header;
/**
 * @description 消息体请求头
 * @author caoxiaoguang
 * @create 2021-09-30 11:12
 **/


import com.alibaba.fastjson.JSONObject;
import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils.ByteUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;


@Data
@Slf4j
public class MsgHeader {
    //消息体长度
    private Integer msgBodyLenth;

    //消息体业务类型
    private MsgBodyType msgBodyType;

    //版本号
    private Short version;

    //消息属性
    private MsgAttr msgAttr;

    //终端接入类型
    private IotDeviceType iotDeviceType;

    //设备id类型
    private DeviceIDType deviceIDType;

    //设备id
    private String deviceID;

    //消息流水号 消息序列号
    private Integer seq;

    //时间戳
    private Long timestampL;

    // 校验码长度
    private byte checkCodeLen;

    @Data
    public static class MsgAttr {
        //bit0-bit1
        private EncryptType encryType;

        private EncodeType encodeType;

        private ZipType zipType;

        private CheckCodeType checkCodeType;

        public MsgAttr(EncryptType encryType, EncodeType encodeType, ZipType zipType, CheckCodeType checkCodeType) {
            this.encryType = encryType;
            this.encodeType = encodeType;
            this.zipType = zipType;
            this.checkCodeType = checkCodeType;
        }


        public EncryptType getEncryType() {
            return encryType;
        }

        public void setEncryType(EncryptType encryType) {
            this.encryType = encryType;
        }

        public EncodeType getEncodeType() {
            return encodeType;
        }

        public void setEncodeType(EncodeType encodeType) {
            this.encodeType = encodeType;
        }

        public ZipType getZipType() {
            return zipType;
        }

        public void setZipType(ZipType zipType) {
            this.zipType = zipType;
        }

        public CheckCodeType getCheckCodeType() {
            return checkCodeType;
        }

        public void setCheckCodeType(CheckCodeType checkCodeType) {
            this.checkCodeType = checkCodeType;
        }
    }

    public MsgAttr of(short msgAttr) {

        //取低位的的一个字节
        byte b = ByteUtils.shortToBytes(msgAttr)[1];

        EncryptType encryptType = EncryptType.of(ByteUtils.getBits(b, 0, 2));
        log.info("encryptType={}",ByteUtils.getBits(b, 0, 2));
        Assert.notNull(encryptType, "EncryptType is null");


        EncodeType encodeType = EncodeType.of(ByteUtils.getBits(b, 2, 2));

        log.info("encodeType={}",ByteUtils.getBits(b, 2, 2));
        Assert.notNull(encodeType, "EncodeType is null");

        ZipType zipType = ZipType.of(ByteUtils.getBits(b, 4, 2));
        log.info("zipType={}",ByteUtils.getBits(b, 4, 2));
        Assert.notNull(zipType, "ZipType is null");

        CheckCodeType checkCodeType = CheckCodeType.of((byte) ByteUtils.getBits(b, 6, 2));
        log.info("checkCodeType={}",ByteUtils.getBits(b, 6, 2));
        Assert.notNull(checkCodeType, "CheckCodeType is null");

        MsgAttr attr = new MsgAttr(encryptType, encodeType, zipType, checkCodeType);
        return attr;
    }

    public short getMsgAttrShort(MsgAttr msgAttr){
        short  encryptValue= msgAttr.getEncryType().getValue();
        Integer  encodeTypeValue =msgAttr.getEncodeType().getValue()<<2;
        Integer zipTypeValue =msgAttr.getZipType().getValue()<<4;
        Integer checkCodeTypeValue =msgAttr.getCheckCodeType().getValue()<<6;
        return (short)(encryptValue+encodeTypeValue+zipTypeValue+checkCodeTypeValue);

    }

    public static void main(String[] args) {
        Integer b = 36;
        System.out.println(Integer.toBinaryString(b));
        MsgHeader msgHeader =new MsgHeader();
        short a =132;
        MsgAttr msgAttr=msgHeader.of(a);
        System.out.println(JSONObject.toJSONString(msgAttr));

        System.out.println(msgHeader.getMsgAttrShort(msgAttr));
    }


}