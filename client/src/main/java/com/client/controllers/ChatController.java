
package com.client.controllers;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import com.client.helpers.ContactManager;
import com.client.model.Contact;
import com.client.model.Message;
import com.client.utils.ApiClient;
import com.client.helpers.ChatManager;
import com.client.utils.CreateMessages;
import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
public class ChatController implements Initializable {

    @FXML
    private VBox MAIN_CONTACTS, MSGS_CONTAINER;
    @FXML
    private AnchorPane MAIN_FRM;
    @FXML
    private ScrollPane SCROLL_BAR_CONTACTS, SCROLL_BAR;
    @FXML
    private AnchorPane ADDUSER_PANE;
    @FXML
    private TextField USERNAME_TXT, PHONENUMBER_TXT;
    @FXML
    private TextArea MSG_TXT;
    @FXML
    private HBox MORE_BTNS, TITLE_FINAL_CONTAINER;
    @FXML
    private Label ERR_NUM_LBL, IMAGE_NAME_LBL;
    @FXML
    private StackPane IMAGE_CONTAINER;
    @FXML
    private TextField SEARCH_TXT;
    @FXML
    private ImageView SEARCH_ICON;

    CreateMessages SR = new CreateMessages();
    File file;
    // Image UserImage;
    ContactManager contactManager = ContactManager.getInstance();
    ChatManager chatManager = ChatManager.getInstance();

    @FXML
    public void searchUser(MouseEvent e) {
        if (!SEARCH_TXT.getText().equals("")) {
            String searchPrompt = SEARCH_TXT.getText();
            boolean found = false;

            for (int i = 0; i < MAIN_CONTACTS.getChildren().size(); i++) {
                if (MAIN_CONTACTS.getChildren().get(i).getAccessibleText().equals(searchPrompt)) {
                    MAIN_CONTACTS.getChildren().get(i).setVisible(true);
                    found = true;
                } else {
                    MAIN_CONTACTS.getChildren().get(i).setVisible(false);
                }
            }

            if (!found) {
                // Send request to the server
                sendSearchRequest(searchPrompt);
            }
        } else {
            System.out.println("Empty");
        }
    }

    private void sendSearchRequest(String searchPrompt) {
        // Send request to the server
        System.out.println("Searching for: " + searchPrompt);
        HashMap<String, String> queryParams = new HashMap();
        queryParams.put("username", searchPrompt);

        Map<String, Object> response = ApiClient.makeHttpRequest("http://localhost:7000/api/users/search", "GET",
                queryParams);
        if (response != null) {
            System.out.println("Response: " + response);
            String firstName = (String) response.get("firstName");
            String lastName = (String) response.get("lastName");
            if (firstName == null || lastName == null) {
                System.out.println("User not found");
                return;
            }

            String username = (String) response.get("username");

            ContactManager.getInstance().addContact(new Contact(firstName, lastName, username));
            ContactManager.getInstance().soutContacts();

            clearContacts();

            contactManager.getContacts().forEach((contact) -> {
                HBox contactHbox = createContact(contact.getNames(), contact.getUsername());

                addContactToMainContacts(contactHbox);
            });

        } else {
            System.out.println("Response is null");
        }
    }

    @FXML
    public void showAddUserDaialog(ActionEvent e) {
        ADDUSER_PANE.setDisable(false);
        ADDUSER_PANE.setLayoutX((MAIN_FRM.getWidth() / 2) - (ADDUSER_PANE.getWidth() / 2));
        ADDUSER_PANE.setLayoutY((MAIN_FRM.getHeight() / 2) - (ADDUSER_PANE.getHeight() / 2));

        IMAGE_NAME_LBL.setText("");
        USERNAME_TXT.clear();
        PHONENUMBER_TXT.clear();
        Image image = new Image(getClass().getResourceAsStream("/Images/plus.png"));
        ImageView imageView = new ImageView(image);
        IMAGE_CONTAINER.getChildren().remove(0);
        IMAGE_CONTAINER.getChildren().add(imageView);

        fadeLabelIn(ADDUSER_PANE);

    }

    @FXML
    public void closeAddPane(ActionEvent e) {
        System.out.println("i am clicking closeAddPane");
        fadeLabelIn(ADDUSER_PANE);
        ERR_NUM_LBL.setOpacity(0);
        ADDUSER_PANE.setDisable(true);
        ADDUSER_PANE.setVisible(false);
    }

