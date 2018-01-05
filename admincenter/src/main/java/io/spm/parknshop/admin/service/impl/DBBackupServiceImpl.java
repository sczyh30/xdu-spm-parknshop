package io.spm.parknshop.admin.service.impl;

import io.spm.parknshop.admin.service.DBBackupService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.text.SimpleDateFormat;

@Service
public class DBBackupServiceImpl implements DBBackupService {

  private static String backupPath = "d:/back";

  @Override
  public Mono<String> backupDB() {
    String command="mysqldump -uroot -p123456 parknshop";

    File saveFile = new File(backupPath);
    if (!saveFile.exists()) {
      saveFile.mkdirs();
    }
    Long currentTime = System.currentTimeMillis();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH");
    String fileName = formatter.format(currentTime)+".sql";
    String filePath=backupPath+"/"+fileName;
    Runtime rt = Runtime.getRuntime();
    try {
      InputStream inputStream = rt.exec(command).getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf8");

      String inStr;
      StringBuffer sb = new StringBuffer("");
      String outStr;

      BufferedReader br = new BufferedReader(inputStreamReader);
      while ((inStr = br.readLine()) != null) {
        sb.append(inStr + "\r\n");
      }
      outStr = sb.toString();
      FileOutputStream fileOutputStream = new FileOutputStream(filePath);
      OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "utf8");
      writer.write(outStr);
      writer.flush();

      inputStream.close();
      inputStreamReader.close();
      br.close();
      writer.close();
      fileOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Mono.just(fileName);
  }

  @Override
  public Flux<String> getBackups() {
    File file = new File(backupPath);
    String [] names = file.list();
    return Flux.fromArray(names);
  }

  @Override
  public Mono<String> recover(String fileName) {
    String command="mysql -uroot -p123456 parknshop";
    try {
      Runtime runtime = Runtime.getRuntime();
      OutputStream outputStream = runtime.exec(command).getOutputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(
          new FileInputStream(backupPath+"/"+fileName), "utf-8"));
      String str = null;
      StringBuffer sb = new StringBuffer();
      while ((str = br.readLine()) != null) {
        sb.append(str + "\r\n");
      }
      str = sb.toString();
      OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
      writer.write(str);
      writer.flush();
      outputStream.close();
      br.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Mono.just(fileName);
  }
}
