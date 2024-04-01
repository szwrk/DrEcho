package net.wilamowski.drecho.client.application.exceptions;
/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class EmptyValueGeneralException extends GeneralUiException{
    private static final String defaultHeaderKey = "e.000.header";
    private static final String defaultContentMsgKey = "e.000.msg";

    public EmptyValueGeneralException() {
        super( defaultHeaderKey , defaultContentMsgKey );
    }

    public EmptyValueGeneralException(String customHeaderKey, String customContentKey ) {
        super( customHeaderKey , customContentKey );
    }

}
