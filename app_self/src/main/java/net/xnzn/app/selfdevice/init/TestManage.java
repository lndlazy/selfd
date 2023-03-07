package net.xnzn.app.selfdevice.init;

import android.os.Handler;
import android.os.Looper;

import net.xnzn.leniu_http.yunshitang.api.DeviceApi;
import net.xnzn.lib_http.config.HttpConfigCache;
import net.xnzn.lib_http.exception.HttpParseException;
import net.xnzn.lib_utils.NetworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

/**
 * 服务器IP(192.168.1.254) 连通性，正常
 * 服务器端口(58100) 连通性，异常
 * 云食堂服务连通性，正常
 * 读卡器已连接
 * 扫码器未连接
 * 人脸初始化完成
 * 上传离线订单：100/300
 * 新增用户数量：100
 * 本地用户总数量：545454
 */
public class TestManage {

    public static TestManage instance = new TestManage();

    public static TestManage getInstance() {
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper());


    public void testIp(Callback callback) {
        //1、服务器ip连通性测试
        String ip = null;
        if (HttpConfigCache.get().isIpMode()) {
            ip = HttpConfigCache.get().getIp();
        } else {
            ip = HttpConfigCache.get().getDomain();
        }

        boolean result = NetworkUtil.isAvailableByPing(ip);
        if (result) {
            callback.result(new InitInfo(true, "服务器IP(" + ip + ") 连通性，正常"));
        } else {
            callback.result(new InitInfo(false, "服务器IP(" + ip + ") 连通性，异常"));
        }
    }


    public void testServer(Callback callback) {
        DeviceApi
                .getMerchantId()
                .observeOn(AndroidSchedulers.mainThread())
//                .to(RxLife.to(this))
                .subscribe(
                        (merchant) -> {

                            //设备已添加，直接进入主界面
//                            startActivity(MainActivity.class);

                            callback.result(new InitInfo(true, "服务器连通性，正常"));

                        }, (throwable) -> {
                            if (throwable instanceof HttpParseException) {
                                HttpParseException parseException = (HttpParseException) throwable;
                                if (parseException.getErrorCode().equals("10001")) {
                                    callback.result(new InitInfo(true, "服务器连通性，正常"));
                                } else if (parseException.getErrorCode().equals("10000")) {
                                    callback.result(new InitInfo(true, "服务器连通性，正常"));
                                }
                            } else {
                                callback.result(new InitInfo(false, "服务器连通性，异常 " + throwable.getMessage()));
                            }
                        });
    }


    public void testPort(Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (HttpConfigCache.get().isIpMode()) {
                    Socket client = null;
                    //ip方式
                    try {
                        client = new Socket(HttpConfigCache.get().getIp(), Integer.parseInt(HttpConfigCache.get().getPort()));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.result(new InitInfo(true, "服务器port(" + HttpConfigCache.get().getPort() + ") 连通性，正常"));
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.result(new InitInfo(false, "服务器port(" + HttpConfigCache.get().getPort() + ") 连通性，异常"));

                    } finally {
                        try {
                            if (client != null) {
                                client.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //域名模式
                    callback.result(null);

                }


            }
        }).start();
    }


    public interface Callback {
        void result(InitInfo result);
    }


    public String getNetworkType() {
        return "";
    }

    public String getDeviceIp() {
        String interfaceName = "eth0";

        try {
            Enumeration<NetworkInterface> enNetworkInterface = NetworkInterface.getNetworkInterfaces(); //获取本机所有的网络接口
            while (enNetworkInterface.hasMoreElements()) {  //判断 Enumeration 对象中是否还有数据
                NetworkInterface networkInterface = enNetworkInterface.nextElement();   //获取 Enumeration 对象中的下一个数据
                if (!networkInterface.isUp()) { // 判断网口是否在使用
                    continue;
                }
                if (!interfaceName.equals(networkInterface.getDisplayName())) { // 网口名称是否和需要的相同
                    continue;
                }
                Enumeration<InetAddress> enInetAddress = networkInterface.getInetAddresses();   //getInetAddresses 方法返回绑定到该网卡的所有的 IP 地址。
                while (enInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enInetAddress.nextElement();
                    if (inetAddress instanceof Inet4Address) {  //判断是否未ipv4
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0.0.0.0";
    }

    public String getDeviceMask() {
        String interfaceName = "eth0";

        try {
            //获取本机所有的网络接口
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            //判断 Enumeration 对象中是否还有数据
            while (networkInterfaceEnumeration.hasMoreElements()) {

                //获取 Enumeration 对象中的下一个数据
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                if (!networkInterface.isUp() && !interfaceName.equals(networkInterface.getDisplayName())) {

                    //判断网口是否在使用，判断是否时我们获取的网口
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    if (interfaceAddress.getAddress() instanceof Inet4Address) {

                        //仅仅处理ipv4
                        //获取掩码位数，通过 calcMaskByPrefixLength 转换为字符串
                        return calcMaskByPrefixLength(interfaceAddress.getNetworkPrefixLength());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return "0.0.0.0";
    }


    /*通过子网掩码的位数计算子网掩码*/
    private static String calcMaskByPrefixLength(int length) {

        int mask = 0xffffffff << (32 - length);
        int partsNum = 4;
        int bitsOfPart = 8;
        int maskParts[] = new int[partsNum];
        int selector = 0x000000ff;

        for (int i = 0; i < maskParts.length; i++) {

            int pos = maskParts.length - 1 - i;
            maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;
        }

        String result = "";
        result = result + maskParts[0];

        for (int i = 1; i < maskParts.length; i++) {

            result = result + "." + maskParts[i];
        }

        return result;
    }

    public String getDeviceGateway() {
        String[] arr;
        try {
            Process process = Runtime.getRuntime().exec("ip route list table 0");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String string = in.readLine();
            arr = string.split("\\s+");
            return arr[2];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    public String getDeviceDns(int num) {
        Process cmdProcess = null;
        BufferedReader reader = null;
        String dnsIP = "";
        try {
            cmdProcess = Runtime.getRuntime().exec(" getprop net.dns" + num);
            reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            dnsIP = reader.readLine();
            return dnsIP;
        } catch (IOException e) {
            return "0.0.0.0";
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
            cmdProcess.destroy();
        }
    }


}
