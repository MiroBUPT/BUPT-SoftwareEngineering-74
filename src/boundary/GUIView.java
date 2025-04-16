package boundary;

class GUIView {
    private TitleView titleView;
    private IndexView indexView;
    private PanelView panelView;

    public GUIView(TitleView titleView, IndexView indexView, PanelView panelView) {
        this.titleView = titleView;
        this.indexView = indexView;
        this.panelView = panelView;
    }
}
