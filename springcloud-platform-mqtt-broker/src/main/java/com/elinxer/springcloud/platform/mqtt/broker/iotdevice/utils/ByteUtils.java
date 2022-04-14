package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils;
/**
 * @description
 * @author caoxiaoguang
 * @create 2021-10-08 10:10
 **/


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtils {

    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        short a = 6;
        byte[] bytes = shortToBytes(a);
        System.out.println(bytes[0]);
        System.out.println(bytes[1]);

        byte[]data =longToByteArray(123233L);
        for(byte b: data){
            System.out.println(b);
        }
        byte[] copy =new byte[4];
        System.arraycopy(data,4,copy,0,4);
        System.out.println(byteArrayToInt(copy));

    }

    /**
     * @param b      为传入的字节
     * @param start  起始位
     * @param length 是长度
     *               <p>
     *               eg 如要获取bit0-bit4的值，则start为0，length为5
     * @return
     */
    public static short getBits(byte b, int start, int length) {
        short bit = (short) ((b >> start) & (0xFF >> (8 - length)));
        return bit;
    }

    /**
     * short 转byte[]
     *
     * @param value
     * @return
     */
    public static byte[] shortToBytes(short value) {
        return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(value).array();
    }

    /**
     *  long轉化成數組
     * @param lo
     * @return
     */
    public static byte[] longToByteArray(long lo) {
        ByteArrayOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);
            dos.writeLong(lo);
            byte[] buf = bos.toByteArray();
            return buf;
        } catch (Exception e) {
            return null;
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * byte數組轉化成int
     * @param data
     * @return
     * @throws IOException
     */
    public static Integer byteArrayToInt(byte[] data)  {
        ByteArrayInputStream bis = null;
        DataInputStream dis = null;
        try {
            bis = new ByteArrayInputStream(data);
            dis = new DataInputStream(bis);
            return dis.readInt();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * byte數組轉化成long
     * @param data
     * @return
     * @throws IOException
     */
    public static Long byteArrayToLong(byte[] data)  {
        ByteArrayInputStream bis = null;
        DataInputStream dis = null;
        try {
            if(data.length<=4){
                byte[] dataNew =new byte[8];
                for(int i=0;i<8-data.length;i++){
                    dataNew[i]=0;
                }
                System.arraycopy(data,0,dataNew,4,4);
                data=dataNew;
            }
            bis = new ByteArrayInputStream(data);
            dis = new DataInputStream(bis);
            return dis.readLong();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Short byteToShort(byte b){
        byte [] data =new byte[2];
        data[0]=0;
        data[1]=b;
        ByteArrayInputStream bis = null;
        DataInputStream dis = null;
        try {
            bis = new ByteArrayInputStream(data);
            dis = new DataInputStream(bis);
            return dis.readShort();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2){
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


}