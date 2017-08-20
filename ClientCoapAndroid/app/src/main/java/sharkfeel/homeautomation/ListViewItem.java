package sharkfeel.homeautomation;

/**
 * Created by Jimyeol on 2017-07-24.
 */

public class ListViewItem {
    private String strUserMessage;
    private String strComMessage;

    void setUserMessage(String str) {
        this.strUserMessage = str;
    }
    String getUserMessage() {
        return this.strUserMessage;
    }
    void setComMessage(String str) {
        this.strComMessage = str;
    }
    String getComMessage() {
        return this.strComMessage;
    }
}