    @FXML
    public void closeAndAddUser(ActionEvent e) {

        if (PHONENUMBER_TXT.getText().equals("")) {
            showError("\'Phone Number\' field is required!");
        } else {
            String contactNameString = USERNAME_TXT.getText();
            String phoneNumberString = PHONENUMBER_TXT.getText();
            createContact(contactNameString, phoneNumberString);
            closeAddPane(e);
        }

    }

    // TODO : change phone number to username
    private HBox createContact(String contactNameString, String usernameString) {

        Label time = new Label(new SimpleDateFormat("hh:mm a").format(new Date()));
        HBox topBar = new HBox(time);
        topBar.setStyle("-fx-background-color: #f0f0f0;"); // Example background color

        Label contactName = new Label(contactNameString);
        Label username = new Label(usernameString);
        VBox contactItem = new VBox(topBar, contactName, username);
        contactItem.setStyle("-fx-padding: 10; -fx-spacing: 5;"); // Example padding and spacing

        Circle userImageCircle = new Circle(25, 25, 25);
        // i want to fill this image place with a static image that is in the resources
        // folder
        Image UserImage = new Image(getClass().getResourceAsStream("/Images/default-user-image.png"));

        userImageCircle.setFill(new ImagePattern(UserImage));
        userImageCircle.setSmooth(true);

        HBox contact = new HBox(userImageCircle, contactItem);
        contact.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-padding: 10;"); // Example border and padding
        contact.setOnMouseClicked(event -> handleContactClick(contact));

        setupContextMenu(contact);

        return contact;

    }

    public void addContactToMainContacts(HBox contact) {
        MAIN_CONTACTS.getChildren().add(contact);
    }

    private void clearContacts() {
        MAIN_CONTACTS.getChildren().clear();
    }

    private void setupContextMenu(HBox contact) {
        MenuItem addToFavoriteItem = new MenuItem("Add to Favorite");
        MenuItem markAsReadItem = new MenuItem("Mark as Read");
        MenuItem deleteChatItem = new MenuItem("Delete Chat");
        MenuItem userInfoItem = new MenuItem("User Info");

        deleteChatItem.setOnAction(
                deleteEvent -> MAIN_CONTACTS.getChildren().remove(Integer.parseInt(contact.getAccessibleText())));

        ContextMenu contextMenu = createContextMenu(contact);
        contextMenu.getItems().addAll(addToFavoriteItem, markAsReadItem, deleteChatItem, userInfoItem);
    }

    private void handleContactClick(HBox contact) {
        Label usernameLabel = ((Label) ((VBox) contact.getChildren().get(1)).getChildren().get(2));

        contactManager.setSelectedContact(usernameLabel.getText());
        loadMessagesIntoContainer();

    }

    private void loadMessagesIntoContainer() {
        String selectedContact = contactManager.getSelectedContact().getUsername();

        chatManager.getMessages().forEach((message) -> {
            if (message.getSender().equals(selectedContact)) {
                SR.receiveMessage(message.getMessage(), "2020-20-2", MSGS_CONTAINER, (event) -> {
                    System.out.println("1. Clecked :D");
                });
            } else {
                SR.sendMessage(message.getMessage(), "2020-20-2", MSGS_CONTAINER, (event) -> {
                    System.out.println("1. Clecked :D");
                });
            }
        });
    }

    private ContextMenu createContextMenu(HBox contact) {
        ContextMenu contextMenu = new ContextMenu();
        contact.setAccessibleText(String.valueOf(MAIN_CONTACTS.getChildren().size()));
        return contextMenu;
    }

    private void showError(String message) {
        ERR_NUM_LBL.setText(message);
        fadeLabelIn(ERR_NUM_LBL);
    }

    private void fadeLabelIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private HBox createTopBar(Label time) {
        HBox topBar = new HBox(time);
        topBar.getStyleClass().add("top-bar");
        return topBar;
    }

    private VBox createContactItem(HBox topBar, Label contactName, Label phoneNumber) {
        VBox contactItem = new VBox(topBar, contactName, phoneNumber);
        contactItem.getStyleClass().add("contact-item");
        return contactItem;
    }

