package me.kjs.mall.connect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.configs.properties.PurrioProperties;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    private final PurrioProperties purrioProperties;
    private String charset = "EUC-KR";
    private String boundary;
    private static String LINE_FEED = "\r\n";

    public SmsResultDto send(String target,
                             String title,
                             String message,
                             int addMinute,
                             LocalDateTime sendDate) {

        try {
            String send = send(purrioProperties.getUserId(),
                    purrioProperties.getSenderPhone(),
                    target,
                    message,
                    "",
                    getSendDate(sendDate, addMinute),
                    title,
                    null
            );
            if (send.contains("ok")) {
                return SmsResultDto.ok(send);
            } else {
                log.warn("SendFailure : " + send);
                return SmsResultDto.fail(send);
            }

        } catch (Exception e) {
            return SmsResultDto.fail("Exception Failure : " + e.getCause());
        }
    }

    private String getSendDate(LocalDateTime sendDate, int addMinute) {
        if (sendDate == null)
            return null;
        if (sendDate.plusMinutes(addMinute).isBefore(LocalDateTime.now().plusMinutes(10))) {
            return null;
        }
        String t = sendDate.plusMinutes(addMinute).toString().replace("-", "").replace("T", "").replace(":", "");
        return t.substring(0, 14);
    }


    private String send(String userid, String callback, String phone, String msg
            , String names, String appdate, String subject, String filePath) throws Exception {
        boundary = "===" + System.currentTimeMillis() + "===";
        HttpURLConnection httpConn = initHttpConnection(purrioProperties.getSendApiUrl());
        OutputStream outputStream = httpConn.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
        File file1 = null;

        // filePath가 있는 경우 포토 발송
        if (filePath != null && !"".equals(filePath.trim())) file1 = new File(filePath);

        addParameter(writer, "userid", userid);
        addParameter(writer, "callback", callback);
        addParameter(writer, "phone", phone);
        addParameter(writer, "msg", msg);
        addParameter(writer, "names", names);
        if (appdate != null)
            addParameter(writer, "appdate", appdate);
        addParameter(writer, "subject", subject);

        if (file1 != null) addFile(writer, outputStream, "file1", file1);

        sendFinish(writer);

        return closeHttpConnection(httpConn);
    }

    private HttpURLConnection initHttpConnection(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection httpConn;

        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("User-Agent", "CodeJava Agent");

        return httpConn;
    }

    private void addParameter(PrintWriter writer, String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    private void addFile(PrintWriter writer, OutputStream outputStream, String name, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }

    private void sendFinish(PrintWriter writer) throws IOException {
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
    }

    private String closeHttpConnection(HttpURLConnection httpConn) throws IOException {
        StringBuffer response = new StringBuffer();

        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return response.toString();
    }
}
