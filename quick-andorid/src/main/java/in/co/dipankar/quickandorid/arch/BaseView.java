package in.co.dipankar.quickandorid.arch;

public interface BaseView<VS extends BaseViewState> {
    void render(VS viewState);
}