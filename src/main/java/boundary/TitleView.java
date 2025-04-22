package main.java.boundary;

class TitleView {
    private String title;
    private Button userButton;

    public TitleView(String title) {
        this.title = title;
        this.userButton = new Button();
    }

    public void userButtonClick() {
        System.out.println("用户按钮点击，跳转至用户页面");
    }

    private class Button {
    }
}
