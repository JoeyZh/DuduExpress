package com.joey.expresscall.protocol.comm;

import java.util.HashMap;

public abstract interface IHttpComm
{
  public abstract String httpRequestPost(String paramString, HashMap<String, Object> paramHashMap);

  public abstract String httpRequestGet(String paramString, HashMap<String, Object> paramHashMap);

  public abstract String httpRequestGetFile(String url, HashMap<String, Object> paramHashMap, String paramString2);
}
