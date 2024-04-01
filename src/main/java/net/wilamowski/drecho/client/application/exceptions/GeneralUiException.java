package net.wilamowski.drecho.client.application.exceptions;

import net.wilamowski.drecho.shared.bundle.Lang;

public class GeneralUiException extends RuntimeException {
  private final String header;
  private final String content;

    public GeneralUiException(String headerKey , String contentMsgKey) {
        this.header = Lang.getString( headerKey);
        this.content = Lang.getString(contentMsgKey);
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }
}
