package com.cherrysoft.ahorrosapp.web.controllers.summaries.utils;

import com.cherrysoft.ahorrosapp.common.core.models.ExcelReportResult;
import com.cherrysoft.ahorrosapp.web.utils.ExtraMediaTypes;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ExcelDocumentResponseBuilder {

  public static ResponseEntity<ByteArrayResource> from(ExcelReportResult result) {
    return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFileName() + ".xls\"")
        .contentType(MediaType.parseMediaType(ExtraMediaTypes.APPLICATION_EXCEL_VALUE))
        .cacheControl(CacheControl.noCache())
        .body(new ByteArrayResource(result.getReportByteArray()));
  }

}
