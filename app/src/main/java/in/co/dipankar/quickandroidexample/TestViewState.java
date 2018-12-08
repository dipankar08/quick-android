package in.co.dipankar.quickandroidexample;

import in.co.dipankar.quickandorid.arch.BaseViewState;

public class TestViewState extends BaseViewState {
    private String mName;
    public String getName(){
        return mName;
    }

    private TestViewState(Builder builder) {
        super(builder);
        mName = builder.mName;
    }

    public static class Builder extends BaseViewState.Builder<Builder>{
        private String mName ="Test";
        public Builder() {}
        public Builder setName(String name){
            mName = name;
            return this;
        }
        public TestViewState build(){
            return new TestViewState(this);
        }
    }
}
