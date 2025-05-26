package boundary;

/**
 * View class for displaying the application title and user button.
 * Manages the title bar interface elements.
 */
class TitleView {
    /** The title text to be displayed */
    private String title;
    /** Button for user-related actions */
    private Button userButton;

    /**
     * Constructs a new TitleView with the specified title.
     * @param title The title text to display
     */
    public TitleView(String title) {
        this.title = title;
        this.userButton = new Button();
    }

    /**
     * Handles user button click events.
     * Navigates to the user profile page.
     */
    public void userButtonClick() {
        System.out.println("用户按钮点击，跳转至用户页面");
    }

    /**
     * Inner class representing a button in the title view.
     */
    private class Button {
    }
}
