package in.co.dipankar.quickandorid.arch;

public class BaseViewState {
    private boolean mIsProcessing;
    private Error mError;
    public boolean getIsProcessing() {
        return mIsProcessing;
    }

    public Error getError() {
        return mError;
    }

    protected BaseViewState(Builder builder){
        mIsProcessing= builder.mIsProcessing;
        mError = builder.mError;
    }

    public static class Builder<T extends Builder<T>>{
        private boolean mIsProcessing;
        private Error mError;
        public Builder(){}

        public T setError(Error mError) {
            this.mError = mError;
            return (T)this;
        }

        public T setIsProcessing(boolean mIsProcessing) {
            this.mIsProcessing = mIsProcessing;
            return (T) this;
        }
        public BaseViewState build(){
            return new BaseViewState(this);
        }
    }
}
