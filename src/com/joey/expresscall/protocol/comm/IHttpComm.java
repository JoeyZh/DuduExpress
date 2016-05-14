package com.joey.expresscall.protocol.comm;

import java.util.HashMap;

public abstract interface IHttpComm
{
  public abstract String httpRequestPost(String paramString, HashMap<String, Object> paramHashMap);

  public abstract String httpRequestGet(String paramString, HashMap<String, Object> paramHashMap);

  public abstract boolean httpRequestGetFile(String url, HashMap<String, Object> paramHashMap, String savePath);
  
  public abstract boolean httpUpLoadFile(String path,String url,HashMap<String,Object> parasMap);
}
