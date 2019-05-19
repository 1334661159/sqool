package com.abuqool.sqool.integration.qcloud;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;

@Component
public class QCloudService {

    public void putObject(String folder, String fileName, File file)
            throws IOException, CosServiceException, CosClientException, InterruptedException {
    // 初始化秘钥信息
    String secretId = "AKIDNfUbcvTvDnJgx8qGFB2lgQ0TqEarhr6D";
    String secretKey = "9B35wuchibcXnyBJm9YpSwKi5KOGzAUi";
    COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
    ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
    COSClient cosClient = new COSClient(cred, clientConfig);

    ExecutorService threadPool = Executors.newFixedThreadPool(32);
    TransferManager transferManager = new TransferManager(cosClient, threadPool);

    String bucketName = "cdn-1258157285";
    String key = "abuqool/";
    if(!StringUtils.isEmpty(folder)) {
        key += folder;
        key += '/';
    }
    key += fileName;

//    CommonsMultipartFile cf = (CommonsMultipartFile) file;
//    DiskFileItem fi = (DiskFileItem) cf.getFileItem();
//    File f = fi.getStoreLocation();
//    File f = new File("/home/dev/Downloads/test.txt");

    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
    Upload upload = transferManager.upload(putObjectRequest);
    UploadResult uploadResult = upload.waitForUploadResult();

    transferManager.shutdownNow();
    
    System.out.println("YES!!!");
    System.out.println("https://cdn-1258157285.cos.ap-shanghai.myqcloud.com/"+key);
}
//    public static QCloudTmpCredential getTempToken() {
//        TreeMap<String, Object> config = new TreeMap<String, Object>();
//
//        /*
//         * SecretId: AKIDNfUbcvTvDnJgx8qGFB2lgQ0TqEarhr6D
//         * SecretKey: 9B35wuchibcXnyBJm9YpSwKi5KOGzAUi
//         * 
//         * 
//            // 固定密钥
//            config.put("SecretId", "AKIDm04vXaiyMY6PWPlLM6Zl9HDtKVWK7Vsy");
//            // 固定密钥
//            config.put("SecretKey", "AKIDHRM3UggRxEKDsKiYJrBKFvMZeU4RjaKp");
//         */
//        try {
////            // 固定密钥
////            config.put("SecretId", "AKIDNfUbcvTvDnJgx8qGFB2lgQ0TqEarhr6D");
////            // 固定密钥
////            config.put("SecretKey", "9B35wuchibcXnyBJm9YpSwKi5KOGzAUi");
//
//            // 固定密钥
//            config.put("SecretId", "AKIDm04vXaiyMY6PWPlLM6Zl9HDtKVWK7Vsy");
//            // 固定密钥
//            config.put("SecretKey", "AKIDHRM3UggRxEKDsKiYJrBKFvMZeU4RjaKp");
//
//            // 临时密钥有效时长，单位是秒
//            config.put("durationSeconds", 1800);
//
//            // 换成您的 bucket
//            config.put("bucket", "cdn-1258157285");
//            // 换成 bucket 所在地区
//            config.put("region", "ap-shanghai");
//
//            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的目录，例子：* 或者 a/* 或者 a.jpg
//            config.put("allowPrefix", "*");
//
//            // 密钥的权限列表。简单上传和分片需要以下的权限，
//            //其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
//            String[] allowActions = new String[] {
//                    // 简单上传
//                    "name/cos:PutObject",
////                    // 分片上传
////                    "name/cos:InitiateMultipartUpload",
////                    "name/cos:ListMultipartUploads",
////                    "name/cos:ListParts",
////                    "name/cos:UploadPart",
////                    "name/cos:CompleteMultipartUpload"
//            };
//            config.put("allowActions", allowActions);
//
//            JSONObject credential = CosStsClient.getCredential(config);
//            System.out.println(credential);
//            String tmpSecretId = (String) credential.get("tmpSecretId");
//            String tmpSecretKey = (String) credential.get("tmpSecretKey");
//            String sessionToken = (String) credential.get("sessionToken");
//            return new QCloudTmpCredential(tmpSecretId, tmpSecretKey, sessionToken);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("no valid secret !");
//        }
//    }

//    
//    public static void main(String[] args) {
////        getTempToken();
//
//        try {
//            putObject();
//        } catch (CosClientException | IOException | InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