    // @FXML
    // public void chooseImage(MouseEvent e) {
    // Stage stage = (Stage) IMAGE_NAME_LBL.getScene().getWindow();
    // FileChooser fileChooser = new FileChooser();
    // fileChooser.getExtensionFilters()
    // .add(new FileChooser.ExtensionFilter("Image Files (*.jpg, *.jpeg, *.png)",
    // "*.jpg", "*.jpeg", "*.png"));
    // file = fileChooser.showOpenDialog(stage);

    // if (file != null) {
    // try {
    // // BufferedImage bufferedImage = ImageIO.read(file);
    // // UserImage = SwingFXUtils.toFXImage(bufferedImage, null);

    // // Circle circleImage = new Circle(30, 30, 30);
    // // circleImage.setFill(new ImagePattern(UserImage));
    // // circleImage.setSmooth(true);

    // // IMAGE_NAME_LBL.setText(file.getName());
    // // IMAGE_CONTAINER.getChildren().remove(0);
    // // IMAGE_CONTAINER.getChildren().add(circleImage);
    // UserImage = new Image(new FileInputStream(file));

    // Circle circleImage = new Circle(30, 30, 30);
    // circleImage.setFill(new ImagePattern(UserImage));
    // circleImage.setSmooth(true);

    // IMAGE_NAME_LBL.setText(file.getName());
    // IMAGE_CONTAINER.getChildren().remove(0);
    // IMAGE_CONTAINER.getChildren().add(circleImage);
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // }

    // }

    @FXML
    public void sendMessage(ActionEvent e) {
        if (!contactManager.hasSelectedContact()) {
            System.out.println("No selected contact");
            return;
        }
        if (!MSG_TXT.getText().equals("")) {

            Message message = new Message(MSG_TXT.getText(), "me", contactManager.getSelectedContact().getUsername(),
                    "connection");
            chatManager.sendMessage(message);
            SR.sendMessage(MSG_TXT.getText(), new SimpleDateFormat("hh:mm a").format(new Date()), MSGS_CONTAINER,
                    (event) -> {
                        System.out.println("1. Clecked :D");
                    });

        }
        MSG_TXT.clear();
    }

    @FXML
    public void receiveMessage(ActionEvent e) {
        if (!MSG_TXT.getText().equals("")) {
            SR.receiveMessage(MSG_TXT.getText(), new SimpleDateFormat("hh:mm a").format(new Date()), MSGS_CONTAINER,
                    (event) -> {
                        System.out.println("1. Clecked :D");
                    });
        }
        MSG_TXT.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ADDUSER_PANE.setDisable(true);
        MAIN_FRM.widthProperty().addListener(e -> {
            if (MAIN_FRM.getWidth() <= 620) {
                AnchorPane.setLeftAnchor(SCROLL_BAR, 0.0);
                AnchorPane.setLeftAnchor(MSG_TXT, 0.0);
                AnchorPane.setLeftAnchor(MORE_BTNS, 70.0);
                SCROLL_BAR_CONTACTS.setVisible(false);
            } else {
                AnchorPane.setLeftAnchor(MORE_BTNS, 360.0);
                AnchorPane.setLeftAnchor(MSG_TXT, 291.0);
                AnchorPane.setLeftAnchor(SCROLL_BAR, 291.0);
                SCROLL_BAR_CONTACTS.setVisible(true);
            }

            ADDUSER_PANE.setLayoutX((MAIN_FRM.getWidth() / 2) - (ADDUSER_PANE.getWidth() / 2));
            ADDUSER_PANE.setLayoutY((MAIN_FRM.getHeight() / 2) - (ADDUSER_PANE.getHeight() / 2));
        });

        MAIN_FRM.heightProperty().addListener(e -> {
            ADDUSER_PANE.setLayoutX((MAIN_FRM.getWidth() / 2) - (ADDUSER_PANE.getWidth() / 2));
            ADDUSER_PANE.setLayoutY((MAIN_FRM.getHeight() / 2) - (ADDUSER_PANE.getHeight() / 2));
        });

        MAIN_CONTACTS.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            // Data.usersCount = -1;
            // for (int i = 0; i < MAIN_CONTACTS.getChildren().size(); i++) {
            // MAIN_CONTACTS.getChildren().get(i).setAccessibleText(String.valueOf(i));
            // Data.usersCount++;
            // }
        });
        IMAGE_CONTAINER.setOnMouseClicked((value) -> {
            // chooseImage(value);
        });
    }

    void setTempInfo() {

    }

}