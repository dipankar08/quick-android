package in.co.dipankar.quickandorid.arch;

public class Error {
    private String mTitle;
    private String mDesc;

    public String getDesc() {
        return mDesc;
    }

    public String getTitle() {
        return mTitle;
    }

    public static class Builder{
        private String mTitle;
        private String mDesc;
        public void Builder(){}

        public Builder setDesc(String mDesc) {
            this.mDesc = mDesc;
            return this;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }
        public Error build(){
            return new Error(this);
        }
    }
    private Error(Builder builder){
        mDesc = builder.mDesc;
        mTitle = builder.mTitle;
    }
}
