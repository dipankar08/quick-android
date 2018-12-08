package in.co.dipankar.quickandorid.arch;

public class BaseViewState {
    private boolean mIsProcessing;
    private Error mError;

    public interface Error{
        String getTitle();
        String getDesc();
    }

    public boolean getIsProcessing() {
        return mIsProcessing;
    }

    public Error getError() {
        return mError;
    }
}
