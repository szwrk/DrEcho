package net.wilamowski.drecho.client.application.exceptions;


import net.wilamowski.drecho.app.bundle.Lang;

public class GeneralUiException extends RuntimeException {
  private final String header;

    public GeneralUiException(String headerBundleKey , String contentBundleKey) {
        super( resolve( headerBundleKey ) + ' ' +  resolve( contentBundleKey ) );
        this.header = resolve( headerBundleKey );
    }

    private static String resolve(String headerBundleKey) {
        return Lang.getString( headerBundleKey );
    }

    public String getHeader() {
        return header;
    }

}
